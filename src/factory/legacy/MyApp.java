package factory.legacy;

/**
 *
 * @author John Mwai
 */
public class MyApp extends ComponentProject {

    public MyApp(Component myComponent) {
        super(myComponent);
    }

    public static void main(String[] args) {
        String name = "bravia service";
        String family = "JEFPrototype";
        ComponentProperties properties = new ComponentProperties(name, "backend",
                "frontend");
        properties.setReleaseIndex(4);
        Component braviacart = new Component(properties);
        braviacart.setVendor("MamboCreative.com");
        braviacart.setVendorEmail("info@mambo.co.ke");
        braviacart.setVendorUrl("http://MamboCreative.com");
        braviacart.setCopyrightInfo(
                "Copyright (c) 2012 Mambo Microsystems Limited. - All rights reserved.");
        braviacart.setLicenceInfo("Commercial Licence");
        braviacart.setDescription(
                "This is a test run of the automated joomla extension factory.");
        braviacart.setClient("Mambo Microsystems.");
        braviacart.setVersion("2.0.0");
        braviacart.setDirectory(baseDir);
        String title = name + " v2.0 [ " + family + " ]";
        braviacart.setAdminMenuName(title);
        braviacart.setTitle(title);
        braviacart.setDisplayName(family);
        braviacart.setTableDataFile(
                "C:\\Joomla Extension Factory\\bravia_cart.tabledata");

        braviacart.setDeleteDirectoryOnZip(false);

        braviacart.setImagesSourceFolder(
                "C:\\Users\\John Mwai\\Documents\\bravia order management system\\com_braviacart-1.0.39\\com_braviacart\\backend\\assets\\images");
        BraviaCart braviaCartImpl = new BraviaCart(braviacart);
        braviaCartImpl.make();
        makeSitePlainView(braviacart, "CheckOut", "Check Out");

        
        makeAdminPlainView(braviacart, "ControlPanel", "Control Panel");
        
        braviacart.setDefaultView("CheckOut", MVCClassType.SITE);


        braviacart.setDefaultView("ControlPanel", MVCClassType.ADMIN);
        braviacart.build();
    }
    
    private class AcademicDefaults extends ListDetailsViewMaker {

        public AcademicDefaults(Component comp) {
            super(comp, "AcademicDefault", "AcademicDefaults", "academic_defaults",
                    "New Academic Default", "Edit Academic Default", "Academic Defaults",
                    MVCClassType.ADMIN);
        }

        @Override
        void make() {
            addField(new Field("id",
                    DataTypes.PrimaryKey,
                    "id",
                    "id",
                    "id"));
            addTitle(new Field("default",
                    DataTypes.Varchar, "Academic"//
                    , "Enter the name of this ac"//
                    , "Enter the name of this ac"));
        }
    }

}
