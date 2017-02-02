package factory.codegen.app;

import com.fuscard.commons.FuscardXMLException;
import factory.Application;
import factory.design.ApplicationControlGraph;
import factory.design.ApplicationSimulatingNode;
import factory.design.ApplicationUnit;
import factory.design.Node;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MouseInputAdapter;

/**
 * Class to add a drawing to the IDE.
 *
 * @author John Mwai
 */
public class DrawingManager {

    /**
     * The icon that is used in the tab components.
     */
    private ImageIcon drawinIcon;
    /**
     * The width that the drawing manager uses for new canvases
     */
    private int preferredCanvasWidth = -1;
    /**
     * The height that the drawing manager uses for new canvases
     */
    private int preferredCanvasHeight = -1;
    /**
     * The default canvas color.
     */
    private Color canvasColor = Color.WHITE;
    /**
     * The drawing that is affected by calls to the drawing manager
     */
    private Drawing currentDrawing;
    /**
     * Lock to set the current drawing
     */
    private final Object drawingLock = new Object();
    /**
     * The list of drawings that are currently open
     */
    private LinkedList<Drawing> drawings = new LinkedList<>();
    /**
     * Whether the DrawingManager has a Node to Drop
     */
    private boolean withNodeToDrop;
    /**
     * The current node to drop
     */
    private ApplicationSimulatingNode simulatingNodeToDrop;
    /**
     * The place to write short one line messages
     */
    private JTextField quickOutputArea;
    /**
     * AllSystems
     */
    private AllSystems allSystems;
    /**
     * GlassPane to block mouse events from dormant Nodes
     */
    private MyGlassPane glassPane;
    /**
     * Color to highlight the candidate drop location
     */
    private Color dropLocationColor = Color.BLUE;

    /**
     * Create a DrawingManager.
     *
     * @param drawinIcon Icon to be displayed in the ButtonTabComponent's
     */
    public DrawingManager(ImageIcon drawinIcon, AllSystems allSystems) {
        this.drawinIcon = drawinIcon;
        this.allSystems = allSystems;
        this.glassPane = new MyGlassPane(this.allSystems.getContentPane());
        allSystems.setGlassPane(glassPane);
    }

    public void turnOnGlassPane() {
        glassPane.setVisible(true);
    }

    /**
     * @return the currentDrawing
     */
    public Drawing getCurrentDrawing() {
        synchronized (drawingLock) {
            return currentDrawing;
        }
    }

    /**
     * @param currentDrawing the currentDrawing to set
     */
    public void setCurrentDrawing(Drawing currentDrawing) {
        synchronized (drawingLock) {
            this.currentDrawing = currentDrawing;
        }
    }

    /**
     * @return if the Drawing Manager has a Node to drop
     */
    public boolean isWithNodeToDrop() {
        return withNodeToDrop;
    }

    /**
     * @param withNodeToDrop whether the Drawing Manager has a Node to drop
     */
    public void setWithNodeToDrop(boolean withNodeToDrop) {
        if (!withNodeToDrop) {
            simulatingNodeToDrop = null;
        }
        this.withNodeToDrop = withNodeToDrop;
    }

    /**
     * @return the simulatingNodeToDrop
     */
    public ApplicationSimulatingNode getSimulatingNodeToDrop() {
        return simulatingNodeToDrop;
    }

    /**
     * @param simulatingNodeToDrop the simulatingNodeToDrop to set
     */
    public void setSimulatingNodeToDrop(
            ApplicationSimulatingNode simulatingNodeToDrop) {
        this.simulatingNodeToDrop = simulatingNodeToDrop;
        setWithNodeToDrop(true);
        write("Set Simulating Node To Drop");
    }

    /**
     * @return the quickOutputArea
     */
    public JTextField getQuickOutputArea() {
        return quickOutputArea;
    }

    /**
     * @param quickOutputArea the quickOutputArea to set
     */
    public void setQuickOutputArea(JTextField quickOutputArea) {
        this.quickOutputArea = quickOutputArea;
    }

    public AllSystems getAllSystems() {
        return allSystems;
    }

    public void write(String message) {
        getQuickOutputArea().setText(message);

    }

    /**
     * @return the DropLocationColor
     */
    public Color getDropLocationColor() {
        return dropLocationColor;
    }

