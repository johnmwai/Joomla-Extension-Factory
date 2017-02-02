package factory.codegen.app;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseWheelEvent;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author John Mwai
 */
public class ScrollingTabbedPane {

    private final JTabbedPane tabbedPane;
    private TabScroller tabScroller = new TabScroller();
    private final HashMap<JComponent, LinkedList<ButtonTabComponent.ButtonClosingHook>> hooks = new HashMap<>();
    private final HashMap<JComponent, LinkedList<ButtonTabComponent.ButtonClosedListener>> closedListeners = new HashMap<>();
    private final HashMap<Component, FileHolder> fileHolders = new HashMap<>();

    public HashMap<JComponent, LinkedList<ButtonTabComponent.ButtonClosingHook>> getHooks() {
        return hooks;
    }

    public HashMap<JComponent, LinkedList<ButtonTabComponent.ButtonClosedListener>> getClosedListeners() {
        return closedListeners;
    }

    public HashMap<Component, FileHolder> getFileHolders() {
        return fileHolders;
    }

    public void showTabForFileHolder(FileHolder fileHolder) {
        Set<Component> keySet = getFileHolders().keySet();
        for (Component c : keySet) {
            FileHolder fh = getFileHolders().get(c);
            if (fh == fileHolder) {
                int i = tabbedPane.indexOfComponent(c);
                if (i != -1) {

                    tabbedPane.setSelectedIndex(i);
                }
            }
        }
    }

    public void showTabForTabbedComponent(final ButtonTabComponent tab) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                int index = tabbedPane.indexOfTabComponent(tab);
                if (index != -1) {
                    tabbedPane.setSelectedIndex(index);
                }
            }
        });
    }

    private class TabScroller extends MouseAdapter {

        @Override
        public void mouseWheelMoved(final MouseWheelEvent e) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    int samt = e.getWheelRotation();
                    ButtonTabComponent tab;
                    if (samt > 0) {
                        tab = nextLeft();
                    } else {
                        tab = nextRight();
                    }
                    if (tab != null) {
                        tab.scrollToVisible();
                    }
                }
            });

        }
    }

    private ButtonTabComponent nextRight() {
        return getNext(true);
    }

    public void cloaseAllTabs() {
        LinkedList<ButtonTabComponent> tabs = new LinkedList<>();
        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            Component comp = tabbedPane.getTabComponentAt(i);
            if (comp instanceof ButtonTabComponent) {
                tabs.add((ButtonTabComponent) comp);
            }
        }
        for (ButtonTabComponent tab : tabs) {
            tab.close();
        }
    }

    private ButtonTabComponent getNext(boolean right) {
        int tabCount = tabbedPane.getTabCount();
        if (tabCount == 0) {
            return null;
        }
        ButtonTabComponent tab = (ButtonTabComponent) tabbedPane.getTabComponentAt(
                right ? tabCount - 1 : 0), last = tab;
        if (tab.canBeSeen()) {
            return null;
        }
        if (right) {
            for (int i = tabCount - 1; i > 0; i--) {
                tab = (ButtonTabComponent) tabbedPane.getTabComponentAt(
                        i);
                if (tab.canBeSeen()) {
                    break;
                }
                last = tab;
            }
        } else {
            for (int i = 0; i < tabCount; i++) {
                tab = (ButtonTabComponent) tabbedPane.getTabComponentAt(
                        i);
                if (tab.canBeSeen()) {
                    break;
                }
                last = tab;
            }
        }
        if (last.canBeSeen()) {
            return null;
        }
        return last;
    }

    private ButtonTabComponent nextLeft() {
        return getNext(false);
    }

    public ScrollingTabbedPane() {
        tabbedPane = new JTabbedPane();
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        tabbedPane.addMouseWheelListener(tabScroller);
    }

    public void addCloseableTab(String title, JComponent component) {
        tabbedPane.add(title, component);
        initTabs();
    }

    public ButtonTabComponent addCloseableTab(String title, ImageIcon icon,
            JComponent component) {
        tabbedPane.addTab(title, icon, component);
        hooks.put(component,
                new LinkedList<ButtonTabComponent.ButtonClosingHook>());
        closedListeners.put(component,
                new LinkedList<ButtonTabComponent.ButtonClosedListener>());
//        tabbedPane.setSelectedComponent(component);
        final int index = tabbedPane.indexOfComponent(component);
        initTabs();
        if (index != -1) {
            ButtonTabComponent tc = (ButtonTabComponent) tabbedPane.getTabComponentAt(
                    index);
            tc.scrollToVisible();
            ButtonTabComponent tab = (ButtonTabComponent) tabbedPane.getTabComponentAt(
                    index);
            return tab;
        }
        return null;
    }

    private void initTabs() {
        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            final ButtonTabComponent btc = new ButtonTabComponent(tabbedPane,
                    this);
            tabbedPane.setTabComponentAt(i, btc);
        }
    }

    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }
}
