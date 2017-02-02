package factory.legacy;

import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * <p/>
 * CONFIDENTIAL PROPRIETARY: FUSCARD<br/>Copyright (c) 2012 Fuscard. All rights
 * reserved.<br/> Creation time: Oct 12, 2012 -- 2:14:33 PM<br/>
 * <p/>
 * @author John Mwai
 */
public class ComponentProject extends Project {

    private final Component myComponent;
    static final String baseDir = "C:\\Joomla Extension Factory\\legacy";

    public ComponentProject(Component myComponent) {
        this.myComponent = myComponent;
    }

    /**
     * This method uses reflection to obtain all the declared ListDetailsView
     * classes, instantiate them and call a method on them.
     */
    void make() {
        for (Class c : this.getClass().getDeclaredClasses()) {
            if (ListDetailsViewMaker.class.isAssignableFrom(c)) {
                try {
                    ListDetailsViewMaker am = (ListDetailsViewMaker) (c.getConstructor(
                            getClass(), Component.class).newInstance(
                            this, myComponent));
                    am._make();
                } catch (Exception moveOn) {
                }
            }
        }
    }

    static View getAdminView(Component c, String name) {
        return (View) c.getMVCClass(name, MVCClassType.ADMIN, "view");
    }

    static View getSiteView(Component c, String name) {
        return (View) c.getMVCClass(name, MVCClassType.SITE, "view");
    }

    static LinkedList<MVCClass> makeAdminPlainView(Component c, String name, String title) {
        return injectTitle(c.makeMVCset(name, MVCClassType.ADMIN,
                new MVCClass.Config(MVCClass.Config.Type.Plain)), title);
    }

    static void makeSitePlainView(Component c, String name, String title) {
        injectTitle(c.makeMVCset(name, MVCClassType.SITE,
                new MVCClass.Config(MVCClass.Config.Type.Plain)), title);
    }

    static LinkedList<MVCClass> injectTitle(LinkedList<MVCClass> classes, String title) {
        for (MVCClass c : classes) {
            if (c instanceof View) {
                ((View) c).setDisplayTitle(title);
            }
        }
        return classes;
    }

    abstract class ListDetailsViewMaker {

        Component comp;
        View v;
        Table t;
        Record r;
        final String Details;
        final String List;
        final String tableName;
        final String NewTitle;
        final String EditTitle;
        final String ListTitle;
        final MVCClassType location;

        public ListDetailsViewMaker(Component comp, String Details, String List, String tableName, String NewTitle, String EditTitle, String ListTitle, MVCClassType location) {
            checkViewNames(Details, List);

            this.comp = comp;
            this.Details = Details;
            this.List = List;
            comp.makeListDetails(this.List, this.Details, location);
            v = (View) comp.getMVCClass(List, location, "view");
            r = new Record();
            this.tableName = tableName;
            t = new Table(this.tableName, r, comp);
            v.setTable(t, true);


            this.NewTitle = NewTitle;
            this.EditTitle = EditTitle;
            this.ListTitle = ListTitle;
            v.setTitles(this.NewTitle, this.EditTitle, this.ListTitle, location);
            this.location = location;
        }

        public ListDetailsViewMaker(Component comp, String Details, String List, String parentView, MVCClassType parentViewLocation, String NewTitle, String EditTitle, String ListTitle, MVCClassType location) {
            checkViewNames(Details, List);

            this.comp = comp;
            this.Details = Details;
            this.List = List;
            comp.makeListDetails(this.List, this.Details, location);
            v = (View) comp.getMVCClass(List, location, "view");

            View otherView = (View) comp.getMVCClass(parentView,
                    parentViewLocation, "view");
            t = otherView.getTable();
            r = t.getRecord();
            v.setTable(t, true);
            tableName = t.getName();


            this.NewTitle = NewTitle;
            this.EditTitle = EditTitle;
            this.ListTitle = ListTitle;
            v.setTitles(this.NewTitle, this.EditTitle, this.ListTitle, location);
            this.location = location;
        }

        private void checkViewNames(String Details, String List) {

            String det = Details;
            String lis = List;
            if (det.endsWith("s") || lis.endsWith("x")) {
                det += "es";
            } else if (det.endsWith("y")) {
                det = det.substring(0, det.lastIndexOf("y")) + "ies";
            } else {
                det += "s";
            }

            if (!ScriptAssistance.stringsEqual(det, lis)) {
                if (JOptionPane.showConfirmDialog(null,
                        "Is this correct?\nDetails: " + Details + "\nList: " + List) == JOptionPane.NO_OPTION) {
                    System.exit(1);
                }
            }
        }

        public ListDetailsViewMaker(Component comp, String Details, MVCClassType type) {
            this(comp, Details, Details + "s", Details.toLowerCase() + "_tbl",
                    "New " + Details, "Edit " + Details, Details + "s", type);
        }

        public ListDetailsViewMaker(Component comp, String Details, String List, MVCClassType type) {
            this(comp, Details, List,
                    Details.toLowerCase() + "_tbl_" + (type == MVCClassType.ADMIN ? "adm" : "ste"),
                    "New " + Details, "Edit " + Details, List, type);
        }

        public ListDetailsViewMaker(Component comp, String Details, String List, String otherView, MVCClassType otherViewLoc, MVCClassType type) {
            this(comp, Details, List, Details.toLowerCase() + "_tbl",
                    "New " + Details, "Edit " + Details, List, type);
        }

        final void _make() {
            make();
            extras();
            if (r.getTitleField() == null) {
                throw new IllegalStateException(
                        t.getName() + " does not have a title");
            }
        }

        abstract void make();

        void addField(Field f) {
            f.setParent(comp);
            f.setTable(t);
            try {
                r.addField(f);
            } catch (Exception ex) {
                System.err.println(
                        "Table " + t.getName() + " couldn't add field " + f.name);
                Logger.getLogger(BraviaCart.class.getName()).log(Level.SEVERE,
                        null, ex);
            }
        }

        void makeAllReadOnly() {
            for (Field f : t.getFields()) {
                f.setReadonly(true);
            }
        }

        void makeAllOptional() {
            for (Field f : t.getFields()) {
                f.setRequired(false);
            }
        }

        void addTitle(Field f) {
            addField(f);
            r.setTitle(f);
        }

        boolean isSite() {
            return location == MVCClassType.SITE;
        }

        void extras() {
            Field f = new Field("Params",
                    DataTypes.Params,
                    "Parameters",
                    "Set the parameters for this item",
                    "");
            f.addOption("", "JGLOBAL_USE_GLOBAL");
            f.addOption("0", "JHIDE");
            f.addOption("1", "JSHOW");
            f.setRequired(false);
            addField(f);
            addField(new Field("catid",
                    DataTypes.Category,
                    "Category",
                    "Set the category that this field belongs to",
                    "The category this field belongs to", false, false, null));

        }
    }
}
