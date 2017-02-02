package factory.codegen.app;

import java.awt.Color;

/**
 *
 * @author John Mwai
 */
public class Theme {

    private String name;
    private Color fg;
    private Color bg;

    public Theme(String name, Color fg, Color bg) {
        this.name = name;
        this.fg = fg;
        this.bg = bg;
    }

    public Color getBg() {
        return bg;
    }

    public Color getTextBg() {
        return computeTextBg();
    }
    private Color textBg;

    private Color computeTextBg() {
        if (textBg == null) {
            int DELTA = 50;
            int red = Math.min(255, getBg().getRed() + DELTA);
            int green = Math.min(255, getBg().getGreen() + DELTA);
            int blue = Math.min(255, getBg().getBlue() + DELTA);

            Color bgc = new Color(red, green, blue);
            textBg = bgc;
//            textBg = getBg().brighter();
        }
        return textBg;
    }

    public void setBg(Color bg) {
        this.bg = bg;
    }

    public Color getFg() {
        return fg;
    }

    public void setFg(Color fg) {
        this.fg = fg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameHTML() {
        String bbg = getHTMLColorString(bg), ffg = getHTMLColorString(fg);
        return "<html><div style=\"width : 150px ; background-color: " + bbg + "; color: " + ffg + "\" >" + name + "</div>";
    }

    private String getHTMLColorString(Color c) {
        String s = Integer.toHexString(c.getRGB());
        s = "#" + s.substring(2);
        return s;
    }
}
