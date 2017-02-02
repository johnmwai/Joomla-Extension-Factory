package factory.design;

import factory.Application;
import factory.codegen.app.AllSystems;
import factory.codegen.app.DrawingManager;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Wraps a entity and allows it to be shown on the screen as a two dimensional
 * box. Boxes allow operations like moving, resizing and they are also used to
 * contain other entities. Because boxes support nesting, they also are free to
 * define constraints on how the nesting affects their dimensions. Should a box
 * expand to allow the added boxes to fit or should it hide the the overflowing
 * content.
 *
 * @author John Mwai
 */
public class Box extends EventProducer<Box.BoxPropertyChangeEvent> implements Displayable {
    //<editor-fold defaultstate="collapsed" desc=" Variable declaration section. ">

    /**
     * Whether children may be added to this Box.
     */
    private boolean allowsChildren;
    /**
     * Top margin.
     */
    private int marginTop;
    /**
     * Bottom margin.
     */
    private int marginBottom;
    /**
     * Right margin.
     */
    private int marginRight;
    /**
     * Left margin.
     */
    private int marginLeft;
    /**
     * Top padding.
     */
    private int paddingTop;
    /**
     * Bottom padding.
     */
    private int paddingBottom;
    /**
     * Right padding.
     */
    private int paddingRight;
    /**
     * Left padding
     */
    private int paddingLeft;
    /**
     * Minimum width
     */
    private int minimumWidth;
    /**
     * Minimum height
     */
    private int minimumHeight;
    /**
     * The x coordinate in the container.
     */
    private int x;
    /**
     * The y coordinate in the container.
     */
    private int y;
    /**
     * Whether this box is absolutely positioned
     */
    private boolean absolutePositioned;
    /**
     * The stacking order of this Box.
     */
    private int zindex;
    /**
     * Height of the box.
     */
    private int height;
    /**
     * Width of the box.
     */
    private int width;
    /**
     * Whether this box is selected.
     */
    private boolean selected;
    /**
     * The color to use to paint the border if selected.
     */
    private Color borderSelectedColor;
    /**
     * The color to use to paint border if not selected.
     */
    private Color borderNormalColor;
    /**
     * Whether to draw border.
     */
    private boolean paintBorder;
    /**
     * The stroke to use to paint border when not selected.
     */
    private Stroke borderNormalStroke;
    /**
     * The stroke to use on the border when the Box is selected.
     */
    private Stroke borderSelectedStroke;
    /**
     * The paint to use on the background of the box.
     */
    private Paint backgroundPaint;
    /**
     * The children of this Box. This box acts as the manager of the Children
     * boxes with respect to the children settings.
     */
    private LinkedList<Box> children;
    /**
     * Whether the Box is visible on the screen.
     */
    private boolean visible;
    /**
     * Whether this Box keyboard events can be received by components within the
     * bounds of this Box.
     */
    private boolean live;
    /**
     * North east border radius.
     */
    private int northEastBorderRadius;
    /**
     * North west border radius.
     */
    private int northWestBorderRadius;
    /**
     * South west border radius.
     */
    private int southEastBorderRadius;
    /**
     * South west border radius.
     */
    private int southWestBorderRadius;
    /**
     * Foreground color.
     */
    private Color foregroundColor;
    /**
     * Provides scroll bars on overflow.
     */
    private boolean overflowScrolls;
    /**
     * Adjusts width to fit widening contents.
     */
    private boolean horizontalResizing;
    /**
     * Adjusts height to fit contents if they overflow vertically.
     */
    private boolean verticalResizing;
    /**
     * Whether the mouse is in the Box.
     */
    private boolean mouseInside;
    /**
     * Whether children are arranged in from left to right. If they are not
     * arranged from left to right they are arranged from top to bottom.
     */
    private boolean horizontalChildrenArranging;
    /**
     * Whether this box draws its border
     */
    private boolean drawsBorder;
    /**
     * Node associated with this Box
     */
    private ApplicationSimulatingNode applicationSimulatingNode;
    //</editor-fold>

