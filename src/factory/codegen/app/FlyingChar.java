package factory.codegen.app;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;

/**
 *
 * @author John Mwai
 */
class FlyingChar {

    private char ch;
    private int x;
    private int y;
    private int w;
    private int h;
    private Font font;
    private Color color;
    private Point origin;
    private CreditsPlayer.Speed speed;
    private Point dest;
    private Thread wrapper;
    private boolean flying = false;
    private final CreditsPlayer.PlayerDelegate playerDelegate;

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public Font getFont() {
        return font;
    }

    public FlyingChar(char c, Point p,
            final CreditsPlayer.PlayerDelegate creditPlayerDelegate) {
        this.playerDelegate = creditPlayerDelegate;
        this.ch = c;
        this.x = p.x;
        this.y = p.y;
        creditPlayerDelegate.resetGraphics();
        Graphics g = creditPlayerDelegate.getGraphics();
        g.setFont(font);
        FontMetrics fontMetrics = g.getFontMetrics();
        h = fontMetrics.getHeight();
        w = fontMetrics.charWidth(ch);
    }

    private class FlightManager implements Runnable {

        @Override
        public void run() {
            int n = (int) ((double) speed.time / playerDelegate.updateInterval);
            for (int i = 0; i <= n;
                    i++) {
                try {
                    playerDelegate.checkPlay();
                    x = (int) (origin.x + (double) i / n * (dest.x - origin.x));
                    y = (int) (origin.y + (double) i / n * (dest.y - origin.y));
                    //                        panel.repaint(x, y, w, h);
                    playerDelegate.panel.repaint();

                    Thread.sleep(playerDelegate.updateInterval);
                } catch (InterruptedException ex) {
                    return;
                }
            }
            flying = false;
        }
    }

    public Point getTarget() {
        return flying ? dest : new Point(x, y);
    }

    public void setY(int y) {
        this.y = y;
    }

    public void fly(CreditsPlayer.Speed speed, Point destination) {
        flying = true;
        this.speed = speed;
        dest = destination;
        origin = new Point(x, y);
        FlightManager fm = new FlightManager();
        wrapper = new Thread(fm);
        wrapper.start();
    }

    public void stop() {
        wrapper.interrupt();
    }

    public String getC() {
        return ch + "";
    }

    public void draw(Graphics g) {
        g.setFont(font);
        g.setColor(color);
        g.drawString(getC(), x, y);
    }
}
