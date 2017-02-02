/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package factory.parsing.javascript;

import java.awt.Color;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author John Mwai
 */
public class ColorTableCellRenderer extends JLabel
        implements TableCellRenderer {

    private Border unselectedBorder = null;
    private Border selectedBorder = null;

    public ColorTableCellRenderer() {
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object color,
            boolean isSelected, boolean hasFocus, int row, int column) {
        Color newColor = (Color) color;
        setBackground(newColor);
        if (isSelected) {
            if (selectedBorder == null) {
                selectedBorder = BorderFactory.createMatteBorder(4, 10, 4, 10,
                        table.getSelectionBackground());
            }
            setBorder(selectedBorder);
        } else {
            if (unselectedBorder == null) {
                unselectedBorder = BorderFactory.createMatteBorder(4, 10, 4, 10,
                        table.getBackground());
            }
            setBorder(unselectedBorder);
        }

        setToolTipText("[RGB value: " + newColor.getRed() + ", "
                + newColor.getGreen() + ", "
                + newColor.getBlue() + "]. Click to change.");
        return this;
    }
}