    /**
     * Create a box with the default settings.
     */
    public Box() {
        allowsChildren = true;
        marginBottom = 10;
        marginLeft = 10;
        marginRight = 10;
        marginTop = 10;
        paddingBottom = 10;
        paddingLeft = 10;
        paddingRight = 10;
        paddingTop = 10;
        minimumHeight = 10;
        minimumWidth = 10;
        selected = false;
        borderSelectedColor = Color.RED;
        borderNormalColor = Color.GRAY;
        paintBorder = true;
        borderNormalStroke = new BasicStroke(1);
        borderSelectedStroke = new BasicStroke(2);
        visible = true;
        live = true;
        northEastBorderRadius = 5;
        northWestBorderRadius = 5;
        southEastBorderRadius = 5;
        southWestBorderRadius = 5;
        foregroundColor = Color.BLACK;
        overflowScrolls = true;
        verticalResizing = false;
        horizontalResizing = true;
        horizontalChildrenArranging = true;
        applicationSimulatingNode = new ApplicationSimulatingNode(this);
        children = new LinkedList<>();
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(getX(), getY(),
                getWidth() + getMarginLeft() + getMarginRight(),
                getHeight() + getMarginTop() + getMarginBottom());
    }

    public Rectangle getPerimeterBounds() {
        return new Rectangle(new Point(x + marginLeft, y + marginTop),
                new Dimension(width, height));
    }

    @Override
    public void setBounds(Rectangle bounds) {
        Point p = bounds.getLocation();
        Dimension d = bounds.getSize();
        setX(p.x);
        setY(p.y);
        setHeight(d.height);
        setWidth(d.width);
    }
    private final Object childLock = new Object();

    private LinkedList<Box> copyChildren() {
        synchronized (childLock) {
            return new LinkedList<>(getChildren());
        }
    }

    /**
     * Add the child to the box at the specific location
     *
     * @param child The child to add
     * @param p The location to add the child
     */
    public void addChild(Box child, Point p) {
        if (!isAllowsChildren() || !getPerimeterBounds().contains(p)) {
            return;
        }
        synchronized (childLock) {
            if (!children.contains(child)) {
                children.add(child);
            }
        }
    }

    public void removeChild(Box child) {
        synchronized (childLock) {
            for (Box b : copyChildren()) {
                if (b == child) {
                    children.remove(b);
                }
            }
        }
    }

    /**
     * Show where on the Box the child would be put if the child was dropped
     *
     * @param testBox The box to test
     * @param p The point of the cursor
     */
    public void testChildLocation(Box testBox, Point p) {
        DrawingManager dm = Application.getInstance().getDrawingManager();
        if (!isAllowsChildren() || !getPerimeterBounds().contains(p)) {
            dm.clearDropLocation();
            return;
        }
        Rectangle rr = getChildBounds(testBox, p);
        if (rr != null) {
            dm.showDropLocation(rr);
        } else {
            dm.clearDropLocation();
        }
    }

    /**
     * Determines the dimensions that this child would have if it was to be
     * added to this parent
     *
     * @param testBox The box to add
     * @param p The point at which to add it
     * @return The bounds that this child would have.
     */
    private Rectangle getChildBounds(Box testBox, Point p) {
        Point loc;
        Dimension dim;
        if (children.isEmpty()) {
            loc = new Point(getX() + getMarginLeft() + getPaddingLeft(),
                    getY() + getMarginTop() + getPaddingTop());
            int bw, bh;
            if (testBox.isHorizontalResizing()) {
                bw = getWidth() - getPaddingLeft() - getPaddingRight();
                bw = Math.max(testBox.getWidth(), bw);
            } else {
                bw = testBox.getWidth();
                bw = Math.max(bw, testBox.getMinimumWidth());
            }
            if (testBox.isVerticalResizing()) {
                bh = getHeight() - getPaddingTop() - getPaddingBottom();
                bh = Math.max(testBox.getHeight(), bh);
            } else {
                bh = testBox.getHeight();
                bh = Math.max(bh, testBox.getMinimumHeight());
            }
            dim = new Dimension(bw, bh);
            Rectangle r = new Rectangle(loc, dim);
            return r;
        } else {
            return null;
        }
    }

    //<editor-fold defaultstate="collapsed" desc="Generated Methods">
    /**
     * @param allowsChildren the allowsChildren to set
     */
    public void setAllowsChildren(boolean allowsChildren) {
        boolean old = this.allowsChildren;
        this.allowsChildren = allowsChildren;
        if (old != allowsChildren) {
            firePropertyChangeEvent(new BoxPropertyChangeEvent(
                    BoxPropertyType.ALLOWS_CHILDREN, this, old, allowsChildren));
        }
    }

    /**
     * @return allowsChildren the allowsChildren
     */
    public boolean isAllowsChildren() {
        return this.allowsChildren;
    }

