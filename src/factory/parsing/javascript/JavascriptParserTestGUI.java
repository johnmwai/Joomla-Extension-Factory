/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package factory.parsing.javascript;

import com.fuscard.commons.INIInputOutput;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.JTextComponent;

/**
 *
 * @author John Mwai
 */
public class JavascriptParserTestGUI extends javax.swing.JFrame implements TableModelListener {
    
    private boolean FILE_NOT_SAVED = true;
    private HashMap<Object, Action> actions;
    private String ini_file_path = "C:\\Users\\John Mwai\\Documents\\bravia order management system\\experimental\\ide_styles.ini";
    private HashMap<String, Object> iniCache = null;

    /**
     * Creates new form JavascriptParserTestGUI
     */
    public JavascriptParserTestGUI() {
        initComponents();
        actions = createActionTable(textPane);
        createMenus();
        setTableModelListener();
        prepareHighlightColorTable();
        loadFile();
        parse();
        
    }
    
    private void prepareHighlightColorTable() {
        highlightColorsTable.setAutoCreateRowSorter(true);
        highlightColorsTable.setDefaultRenderer(Color.class,
                new ColorTableCellRenderer());
        highlightColorsTable.setDefaultEditor(Color.class,
                new ColorTableCellEditor());
        
        TableColumn tc = highlightColorsTable.getColumn("Sample");        
        
        tc.setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table,
                    Object value, boolean isSelected, boolean hasFocus, int row,
                    int column) {
                TableModel tm = table.getModel();
                row = table.convertRowIndexToModel(row);
                
                int fgi = table.convertColumnIndexToView(3);
                int bgi = table.convertColumnIndexToView(4);
                Color fg = (Color) tm.getValueAt(row, fgi);
                Color bg = (Color) tm.getValueAt(row, bgi);
                setForeground(fg);
                setBackground(bg);
                return super.getTableCellRendererComponent(table, value,
                        isSelected, hasFocus, row, column);
            }
        });
        
        
        highlightColorsTable.getColumn("Name").setCellRenderer(new DefaultTableCellRenderer(){

            @Override
            public Component getTableCellRendererComponent(JTable table,
                    Object color, boolean isSelected, boolean hasFocus, int row,
                    int column) {                
                
                return super.getTableCellRendererComponent(table, color,
                        isSelected, hasFocus, row, column);
            }

            @Override
            protected void paintComponent(Graphics g) {
                g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 15));
                super.paintComponent(g);
            }
            
        });
        
        resetHighlightColorTableData();
        highlightColorsTable.getColumnModel().getColumn(1).setPreferredWidth(
                250);
        highlightColorsTable.getColumnModel().getColumn(5).setPreferredWidth(
                20);
        highlightColorsTable.setRowHeight(20);
        TableColumnModel m = highlightColorsTable.getColumnModel();
        m.removeColumn(m.getColumn(0));
    }
    
    private TableModel getHighLightColorTableModel() {
        return new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                getHighlightColorTableColNames()) {
            Class[] types = new Class[]{
                java.lang.String.class, java.lang.Object.class, Color.class,
                Color.class, java.lang.Object.class, java.lang.Boolean.class
            };
            
            @Override
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }
            
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0 && column != 1 && column != 4;
            }
        };
    }
    
    private Object[] getHighlightColorTableColNames() {
        return new String[]{
                    "ID", "Name", "Foreground", "Background", "Sample", "Use"
                };
    }
    
    private void saveHighlightColorTableData() {
        INIInputOutput ini = new INIInputOutput(ini_file_path);
        HashMap<String, String> map = new HashMap<String, String>();
        DefaultTableModel model = (DefaultTableModel) highlightColorsTable.getModel();
        iniCache = new HashMap<String, Object>();
        
        for (int i = 0; i < model.getRowCount(); i++) {
            String id = (String) model.getValueAt(i, 0);
            Color fgc = (Color) model.getValueAt(i, 2);
            Color bgc = (Color) model.getValueAt(i, 3);
            Boolean usd = (Boolean) model.getValueAt(i, 5);
            
            
            String used = getStringFromBoolean(usd);
            String fg = getHexStringColor(fgc);
            String bg = getHexStringColor(bgc);
            map.put(id + "_foreground", fg);
            map.put(id + "_background", bg);
            map.put(id + "_used", used);
            
            if (usd) {
                iniCache.put(id + "_foreground", fgc);
                iniCache.put(id + "_background", bgc);
                iniCache.put(id + "_used", usd);
            }
            
        }
        try {
            ini.save(map);
        } catch (IOException ex) {
            Logger.getLogger(JavascriptParserTestGUI.class.getName()).log(
                    Level.SEVERE,
                    null, ex);
        }
    }
    
    private String getStringFromBoolean(boolean aBool) {
        if (aBool) {
            return "yes";
        }
        return "no";
    }
    
    private String getHexStringColor(Color c) {
        return String.format("%02x%02x%02x", c.getRed(), c.getGreen(),
                c.getBlue());
    }
    
    private void resetHighlightColorTableData() {
        HashMap<String, Object> map = getHighlightColorsFromINI(false);
        Object data[][];
        if (map == null) {
            data = new Object[][]{};
        } else {
            data = new Object[][]{
                {"LineTerminator",
                    "Line Terminator",
                    map.get("LineTerminator_foreground"),
                    map.get("LineTerminator_background"),
                    "\n", map.get(
                    "LineTerminator_used")},
                {"InputElement", "Input Element", map.get(
                    "InputElement_foreground"), map.get(
                    "InputElement_background"), "my element", map.get(
                    "InputElement_used")},
                {"TraditionalComment", "Traditional Comment", map.get(
                    "TraditionalComment_foreground"), map.get(
                    "TraditionalComment_background"), "/** comment */", map.get(
                    "TraditionalComment_used")},
                {"SingleLineComment", "Single Line Comment", map.get(
                    "SingleLineComment_foreground"), map.get(
                    "SingleLineComment_background"), "// comment", map.get(
                    "SingleLineComment_used")},
                {"Keyword", "Key Word", map.get(
                    "Keyword_foreground"), map.get(
                    "Keyword_background"), "function", map.get(
                    "Keyword_used")},
                {"ReserverdWord", "Reserved Word", map.get(
                    "ReserverdWord_foreground"), map.get(
                    "ReserverdWord_background"), "goto", map.get(
                    "ReserverdWord_used")},
                {"BooleanLiteral", "Boolean Literal", map.get(
                    "BooleanLiteral_foreground"), map.get(
                    "BooleanLiteral_background"), "true", map.get(
                    "BooleanLiteral_used")},
                {"NullLiteral", "Null Literal", map.get(
                    "NullLiteral_foreground"), map.get(
                    "NullLiteral_background"), "null", map.get(
                    "NullLiteral_used")},
                {"Identifier", "Identifier", map.get(
                    "Identifier_foreground"), map.get(
                    "Identifier_background"), "myIdentifier", map.get(
                    "Identifier_used")},
                {"DecimalLiteral", "Decimal Literal", map.get(
                    "DecimalLiteral_foreground"), map.get(
                    "DecimalLiteral_background"), "1000", map.get(
                    "DecimalLiteral_used")},
                {"HexLiteral", "Hexadecimal Literal", map.get(
                    "HexLiteral_foreground"), map.get(
                    "HexLiteral_background"), "0x00da45", map.get(
                    "HexLiteral_used")},
                {"OctalLiteral", "Octal Literal", map.get(
                    "OctalLiteral_foreground"), map.get(
                    "OctalLiteral_background"), "0124", map.get(
                    "OctalLiteral_used")},
                {"FloatingPointLiteral", "Floating Point Literal", map.get(
                    "FloatingPointLiteral_foreground"), map.get(
                    "FloatingPointLiteral_background"), "12e78", map.get(
                    "FloatingPointLiteral_used")},
                {"StringLiteral", "String Literal", map.get(
                    "StringLiteral_foreground"), map.get(
                    "StringLiteral_background"), "'my string!'", map.get(
                    "StringLiteral_used")},
                {"EscapeSequence", "Escape Sequence", map.get(
                    "EscapeSequence_foreground"), map.get(
                    "EscapeSequence_background"), "\\t", map.get(
                    "EscapeSequence_used")},
                {"OctalEscape", "Octal Escape", map.get(
                    "OctalEscape_foreground"), map.get(
                    "OctalEscape_background"), "\\245", map.get(
                    "OctalEscape_used")},
                {"Operator", "Operator", map.get(
                    "Operator_foreground"), map.get(
                    "Operator_background"), ">>>", map.get(
                    "Operator_used")},
                {"Seperator", "Seperator", map.get(
                    "Seperator_foreground"), map.get(
                    "Seperator_background"), "{", map.get(
                    "Seperator_used")},
                {"Literal", "Literal", map.get(
                    "Literal_foreground"), map.get(
                    "Literal_background"), "myLiteral", map.get(
                    "Literal_used")}
            };
        }
        int rows = data.length;
        int cols = 6;
        DefaultTableModel model = (DefaultTableModel) highlightColorsTable.getModel();
        model.setRowCount(0);
        for (int i = 0; i < rows; i++) {
            model.addRow(new Object[6]);
            for (int j = 0; j < cols; j++) {
                model.setValueAt(data[i][j], i, j);
            }
        }
    }
    
    private HashMap<String, Object> getHighlightColorsFromINI(boolean canCache) {
        if (canCache && iniCache != null) {
            return iniCache;
        }
        HashMap<String, String> map;
        INIInputOutput ini = new INIInputOutput(ini_file_path);
        try {
            map = ini.getIniData();
        } catch (IOException ex) {
            return null;
        }
        iniCache = new HashMap<String, Object>();
        HashMap<String, Object> res = new HashMap<String, Object>();
        for (String key : map.keySet()) {
            StringTokenizer st = new StringTokenizer(key, "_");
            if (st.countTokens() == 2) {
                String s1 = st.nextToken();
                String s2 = st.nextToken();
                if ("foreground".equals(s2) || "background".equals(s2)) {
                    res.put(key, getColor(map.get(key)));
                } else if ("used".equals(s2)) {
                    res.put(key, new Boolean(getBool(map.get(key))));
                }
            }
        }
        iniCache = res;
        return res;
    }
    
    private static Color getColor(Object s) {
        
        return new Color(Integer.parseInt((String) s, 16));
    }
    
    private static boolean getBool(Object s) {
        if ("yes".equals(s)) {
            return true;
        } else {
            return false;
        }
    }
    
    private void setTableModelListener() {
        highlightColorsTable.getModel().addTableModelListener(this);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        fileChooser = new javax.swing.JFileChooser();
        highlightColorDialog = new javax.swing.JDialog(this);
        jScrollPane2 = new javax.swing.JScrollPane();
        highlightColorsTable = new javax.swing.JTable();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        ideStylesFileJTextField = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        textPane = new javax.swing.JTextPane();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        fileLocationField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        fileNameField = new javax.swing.JTextField();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        cutMenuItem = new javax.swing.JMenuItem();
        copyMenuItem = new javax.swing.JMenuItem();
        pasteMenuItem = new javax.swing.JMenuItem();

        highlightColorDialog.setModal(true);
        highlightColorDialog.setResizable(false);

        highlightColorsTable.setBackground(new java.awt.Color(255, 244, 244));
        highlightColorsTable.setModel(getHighLightColorTableModel());
        highlightColorsTable.setRowSelectionAllowed(false);
        jScrollPane2.setViewportView(highlightColorsTable);

        jButton3.setText("Cancel");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Save");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        ideStylesFileJTextField.setText(ini_file_path);
        ideStylesFileJTextField.setEnabled(false);

        jButton5.setText("browse");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout highlightColorDialogLayout = new javax.swing.GroupLayout(highlightColorDialog.getContentPane());
        highlightColorDialog.getContentPane().setLayout(highlightColorDialogLayout);
        highlightColorDialogLayout.setHorizontalGroup(
            highlightColorDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(highlightColorDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(highlightColorDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(highlightColorDialogLayout.createSequentialGroup()
                        .addComponent(ideStylesFileJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 690, Short.MAX_VALUE))
                .addContainerGap())
        );
        highlightColorDialogLayout.setVerticalGroup(
            highlightColorDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(highlightColorDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 368, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(highlightColorDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3)
                    .addComponent(jButton4)
                    .addComponent(ideStylesFileJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5))
                .addContainerGap())
        );

        highlightColorDialog.getAccessibleContext().setAccessibleParent(this);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Javascript Parser");
        setMinimumSize(new java.awt.Dimension(584, 321));

        textPane.setBackground(new java.awt.Color(255, 244, 244));
        textPane.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                textPaneKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(textPane);

        jButton1.setText("Parse");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Set Colors");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jPanel1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel1.setText("File location");

        fileLocationField.setText("C:\\Users\\John Mwai\\Documents\\bravia order management system\\experimental\\myorders.js");
        fileLocationField.setEnabled(false);

        jLabel2.setText("File name");

        fileNameField.setText("myorders.js");
        fileNameField.setEnabled(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fileNameField)
                    .addComponent(fileLocationField, javax.swing.GroupLayout.DEFAULT_SIZE, 308, Short.MAX_VALUE))
                .addGap(10, 10, 10))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(fileLocationField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(fileNameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        jMenu1.setText("File");

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setMnemonic('o');
        jMenuItem1.setText("Open");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem8.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem8.setMnemonic('s');
        jMenuItem8.setText("Save");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem8);
        jMenu1.add(jSeparator1);

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem2.setMnemonic('x');
        jMenuItem2.setText("Exit");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");

        jMenuItem3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem3.setMnemonic('u');
        jMenuItem3.setText("Undo");
        jMenu2.add(jMenuItem3);

        jMenuItem4.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Y, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem4.setMnemonic('r');
        jMenuItem4.setText("Redo");
        jMenu2.add(jMenuItem4);
        jMenu2.add(jSeparator2);

        cutMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_MASK));
        cutMenuItem.setMnemonic('t');
        cutMenuItem.setText("Cut");
        jMenu2.add(cutMenuItem);

        copyMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
        copyMenuItem.setMnemonic('y');
        copyMenuItem.setText("Copy");
        jMenu2.add(copyMenuItem);

        pasteMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.CTRL_MASK));
        pasteMenuItem.setMnemonic('p');
        pasteMenuItem.setText("Paste");
        jMenu2.add(pasteMenuItem);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 187, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton1)
                        .addComponent(jButton2))
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jMenuItem2ActionPerformed
    
    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        openFile();
    }//GEN-LAST:event_jMenuItem1ActionPerformed
    
    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        saveFile();
    }//GEN-LAST:event_jMenuItem8ActionPerformed
    
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        parse();
    }//GEN-LAST:event_jButton1ActionPerformed
    
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        dispalyHighlightColorDialog(true);
    }//GEN-LAST:event_jButton2ActionPerformed
    
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        dispalyHighlightColorDialog(false);
    }//GEN-LAST:event_jButton3ActionPerformed
    
    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        dispalyHighlightColorDialog(false);
    }//GEN-LAST:event_jButton4ActionPerformed
    
    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        displayIDEStylesFileChooser(ideStylesFileJTextField);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void textPaneKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textPaneKeyReleased
        parse();
    }//GEN-LAST:event_textPaneKeyReleased
    
    private void displayIDEStylesFileChooser(JTextField filePath) {
        fileChooser.setCurrentDirectory(
                new File(filePath.getText()));
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int returnVal = fileChooser.showDialog(this,
                "Select");
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            String path = file.getPath();
            filePath.setText(path);
            ini_file_path = path;
        }
        fileChooser.setSelectedFile(null);
    }
    
    private void dispalyHighlightColorDialog(boolean aFlag) {
        if (aFlag) {
            highlightColorDialog.setSize(
                    highlightColorDialog.getPreferredSize());
            highlightColorDialog.setLocationRelativeTo(this);
            
            highlightColorDialog.setVisible(true);
        } else {
            saveHighlightColorTableData();
            highlightColorDialog.setVisible(false);
        }
    }
    
    private void openFile() {
        if (FILE_NOT_SAVED) {
            int option = JOptionPane.showConfirmDialog(rootPane,
                    "The file is not saved. "
                    + "Do you want to save it first?");
            if (option == JOptionPane.OK_OPTION) {
                saveFile();
            } else if (option == JOptionPane.CANCEL_OPTION) {
                return;
            } else {
                //"Don't save" selected
            }
        }
        selectFile(fileLocationField, fileNameField);
        loadFile();
    }
    
    private void loadFile() {
        String s = fileNameField.getText();
        if (s == null) {
            s = "File";
        }
        try {
            textPane.read(new FileReader(new File(fileLocationField.getText())),
                    s);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(rootPane, "An io exception occurred.");
            
            
        }
    }
    
    static class ColorRenderer extends DefaultTableCellRenderer {
        
        @Override
        public void setValue(Object value) {
            setBackground((value == null) ? Color.white : new Color(
                    (Integer) value));
        }
    }
    
    private void saveFile() {
        FILE_NOT_SAVED = false;
        try {
            textPane.write(new FileWriter(new File(fileLocationField.getText())));
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(rootPane, "An io exception occurred.");
        }
    }
    
    private void selectFile(JTextField filePath, JTextField fileName) {
        fileChooser.setCurrentDirectory(
                new File(filePath.getText()));
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int returnVal = fileChooser.showDialog(this,
                "Select");
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            filePath.setText(file.getPath());
            fileName.setText(file.getName());
        }
        fileChooser.setSelectedFile(null);
    }
    
    private HashMap<Object, Action> createActionTable(
            JTextComponent textComponent) {
        HashMap<Object, Action> as = new HashMap<Object, Action>();
        Action[] actionsArray = textComponent.getActions();
        for (int i = 0; i < actionsArray.length; i++) {
            Action a = actionsArray[i];
            as.put(a.getValue(Action.NAME), a);
        }
        return as;
    }
    
    private Action getActionByName(String name) {
        return actions.get(name);
    }
    
    private void createMenus() {
        createEditMenu();
    }
    
    private void createEditMenu() {
        Action cut = getActionByName(DefaultEditorKit.cutAction);
        cut.putValue(Action.NAME, "Cut");
        cut.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_T);
        cutMenuItem.setAction(cut);
        
        Action copy = getActionByName(DefaultEditorKit.copyAction);
        copy.putValue(Action.NAME, "Copy");
        copy.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_Y);
        copyMenuItem.setAction(copy);
        
        Action paste = getActionByName(DefaultEditorKit.pasteAction);
        paste.putValue(Action.NAME, "Paste");
        paste.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_P);
        pasteMenuItem.setAction(paste);
    }
    
    private void parse() {
        new JavascriptParserTestApplication().parse(textPane,
                getHighlightColorsFromINI(true));
    }
    
    @Override
    public void tableChanged(TableModelEvent e) {
        highlightColorsTable.repaint();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info :
                    javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                    
                    
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(
                    JavascriptParserTestGUI.class
                    .getName()).log(
                    java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(
                    JavascriptParserTestGUI.class
                    .getName()).log(
                    java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(
                    JavascriptParserTestGUI.class
                    .getName()).log(
                    java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(
                    JavascriptParserTestGUI.class
                    .getName()).log(
                    java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new JavascriptParserTestGUI().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem copyMenuItem;
    private javax.swing.JMenuItem cutMenuItem;
    private javax.swing.JFileChooser fileChooser;
    private javax.swing.JTextField fileLocationField;
    private javax.swing.JTextField fileNameField;
    private javax.swing.JDialog highlightColorDialog;
    private javax.swing.JTable highlightColorsTable;
    private javax.swing.JTextField ideStylesFileJTextField;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JMenuItem pasteMenuItem;
    private javax.swing.JTextPane textPane;
    // End of variables declaration//GEN-END:variables
}
