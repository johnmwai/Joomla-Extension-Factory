package factory.codegen.app;

import java.nio.file.Path;
import javax.swing.JComponent;

/**
 *
 * @author John Mwai
 */
public class FileHolder {

    Path file;
    boolean isOpen = false;
    JComponent component;
    String name;

    @Override
    public String toString() {
        return name;
    }
}
