package factory.codegen.app;

import com.fuscard.commons.FileUtils;
import factory.Application;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

/**
 *
 * @author John Mwai
 */
public class JEFHelp extends javax.swing.JFrame {

    private TreeMouseAdapter myTreeMouseAdapter = new TreeMouseAdapter();
    private MyTreeCellRenderer myTreeCellRenderer = new MyTreeCellRenderer();

    /**
     * Creates new form JEFHelp
     */
    public JEFHelp() {
        initComponents();
        jTree1.addMouseListener(myTreeMouseAdapter);
        jTree1.setCellRenderer(myTreeCellRenderer);
        createHelpTree();
        jEditorPane1.addHyperlinkListener(new HyperlinkListener() {
            @Override
            public void hyperlinkUpdate(HyperlinkEvent hle) {
                if (HyperlinkEvent.EventType.ACTIVATED.equals(hle.getEventType())) {
                    try {
                        jEditorPane1.setPage(hle.getURL());
                    } catch (IOException ex) {
                        Application.getInstance().error(ex.getMessage());
                    }
                }
            }
        });
    }

    private class TreeMouseAdapter extends AllSystems.ClickListener {

        JPopupMenu popup = new JPopupMenu();

        @Override
        public void doubleClick(MouseEvent e) {
            if (e.getSource() == jTree1) {
                Point p = e.getPoint();
                TreePath treePath = jTree1.getPathForLocation(p.x, p.y);
                openFile(jTree1, treePath);
            }
        }

        @Override
        public void singleClick(MouseEvent e) {
        }
    }

    private void openFile(JTree tree, TreePath treePath) {
        if (tree == jTree1) {
            if (treePath != null) {
                Object obj = treePath.getLastPathComponent();
                if (obj instanceof DefaultMutableTreeNode) {
                    Object obj1 = ((DefaultMutableTreeNode) obj).getUserObject();
                    if (obj1 instanceof File) {
                        File f = (File) obj1;
                        try {
                            URL url = f.toURI().toURL();
                            jEditorPane1.setPage(url);
                        } catch (Exception ex) {
                            Logger.getLogger(JEFHelp.class.getName()).log(
                                    Level.SEVERE,
                                    null, ex);
                        }
                    }
                }
            }
        }
    }

    /**
     *
     */
    public void createHelpTree() {//
        File helpPath = new File(
                "help");
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(
                "JEF Help Content");
        jTree1.setModel(new DefaultTreeModel(root));
        jTree1.setRootVisible(true);
        buildNode(helpPath, root);
        File f1 = new File("help/index.html");
        File f2 = new File("help/index.htm");
        if (f1.isFile()) {
            try {
                jEditorPane1.setPage(f1.toURI().toURL());
            } catch (IOException ex) {
            }
        } else if (f2.isFile()) {
            try {
                jEditorPane1.setPage(f2.toURI().toURL());
            } catch (IOException ex) {
            }
        }
    }

    private void buildNode(File dir, DefaultMutableTreeNode node) {
        for (File f : dir.listFiles()) {
            boolean add = false;
            DefaultMutableTreeNode child = null;
            if (f.isDirectory()) {
                add = true;
            } else {
                String ext = FileUtils.getExtension(f);
                if ("html".equals(ext) || "htm".equals(ext)) {
                    add = true;
                }
            }
            if (add) {
                child = new DefaultMutableTreeNode(f);
                node.add(child);
            }
            if (f.isDirectory()) {
                buildNode(f, child);
            }
        }
    }

    private class MyTreeCellRenderer extends DefaultTreeCellRenderer {

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value,
                boolean sel, boolean expanded, boolean leaf, int row,
                boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, sel,
                    expanded, leaf, row, hasFocus);


            if (!sel) {
                setOpaque(true);
                setBackground(jTree1.getBackground());
                setForeground(jTree1.getForeground());
            } else {
                setOpaque(false);
            }
            Object userobj = ((DefaultMutableTreeNode) value).
                    getUserObject();

            if (userobj instanceof File) {
                setText(((File) userobj).getName());
            }
            return this;

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

        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree();
        jScrollPane3 = new javax.swing.JScrollPane();
        jEditorPane1 = new javax.swing.JEditorPane();

        setTitle("Joomla Extension Factory Help");

        jSplitPane1.setDividerLocation(150);
        jSplitPane1.setResizeWeight(0.25);

        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("Help Contents");
        jTree1.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        jScrollPane1.setViewportView(jTree1);

        jSplitPane1.setLeftComponent(jScrollPane1);

        jEditorPane1.setEditable(false);
        jScrollPane3.setViewportView(jEditorPane1);

        jSplitPane1.setRightComponent(jScrollPane3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 622, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 368, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
            for (javax.swing.UIManager.LookAndFeelInfo info
                    : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(JEFHelp.class.getName()).log(
                    java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JEFHelp.class.getName()).log(
                    java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JEFHelp.class.getName()).log(
                    java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JEFHelp.class.getName()).log(
                    java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new JEFHelp().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JEditorPane jEditorPane1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTree jTree1;
    // End of variables declaration//GEN-END:variables
}