    /**
     * @param marginTop the marginTop to set
     */
    public void setMarginTop(int marginTop) {
        int old = this.marginTop;
        this.marginTop = marginTop;
        if (old != marginTop) {
            firePropertyChangeEvent(new BoxPropertyChangeEvent(
                    BoxPropertyType.MARGIN_TOP, this, old, marginTop));
        }
    }

    /**
     * @return marginTop the marginTop
     */
    public int getMarginTop() {
        return this.marginTop;
    }

    /**
     * @param marginBottom the marginBottom to set
     */
    public void setMarginBottom(int marginBottom) {
        int old = this.marginBottom;
        this.marginBottom = marginBottom;
        if (old != marginBottom) {
            firePropertyChangeEvent(new BoxPropertyChangeEvent(
                    BoxPropertyType.MARGIN_BOTTOM, this, old, marginBottom));
        }
    }

    /**
     * @return marginBottom the marginBottom
     */
    public int getMarginBottom() {
        return this.marginBottom;
    }

    /**
     * @param marginRight the marginRight to set
     */
    public void setMarginRight(int marginRight) {
        int old = this.marginRight;
        this.marginRight = marginRight;
        if (old != marginRight) {
            firePropertyChangeEvent(new BoxPropertyChangeEvent(
                    BoxPropertyType.MARGIN_RIGHT, this, old, marginRight));
        }
    }

    /**
     * @return marginRight the marginRight
     */
    public int getMarginRight() {
        return this.marginRight;
    }

    /**
     * @param marginLeft the marginLeft to set
     */
    public void setMarginLeft(int marginLeft) {
        int old = this.marginLeft;
        this.marginLeft = marginLeft;
        if (old != marginLeft) {
            firePropertyChangeEvent(new BoxPropertyChangeEvent(
                    BoxPropertyType.MARGIN_LEFT, this, old, marginLeft));
        }
    }

    /**
     * @return marginLeft the marginLeft
     */
    public int getMarginLeft() {
        return this.marginLeft;
    }

    /**
     * @param paddingTop the paddingTop to set
     */
    public void setPaddingTop(int paddingTop) {
        int old = this.paddingTop;
        this.paddingTop = paddingTop;
        if (old != paddingTop) {
            firePropertyChangeEvent(new BoxPropertyChangeEvent(
                    BoxPropertyType.PADDING_TOP, this, old, paddingTop));
        }
    }

    /**
     * @return paddingTop the paddingTop
     */
    public int getPaddingTop() {
        return this.paddingTop;
    }

    /**
     * @param paddingBottom the paddingBottom to set
     */
    public void setPaddingBottom(int paddingBottom) {
        int old = this.paddingBottom;
        this.paddingBottom = paddingBottom;
        if (old != paddingBottom) {
            firePropertyChangeEvent(new BoxPropertyChangeEvent(
                    BoxPropertyType.PADDING_BOTTOM, this, old, paddingBottom));
        }
    }

    /**
     * @return paddingBottom the paddingBottom
     */
    public int getPaddingBottom() {
        return this.paddingBottom;
    }

    /**
     * @param paddingRight the paddingRight to set
     */
    public void setPaddingRight(int paddingRight) {
        int old = this.paddingRight;
        this.paddingRight = paddingRight;
        if (old != paddingRight) {
            firePropertyChangeEvent(new BoxPropertyChangeEvent(
                    BoxPropertyType.PADDING_RIGHT, this, old, paddingRight));
        }
    }

    /**
     * @return paddingRight the paddingRight
     */
    public int getPaddingRight() {
        return this.paddingRight;
    }

    /**
     * @param paddingLeft the paddingLeft to set
     */
    public void setPaddingLeft(int paddingLeft) {
        int old = this.paddingLeft;
        this.paddingLeft = paddingLeft;
        if (old != paddingLeft) {
            firePropertyChangeEvent(new BoxPropertyChangeEvent(
                    BoxPropertyType.PADDING_LEFT, this, old, paddingLeft));
        }
    }

    /**
     * @return paddingLeft the paddingLeft
     */
    public int getPaddingLeft() {
        return this.paddingLeft;
    }

    /**
     * @param minimumWidth the minimumWidth to set
     */
    public void setMinimumWidth(int minimumWidth) {
        int old = this.minimumWidth;
        this.minimumWidth = minimumWidth;
        if (old != minimumWidth) {
            firePropertyChangeEvent(new BoxPropertyChangeEvent(
                    BoxPropertyType.MINIMUM_WIDTH, this, old, minimumWidth));
        }
    }

    /**
     * @return minimumWidth the minimumWidth
     */
    public int getMinimumWidth() {
        return this.minimumWidth;
    }

