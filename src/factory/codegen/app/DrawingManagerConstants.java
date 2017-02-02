package factory.codegen.app;

/**
 * Properties that the drawing manager saves and retrieves from the
 * configuration file
 *
 * @author John Mwai
 */
public enum DrawingManagerConstants {

    /**
     * Constant for saving preferred width in the configuration file
     */
    CANVAS_PREFERRED_WIDTH("Preferred canvas width",
    "design.canvas.preferred_width"),
    /**
     * Constant for saving preferred canvas height in the configuration file.
     */
    CANVAS_PREFERRED_HEIGHT("Preferred canvas height",
    "design.canvas.preferred_height"),
    /**
     * Constant for saving preferred canvas background in the configuration file.
     */
    CANVAS_PREFERRED_BACKGROUND("Preferred canvas background colour",
    "design.canvas.preferred_background");
    /**
     * Display name of a property
     */
    private String propertyName;
    /**
     * XML path for saving and retrieving a property from the configuration
     * file.
     */
    private String xmlPath;

    /**
     * Create property
     *
     * @param propertyName Display name of the property
     * @param xmlPath XML path for saving the property in the configuration
     * file.
     */
    private DrawingManagerConstants(String propertyName, String xmlPath) {
        this.propertyName = propertyName;
        this.xmlPath = xmlPath;
    }

    /**
     * Get the path to save a property in the configuration file
     *
     * @return The path
     */
    public String getXmlPath() {
        return xmlPath;
    }

    /**
     * Get the display name of an XML property
     *
     * @return The display name
     */
    public String getPropertyName() {
        return propertyName;
    }
}
