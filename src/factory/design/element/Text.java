package factory.design.element;

import factory.design.Box;
import java.awt.Color;
import java.awt.Font;

/**
 *
 * @author John Mwai
 */
public class Text extends Box {

    /**
     * Text value of this Text.
     */
    private String value;
    /**
     * Font to render this text;
     */
    private Font font;
    /**
     * Font color;
     */
    private Color color;
    /**
     * Font size.
     */
    private int fontSize;
    /**
     * Font style.
     */
    private int fontStyle;

    public Text(String value) {
        this.value = value;
        fontSize = 15;
        fontStyle = Font.PLAIN;
        font = new Font("monospaced", fontStyle, fontSize);
        color = Color.BLACK;
    }

    public Text(String value, Font font, Color color, int fontSize,
            int fontStyle) {
        this.value = value;
        this.font = font;
        this.color = color;
        this.fontSize = fontSize;
        this.fontStyle = fontStyle;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return the font
     */
    public Font getFont() {
        return font;
    }

    /**
     * @param font the font to set
     */
    public void setFont(Font font) {
        this.font = font;
    }

    /**
     * @return the color
     */
    public Color getColor() {
        return color;
    }

    /**
     * @param color the color to set
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * @return the fontSize
     */
    public int getFontSize() {
        return fontSize;
    }

    /**
     * @param fontSize the fontSize to set
     */
    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    /**
     * @return the fontStyle
     */
    public int getFontStyle() {
        return fontStyle;
    }

    /**
     * @param fontStyle the fontStyle to set
     */
    public void setFontStyle(int fontStyle) {
        this.fontStyle = fontStyle;
    }
}
