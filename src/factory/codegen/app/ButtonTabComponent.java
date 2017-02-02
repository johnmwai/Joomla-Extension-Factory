package factory.codegen.app;

import static factory.Application.invisibleFG;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedList;
import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;

/**
 * Component to be used as tabComponent; Contains a JLabel to show the text and
 * a JButton to close the tab it belongs to
 */
public class ButtonTabComponent extends JPanel implements PropertyChangeListener {

    private final JTabbedPane pane;
    private boolean visibleOnScreen = false;
    private String title;
    private ScrollingTabbedPane stp;
    private JLabel label;

    public ButtonTabComponent(final JTabbedPane pane, ScrollingTabbedPane parent) {

        super(new FlowLayout(FlowLayout.LEFT, 0, 0));
        this.stp = parent;
        if (pane == null) {
            throw new NullPointerException("TabbedPane is null");
        }
        this.pane = pane;
        setOpaque(false);

        //Create an icon
        JLabel iconLabel = new JLabel() {
            @Override
            public Icon getIcon() {
                int i = pane.indexOfTabComponent(ButtonTabComponent.this);
                if (i != -1) {
                    return pane.getIconAt(i);
                }
                return null;
            }
        };
        add(iconLabel);
        //add more space between the label and the button
        iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));

        //make JLabel read titles from JTabbedPane
        label = new JLabel() {
            @Override
            public String getText() {
                int i = pane.indexOfTabComponent(ButtonTabComponent.this);
                if (i != -1) {
                    String s = pane.getTitleAt(i);
                    title = s;
                    return s;
                }
                return null;
            }
        };

        label.addPropertyChangeListener(this);

        label.setForeground(pane.getForeground());

        add(label);

        //add more space between the label and the button
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
        //tab button
        JButton button = new TabButton();
        add(button);
        //add more space to the top of the component
        setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));

        //Add component listener
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentMoved(ComponentEvent e) {
                determineIfVisible();
            }
        });
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

        if (evt.getSource() == label) {
            if ("foreground".equals(evt.getPropertyName())) {
                Color c = (Color) evt.getNewValue();
                boolean invisibleColor = invisibleFG(c);
                if (invisibleColor) {
                    label.setForeground(Color.black);
                }
            }
        }
    }

    public boolean canBeSeen() {
        determineIfVisible();
        return visibleOnScreen;
    }

    public String getTitle() {
        return title;
    }

    public void setFileholder(FileHolder holder) {
        int i = pane.indexOfTabComponent(ButtonTabComponent.this);
        if (i != -1) {
            stp.getFileHolders().put((JComponent) pane.getComponentAt(i), holder);
        }
    }

    public FileHolder getFileHolder() {
        int i = pane.indexOfTabComponent(ButtonTabComponent.this);
        if (i != -1) {
            return stp.getFileHolders().get((JComponent) pane.getComponentAt(i));
        }
        return null;
    }

    public void close() {
        LinkedList<ButtonClosingHook> closingHooks = getClosingHooks();
        LinkedList<ButtonClosedListener> tabClosedListeners = getTabClosedListeners();
        for (ButtonClosingHook hook : closingHooks) {
            if (!hook.tabClosing()) {
                return;
            }
        }
        int i = pane.indexOfTabComponent(ButtonTabComponent.this);
        if (i != -1) {
            JComponent comp = (JComponent) pane.getComponentAt(i);
            stp.getHooks().remove(comp);
            stp.getClosedListeners().remove(comp);
            FileHolder fh = stp.getFileHolders().get(comp);
            if (fh != null) {
                fh.isOpen = false;
            }
            stp.getFileHolders().remove(comp);
            pane.remove(i);
        }
        for (ButtonClosedListener bcl : tabClosedListeners) {
            bcl.tabClosed();
        }
    }

    public void scrollToVisible() {
        JComponent parent = (JComponent) getParent();
        Rectangle bounds = getBounds();
        parent.scrollRectToVisible(bounds);
    }

    private LinkedList<ButtonClosingHook> getClosingHooks() {
        int i = pane.indexOfTabComponent(ButtonTabComponent.this);
        LinkedList<ButtonClosingHook> res = stp.getHooks().get(
                (JComponent) pane.getComponentAt(i));
        return res;
    }

    private LinkedList<ButtonClosedListener> getTabClosedListeners() {
        int i = pane.indexOfTabComponent(ButtonTabComponent.this);
        LinkedList<ButtonClosedListener> res = stp.getClosedListeners().get(
                (JComponent) pane.getComponentAt(i));
        return res;
    }

    public void addClosingHook(ButtonClosingHook hook) {
        LinkedList<ButtonClosingHook> closingHooks = getClosingHooks();
        if (!closingHooks.contains(hook)) {
            closingHooks.add(hook);
        }
    }

    public void addButtonClosedListener(ButtonClosedListener listener) {
        LinkedList<ButtonClosedListener> listeners = getTabClosedListeners();
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }
    public void removeButtonClosedListener(ButtonClosedListener listener) {
        LinkedList<ButtonClosedListener> listeners = getTabClosedListeners();
        if (listeners.contains(listener)) {
            listeners.remove(listener);
        }
    }

    public void removeClosingHook(ButtonClosingHook hook) {
        LinkedList<ButtonClosingHook> closingHooks = getClosingHooks();
        if (closingHooks.contains(hook)) {
            closingHooks.remove(hook);
        }
    }

    private void determineIfVisible() {
        Rectangle r1 = pane.getBounds(), r2 = getBounds();
        r1.setLocation(pane.getLocationOnScreen());
        r2.setLocation(getLocationOnScreen());
        if (r1.contains(r2)) {
            visibleOnScreen = true;
        } else {
            visibleOnScreen = false;
        }
    }

    private class TabButton extends JButton implements ActionListener {

        public TabButton() {
            int size = 17;
            setPreferredSize(new Dimension(size, size));
            setToolTipText("close this tab");
            //Make the button looks the same for all Laf's
            setUI(new BasicButtonUI());
            //Make it transparent
            setContentAreaFilled(false);
            //No need to be focusable
            setFocusable(false);
            setBorder(BorderFactory.createEtchedBorder());
            setBorderPainted(false);
            //Making nice rollover effect
            //we use the same listener for all buttons
            addMouseListener(buttonMouseListener);
            setRolloverEnabled(true);
            //Close the proper tab by clicking the button
            addActionListener(this);
        }

        public void actionPerformed(ActionEvent e) {
            close();

        }

        //we don't want to update UI for this button
        @Override
        public void updateUI() {
        }

        //paint the cross
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            //shift the image for pressed buttons
            if (getModel().isPressed()) {
                g2.translate(1, 1);
            }
            g2.setStroke(new BasicStroke(2));
            g2.setColor(Color.BLACK);
            if (getModel().isRollover()) {
                g2.setColor(Color.MAGENTA);
            }
            int delta = 6;
            g2.drawLine(delta, delta, getWidth() - delta - 1,
                    getHeight() - delta - 1);
            g2.drawLine(getWidth() - delta - 1, delta, delta,
                    getHeight() - delta - 1);
            g2.dispose();
        }
    }
    private final static MouseListener buttonMouseListener = new MouseAdapter() {
        public void mouseEntered(MouseEvent e) {
            Component component = e.getComponent();
            if (component instanceof AbstractButton) {
                AbstractButton button = (AbstractButton) component;
                button.setBorderPainted(true);
            }
        }

        public void mouseExited(MouseEvent e) {
            Component component = e.getComponent();
            if (component instanceof AbstractButton) {
                AbstractButton button = (AbstractButton) component;
                button.setBorderPainted(false);
            }
        }
    };

    public static interface ButtonClosingHook {

        public boolean tabClosing();
    }

    public static interface ButtonClosedListener {

        public void tabClosed();
    }
}