    /**
     * @param minimumHeight the minimumHeight to set
     */
    public void setMinimumHeight(int minimumHeight) {
        int old = this.minimumHeight;
        this.minimumHeight = minimumHeight;
        if (old != minimumHeight) {
            firePropertyChangeEvent(new BoxPropertyChangeEvent(
                    BoxPropertyType.MINIMUM_HEIGHT, this, old, minimumHeight));
        }
    }

    /**
     * @return minimumHeight the minimumHeight
     */
    public int getMinimumHeight() {
        return this.minimumHeight;
    }

    /**
     * @param x the x to set
     */
    public void setX(int x) {
        int old = this.x;
        this.x = x;
        if (old != x) {
            firePropertyChangeEvent(new BoxPropertyChangeEvent(
                    BoxPropertyType.X, this, old, x));
        }
    }

    /**
     * @return x the x
     */
    public int getX() {
        return this.x;
    }

    /**
     * @param y the y to set
     */
    public void setY(int y) {
        int old = this.y;
        this.y = y;
        if (old != y) {
            firePropertyChangeEvent(new BoxPropertyChangeEvent(
                    BoxPropertyType.Y, this, old, y));
        }
    }

    /**
     * @return y the y
     */
    public int getY() {
        return this.y;
    }

    /**
     * @param absolutePositioned the absolutePositioned to set
     */
    public void setAbsolutePositioned(boolean absolutePositioned) {
        boolean old = this.absolutePositioned;
        this.absolutePositioned = absolutePositioned;
        if (old != absolutePositioned) {
            firePropertyChangeEvent(new BoxPropertyChangeEvent(
                    BoxPropertyType.ABSOLUTE_POSITIONED, this, old,
                    absolutePositioned));
        }
    }

    /**
     * @return absolutePositioned the absolutePositioned
     */
    public boolean isAbsolutePositioned() {
        return this.absolutePositioned;
    }

    /**
     * @param zindex the zindex to set
     */
    public void setZindex(int zindex) {
        int old = this.zindex;
        this.zindex = zindex;
        if (old != zindex) {
            firePropertyChangeEvent(new BoxPropertyChangeEvent(
                    BoxPropertyType.ZINDEX, this, old, zindex));
        }
    }

    /**
     * @return zindex the zindex
     */
    public int getZindex() {
        return this.zindex;
    }

    /**
     * @param height the height to set
     */
    public void setHeight(int height) {
        int old = this.height;
        this.height = height;
        if (old != height) {
            firePropertyChangeEvent(new BoxPropertyChangeEvent(
                    BoxPropertyType.HEIGHT, this, old, height));
        }
    }

    /**
     * @return height the height
     */
    public int getHeight() {
        return this.height;
    }

    /**
     * @param width the width to set
     */
    public void setWidth(int width) {
        int old = this.width;
        this.width = width;
        if (old != width) {
            firePropertyChangeEvent(new BoxPropertyChangeEvent(
                    BoxPropertyType.WIDTH, this, old, width));
        }
    }

    /**
     * @return width the width
     */
    public int getWidth() {
        return this.width;
    }

    /**
     * @param selected the selected to set
     */
    public void setSelected(boolean selected) {
        boolean old = this.selected;
        this.selected = selected;
        if (old != selected) {
            firePropertyChangeEvent(new BoxPropertyChangeEvent(
                    BoxPropertyType.SELECTED, this, old, selected));
        }
    }

    /**
     * @return selected the selected
     */
    public boolean isSelected() {
        return this.selected;
    }

    /**
     * @param borderSelectedColor the borderSelectedColor to set
     */
    public void setBorderSelectedColor(java.awt.Color borderSelectedColor) {
        java.awt.Color old = this.borderSelectedColor;
        this.borderSelectedColor = borderSelectedColor;
        if (old == null || !old.equals(borderSelectedColor)) {
            firePropertyChangeEvent(new BoxPropertyChangeEvent(
                    BoxPropertyType.BORDER_SELECTED_COLOR, this, old,
                    borderSelectedColor));
        }
    }

    /**
     * @return borderSelectedColor the borderSelectedColor
     */
    public java.awt.Color getBorderSelectedColor() {
        return this.borderSelectedColor;
    }

    /**
     * @param borderNormalColor the borderNormalColor to set
     */
    public void setBorderNormalColor(java.awt.Color borderNormalColor) {
        java.awt.Color old = this.borderNormalColor;
        this.borderNormalColor = borderNormalColor;
        if (old == null || !old.equals(borderNormalColor)) {
            firePropertyChangeEvent(new BoxPropertyChangeEvent(
                    BoxPropertyType.BORDER_NORMAL_COLOR, this, old,
                    borderNormalColor));
        }
    }

