package factory.codegen.app;

import jjava.JavaParserConstants;
import jjava.JavaParserTreeConstants;
import com.fuscard.commons.FuscardXMLException;
import com.fuscard.commons.XMLDocument;
import css.CSSParserConstants;
import css.CSSParserTreeConstants;
import ecmascript.EcmaScriptConstants;
import ecmascript.EcmaScriptTreeConstants;
import factory.Application;
import html.html32Constants;
import html.html32TreeConstants;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractCellEditor;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.border.BevelBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import php.PHPConstants;
import php.PHPTreeConstants;

/**
 *
 * @author John Mwai
 */
public class JDialogColorCodeEditor extends javax.swing.JDialog implements FuscardXMLConstants {

    private int defaultFontSize = 15;
    private HashMap<String, LanguageRecord> languageRecords = new LinkedHashMap<>();
    private FontFamilyEditor fontFamilyEditor;

    private static class LanguageRecord {

        JTable prodJTable;
        JTable tknJTable;
        JTabbedPane tabbedPane;
    }

    private ProductionTableModel prepareProductionTableModel(JTable proJTable) {
        Col[] cols = {
            new Col(String.class, "Production Name", false, 300),
            new Col(Font.class, "Font Family", true, 300),
            new Col(Integer.class, "Font Style", true, 100),
            new Col(Color.class, "Background", true, 50),
            new Col(Color.class, "Foreground", true, 50),
            new Col(Color.class, "Underline", true, 50),
            new Col(Integer.class, "Size", true, 50),
            new Col(Boolean.class, "Enabled", true, 90)};
        String[] colNames = new String[cols.length];
        Class[] classes = new Class[cols.length];
        boolean[] editable = new boolean[cols.length];
        int[] colWidths = new int[cols.length];
        for (int i = 0; i < cols.length; i++) {
            Col c = cols[i];
            colNames[i] = c.name;
            classes[i] = c._class;
            colWidths[i] = c.preferredWidth;
            editable[i] = c.editable;
        }
        ProductionTableModel productionTableModel = new ProductionTableModel(
                colNames, null, classes, editable);
        proJTable.setModel(productionTableModel);
        TableColumn column;
        for (int i = 0; i < colWidths.length; i++) {
            column = proJTable.getColumnModel().getColumn(i);
            column.setPreferredWidth(colWidths[i]);
            //Set renderers
            switch (i) {
                case ROW_PRODUCTION_NAME:
                case ROW_FONT_STYLE:
                case ROW_FONT_FAMILY:
                    column.setCellRenderer(new StringCellRenderer());
            }
            //set editors
            switch (i) {
//                case ROW_PRODUCTION_NAME:
                case ROW_FONT_STYLE:
                    column.setCellEditor(new FontStyleEditor(new JComboBox()));
                    break;
                case ROW_FONT_FAMILY:
                    column.setCellEditor(fontFamilyEditor);
                    break;
            }
        }
        return productionTableModel;
    }
//    private Theme currentTheme;

    public void setThemeColors(Theme theme) {
        Application.setColors(getRootPane(), theme);
//        currentTheme = theme;
    }

    private TokenTableModel prepareTokenTableModel(JTable tknJTable) {
        Col[] cols = {
            new Col(String.class, "Token Name", false, 300),
            new Col(Font.class, "Font Family", true, 300),
            new Col(Integer.class, "Font Style", true, 100),
            new Col(Color.class, "Background", true, 50),
            new Col(Color.class, "Foreground", true, 50),
            new Col(Color.class, "Underline", true, 50),
            new Col(Integer.class, "Size", true, 50),
            new Col(Boolean.class, "Enabled", true, 90)};
        String[] colNames = new String[cols.length];
        Class[] classes = new Class[cols.length];
        boolean[] editable = new boolean[cols.length];
        int[] colWidths = new int[cols.length];
        for (int i = 0; i < cols.length; i++) {
            Col c = cols[i];
            colNames[i] = c.name;
            classes[i] = c._class;
            colWidths[i] = c.preferredWidth;
            editable[i] = c.editable;
        }
        TokenTableModel tokenTableModel = new TokenTableModel(colNames, null,
                classes, editable);
        tknJTable.setModel(tokenTableModel);
        TableColumn column;
        for (int i = 0; i < colWidths.length; i++) {
            column = tknJTable.getColumnModel().getColumn(i);
            column.setPreferredWidth(colWidths[i]);
            //Set renderers
            switch (i) {
                case ROW_TOKEN_NAME:
                case ROW_FONT_STYLE:
                case ROW_FONT_FAMILY:
                    column.setCellRenderer(new StringCellRenderer());
            }
            //set editors
            switch (i) {
                case ROW_FONT_STYLE:
                    column.setCellEditor(fontFamilyEditor);
                    break;
                case ROW_FONT_FAMILY:
                    column.setCellEditor(fontFamilyEditor);
                    break;
            }
        }
        return tokenTableModel;
    }

