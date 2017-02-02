/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package factory.codegen.app;

import com.fuscard.commons.GUIUtils;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import javax.swing.Timer;

/**
 *
 * @author John Mwai
 */
public class FuscardLogo extends javax.swing.JDialog {

    private BufferedImage img;
    private Image scaled;
    private static final int MIN_HEIGHT = 50;
    private Point anchor;
    private static String IMAGE_1 = "/images/fuscard-789x1318.png";
    private static String IMAGE_2 = "images/fuscard_modified_1.png";
    //
    private int MARGIN = 20;//Initial margin
    //
    private int imageX = 0;//X location of the image
    private int imageY = 0;//Y location of the image
    private int imageH = 0;//Height of image on screen
    private int imageW = 0;//Width of image on screen

    public static FuscardLogo createNewInstance(final AllSystems allSystems) {
        final FuscardLogo fuscardLogo = new FuscardLogo(allSystems, true);
        final MyTimer myTimer = new MyTimer() {
            @Override
            public void actionPerformed(ActionEvent e) {
                super.actionPerformed(e);
                fuscardLogo.resetImage();
            }
        };

        fuscardLogo.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                Dimension ad = allSystems.getSize();
                ad.height -= 100;
                ad.width -= 200;
                fuscardLogo.setSize(ad);
                fuscardLogo.setLocationRelativeTo(allSystems);
                fuscardLogo.resetImage();
                myTimer.run();
            }