    /**
     * @return borderNormalColor the borderNormalColor
     */
    public java.awt.Color getBorderNormalColor() {
        return this.borderNormalColor;
    }

    /**
     * @param paintBorder the paintBorder to set
     */
    public void setPaintBorder(boolean paintBorder) {
        boolean old = this.paintBorder;
        this.paintBorder = paintBorder;
        if (old != paintBorder) {
            firePropertyChangeEvent(new BoxPropertyChangeEvent(
                    BoxPropertyType.PAINT_BORDER, this, old, paintBorder));
        }
    }

    /**
     * @return paintBorder the paintBorder
     */
    public boolean isPaintBorder() {
        return this.paintBorder;
    }

    /**
     * @param borderNormalStroke the borderNormalStroke to set
     */
    public void setBorderNormalStroke(java.awt.Stroke borderNormalStroke) {
        java.awt.Stroke old = this.borderNormalStroke;
        this.borderNormalStroke = borderNormalStroke;
        if (old == null || !old.equals(borderNormalStroke)) {
            firePropertyChangeEvent(new BoxPropertyChangeEvent(
                    BoxPropertyType.BORDER_NORMAL_STROKE, this, old,
                    borderNormalStroke));
        }
    }

    /**
     * @return borderNormalStroke the borderNormalStroke
     */
    public java.awt.Stroke getBorderNormalStroke() {
        return this.borderNormalStroke;
    }

    /**
     * @param borderSelectedStroke the borderSelectedStroke to set
     */
    public void setBorderSelectedStroke(java.awt.Stroke borderSelectedStroke) {
        java.awt.Stroke old = this.borderSelectedStroke;
        this.borderSelectedStroke = borderSelectedStroke;
        if (old == null || !old.equals(borderSelectedStroke)) {
            firePropertyChangeEvent(new BoxPropertyChangeEvent(
                    BoxPropertyType.BORDER_SELECTED_STROKE, this, old,
                    borderSelectedStroke));
        }
    }

    /**
     * @return borderSelectedStroke the borderSelectedStroke
     */
    public java.awt.Stroke getBorderSelectedStroke() {
        return this.borderSelectedStroke;
    }

    /**
     * @param backgroundPaint the backgroundPaint to set
     */
    public void setBackgroundPaint(java.awt.Paint backgroundPaint) {
        java.awt.Paint old = this.backgroundPaint;
        this.backgroundPaint = backgroundPaint;
        if (old == null || !old.equals(backgroundPaint)) {
            firePropertyChangeEvent(new BoxPropertyChangeEvent(
                    BoxPropertyType.BACKGROUND_PAINT, this, old, backgroundPaint));
        }
    }

    /**
     * @return backgroundPaint the backgroundPaint
     */
    public java.awt.Paint getBackgroundPaint() {
        return this.backgroundPaint;
    }

    /**
     * @param children the children to set
     */
    public void setChildren(java.util.LinkedList children) {
        java.util.LinkedList old = this.children;
        this.children = children;
        if (old == null || !old.equals(children)) {
            firePropertyChangeEvent(new BoxPropertyChangeEvent(
                    BoxPropertyType.CHILDREN, this, old, children));
        }
    }

    /**
     * @return children the children
     */
    public java.util.LinkedList getChildren() {
        return this.children;
    }

    /**
     * @param visible the visible to set
     */
    public void setVisible(boolean visible) {
        boolean old = this.visible;
        this.visible = visible;
        if (old != visible) {
            firePropertyChangeEvent(new BoxPropertyChangeEvent(
                    BoxPropertyType.VISIBLE, this, old, visible));
        }
    }

    /**
     * @return visible the visible
     */
    public boolean isVisible() {
        return this.visible;
    }

    /**
     * @param live the live to set
     */
    public void setLive(boolean live) {
        boolean old = this.live;
        this.live = live;
        if (old != live) {
            firePropertyChangeEvent(new BoxPropertyChangeEvent(
                    BoxPropertyType.LIVE, this, old, live));
        }
    }

    /**
     * @return live the live
     */
    public boolean isLive() {
        return this.live;
    }

    /**
     * @param northEastBorderRadius the northEastBorderRadius to set
     */
    public void setNorthEastBorderRadius(int northEastBorderRadius) {
        int old = this.northEastBorderRadius;
        this.northEastBorderRadius = northEastBorderRadius;
        if (old != northEastBorderRadius) {
            firePropertyChangeEvent(new BoxPropertyChangeEvent(
                    BoxPropertyType.NORTH_EAST_BORDER_RADIUS, this, old,
                    northEastBorderRadius));
        }
    }