    /**
     * @param DropLocationColor the DropLocationColor to set
     */
    public void setDropLocationColor(Color DropLocationColor) {
        this.dropLocationColor = DropLocationColor;
    }

    private class TabbedPaneChangeListener implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            //Check which of the drawings the user is currently viewing
            Object obj = e.getSource();
            if (obj instanceof JTabbedPane) {
                JTabbedPane jtp = (JTabbedPane) obj;
                //Get the selected index
                int index = jtp.getSelectedIndex();
                if (index != -1) {
                    Component component = jtp.getComponentAt(index);
                    if (component instanceof JScrollPane && ((JScrollPane) component).getViewport().getView() instanceof JEFCanvas) {
                        JEFCanvas jEFCanvas = (JEFCanvas) ((JScrollPane) component).getViewport().getView();
                        for (Drawing drawing : drawings) {
                            if (drawing.getCanvas() == jEFCanvas) {
                                write("Trapping events");
                                setCurrentDrawing(drawing);
                                /**
                                 * Start trapping events
                                 */
                                glassPane.setVisible(true);
                                return;
                            }
                        }
                    } else {
                        write("Not trapping events");
                        setCurrentDrawing(null);
                        /**
                         * Stop trapping events
                         */
                        glassPane.setVisible(false);
                    }
                }
            }
        }
    }

    private void addTabbedPaneChangeListener(ScrollingTabbedPane stp,
            TabbedPaneChangeListener tpcl) {
        for (ChangeListener cl : stp.getTabbedPane().getChangeListeners()) {
            if (cl == tpcl) {
                return;
            }
        }
        stp.getTabbedPane().addChangeListener(tpcl);
    }
    private TabbedPaneChangeListener tabbedPaneChangeListener = new TabbedPaneChangeListener();

    private void addOpeningAndClosingHooks(final Drawing drawing) {
        drawing.addDrawingListener(new Drawing.DrawingListener() {
            @Override
            public void drawingPropertyChanged(
                    Drawing.DrawingPropertyChangeEvent event) {
                Drawing.DrawingProperty type = event.getPropertyType();
                if (type == Drawing.DrawingProperty.OPEN) {
                    Object oldobj = event.getOldValue(), newobj = event.getNewValue();
                    if (newobj instanceof Boolean && (oldobj != newobj)) {
                        if ((boolean) newobj) {
                            //Drawing opened
                            handleDrawingOpened();
                        } else {
                            //Closed drawing
                            handleDrawingClosed();
                        }
                    }
                }
            }

            private void handleDrawingOpened() {
                if (!drawings.contains(drawing)) {
                    drawings.add(drawing);
                }
                drawing.getGraph().revalidate();
            }

            private void handleDrawingClosed() {
                drawings.remove(drawing);
            }
        });
    }

    /**
     * Adds a drawing to the IDE.
     */
    public Drawing newDrawing(Project project, ScrollingTabbedPane tabbedPane,
            String drawingName) {
        addTabbedPaneChangeListener(tabbedPane, tabbedPaneChangeListener);
        ApplicationControlGraph graph = new ApplicationControlGraph();
        //
        graph.setHeight(getPreferredCanvasHeight());
        graph.setWidth(getPreferredCanvasWidth());
        graph.setBackgroundPaint(getCanvasColor());
        //

        JEFCanvas canvas = new JEFCanvas();
        final Drawing drawing = new Drawing();
        setCurrentDrawing(drawing);
        drawing.setName(drawingName);
        drawing.setGraph(graph);
        drawing.setCanvas(canvas);
        addOpeningAndClosingHooks(drawing);
        graph.setCanvas(canvas);
        project.addDrawing(drawing);
        project.markNotSaved();
        ButtonTabComponent tab = tabbedPane.addCloseableTab(
                drawingName, drawinIcon,
                new JScrollPane(canvas));
        tab.addButtonClosedListener(
                new ButtonTabComponent.ButtonClosedListener() {
            @Override
            public void tabClosed() {
                drawing.setOpen(false);
            }
        });
        drawing.setOpen(true);
        tabbedPane.showTabForTabbedComponent(tab);
        return drawing;
    }
    private Rectangle dropLocation;

    /**
     * @return the preferredCanvasWidth
     */
    public int getPreferredCanvasWidth() {
        if (preferredCanvasWidth == -1) {
            preferredCanvasWidth = getInt(
                    DrawingManagerConstants.CANVAS_PREFERRED_WIDTH,
                    -1);
        }
        return preferredCanvasWidth;
    }

    /**
     * @param preferredCanvasWidth the preferredCanvasWidth to set
     */
    public void setPreferredCanvasWidth(int preferredCanvasWidth) {
        this.preferredCanvasWidth = preferredCanvasWidth;
        if (currentDrawing != null) {
            currentDrawing.getGraph().setWidth(preferredCanvasWidth);
        }
        setInt(preferredCanvasWidth,
                DrawingManagerConstants.CANVAS_PREFERRED_WIDTH);
    }

    private void setInt(int val, DrawingManagerConstants type) {
        try {
            Application.getInstance().setValue(
                    type.getXmlPath(),
                    Integer.toString(val));
        } catch (FuscardXMLException ex) {
            Logger.getLogger(DrawingManager.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
    }

    private int getInt(DrawingManagerConstants type, int deflt) {
        String s = Application.getInstance().getValue(
                type.getXmlPath());
        if (s != null) {
            try {
                deflt = Integer.parseInt(s);
            } catch (NumberFormatException nfe) {
            }
        }
        return deflt;
    }

    private void setColor(Color val, DrawingManagerConstants type) {
        try {
            Application.getInstance().setValue(
                    type.getXmlPath(),
                    Integer.toString(val.getRGB()));
        } catch (FuscardXMLException ex) {
            Logger.getLogger(DrawingManager.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
    }

    private Color getColor(DrawingManagerConstants type, Color deflt) {
        String s = Application.getInstance().getValue(
                type.getXmlPath());
        if (s != null) {
            try {
                deflt = new Color(Integer.parseInt(s));
            } catch (NumberFormatException nfe) {
            }
        }
        return deflt;
    }

    /**
     * @return the preferredCanvasHeight
     */
    public int getPreferredCanvasHeight() {
        if (preferredCanvasHeight == -1) {
            preferredCanvasHeight = getInt(
                    DrawingManagerConstants.CANVAS_PREFERRED_HEIGHT,
                    -1);
        }
        return preferredCanvasHeight;
    }

    /**
     * @param preferredCanvasHeight the preferredCanvasHeight to set
     */
    public void setPreferredCanvasHeight(int preferredCanvasHeight) {
        this.preferredCanvasHeight = preferredCanvasHeight;
        if (currentDrawing != null) {
            currentDrawing.getGraph().setHeight(preferredCanvasHeight);
        }
        setInt(preferredCanvasHeight,
                DrawingManagerConstants.CANVAS_PREFERRED_HEIGHT);
    }

    /**
     * @return the canvasColor
     */
    public Color getCanvasColor() {
        if (Color.WHITE.equals(canvasColor)) {
            canvasColor = getColor(
                    DrawingManagerConstants.CANVAS_PREFERRED_BACKGROUND,
                    Color.WHITE);
        }
        return canvasColor;
    }

    /**
     * @param canvasColor the canvasColor to set
     */
    public void setCanvasColor(Color canvasColor) {
        this.canvasColor = canvasColor;
        if (currentDrawing != null) {
            currentDrawing.getGraph().setBackgroundPaint(canvasColor);
        }
        setColor(canvasColor,
                DrawingManagerConstants.CANVAS_PREFERRED_BACKGROUND);
    }
    
    public void clearDropLocation(){
        dropLocation = null;
        glassPane.repaint();
    }

    public void showDropLocation(Rectangle r) {
        if (currentDrawing == null || glassPane == null) {
            return;
        }
        JEFCanvas canvas = currentDrawing.getCanvas();
        dropLocation = SwingUtilities.convertRectangle(canvas, r, glassPane);
        if (canvasRect != null) {
            dropLocation = dropLocation.intersection(canvasRect);
        }
        glassPane.repaint();
    }

    private void drawOnGlassPane(Graphics g) {
        g.setColor(Color.RED);
        if (canvasRect != null) {
            ((Graphics2D) g).draw(canvasRect);
        }
        g.setColor(dropLocationColor);
        if (dropLocation != null) {
            ((Graphics2D) g).draw(dropLocation);
        }
    }

    /**
     * Class to help us block mouse events when we do not need them interfering
     * with the drawing process
     */
    private class MyGlassPane extends JComponent {

        Point point;

        @Override
        protected void paintComponent(Graphics g) {
            drawOnGlassPane(g);
        }

        public void setPoint(Point p) {
            point = p;
        }

        public MyGlassPane(Container contentPane) {
            UnblockListener listener = new UnblockListener(this, contentPane);
            addMouseListener(listener);
            addMouseMotionListener(listener);
            addMouseWheelListener(listener);
        }
    }
    private Rectangle canvasRect;

    /**
     * Listen for all events and redispatch them to all components except the
     * canvas
     */
    private class UnblockListener extends MouseInputAdapter {

        Toolkit toolkit;
        MyGlassPane glassPane;
        Container contentPane;

        public UnblockListener(MyGlassPane glassPane, Container contentPane) {
            toolkit = Toolkit.getDefaultToolkit();
            this.glassPane = glassPane;
            this.contentPane = contentPane;
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            redispatchMouseEvent(e, true);
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            redispatchMouseEvent(e, false);
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            redispatchMouseEvent(e, false);
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            redispatchMouseEvent(e, true);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            redispatchMouseEvent(e, false);
        }

        @Override
        public void mousePressed(MouseEvent e) {
            redispatchMouseEvent(e, false);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            redispatchMouseEvent(e, true);
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            redispatchMouseEvent(e, false);
        }

        private void dispatchSub(MouseEvent e, Container container,
                Point containerPoint, Point glassPanePoint) {
            Component component =
                    SwingUtilities.getDeepestComponentAt(
                    container,
                    containerPoint.x,
                    containerPoint.y);

            if (component != null) {
//                Point componentPoint = SwingUtilities.convertPoint(
//                        glassPane,
//                        glassPanePoint,
//                        component);
//                component.dispatchEvent(new MouseEvent(component,
//                        e.getID(),
//                        e.getWhen(),
//                        e.getModifiers(),
//                        componentPoint.x,
//                        componentPoint.y,
//                        e.getClickCount(),
//                        e.isPopupTrigger()));
                MouseEvent me = SwingUtilities.convertMouseEvent(glassPane, e,
                        component);
                component.dispatchEvent(me);
            }
        }

        private void redispatchMouseEvent(MouseEvent e,
                boolean repaint) {
            Point glassPanePoint = e.getPoint();
            Container container = contentPane;
            Point containerPoint = SwingUtilities.convertPoint(
                    glassPane,
                    glassPanePoint,
                    contentPane);
            if (currentDrawing == null) {
                glassPane.setVisible(false);
                return;
            }

            //Determine if if is in the drawing area.
            Rectangle visibleCanvasRect = currentDrawing.getCanvas().getVisibleRect();

            Rectangle glassPaneCanvasRect = SwingUtilities.convertRectangle(
                    currentDrawing.getCanvas(), visibleCanvasRect, glassPane);
            canvasRect = glassPaneCanvasRect;
            boolean pointInDrawingArea = glassPaneCanvasRect.contains(
                    glassPanePoint);

            if (pointInDrawingArea) {
                write(">>Point in drawing area");
                /**
                 * Dispatch to all live SimulatingNode's
                 */
                ApplicationControlGraph graph = currentDrawing.getGraph();
                ApplicationUnit applicationUnit = graph.getApplicationUnit();
                LinkedList<Node> nodes = applicationUnit.copyNodes();
                boolean dispatched = false;
                for (Node n : nodes) {
                    if (n instanceof ApplicationSimulatingNode) {
                        ApplicationSimulatingNode asn = (ApplicationSimulatingNode) n;
                        if (asn.isLive()) {
                            //The node is live and we can dispatch event here
                            dispatchSub(e, container, containerPoint,
                                    glassPanePoint);
                            dispatched = true;
                            break;
                        }
                    }
                }
                if (!dispatched) {
                    dispatchSub(e, container, containerPoint,
                            glassPanePoint);
                }
            } else {
                write(">>Point not in drawing area");
                glassPane.setVisible(false);
            }
            //Update the glass pane if requested.
            if (repaint) {
                glassPane.repaint();
            }
        }
    }
}