            @Override
            public void componentResized(ComponentEvent e) {
                 myTimer.run();
            }
        });
        fuscardLogo.addWindowListener(new WindowAdapter() {
            @Override
            public void windowStateChanged(WindowEvent e) {
                 myTimer.run();
            }
        });
        return fuscardLogo;
    }

    private static class MyTimer implements ActionListener {

        Timer timer = new Timer(300, this);

        public void run() {
            timer.restart();
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            timer.stop();
        }
    }

    private void initImage() {
        try {
            img = (BufferedImage) GUIUtils.createImage(IMAGE_2);
            /**
             * Assume that the margin m for both sides of the image viewer is
             * the same
             */
            double c; //Clearance of image from right and left margins
            double d; //clearance of image from top and bottom margins
            double r; //Ratio of height of image to width of the image
            double h; //height of image on screen
            double w; //width of image on screen
            double H; //Height of the viewing component
            double W; //Width of viewing component
            int m = MARGIN;//Initial Margin
            W = jPanelImageArea.getWidth();
            H = jPanelImageArea.getHeight();
            w = img.getWidth();
            h = img.getHeight();
            r = h / w;

            //Calculate c and d
            //c = 1/r *(d + m - H/2)+W/2-m --------->(1)
            //Making d = 0
            //c = 1/r *(m - H/2)+W/2-m
            c = 1 / r * (m - H / 2) + W / 2 - m;

            //if c is non negative accept it
            if (!(c < 0)) {
                imageX = (int) (MARGIN + c);
                imageW = (int) (W - 2 * (m + c));
                imageY = MARGIN;
                imageH = (int) (r * imageW);
                refreshImage();
                return;
            }
            //c is negative so use d

            //Making d the subject in (1)
            //d = r*c + m*(r - 1)+1/2(H - W*r)  --------->(2)
            //Making c = 0
            //d = m*(r - 1)+1/2(H - W*r)
            d = m * (r - 1) + 1 / 2 * (H - W * r);

            imageY = (int) (MARGIN + d);
            imageX = MARGIN;
            imageH = (int) (H - 2 * (d + m));
            imageW = (int) (imageH / r);
            refreshImage();

        } catch (Exception e) {
        }
    }

    private void init() {
        initComponents();
        initImage();
        percentZoom = percentageHeight(imageH);
        System.out.println("PC zoom : " + percentZoom);
        jSlider1.setValue(percentZoom);
        jPanelImageArea.repaint();
    }
    private int percentZoom;

    /**
     * Creates new form FuscardLogo
     */
    public FuscardLogo(AllSystems parent, boolean modal) {
        super(parent, modal);
        init();
    }

    public FuscardLogo(Dialog owner, boolean modal) {
        super(owner, modal);
        init();
    }

    /**
     * Gets the percentage that this image needs to be zoomed to fit in the area
     * defined by the height and the width
     *
     * @param height
     * @return
     */
    private int percentageHeight(int height) {
        int imgHeight = img.getHeight();
        if (height >= imgHeight) {
            return 100;
        }
        if (height <= MIN_HEIGHT) {
            System.out.println("height: " + height + " less than min height");
            return 0;
        }
        return (int) ((double) (height - MIN_HEIGHT) / imgHeight * 100);

    }

    private void drawImage(final Graphics2D g2) {
        if (scaled != null) {
            g2.drawImage(scaled, imageX, imageY, imageW, imageH, null);
        }
    }
    private Point pinnedLoc;
    private Dimension pinnedDim;
    private boolean pinned = false;

    private void pinImage(Point p) {
        if (!pinned && p != null) {
            anchor = p;
            pinnedLoc = new Point(imageX, imageY);
            pinnedDim = new Dimension(imageW, imageH);
            pinned = true;
        }
    }

    private void drag(Point p) {
        int dx = p.x - anchor.x;
        int dy = p.y - anchor.y;
        imageX = pinnedLoc.x + dx;
        imageY = pinnedLoc.y + dy;
        jPanelImageArea.repaint();
    }
    private ImageSmoothener imageSmoothener = new ImageSmoothener();

    private void refreshImage() {
        scaled = img;
        imageSmoothener.reDraw();
        jPanelImageArea.repaint();
    }

    private class ImageSmoothener {

        private Thread t;
        private final Object lapseLock = new Object();
        private int interrupted = 0;

        private synchronized void setInterrupted(boolean aBool) {
            if (aBool) {
                interrupted++;
            } else {
                interrupted--;
            }
        }

        private synchronized int getInterrupted() {
            return interrupted;
        }

        private void reDraw() {
            setInterrupted(true);
            if (t != null) {
                t.interrupt();
                try {
                    t.join();
                } catch (InterruptedException ex) {
                }
            }
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    try {
                        lapse();
                    } catch (InterruptedException ex) {
                    }
                    setInterrupted(false);
                    if (getInterrupted() > 0) {
                        return;
                    }
                    Image i = img.getScaledInstance(-1, -1, Image.SCALE_FAST);
                    scaled = i.getScaledInstance(imageW, imageH,
                            Image.SCALE_SMOOTH);
                    jPanelImageArea.repaint();
                }
            };
            t = new Thread(r);
            t.start();
        }

        private void lapse() throws InterruptedException {
            synchronized (lapseLock) {
                lapseLock.wait(1000);
            }
        }
    }

    private void zoom(int value) {
        percentZoom = value;
        if (percentZoom <= 0) {
            percentZoom = 1;
        }
        if (percentZoom > 100) {
            percentZoom = 100;
        }
        if (jSlider1.getValue() != percentZoom) {
            jSlider1.setValue(percentZoom);
        }
        if (anchor == null) {
            obtainDefaultAnchor();
        }
//        System.out.println("img: " + (img != null));
//        System.out.println(
//                "Image X: " + imageX + " Image Y: " + imageY + " Iamge Height: " + imageH + " Image Width: " + imageW);

        double x0, y0;//The initial image X; Y nought: The initial image Y
        double a0, a1;//The initial and final offset of anchor x coordinates inside the image
        double b0, b1;//The initial and final offset of anchor y coordinates inside the image
        double xa, ya;//X offset: The coords of the anchor point
        double w0, w1;//The initial and final width of the image before and after the zoom
        double h0, h1;//The initial and final height of the image before and after the zoom
        double Himg = img.getHeight(), Wimg = img.getWidth();//Dimensions of the image
        double Hmin = MIN_HEIGHT, Wmin = Hmin * Wimg / Himg;//Minimum dimensions of image on screen

        xa = anchor.x;
        ya = anchor.y;
        x0 = pinnedLoc.x;
        y0 = pinnedLoc.y;
        w0 = pinnedDim.width;
        h0 = pinnedDim.height;
        w1 = ((double) percentZoom) / 100 * Wimg + Wmin;
        h1 = ((double) percentZoom) / 100 * Himg + Hmin;
        a0 = xa - x0;
        b0 = ya - y0;
        a1 = a0 * w1 / w0;
        b1 = b0 * h1 / h0;
        imageX = (int) (xa - a1);
        imageY = (int) (ya - b1);
        imageW = (int) w1;
        imageH = (int) h1;
        refreshImage();

    }

    private void obtainDefaultAnchor() {
        int X = (int) ((double) jPanelImageArea.getWidth() / 2);
        int Y = (int) ((double) jPanelImageArea.getHeight() / 2);
        pinImage(new Point(X, Y));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jPanelImageArea = new javax.swing.JPanel(){
            public void paintComponent(Graphics g){
                super.paintComponent(g);
                drawImage((Graphics2D)g);
            }
        };
        jButton1 = new javax.swing.JButton();
        jSlider1 = new javax.swing.JSlider();
        jLabel2 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Fuscard Logo");
        setMinimumSize(null);

        jPanelImageArea.setBackground(new java.awt.Color(255, 255, 255));
        jPanelImageArea.setToolTipText("");
        jPanelImageArea.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                jPanelImageAreaMouseWheelMoved(evt);
            }
        });
        jPanelImageArea.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jPanelImageAreaMouseDragged(evt);
            }
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                jPanelImageAreaMouseMoved(evt);
            }
        });

        javax.swing.GroupLayout jPanelImageAreaLayout = new javax.swing.GroupLayout(jPanelImageArea);
        jPanelImageArea.setLayout(jPanelImageAreaLayout);
        jPanelImageAreaLayout.setHorizontalGroup(
            jPanelImageAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 694, Short.MAX_VALUE)
        );
        jPanelImageAreaLayout.setVerticalGroup(
            jPanelImageAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 445, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(jPanelImageArea);

        jButton1.setText("Close");
        jButton1.setFocusable(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jSlider1.setFocusable(false);
        jSlider1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider1StateChanged(evt);
            }
        });

        jLabel2.setText("Zoom");

        jButton2.setText("Reset");
        jButton2.setFocusPainted(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSlider1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(41, 41, 41)
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1)))
                .addGap(1, 1, 1))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton1)
                        .addComponent(jButton2))
                    .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jSlider1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider1StateChanged
        obtainDefaultAnchor();
        zoom(jSlider1.getValue());
    }//GEN-LAST:event_jSlider1StateChanged

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jPanelImageAreaMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_jPanelImageAreaMouseWheelMoved

        int wheelRotation = evt.getWheelRotation() * evt.getScrollAmount();
        Point p = evt.getPoint();
        pinImage(p);
        int sum = wheelRotation + percentZoom;
        if (sum > 100) {
            sum = 100;

        } else if (sum <= 0) {
            sum = 1;
        }
        zoom(sum);
    }//GEN-LAST:event_jPanelImageAreaMouseWheelMoved

    private void resetImage() {
        initImage();
        percentZoom = percentageHeight(imageH);
        jSlider1.setValue(percentZoom);
        jPanelImageArea.repaint();
    }
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        resetImage();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jPanelImageAreaMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanelImageAreaMouseDragged
        pinImage(evt.getPoint());
        drag(evt.getPoint());
    }//GEN-LAST:event_jPanelImageAreaMouseDragged

    private void jPanelImageAreaMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanelImageAreaMouseMoved
        pinned = false;
    }//GEN-LAST:event_jPanelImageAreaMouseMoved
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanelImageArea;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSlider jSlider1;
    // End of variables declaration//GEN-END:variables
}