    /**
     * @return northEastBorderRadius the northEastBorderRadius
     */
    public int getNorthEastBorderRadius() {
        return this.northEastBorderRadius;
    }

    /**
     * @param northWestBorderRadius the northWestBorderRadius to set
     */
    public void setNorthWestBorderRadius(int northWestBorderRadius) {
        int old = this.northWestBorderRadius;
        this.northWestBorderRadius = northWestBorderRadius;
        if (old != northWestBorderRadius) {
            firePropertyChangeEvent(new BoxPropertyChangeEvent(
                    BoxPropertyType.NORTH_WEST_BORDER_RADIUS, this, old,
                    northWestBorderRadius));
        }
    }

    /**
     * @return northWestBorderRadius the northWestBorderRadius
     */
    public int getNorthWestBorderRadius() {
        return this.northWestBorderRadius;
    }

    /**
     * @param southEastBorderRadius the southEastBorderRadius to set
     */
    public void setSouthEastBorderRadius(int southEastBorderRadius) {
        int old = this.southEastBorderRadius;
        this.southEastBorderRadius = southEastBorderRadius;
        if (old != southEastBorderRadius) {
            firePropertyChangeEvent(new BoxPropertyChangeEvent(
                    BoxPropertyType.SOUTH_EAST_BORDER_RADIUS, this, old,
                    southEastBorderRadius));
        }
    }

    /**
     * @return southEastBorderRadius the southEastBorderRadius
     */
    public int getSouthEastBorderRadius() {
        return this.southEastBorderRadius;
    }

    /**
     * @param southWestBorderRadius the southWestBorderRadius to set
     */
    public void setSouthWestBorderRadius(int southWestBorderRadius) {
        int old = this.southWestBorderRadius;
        this.southWestBorderRadius = southWestBorderRadius;
        if (old != southWestBorderRadius) {
            firePropertyChangeEvent(new BoxPropertyChangeEvent(
                    BoxPropertyType.SOUTH_WEST_BORDER_RADIUS, this, old,
                    southWestBorderRadius));
        }
    }

    /**
     * @return southWestBorderRadius the southWestBorderRadius
     */
    public int getSouthWestBorderRadius() {
        return this.southWestBorderRadius;
    }

    /**
     * @param foregroundColor the foregroundColor to set
     */
    public void setForegroundColor(java.awt.Color foregroundColor) {
        java.awt.Color old = this.foregroundColor;
        this.foregroundColor = foregroundColor;
        if (old == null || !old.equals(foregroundColor)) {
            firePropertyChangeEvent(new BoxPropertyChangeEvent(
                    BoxPropertyType.FOREGROUND_COLOR, this, old, foregroundColor));
        }
    }

    /**
     * @return foregroundColor the foregroundColor
     */
    public java.awt.Color getForegroundColor() {
        return this.foregroundColor;
    }

    /**
     * @param overflowScrolls the overflowScrolls to set
     */
    public void setOverflowScrolls(boolean overflowScrolls) {
        boolean old = this.overflowScrolls;
        this.overflowScrolls = overflowScrolls;
        if (old != overflowScrolls) {
            firePropertyChangeEvent(new BoxPropertyChangeEvent(
                    BoxPropertyType.OVERFLOW_SCROLLS, this, old, overflowScrolls));
        }
    }

    /**
     * @return overflowScrolls the overflowScrolls
     */
    public boolean isOverflowScrolls() {
        return this.overflowScrolls;
    }

    /**
     * @param horizontalResizing the horizontalResizing to set
     */
    public void setHorizontalResizing(boolean horizontalResizing) {
        boolean old = this.horizontalResizing;
        this.horizontalResizing = horizontalResizing;
        if (old != horizontalResizing) {
            firePropertyChangeEvent(new BoxPropertyChangeEvent(
                    BoxPropertyType.HORIZONTAL_RESIZING, this, old,
                    horizontalResizing));
        }
    }

    /**
     * @return horizontalResizing the horizontalResizing
     */
    public boolean isHorizontalResizing() {
        return this.horizontalResizing;
    }

    /**
     * @param verticalResizing the verticalResizing to set
     */
    public void setVerticalResizing(boolean verticalResizing) {
        boolean old = this.verticalResizing;
        this.verticalResizing = verticalResizing;
        if (old != verticalResizing) {
            firePropertyChangeEvent(new BoxPropertyChangeEvent(
                    BoxPropertyType.VERTICAL_RESIZING, this, old,
                    verticalResizing));
        }
    }