    private JTabbedPane initializeLanguageTables(String language,
            String languageDisplayName, Class ci, String[] nodeNames) throws FuscardXMLException {
        System.out.println("Creating the tables");
        //Create the  table
        JTable prodJTable = new JTable();
        JTable tknJTable = new JTable();
        //Set column widths


        LinkedList<TokenRow> tokenRows;
        LinkedList<ProductionRow> productionRows;
        System.out.println("Preparing token rows");
        tokenRows = prepareTokenRows(language, ci);
        System.out.println("Preparing production rows");
        productionRows = prepareProductionRows(language, nodeNames);

        System.out.println("Creating token table model");
        TokenTableModel tokenTableModel = prepareTokenTableModel(tknJTable);
        System.out.println("Setting the token data");
        tokenTableModel.setData(tokenRows);
        //
        System.out.println("Preparing the production table model");
        ProductionTableModel productionTableModel = prepareProductionTableModel(
                prodJTable);
        System.out.println("Setting production table data");
        productionTableModel.setData(productionRows);
        //Set default renderers
        System.out.println("Setting editors and renderers");
        prodJTable.setDefaultRenderer(Color.class,
                new ColorTableCellRenderer());
        tknJTable.setDefaultRenderer(Color.class,
                new ColorTableCellRenderer());
        //Set default editors        
        prodJTable.setDefaultEditor(Color.class,
                new MyColorTableCellEditor());
        tknJTable.setDefaultEditor(Color.class,
                new MyColorTableCellEditor());
        //Set Row heights
        prodJTable.setRowHeight(30);
        tknJTable.setRowHeight(30);


        //Create auto sorters
        prodJTable.setAutoCreateRowSorter(true);
        tknJTable.setAutoCreateRowSorter(true);

        System.out.println("Creating " + languageDisplayName + " tabbed pane");
        //create a jscroll pane and add the two tables
        JTabbedPane jTabbedPane = new JTabbedPane();

        System.out.println("Adding productions tab");
        //
        jTabbedPane.addTab(languageDisplayName + " Productions",
                new JScrollPane(
                prodJTable));
        System.out.println("Adding token tab");
        jTabbedPane.addTab(languageDisplayName + " Tokens", new JScrollPane(
                tknJTable));
        LanguageRecord lr = new LanguageRecord();
        lr.prodJTable = prodJTable;
        lr.tknJTable = tknJTable;
        lr.tabbedPane = jTabbedPane;
        languageRecords.put(language, lr);
        return jTabbedPane;
    }

    private boolean find(String[] array, String s) {
        for (String ss : array) {
            if (ss.equals(s)) {
                return true;
            }
        }
        return false;
    }

    private void initialize(Theme theme) throws FuscardXMLException {
        System.out.println("Initializing...");
        String[] parsers = Application.getInstance().getValues(base_paser);

        fontFamilyEditor = new FontFamilyEditor(new JComboBox());
//Add the tabs
        System.out.println("Removing loading .");
        jTabbedPane1.removeAll();
        String jv = "Java", ph = "PHP", ht = "HTML", ecma = "ECMA Script (JavaScript)", cs = "CSS";
        if (find(parsers, java)) {
            jCheckBoxJava.setSelected(true);
            System.out.println("\n\n###   Creating java tab");
            System.out.println("");
            JTabbedPane javaTab = initializeLanguageTables("java", jv,
                    JavaParserConstants.class,
                    JavaParserTreeConstants.jjtNodeName);

            System.out.println("Adding java tab");
            jTabbedPane1.addTab(jv, javaTab);
        }
        if (find(parsers, php)) {
            jCheckBoxPHP.setSelected(true);
            System.out.println("\n\n###   Creating php tab");
            System.out.println("");
            JTabbedPane phpTab = initializeLanguageTables("php", php,
                    PHPConstants.class,
                    PHPTreeConstants.jjtNodeName);
            System.out.println("Adding php tab");
            jTabbedPane1.addTab(ph, phpTab);
        }
        if (find(parsers, html)) {
            System.out.println("\n\n###   Creating HTML tab");
            System.out.println("");
            JTabbedPane htmlTab = initializeLanguageTables("html", html,
                    html32Constants.class,
                    html32TreeConstants.jjtNodeName);
            System.out.println("Adding html tab");
            jTabbedPane1.addTab(ht, htmlTab);
        }

        if (find(parsers, javascript)) {
            jCheckBoxJS.setSelected(true);
            System.out.println("\n\n###   Creating ECMA tab");
            System.out.println("");
            JTabbedPane ecmaTab = initializeLanguageTables("ecma", ecma,
                    EcmaScriptConstants.class,
                    EcmaScriptTreeConstants.jjtNodeName);
            System.out.println("Adding ecma tab");
            jTabbedPane1.addTab(ecma, ecmaTab);
        }
        if (find(parsers, css)) {
            jCheckBoxCSS.setSelected(true);
            System.out.println("\n\n###   Creating css tab");
            System.out.println("");
            JTabbedPane cssTab = initializeLanguageTables("css", css,
                    CSSParserConstants.class, CSSParserTreeConstants.jjtNodeName);
            System.out.println("Adding css tab");
            jTabbedPane1.addTab(cs, cssTab);
        }
        System.out.println("Enabling saving option");
        jButtonApplyStyles.setEnabled(true);
        System.out.println("Setting theme colors");
        setThemeColors(theme);
        System.out.println("After setting theme colors");
        
    }

