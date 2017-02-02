package factory.legacy;

/**
 * <p/>
 * CONFIDENTIAL PROPRIETARY: FUSCARD<br/>Copyright (c) 2012 Fuscard. All rights
 * reserved.<br/> Creation time: Oct 12, 2012 -- 2:08:31 PM<br/>
 * <p/>
 * @author John Mwai
 */
public class Simple extends ComponentProject {

    public Simple(Component myComponent) {
        super(myComponent);
    }

    public static void main(String[] args) {
        String name = "Shopping Cart";
        String family = "Fuscard Enterprise";
        ComponentProperties properties = new ComponentProperties(name, "backend",
                "frontend");
        properties.setReleaseIndex(4);
        Component smp = new Component(properties);
        smp.setVendor("Fuscard Inc.");
        smp.setVendorEmail("info@fuscard.com");
        smp.setVendorUrl("http://fuscard.com");
        smp.setCopyrightInfo(
                "Copyright (c) 2012 Fuscard Inc. - All rights reserved.");
        smp.setLicenceInfo("Commercial Licence");
        smp.setDescription(
                "This is a test run of the automated joomla extension factory.");
        smp.setClient("Mambo Microsystems.");
        smp.setVersion("2.0.0");
        smp.setDirectory(baseDir);

        String title = name + " v2.0 [ " + family + " ]";
        smp.setAdminMenuName(title);
        smp.setTitle(title);
        smp.setDisplayName(family);

        smp.setTableDataFile(
                "C:\\Joomla Extension Factory\\notable.tabledata");

        smp.setDeleteDirectoryOnZip(false);

        smp.setImagesSourceFolder(
                "C:\\Users\\mwai\\Documents\\bravia order management system\\com_braviacart-1.0.39\\com_braviacart\\backend\\assets\\images");
        Simple simple = new Simple(smp);

        simple.make();



        makeAdminPlainView(smp, "SimpleAdmin", "Simple Administrator");

        //
        makeAdminPlainView(smp, "SimpleAdminCp",
                "Simple Administrator Control Panel");
        smp.setDefaultView("SimpleAdminCp", MVCClassType.ADMIN);
        View view = getAdminView(smp, "SimpleAdminCp");
        //

        view.addChild(getAdminView(smp, "SimpleAdmin"));
        view.addChild(getAdminView(smp, "Orders"));
        view.addChild(getAdminView(smp, "AcademicOrders"));
        view.addChild(getAdminView(smp, "ArticleOrders"));

        makeSitePlainView(smp, "SimpleSite", "Simple Site");
        makeSitePlainView(smp, "ListProducts", "Product Listing");
        makeSitePlainView(smp, "SeeShoppingCart", "View Shopping Cart");
        makeSitePlainView(smp, "EditDetails", "Edit Product Order Details");
        makeSitePlainView(smp, "PreviewDetails", "Preview Product Order Details");
        makeSitePlainView(smp, "ProceedToCheckout",
                "Proceed To Secure Payment Gateway");
        makeSitePlainView(smp, "SimpleSiteCP", "Simple Site Control Panel");

        //
        View v = getSiteView(smp, "SimpleSiteCP");
        v.addChild(getSiteView(smp, "SimpleSite"));
        v.addChild(getSiteView(smp, "ListProducts"));
        v.addChild(getSiteView(smp, "SeeShoppingCart"));

        smp.setDefaultView("SimpleSiteCP", MVCClassType.SITE);
        //


        smp.build();
    }

    private class Plans extends ListDetailsViewMaker {

        public Plans(Component comp) {
            super(comp, "Plan",
                    MVCClassType.ADMIN);
        }

        @Override
        void make() {
            addField(new Field("id",
                    DataTypes.PrimaryKey));
            addField(new Field("date_published",
                    DataTypes.Date, "Date Published", "", ""));
            addField(new Field("published", "Published",
                    "Is this plan available?", "", "Yes", "No"));
            addField(new Field("product",
                    DataTypes.Varchar));
            addTitle(new Field("plan_name",
                    DataTypes.Varchar, "Plan Name",
                    "Name this plan for your convenience", ""));
        }
    }

    private class Orders extends ListDetailsViewMaker {

        public Orders(Component comp) {
            super(comp, "Order",
                    "Orders", "orders", "New Order", "Edit Order Details",
                    "List of Orders", MVCClassType.ADMIN);
        }

        @Override
        void make() {
            addField(new Field("id",
                    DataTypes.PrimaryKey));
            addField(new Field("total_amount",
                    DataTypes.Integer));
            addTitle(new Field("order_number",
                    DataTypes.Integer));
        }
    }

    private class AcademicOrders extends ListDetailsViewMaker {

        public AcademicOrders(Component comp) {
            super(comp, "AcademicOrder",
                    MVCClassType.ADMIN);
        }

        @Override
        void make() {
            addField(new Field("id",
                    DataTypes.PrimaryKey));
            addField(new Field("subject_area",
                    DataTypes.Varchar));
            addTitle(new Field("order",
                    DataTypes.Sql, "Order Number",
                    "The order number of this academic order in the orders table",
                    "", "orders", new String[]{
                        "'': -- Select an order -- "
                    }));
        }
    }

    private class ArticleOrders extends ListDetailsViewMaker {

        public ArticleOrders(Component comp) {
            super(comp, "ArticleOrder",
                    MVCClassType.ADMIN);
        }

        @Override
        void make() {
            addField(new Field("id",
                    DataTypes.PrimaryKey));
            addField(new Field("tone_of_project",
                    DataTypes.Varchar));
            addTitle(new Field("order",
                    DataTypes.Sql, "Order Number",
                    "The order number of this academic order in the orders table",
                    "", "orders", new String[]{
                        "'': -- Select an order -- "
                    }));
        }
    }
    private class Reserve1 extends ListDetailsViewMaker {

        public Reserve1(Component comp) {
            super(comp, "Reserve1",
                    MVCClassType.ADMIN);
        }

        @Override
        void make() {
            addField(new Field("id",
                    DataTypes.PrimaryKey));
            addField(new Field("foo",
                    DataTypes.Varchar));
            addTitle(new Field("bar",
                    DataTypes.Varchar));
        }
    }
    private class Reserve2 extends ListDetailsViewMaker {

        public Reserve2(Component comp) {
            super(comp, "Reserve2",
                    MVCClassType.ADMIN);
        }

        @Override
        void make() {
            addField(new Field("id",
                    DataTypes.PrimaryKey));
            addField(new Field("foo",
                    DataTypes.Varchar));
            addTitle(new Field("bar",
                    DataTypes.Varchar));
        }
    }
}