    /**
     * @return verticalResizing the verticalResizing
     */
    public boolean isVerticalResizing() {
        return this.verticalResizing;
    }

    /**
     * @param mouseInside the mouseInside to set
     */
    public void setMouseInside(boolean mouseInside) {
        boolean old = this.mouseInside;
        this.mouseInside = mouseInside;
        if (old != mouseInside) {
            firePropertyChangeEvent(new BoxPropertyChangeEvent(
                    BoxPropertyType.MOUSE_INSIDE, this, old, mouseInside));
        }
    }

    /**
     * @return mouseInside the mouseInside
     */
    public boolean isMouseInside() {
        return this.mouseInside;
    }

    /**
     * @param horizontalChildrenArranging the horizontalChildrenArranging to set
     */
    public void setHorizontalChildrenArranging(
            boolean horizontalChildrenArranging) {
        boolean old = this.horizontalChildrenArranging;
        this.horizontalChildrenArranging = horizontalChildrenArranging;
        if (old != horizontalChildrenArranging) {
            firePropertyChangeEvent(new BoxPropertyChangeEvent(
                    BoxPropertyType.HORIZONTAL_CHILDREN_ARRANGING, this, old,
                    horizontalChildrenArranging));
        }
    }

    /**
     * @return horizontalChildrenArranging the horizontalChildrenArranging
     */
    public boolean isHorizontalChildrenArranging() {
        return this.horizontalChildrenArranging;
    }

    /**
     * @param drawsBorder the drawsBorder to set
     */
    public void setDrawsBorder(boolean drawsBorder) {
        boolean old = this.drawsBorder;
        this.drawsBorder = drawsBorder;
        if (old != drawsBorder) {
            firePropertyChangeEvent(new BoxPropertyChangeEvent(
                    BoxPropertyType.DRAWS_BORDER, this, old, drawsBorder));
        }
    }

    /**
     * @return drawsBorder the drawsBorder
     */
    public boolean isDrawsBorder() {
        return this.drawsBorder;
    }

    /**
     * @param applicationSimulatingNode the applicationSimulatingNode to set
     */
    public void setApplicationSimulatingNode(
            factory.design.ApplicationSimulatingNode applicationSimulatingNode) {
        factory.design.ApplicationSimulatingNode old = this.applicationSimulatingNode;
        this.applicationSimulatingNode = applicationSimulatingNode;
        if (old == null || !old.equals(applicationSimulatingNode)) {
            firePropertyChangeEvent(new BoxPropertyChangeEvent(
                    BoxPropertyType.APPLICATION_SIMULATING_NODE, this, old,
                    applicationSimulatingNode));
        }
    }

    /**
     * @return applicationSimulatingNode the applicationSimulatingNode
     */
    public factory.design.ApplicationSimulatingNode getApplicationSimulatingNode() {
        return this.applicationSimulatingNode;
    }

    /**
     * Type of properties supported by BoxPropertyChangeEvent
     */
    public static enum BoxPropertyType {

        CANVAS,
        ALLOWS_CHILDREN,
        MARGIN_TOP,
        MARGIN_BOTTOM,
        MARGIN_RIGHT,
        MARGIN_LEFT,
        PADDING_TOP,
        PADDING_BOTTOM,
        PADDING_RIGHT,
        PADDING_LEFT,
        MINIMUM_WIDTH,
        MINIMUM_HEIGHT,
        X,
        Y,
        ABSOLUTE_POSITIONED,
        ZINDEX,
        HEIGHT,
        WIDTH,
        SELECTED,
        BORDER_SELECTED_COLOR,
        BORDER_NORMAL_COLOR,
        PAINT_BORDER,
        BORDER_NORMAL_STROKE,
        BORDER_SELECTED_STROKE,
        BACKGROUND_PAINT,
        CHILDREN,
        VISIBLE,
        LIVE,
        NORTH_EAST_BORDER_RADIUS,
        NORTH_WEST_BORDER_RADIUS,
        SOUTH_EAST_BORDER_RADIUS,
        SOUTH_WEST_BORDER_RADIUS,
        FOREGROUND_COLOR,
        OVERFLOW_SCROLLS,
        HORIZONTAL_RESIZING,
        VERTICAL_RESIZING,
        MOUSE_INSIDE,
        HORIZONTAL_CHILDREN_ARRANGING,
        DRAWS_BORDER,
        APPLICATION_SIMULATING_NODE,
    }
//</editor-fold>