    private void applyStyleSettings() {

        SwingWorker sw = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                for (String lang : languageRecords.keySet()) {
                    LanguageRecord lr = languageRecords.get(lang);
                    try {
                        saveTable(lang, lr.prodJTable, lr.tknJTable);
                    } catch (FuscardXMLException ex) {
                        Logger.getLogger(JDialogColorCodeEditor.class.getName()).log(
                                Level.SEVERE,
                                null, ex);
                    }
                }
                return null;
            }
        };
        sw.execute();
    }

    private void setValuesToXML(XMLDocument xml, String lang, String type,
            String name, Row row) throws FuscardXMLException {
        xml.setElementValue(
                new String[]{"root", lang, type, name, XML_STYLE},
                Integer.toString(row.Style));
        xml.setElementValue(
                new String[]{"root", lang, type, name, XML_BACKGROUND},
                Integer.toString(row.background.getRGB()));
        xml.setElementValue(
                new String[]{"root", lang, type, name, XML_FOREGROUND},
                Integer.toString(row.foreground.getRGB()));
        xml.setElementValue(
                new String[]{"root", lang, type, name, XML_FONT_SIZE},
                Integer.toString(row.fontSize));
        xml.setElementValue(
                new String[]{"root", lang, type, name, XML_ENABLED},
                Boolean.toString(row.enabled));
        xml.setElementValue(
                new String[]{"root", lang, type, name, XML_FONT_FAMILY},
                row.font.getFamily());
        xml.setElementValue(
                new String[]{"root", lang, type, name, XML_UNDERLINE_COLOR},
                Integer.toString(row.underlineColor.getRGB()));
    }

    private void saveTable(String language, JTable proJTable, JTable tknJTable) throws FuscardXMLException {
        XMLDocument xml = Application.getInstance().getParserDoc();

        //Productions
        ProductionTableModel model = (ProductionTableModel) proJTable.getModel();
        LinkedList<ProductionRow> rows = model.getRows();

        for (ProductionRow row : rows) {
            setValuesToXML(xml, language, "productions", row.productionName, row);
        }
        //Tokens
        TokenTableModel tokenModel = (TokenTableModel) tknJTable.getModel();
        LinkedList<TokenRow> tokenRows = tokenModel.getRows();
        for (TokenRow row : tokenRows) {
            setValuesToXML(xml, language, "tokens", row.tokenName, row);
        }
        xml.saveToFile();
    }

    private LinkedList<TokenRow> prepareTokenRows(String lang,
            Class constInterface) throws FuscardXMLException {
        //Use reflection to get the defined fields in the interface
        System.out.println("In parepare token rows");
        System.out.println("Getting interface fields");
        Field[] fields = constInterface.getFields();
        LinkedList<TokenRow> res = new LinkedList<>();
        System.out.println("Getting xml document");
        XMLDocument xml = Application.getInstance().getParserDoc();
        System.out.println("loading rows");
        for (Field field : fields) {
            if (field.getType() == Integer.TYPE) {
                String tokenName = field.getName();
                TokenRow r = new TokenRow();
                res.add(r);
                r.tokenName = tokenName;
                try {
                    loadRowFromXML(xml, r, lang, "tokens", tokenName);
                } catch (FuscardXMLException ignore) {
                    continue;
                }
            }
        }
        return res;
    }

    private void loadRowFromXML(XMLDocument xml, Row r, String lang, String type,
            String name) throws FuscardXMLException {
        String style;
        String background;
        String foreground;
        String fontsize;
        String enabled;
        String fontfamily;
        String underlineColor;
        style = xml.getElementValue(
                new String[]{"root", lang, type, name, XML_STYLE});
        background = xml.getElementValue(
                new String[]{"root", lang, type, name, XML_BACKGROUND});
        foreground = xml.getElementValue(
                new String[]{"root", lang, type, name, XML_FOREGROUND});
        fontsize = xml.getElementValue(
                new String[]{"root", lang, type, name, XML_FONT_SIZE});
        enabled = xml.getElementValue(
                new String[]{"root", lang, type, name, XML_ENABLED});
        fontfamily = xml.getElementValue(
                new String[]{"root", lang, type, name, XML_FONT_FAMILY});
        underlineColor = xml.getElementValue(
                new String[]{"root", lang, type, name, XML_UNDERLINE_COLOR});
        //Style
        if (style != null) {
            r.Style = Integer.parseInt(style);
        } else {
            r.Style = Font.PLAIN;
        }
        //Font
        if (fontfamily != null) {
            Font f = new Font(fontfamily, Font.PLAIN, defaultFontSize);
            r.font = f;
        } else {
            Font f = new Font("monospaced", Font.PLAIN, defaultFontSize);
            r.font = f;
        }
        //Background
        if (background != null) {
            int rgb = Integer.parseInt(background);
            r.background = new Color(rgb);
        } else {
            r.background = Color.WHITE;
        }
        //Foreground
        if (foreground != null) {
            int rgb = Integer.parseInt(foreground);
            r.foreground = new Color(rgb);
        } else {
            r.foreground = Color.BLACK;
        }
        //Underline
        if (underlineColor != null) {
            int rgb = Integer.parseInt(underlineColor);
            r.underlineColor = new Color(rgb);
        } else {
            r.underlineColor = Color.WHITE;
        }
        //Font size
        if (fontsize != null) {
            r.fontSize = Integer.parseInt(fontsize);
        } else {
            r.fontSize = 15;
        }
        if (enabled != null) {
            switch (enabled) {
                case "true":
                    r.enabled = true;
                    break;
                case "false":
                default:
                    r.enabled = false;
            }
        } else {
            r.enabled = false;
        }
    }

    private LinkedList<ProductionRow> prepareProductionRows(String lang,
            String[] nodeNames) throws FuscardXMLException {
        LinkedList<ProductionRow> res = new LinkedList<>();
        String path = Application.getInstance().getParserConfigurationPath();
        XMLDocument xml = new XMLDocument(path);
        xml.loadFromFile();
        for (int i = 0; i < nodeNames.length; i++) {
            String production = nodeNames[i];
            ProductionRow r = new ProductionRow();
            res.add(r);
            r.productionName = production;
            try {
                loadRowFromXML(xml, r, lang, "productions", production);
            } catch (FuscardXMLException ignore) {
                continue;
            }
        }
        return res;
    }

    private void create() {
        initializer.start();
    }
    private Thread initializer;

    /**
     * Creates new form JDialogColorCodeEditor
     */
    public JDialogColorCodeEditor(java.awt.Frame parent, boolean modal,
            final Theme theme) {
        super(parent, modal);
        initComponents();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    initialize(theme);
                } catch (FuscardXMLException ex) {
                    jLabelLoading.setText("Loading failed");
                    jLabelPleaseWait.setText("");
                }
            }
        };
        initializer = new Thread(r);
        create();
        System.out.println("Created Color editor");
    }

    public static JDialogColorCodeEditor getNewInstance(JFrame frame,
            Theme theme) {
        final JDialogColorCodeEditor dialog;
        dialog = new JDialogColorCodeEditor(
                frame, true, theme);
        return dialog;
    }
    private static final int ROW_PRODUCTION_NAME = 0;
    private static final int ROW_TOKEN_NAME = 0;
    private static final int ROW_FONT_FAMILY = 1;
    private static final int ROW_FONT_STYLE = 2;
    private static final int ROW_BACKGROUND_COLOR = 3;
    private static final int ROW_FOREGROUND_COLOR = 4;
    private static final int ROW_UNDERLINE_COLOR = 5;
    private static final int ROW_FONT_SIZE = 6;
    private static final int ROW_ENABLED = 7;

    private class TokenTableModel extends AbstractTableModel {

        private String[] columnNames;
        private LinkedList<TokenRow> data;
        private Class[] classes;
        private boolean[] editable;

        public TokenTableModel(String[] columnNames,
                LinkedList<TokenRow> data, Class[] classes, boolean[] editable) {
            this.columnNames = columnNames;
            this.data = data;
            this.classes = classes;
            this.editable = editable;
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public int getRowCount() {
            if (data == null) {
                return 0;
            }
            return data.size();
        }

        @Override
        public String getColumnName(int col) {
            return columnNames[col];
        }

        @Override
        public Object getValueAt(int row, int col) {
            if (data == null) {
                return null;
            }
            TokenRow tr = data.get(row);
            switch (col) {
                case ROW_TOKEN_NAME:
                    return tr.tokenName;
                case ROW_FONT_FAMILY:
                    return tr.font;
                case ROW_FONT_STYLE:
                    return tr.Style;
                case ROW_BACKGROUND_COLOR:
                    return tr.background;
                case ROW_FOREGROUND_COLOR:
                    return tr.foreground;
                case ROW_FONT_SIZE:
                    return tr.fontSize;
                case ROW_UNDERLINE_COLOR:
                    return tr.underlineColor;
                case ROW_ENABLED:
                    return tr.enabled;
            }
            return null;
        }

        @Override
        public Class getColumnClass(int c) {
            return classes[c];
        }

        @Override
        public boolean isCellEditable(int row, int col) {
            return editable[col];
        }

        public void addRow(TokenRow row) {
            data.add(row);
            fireTableRowsInserted(data.indexOf(row), data.indexOf(row));
        }

        public void setData(LinkedList<TokenRow> rows) {
            data = rows;
            fireTableDataChanged();
        }

        public LinkedList<TokenRow> getRows() {
            return data;
        }

        @Override
        public void setValueAt(Object value, int row, int col) {
            TokenRow tr = data.get(row);
            switch (col) {
                case ROW_TOKEN_NAME:
                    tr.tokenName = (String) value;
                    break;
                case ROW_FONT_FAMILY:
                    tr.font = (Font) value;
                    break;
                case ROW_FONT_STYLE:
                    tr.Style = (int) value;
                    break;
                case ROW_BACKGROUND_COLOR:
                    tr.background = (Color) value;
                    break;
                case ROW_FOREGROUND_COLOR:
                    tr.foreground = (Color) value;
                    break;
                case ROW_UNDERLINE_COLOR:
                    tr.underlineColor = (Color) value;
                    break;
                case ROW_FONT_SIZE:
                    tr.fontSize = (int) value;
                    break;
                case ROW_ENABLED:
                    tr.enabled = (boolean) value;
            }
            fireTableCellUpdated(row, col);
        }
    }

    private class ProductionTableModel extends AbstractTableModel {

        private String[] columnNames;
        private LinkedList<ProductionRow> data;
        private Class[] classes;
        private boolean[] editable;

        public ProductionTableModel(String[] columnNames,
                LinkedList<ProductionRow> data, Class[] classes,
                boolean[] editable) {
            this.columnNames = columnNames;
            this.data = data;
            this.classes = classes;
            this.editable = editable;
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public int getRowCount() {
            if (data == null) {
                return 0;
            }
            return data.size();
        }

        @Override
        public String getColumnName(int col) {
            return columnNames[col];
        }

        @Override
        public Object getValueAt(int row, int col) {
            if (data == null) {
                return null;
            }
            ProductionRow tr = data.get(row);
            switch (col) {
                case ROW_PRODUCTION_NAME:
                    return tr.productionName;
                case ROW_FONT_FAMILY:
                    return tr.font;
                case ROW_FONT_STYLE:
                    return tr.Style;
                case ROW_BACKGROUND_COLOR:
                    return tr.background;
                case ROW_FOREGROUND_COLOR:
                    return tr.foreground;
                case ROW_UNDERLINE_COLOR:
                    return tr.underlineColor;
                case ROW_FONT_SIZE:
                    return tr.fontSize;
                case ROW_ENABLED:
                    return tr.enabled;
            }
            return null;
        }

        @Override
        public Class getColumnClass(int c) {
            return classes[c];
        }

        @Override
        public boolean isCellEditable(int row, int col) {
            return editable[col];
        }

        public void addRow(ProductionRow row) {
            data.add(row);
            fireTableRowsInserted(data.indexOf(row), data.indexOf(row));
        }

        public void setData(LinkedList<ProductionRow> rows) {
            data = rows;
            fireTableDataChanged();
        }

        public LinkedList<ProductionRow> getRows() {
            return data;
        }

        @Override
        public void setValueAt(Object value, int row, int col) {
            ProductionRow tr = data.get(row);
            switch (col) {
                case ROW_PRODUCTION_NAME:
                    tr.productionName = (String) value;
                    break;
                case ROW_FONT_FAMILY:
                    tr.font = (Font) value;
                    break;
                case ROW_FONT_STYLE:
                    tr.Style = (int) value;
                    break;
                case ROW_BACKGROUND_COLOR:
                    tr.background = (Color) value;
                    break;
                case ROW_FOREGROUND_COLOR:
                    tr.foreground = (Color) value;
                    break;
                case ROW_UNDERLINE_COLOR:
                    tr.underlineColor = (Color) value;
                    break;
                case ROW_FONT_SIZE:
                    tr.fontSize = (int) value;
                    break;
                case ROW_ENABLED:
                    tr.enabled = (boolean) value;
            }
            fireTableCellUpdated(row, col);
        }
    }

    private class Row {

        Font font;
        int Style;
        Color background;
        Color foreground;
        int fontSize;
        boolean enabled;
        Color underlineColor;

        public Row(Font font, int Style,
                Color background, Color foreground, int fontSize,
                Color underlineColor,
                boolean enabled) {
            this.font = font;
            this.Style = Style;
            this.background = background;
            this.foreground = foreground;
            this.fontSize = fontSize;
            this.underlineColor = underlineColor;
            this.enabled = enabled;
        }

        public Row() {
        }
    }

    private class ProductionRow extends Row {

        private String productionName;

        public ProductionRow() {
        }

        public ProductionRow(String productionName, Font font, int Style,
                Color background, Color foreground, int fontSize,
                Color underlineColor,
                boolean enabled) {
            super(font, Style, background, foreground, fontSize,
                    underlineColor, enabled);
            this.productionName = productionName;
        }
    }

    private class TokenRow extends Row {

        private String tokenName;

        public TokenRow(String tokenName, Font font, int Style, Color background,
                Color foreground, int fontSize, Color underlineColor,
                boolean enabled) {
            super(font, Style, background, foreground, fontSize,
                    underlineColor, enabled);
            this.tokenName = tokenName;
        }

        public TokenRow() {
        }
    }

    private static class Col {

        Class _class;
        String name;
        int preferredWidth;
        boolean editable;
        TableCellEditor editor;
        TableCellRenderer renderer;

        public Col(Class _class, String name, boolean editable,
                int preferredWidth) {
            this._class = _class;
            this.name = name;
            this.editable = editable;
            this.preferredWidth = preferredWidth;
        }

        public Col() {
        }

        public Col(Class _class, String name, int preferredWidth,
                TableCellEditor editor, TableCellRenderer renderer) {
            this._class = _class;
            this.name = name;
            this.preferredWidth = preferredWidth;
            this.editor = editor;
            this.renderer = renderer;
        }
    }

    private static class ColorTableCellRenderer extends JLabel implements TableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table,
                Object value, boolean isSelected, boolean hasFocus, int row,
                int column) {
            if (isSelected) {
                setBorder(new BevelBorder(BevelBorder.RAISED));
            } else {
                setBorder(null);
            }
            if (value instanceof Color) {
                setOpaque(true);
                Color c = (Color) value;
                Color c1 = new Color(0xffffffff ^ c.getRGB());
                setBackground(c);
                setForeground(c1);
                setText("0x" + Integer.toHexString(c.getRGB()));
            }
            return this;
        }
    }

    private class FontFamilyEditor extends DefaultCellEditor implements TableCellEditor {

        private Font currentFont;
        private JComboBox combo;
        private Font[] fonts;

        public FontFamilyEditor(JComboBox comboBox) {
            super(comboBox);
            this.combo = comboBox;
            combo.setEditable(true);
            String ff[] =
                    GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
            fonts = new Font[ff.length];
            int i = 0;
            System.out.println("Creating fonts...");
            for (String s : ff) {
                Font f = new Font(s, Font.PLAIN, defaultFontSize);
                combo.addItem(f);
                fonts[i++] = f;
            }

            combo.setRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(
                        JList<?> list, Object value, int index,
                        boolean isSelected, boolean cellHasFocus) {
                    super.getListCellRendererComponent(list, value,
                            index, isSelected, cellHasFocus);
                    if (value instanceof Font) {
                        setText(((Font) value).getFamily());
                        setFont((Font) value);
                    }
                    return this;
                }
            });
            combo.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Object v = combo.getSelectedItem();
                    if (v instanceof Font) {
                        combo.setSelectedItem(((Font) v).getFamily());
                    }
                }
            });
        }

        @Override
        public Object getCellEditorValue() {
            return currentFont;
        }
        int c = 0;

        private boolean acceptValue(Object obj) {
            System.out.println(obj);
            if (obj instanceof Font) {
                return true;
            } else if (obj instanceof String) {
                String name = (String) obj;
                //Compare this string with the list of fonts that we have
                boolean found = false;
                for (int i = 0; i < combo.getItemCount(); i++) {
                    Font f = (Font) combo.getItemAt(i);
                    if (f.getFamily().equalsIgnoreCase(name)) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    return false;
                }
                return true;
            }

            return false;
        }

        @Override
        public boolean stopCellEditing() {
            Object obj = combo.getSelectedItem();
            boolean b = acceptValue(obj);
            if (!b) {
                return false;
            }

            if (obj instanceof Font) {
                currentFont = (Font) obj;
            } else if (obj instanceof String) {
                String name = (String) obj;
                Font ff = null;
                for (Font f : fonts) {
                    if (f.getFamily().equals(name.trim())) {
                        ff = f;
                    }
                }
                currentFont = ff;
            }
            return super.stopCellEditing();
        }
    }

    private class FontStyleEditor extends DefaultCellEditor implements TableCellEditor {

        private int fontStyle;
        private JComboBox combo;
        final String BOLD = "Bold";
        final String ITALIC = "Italic";
        final String BOLD_ITALIC = "Bold Italic";
        final String PLAIN = "Plain";

        public FontStyleEditor(JComboBox combo) {
            super(combo);
            this.combo = combo;
            combo.addItem(PLAIN);
            combo.addItem(ITALIC);
            combo.addItem(BOLD);
            combo.addItem(BOLD_ITALIC);
        }

        @Override
        public Object getCellEditorValue() {
            return fontStyle;
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            setComboSelectedValue();
            return combo;
        }

        @Override
        public boolean stopCellEditing() {
            Object obj = combo.getSelectedItem();
            if (obj == null) {
                fontStyle = Font.PLAIN;
            } else {
                switch (obj.toString()) {
                    case PLAIN:
                        fontStyle = Font.PLAIN;
                        break;
                    case ITALIC:
                        fontStyle = Font.ITALIC;
                        break;
                    case BOLD:
                        fontStyle = Font.BOLD;
                        break;
                    case BOLD_ITALIC:
                        fontStyle = Font.BOLD + Font.ITALIC;
                        break;
                    default:
                        fontStyle = Font.PLAIN;
                        break;
                }
            }
            return super.stopCellEditing();
        }

        private void setComboSelectedValue() {
            switch (fontStyle) {
                case Font.BOLD:
                    combo.setSelectedItem(BOLD);
                    break;
                case Font.ITALIC:
                    combo.setSelectedItem(ITALIC);
                    break;
                case Font.PLAIN:
                    combo.setSelectedItem(PLAIN);
                    break;
                case Font.BOLD + Font.ITALIC:
                    combo.setSelectedItem(BOLD_ITALIC);
                    break;
                default:
                    combo.setSelectedItem(PLAIN);
            }
        }
    }

    private class StringCellRenderer extends DefaultTableCellRenderer {

        Font production = new Font(Font.MONOSPACED, Font.BOLD + Font.ITALIC,
                defaultFontSize),
                fontStyle = new Font(Font.DIALOG, Font.PLAIN, 12);

        @Override
        public Component getTableCellRendererComponent(JTable table,
                Object value, boolean isSelected, boolean hasFocus, int row,
                int column) {
            super.getTableCellRendererComponent(table, value, isSelected,
                    hasFocus, row, column);
            if (value instanceof String) {
                setText((String) value);
            } else if (value instanceof Font) {
                setText(((Font) value).getFamily());
            } else if (column == ROW_FONT_STYLE) {
                if (value instanceof Integer) {
                    int s = (Integer) value;
                    if (s == Font.BOLD) {
                        setText("Bold");
                    } else if (s == Font.ITALIC) {
                        setText("Italic");
                    } else if (s == Font.PLAIN) {
                        setText("Plain");
                    } else if (s == (Font.ITALIC + Font.BOLD)) {
                        setText("Bold Italic");
                    }
                }
            } else {
                setText(value == null ? "" : value.toString());
            }
            column = table.convertColumnIndexToModel(column);
            if (column == ROW_PRODUCTION_NAME) {
                setFont(production);
            } else if (column == ROW_FONT_STYLE) {
                setFont(fontStyle);
            } else if (column == ROW_FONT_FAMILY) {
                setFont((Font) value);
            } else {
                setFont(null);
            }
            return this;

        }
    }

    private class MyColorTableCellEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {

        private JButton button;
        private JDialog dialog;
        private JColorChooser colorChooser;
        private Color currentColor;

        public MyColorTableCellEditor() {
            button = new JButton();
            button.setBorderPainted(false);
            button.addActionListener(this);
            button.setOpaque(false);


            colorChooser = new JColorChooser();
            dialog = JColorChooser.createDialog(JDialogColorCodeEditor.this,
                    "Choose Colour",
                    true, colorChooser, this, null);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            currentColor = (Color) value;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return currentColor;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == button) {
                //Show the color chooser
                button.setBackground(currentColor);
                colorChooser.setColor(currentColor);
                dialog.setVisible(true);
                //Make the renderer reappear
                fireEditingStopped();
            } else {
                //Set the color 
                currentColor = colorChooser.getColor();
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jLabelLoading = new javax.swing.JLabel();
        jLabelPleaseWait = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jButtonApplyStyles = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jCheckBoxJava = new javax.swing.JCheckBox();
        jCheckBoxPHP = new javax.swing.JCheckBox();
        jCheckBoxJS = new javax.swing.JCheckBox();
        jCheckBoxCSS = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Joomla Extension Factory - Hightlight Color Manager");

        jLabelLoading.setFont(new java.awt.Font("Tempus Sans ITC", 1, 14)); // NOI18N
        jLabelLoading.setForeground(new java.awt.Color(0, 102, 102));
        jLabelLoading.setText("Loading ...");

        jLabelPleaseWait.setText("Please wait");

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/fuscard/g.png"))); // NOI18N

        jLabel2.setFont(new java.awt.Font("Andalus", 0, 14)); // NOI18N
        jLabel2.setText("Fuscard");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(65, 65, 65)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelLoading)
                            .addComponent(jLabelPleaseWait))))
                .addContainerGap(631, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(65, 65, 65)
                .addComponent(jLabelLoading)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabelPleaseWait)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 47, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(86, 86, 86))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(141, 141, 141))))
        );

        jTabbedPane1.addTab("Loading ... ", jPanel2);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/fuscard strip.png"))); // NOI18N

        jButtonApplyStyles.setText("Apply Styles");
        jButtonApplyStyles.setEnabled(false);
        jButtonApplyStyles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonApplyStylesActionPerformed(evt);
            }
        });

        jButton2.setText("Cancel");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jCheckBoxJava.setText("java");

        jCheckBoxPHP.setText("php");

        jCheckBoxJS.setText("javascript");

        jCheckBoxCSS.setText("css");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jCheckBoxJava)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBoxPHP)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCheckBoxJS)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBoxCSS)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonApplyStyles)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton2))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonApplyStyles)
                    .addComponent(jButton2)
                    .addComponent(jCheckBoxJava)
                    .addComponent(jCheckBoxPHP)
                    .addComponent(jCheckBoxJS)
                    .addComponent(jCheckBoxCSS)))
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTabbedPane1))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jTabbedPane1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        setVisible(false);
    }//GEN-LAST:event_jButton2ActionPerformed
    private String base_paser = "enabled_parsers.parser";
    private String sql = "sql", java = "java", php = "php", html = "html", xml = "xml", javascript = "javascript", css = "css";
    private void jButtonApplyStylesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonApplyStylesActionPerformed
        applyStyleSettings();
        Application app = Application.getInstance();
        ArrayList<String> parsers = new ArrayList<>();
        if (jCheckBoxJava.isSelected()) {
            parsers.add(java);
        }
        if (jCheckBoxCSS.isSelected()) {
            parsers.add(css);
        }
        if (jCheckBoxJava.isSelected()) {
            parsers.add(java);
        }
        if (jCheckBoxJS.isSelected()) {
            parsers.add(javascript);
        }
        if (jCheckBoxPHP.isSelected()) {
            parsers.add(php);
        }
        try {
            app.setValues(base_paser, parsers.toArray(new String[0]));
        } catch (FuscardXMLException ex) {
            Logger.getLogger(JDialogColorCodeEditor.class.getName()).log(
                    Level.SEVERE,
                    null, ex);
        }
        setVisible(false);
    }//GEN-LAST:event_jButtonApplyStylesActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButtonApplyStyles;
    private javax.swing.JCheckBox jCheckBoxCSS;
    private javax.swing.JCheckBox jCheckBoxJS;
    private javax.swing.JCheckBox jCheckBoxJava;
    private javax.swing.JCheckBox jCheckBoxPHP;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabelLoading;
    private javax.swing.JLabel jLabelPleaseWait;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JTabbedPane jTabbedPane1;
    // End of variables declaration//GEN-END:variables
}