    @Override
    public void draw(Graphics2D g) {
        g.setPaint(getBackgroundPaint());
        g.fill(getPerimeterBounds());
        if (isDrawsBorder()) {
            g.setColor(getForegroundColor());
            g.draw(getPerimeterBounds());
        }
        //Draw the children
        for (Box b : copyChildren()) {
            b.draw(g);
        }
    }

    /**
     * An event fired whenever there is a property change in the Box
     */
    public static class BoxPropertyChangeEvent extends PropertyChangeEvent<BoxPropertyType, Box, Object> {

        public BoxPropertyChangeEvent() {
        }

        public BoxPropertyChangeEvent(BoxPropertyType type, Box source,
                Object oldValue, Object newValue) {
            super(type, source, oldValue, newValue);
        }
    }
    //<editor-fold defaultstate="collapsed" desc=" Development Area. ">

    private static String capitalize(String s) {
        if ("".equals(s)) {
            return "";
        }
        return s.toUpperCase().substring(0, 1) + s.substring(1);
    }

    private static String addUnderScores(String input) {
        Pattern p = Pattern.compile("[A-Z]");
        Matcher m = p.matcher(input);
        String output = "";
        int s = 0;
        boolean matched = false;
        while (m.find()) {
            matched = true;
            output += input.substring(s, m.start()) + "_";
            s = m.start();
        }
        if (matched) {
            output += input.substring(s);
        } else {
            output = input;
        }
        return output;
    }

    private static void generateSettersAndGetters() {
        System.out.println(
                "//<editor-fold defaultstate=\"collapsed\" desc=\"Generated Methods\">");
        for (Field f : Box.class.getDeclaredFields()) {
            //            System.out.println(f.getName().toString().toUpperCase() + ",");
            //            System.out.println(f.getType().getName());
            String typeName = f.getType().getName();
            String fieldName = f.getName();
            String fn = addUnderScores(fieldName);
            String propertyName = fn.toUpperCase();
            boolean isBoolean = f.getType() == Boolean.TYPE;
            boolean isPrimitive = f.getType().isPrimitive();
            /**
             * <pre>
             *
             * public void setMarginTop(int marginTop) {
             *      int old = this.marginTop;
             *      this.marginTop = marginTop;
             *      if (old != marginTop) {
             *          firePropertyChangeEvent(new BoxPropertyChangeEvent(
             *                  BoxPropertyType.MARGINTOP, this, old, marginTop));
             *      }
             *  }
             * </pre>
             */
            String s = "/**\n";
            s += "*@param " + fieldName + " the " + fieldName + " to set\n";
            s += "*/\n";
            s += "public void set" + capitalize(fieldName) + "(" + typeName + " " + fieldName + ") {\n";
            s += "" + typeName + " old = this." + fieldName + ";\n";
            s += "this." + fieldName + " = " + fieldName + ";\n";
            if (isPrimitive) {
                s += "if (old != " + fieldName + ") {\n";
            } else {
                s += "if (old == null || !old.equals(" + fieldName + ")) {\n";
            }
            s += "firePropertyChangeEvent(new BoxPropertyChangeEvent(\n";
            s += "BoxPropertyType." + propertyName + ", this, old, " + fieldName + "));\n";
            s += "}\n";
            s += "}\n";
            s += "\n";
            s += "/**\n";
            s += "*@return " + fieldName + " the " + fieldName + "\n";
            s += "*/\n";
            String theGet = isBoolean ? "is" : "get";
            s += "public " + typeName + " " + theGet + capitalize(fieldName) + "() {\n";
            s += "return this." + fieldName + ";\n";
            s += "}\n";
            s += "\n";

            System.out.println(s);
        }
        String s = "\n";
        s += " /**\n";
        s += " * Type of properties supported by BoxPropertyChangeEvent\n";
        s += "*/\n";
        s += "public static enum BoxPropertyType {\n";
        s += "\n";
        s += "CANVAS,\n";
        s += "\n";
        System.out.println(s);
        for (Field f : Box.class.getDeclaredFields()) {
            String fieldName = f.getName();
            String fn = addUnderScores(fieldName);
            String propertyName = fn.toUpperCase();

            System.out.println(propertyName + ",");
        }
        System.out.println("}");
        System.out.println("//</editor-fold>\n\n\n");
    }

    public static void main(String[] args) {
        //        generateSettersAndGetters();
    }
    //</editor-fold>
}
