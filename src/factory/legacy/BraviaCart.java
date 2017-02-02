package factory.legacy;

/**
 * n
 * <p/>
 * CONFIDENTIAL PROPRIETARY: FUSCARD<br/>Copyright (c) 2012 Fuscard. All rights
 * reserved.<br/> Creation time: Sep 25, 2012 -- 5:57:20 AM<br/>
 * <p/>
 * @author John Mwai
 */
public class BraviaCart extends ComponentProject {

    public BraviaCart(Component myComponent) {
        super(myComponent);
    }
    

    public static void main(String[] args) {

        String name = "university";
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
        makeSitePlainView(braviacart, "CheckOut", "Chack Out");
        makeSitePlainView(braviacart, "ClientArea", "Client Area");
        makeSitePlainView(braviacart, "ClientDownloads", "My Downloads");

        View v = getSiteView(braviacart, "ClientArea");
        v.addChild(getSiteView(braviacart, "ClientOrders"));
        v.addChild(getSiteView(braviacart, "ClientOrder"));
        v.addChild(getSiteView(braviacart, "ClientMessage"));
        v.addChild(getSiteView(braviacart, "ClientMessages"));
        v.addChild(getSiteView(braviacart, "ClientDownloads"));



        makeAdminPlainView(braviacart, "AttachmentOptions", "Attachment Options");
        makeAdminPlainView(braviacart, "ControlPanel", "Control Panel");
        makeAdminPlainView(braviacart, "RootPanel", "Concise Control Panel");

        makeAdminPlainView(braviacart, "Emails", "Email Configurations");
        makeAdminPlainView(braviacart, "LogFile", "Log File");
        makeAdminPlainView(braviacart, "NewOrder", "New Order");
        makeAdminPlainView(braviacart, "OrderOptions", "Order Options");
        makeAdminPlainView(braviacart, "PaymentGateways", "Payment Gateways");
        makeAdminPlainView(braviacart, "Paypal", "Paypal");
        makeAdminPlainView(braviacart, "ReCaptcha", "ReCaptcha");
        makeAdminPlainView(braviacart, "MoreParameters",
                "Non Pricing Order Parameters");
        makeAdminPlainView(braviacart, "GeneralSettings",
                "Miscellaneous Settings");//GeneralSettings
        makeAdminPlainView(braviacart, "PricingParameters", "Pricing Parameters");
        makeAdminPlainView(braviacart, "Sms", "Sms Settings");
        makeAdminPlainView(braviacart, "Swreg", "Swreg Payment Gateway");
        makeAdminPlainView(braviacart, "Introduction", "Index");
        braviacart.setDefaultView("ClientArea", MVCClassType.SITE);


        View view = getAdminView(braviacart, "Introduction");
        braviacart.setAdminViewIndex(view);
        view = getAdminView(braviacart, "ControlPanel");
        braviacart.setControlPanel(view);
        view = getAdminView(braviacart, "RootPanel");
        braviacart.setRootPanel(view);
        view.addChild(getAdminView(braviacart, "MyOrders"));

        view.addChild(getAdminView(braviacart, "PricingParameters"));

        View view2 = getAdminView(braviacart, "PricingParameters");
        view2.addChild(getAdminView(braviacart, "DocTypes"));
        view2.addChild(getAdminView(braviacart, "Urgencies"));
        view2.addChild(getAdminView(braviacart, "Spacings"));
        view2.addChild(getAdminView(braviacart, "AcademicLevels"));
        view2.addChild(getAdminView(braviacart, "Coupons"));

        view.addChild(getAdminView(braviacart, "AttachmentOptions"));
        view.addChild(getAdminView(braviacart, "Customers"));
        view.addChild(getAdminView(braviacart, "Attachments"));
        view.addChild(getAdminView(braviacart, "Writers"));
        view.addChild(getAdminView(braviacart, "GeneralSettings"));
        view2 = getAdminView(braviacart, "PaymentGateways");
        view.addChild(view2);
        view2.addChild(getAdminView(braviacart, "Paypal"));
        view2.addChild(getAdminView(braviacart, "Swreg"));

        view = getAdminView(braviacart, "GeneralSettings");//General Settings

        view.addChild(getAdminView(braviacart, "LogFile"));
        view.addChild(getAdminView(braviacart, "MoreParameters"));
        view.addChild(getAdminView(braviacart, "ReCaptcha"));
        view.addChild(getAdminView(braviacart, "Sms"));

        view = getAdminView(braviacart, "MoreParameters");//Non pricing parameters

        view.addChild(getAdminView(braviacart, "Countries"));
        view.addChild(getAdminView(braviacart, "OrderStyles"));
        view.addChild(getAdminView(braviacart, "LanguageStyles"));


        braviacart.setDefaultView("ControlPanel", MVCClassType.ADMIN);
        braviacart.build();
    }

    private class AcademicLevels extends ListDetailsViewMaker {

        public AcademicLevels(Component comp) {
            super(comp, "AcademicLevel", "AcademicLevels", "academic_levels",
                    "Add New Academic Level Definition",
                    "Edit Academic Level Definition", "List of Academic Levels",
                    MVCClassType.ADMIN);
        }

        @Override
        void make() {
            addField(new Field("id",
                    DataTypes.PrimaryKey,
                    "id",
                    "id",
                    "id"));
            addField(new Field("cost_factor",
                    DataTypes.Double, "Cost Factor"//
                    ,
                    "The cost factor that will be added when the academic level is selected in the order form"//
                    , ""));
            addTitle(new Field("academic_lvl",
                    DataTypes.Varchar, "Academic Level"//
                    , "Enter a descriptive title for this academic level"//
                    , ""));

        }
    }

    /*
     */
    private class Attachments extends ListDetailsViewMaker {

        public Attachments(Component comp) {
            super(comp, "Attachment", "Attachments", "attachments",
                    "New Attachment", "Edit Attachment", "Attachments",
                    MVCClassType.ADMIN);
        }

        @Override
        void make() {
            addField(new Field("id",
                    DataTypes.PrimaryKey,
                    "id",
                    "id",
                    "id"));
            addTitle(new Field("attachment",
                    DataTypes.Varchar, "File Name"//
                    , "Enter the name of this file"//
                    , "Enter the name of this file"));
            Field f = new Field("customer_id",
                    DataTypes.Sql, "Customer Name",
                    "Set the customer who owns this attachment", "", "customers");
            f.addOption("", " -- Select customer -- ");
            addField(f);
            f = new Field("order_id",
                    DataTypes.Sql, "Order",
                    "Set the order that this attachment belongs to",
                    "Set the order that this attachment belongs to", "orders");
            f.addOption("", "-- Select order -- ");
            addField(f);
            f = new Field("status",
                    DataTypes.List, "Attachment Status",
                    "Change the status of this attachment", "");
            f.addOption("", " -- Select status -- ");
            f.addOption("BLOCKED", "Blocked");
            f.addOption("OK", "Okay");
            f.setDisplayedSite(false);
            addField(f);
        }
    }

    private class Countries extends ListDetailsViewMaker {

        public Countries(Component comp) {
            super(comp, "Country", "Countries", "countries", "Add New Country",
                    "Edit Country Details", "List of Countries",
                    MVCClassType.ADMIN);
        }

        @Override
        void make() {
            addField(new Field("id",
                    DataTypes.PrimaryKey,
                    "id",
                    "id",
                    "id"));
            addTitle(new Field("country_name",
                    DataTypes.Varchar, "Country Name",
                    "Enter the name of the country", ""));
            addField(new Field("country_code",
                    DataTypes.Varchar, "Country Code",
                    "Enter the country calling code of this country e.g. 254 for Kenya",
                    ""));
        }
    }

    private class Messages extends ListDetailsViewMaker {

        public Messages(Component comp) {
            super(comp, "Message", MVCClassType.ADMIN);
        }

        @Override
        void make() {
            addField(new Field("id",
                    DataTypes.PrimaryKey));
            addField(new Field("read",
                    DataTypes.Integer, "Read",
                    "Mark this message as read or not read", ""));
            addTitle(new Field("subject",
                    DataTypes.Integer, "Subject",
                    "Enter a subject for this message",
                    "Enter a subject for this message"));
            Field f = new Field("sender",
                    DataTypes.Sql, "Sender", "Select a sender for this message",
                    "", "customers", new String[]{
                        "-1:Admin"
                    });
            f.addOption("", " -- Select a sender -- ");
            f.setDisplayedSite(false);
            addField(f);
            f = new Field("recipient",
                    DataTypes.Sql, "Receipient",
                    "Select the customer to send the message to", "",
                    "customers", new String[]{
                        "-1:Admin"
                    });
            f.setDisplayedSite(false);
            addField(f);
            addField(new Field("body",
                    DataTypes.Text, "Body",
                    "Enter the body of the message to send to the customer",
                    "Enter the body of the message"));
        }
    }

    private class Coupons extends ListDetailsViewMaker {

        public Coupons(Component comp) {
            super(comp, "Coupon", "Coupons", "coupons", "Add new Coupon",
                    "Edit coupon Details", "List of Coupons", MVCClassType.ADMIN);
        }

        @Override
        void make() {
            addField(new Field("id",
                    DataTypes.PrimaryKey));
            addTitle(new Field("coupon_code",
                    DataTypes.Varchar, "Coupon Code",
                    "Enter a unique coupon code", ""));
            addField(new Field("discount_rate",
                    DataTypes.Double, "Discount Rate",
                    "The discount rate that this coupon entitles the customer",
                    ""));
            addField(new Field("coupon_status",
                    DataTypes.List, "Coupon Status",
                    "The status of this coupon", "", new String[]{
                        "'': -- Select status -- ",
                        "ACTIVE:Active",
                        "INACTIVE:Inactive",
                        "EXPIRED:Expired"
                    }));
            addField(new Field("usage_limit",
                    DataTypes.Integer, "Usage Limit",
                    "The maximum number of times this coupon will be used", ""));
            addField(new Field("expiry_date",
                    DataTypes.Date, "Expiry Date",
                    "The date this coupon expires", ""));
            addField(new Field("times_used",
                    DataTypes.Integer, "Usage Count",
                    "The number of times this coupon has been used", "", false,
                    true, "0"));
        }
    }

    private class Customers extends ListDetailsViewMaker {

        public Customers(Component comp) {
            super(comp, "Customer", "Customers", "customers",
                    "Add New Customer Record", "Edit Customer Details",
                    "List of Customers", MVCClassType.ADMIN);
        }

        @Override
        void make() {
            addField(new Field("id",
                    DataTypes.PrimaryKey));
            addField(new Field("user_table_id",
                    DataTypes.Integer, "User Id",
                    "The id of this customer in the user table", ""));
            addTitle(new Field("fname",
                    DataTypes.Varchar, "First Name",
                    "The fist name of the customer",
                    "Please Enter your first name"));
            addField(new Field("lname",
                    DataTypes.Varchar, "Last Name",
                    "The last name of the customer",
                    "Please enter your last name"));
            addField(new Field("email",
                    DataTypes.Email, "Email",
                    "The email address of the customer",
                    "Please enter your email address"));
            addField(new Field("country_code",
                    DataTypes.Sql, "Country", "The country of the customer",
                    "Please select your country", "countries"));
            addField(new Field("contact_phone",
                    DataTypes.Varchar, "Contact phone",
                    "The contact phone of this customer",
                    "Please enter your contact phone"));
            addField(new Field("mobile",
                    DataTypes.Varchar, "Cell", "The Cell phone of this customer",
                    "Please enter your cell phone here"));
            addField(new Field("agree_sms",
                    DataTypes.List, "Sms notifications",
                    "Set whether the client agrees to sms notificatios",
                    "Agree to sms notifications", new String[]{
                        "'': -- select an option -- ",
                        "0:Don't send me sms notifications",
                        "1:I agree to sms notificatios"}));
            addField(new Field("night_calls",
                    DataTypes.List, "Night calls",
                    "Set whether this customer agrees to night calls",
                    "Do you agree to be called at night?", new String[]{
                        "'': -- Select an option -- ",
                        "1:Yes, you may call me at night!",
                        "0:No thank you.",
                        "[def]:''"
                    }));
        }
    }

    private class ArticleProjectTypes extends ListDetailsViewMaker {

        public ArticleProjectTypes(Component comp) {
            super(comp, "ArticleProjectType", "ArticleProjectTypes",
                    "article_project_types", "Add New Type of Article Project",
                    "Edit Article Project Type Definition",
                    "List of Article Project Types", MVCClassType.ADMIN);
        }

        @Override
        void make() {
            addField(new Field("id",
                    DataTypes.PrimaryKey));
            addTitle(new Field("title",
                    DataTypes.Varchar, "Project Type",
                    "Define a project type name", ""));
            addField(new Field("cost_factor",
                    DataTypes.Double, "Cost Factor",
                    "The cost factor to be added to the order default cost "
                    + "per page if this project type is selected",
                    ""));
        }
    }

    private class ArticleTones extends ListDetailsViewMaker {

        public ArticleTones(Component comp) {
            super(comp, "ArticleTone", "ArticleTones", "article_tones",
                    "New Artcle Tone Definition", "Edit Article Tone Details",
                    "List of Article Tones", MVCClassType.ADMIN);
        }

        @Override
        void make() {
            addField(new Field("id",
                    DataTypes.PrimaryKey));
            addTitle(new Field("tone",
                    DataTypes.Varchar, "Tone",
                    "Please enter a descriptive article tone", ""));
        }
    }

    private class DocTypes extends ListDetailsViewMaker {

        public DocTypes(Component comp) {
            super(comp, "DocType", "DocTypes", "doc_types",
                    "New Document Type Definition",
                    "Edit Document Type Definition", "List of Document Types",
                    MVCClassType.ADMIN);
        }

        @Override
        void make() {
            addField(new Field("id",
                    DataTypes.PrimaryKey));
            addTitle(new Field("doc_type",
                    DataTypes.Varchar, "Document type",
                    "Enter the descriptive title of this document type", ""));
            addField(new Field("cost_per_page",
                    DataTypes.Double, "Cost per page",
                    "Enter the cost that will be charged per page of this document type",
                    ""));
            addField(new Field("type_of_page",
                    DataTypes.Varchar, "Type of page",
                    "The type of page of this document type eg page slide etc. in singular",
                    ""));
            addField(new Field("plural_of_page",
                    DataTypes.Varchar, "Type of page plural",
                    "The type of page of this document type eg pages slides etc. in plural",
                    ""));
            addField(new Field("words_per_page",
                    DataTypes.Integer, "Words per page",
                    "The estimated number of words per page of this documet type",
                    ""));
            addField(new Field("page_limit",
                    DataTypes.Integer, "Page limit",
                    "The maximum number of pages that can be ordered for this document type",
                    ""));
            addField(new Field("uses_page_estimate", "Use words per page",
                    "Use estimated number of words e.g. approx 275 words", "",
                    "Yes, use page estimetes", "No, don't use page estimates"));
        }
    }

    private class EmailTemplates extends ListDetailsViewMaker {

        public EmailTemplates(Component comp) {
            super(comp, "EmailTemplate", "EmailTemplates", "email_templates",
                    "New Email Template", "Edit Email Template",
                    "List of Email Templates", MVCClassType.ADMIN);
        }

        @Override
        void make() {
            addField(new Field("id",
                    DataTypes.PrimaryKey));

            addTitle(
                    new Field("title",
                    DataTypes.Varchar, "Name", "Name of this email template", ""));

            addField(new Field("type",
                    DataTypes.List, "Type",
                    "The designation of this email template", "", new String[]{
                        "'': -- Select a template designation -- ",
                        "[inc]:Admin New Order Email",
                        "[inc]:Admin New Client Email",
                        "[inc]:Client Welcome Email",
                        "[inc]:Client Order Acknowledge",
                        "[inc]:Admin Order Status - Paid",
                        "[inc]:Client Order Paid Acknowledge",
                        "[inc]:Admin Order Status - Assigned",
                        "[inc]:Admin Oder Staus - Disputed",
                        "[inc]:Admin Order Status - Approved",
                        "[inc]:Admin Order Status - Overdue",
                        "[inc]:Admin Order Status - Completed",
                        "[inc]:Admin Order Status - Refunded",
                        "[inc]:Admin Order Staus - Canceled",
                        "[inc]:Client Order Status - Assigned",
                        "[inc]:Client Oder Staus - Disputed",
                        "[inc]:Client Order Status - Approved",
                        "[inc]:Client Order Status - Overdue",
                        "[inc]:Client Order Status - Completed",
                        "[inc]:Client Order Status - Refunded",
                        "[inc]:Client Order Staus - Canceled",
                        "[def]:''"}));
            addField(new Field("subject",
                    DataTypes.Varchar, "Subject",
                    "Enter the subject of the Email Template", ""));

            addField(new Field("body",
                    DataTypes.Editor, "Body",
                    "Enter the body of the Email Template", ""));

            addField(new Field("assigned",
                    "Assigned", "Set whether this email template is assigned",
                    "", "Assigned", "Unassigned"));

        }
    }

    private class Ipns extends ListDetailsViewMaker {

        public Ipns(Component comp) {
            super(comp, "Ipn", "Ipns", "ipns", "View IPN Details",
                    "View IPN Details", "List of IPNs", MVCClassType.ADMIN);
        }

        @Override
        void make() {
            addField(new Field("id",
                    DataTypes.PrimaryKey));
            addField(new Field("time_of_ipn",
                    DataTypes.TimeStamp));
            addField(new Field("receiver_email",
                    DataTypes.Text));
            addField(new Field("receiver_id",
                    DataTypes.Text));
            addField(new Field("residence_country",
                    DataTypes.Text));
            addField(new Field("test_ipn",
                    DataTypes.Text));
            addField(new Field("transaction_subject",
                    DataTypes.Text));
            addField(new Field("txn_id",
                    DataTypes.Text));
            addField(new Field("txn_type",
                    DataTypes.Text));
            addField(new Field("payer_email",
                    DataTypes.Text));
            addField(new Field("payer_id",
                    DataTypes.Text));
            addField(new Field("payer_status",
                    DataTypes.Text));
            addField(new Field("first_name",
                    DataTypes.Text));
            addField(new Field("last_name",
                    DataTypes.Text));
            addField(new Field("address_city",
                    DataTypes.Text));
            addField(new Field("address_country",
                    DataTypes.Text));
            addField(new Field("address_country_code",
                    DataTypes.Text));
            addField(new Field("address_name",
                    DataTypes.Text));
            addField(new Field("address_state",
                    DataTypes.Text));
            addField(new Field("address_status",
                    DataTypes.Text));
            addField(new Field("address_street",
                    DataTypes.Text));
            addField(new Field("address_zip",
                    DataTypes.Text));
            addTitle(new Field("custom",
                    DataTypes.Text));
            addField(new Field("handling_amount",
                    DataTypes.Text));
            addField(new Field("item_name",
                    DataTypes.Text));
            addField(new Field("item_number",
                    DataTypes.Text));
            addField(new Field("mc_currency",
                    DataTypes.Text));
            addField(new Field("mc_fee",
                    DataTypes.Text));
            addField(new Field("mc_gross",
                    DataTypes.Text));
            addField(new Field("payment_date",
                    DataTypes.Text));
            addField(new Field("payment_fee",
                    DataTypes.Text));
            addField(new Field("payment_gross",
                    DataTypes.Text));
            addField(new Field("payment_status",
                    DataTypes.Text));
            addField(new Field("payment_type",
                    DataTypes.Text));
            addField(new Field("protection_eligibility",
                    DataTypes.Text));
            addField(new Field("quantity",
                    DataTypes.Text));
            addField(new Field("shipping",
                    DataTypes.Text));
            addField(new Field("tax",
                    DataTypes.Text));
            addField(new Field("notify_version",
                    DataTypes.Text));
            addField(new Field("charset",
                    DataTypes.Text));
            addField(new Field("verify_sign",
                    DataTypes.Text));
            makeAllReadOnly();
            makeAllOptional();
        }
    }

    private class ArticleOrders extends ListDetailsViewMaker {

        public ArticleOrders(Component comp) {
            super(comp, "ArticleOrder", MVCClassType.ADMIN);
        }

        @Override
        void make() {
            addField(new Field("id",
                    DataTypes.PrimaryKey));
            addField(new Field("orderid",
                    DataTypes.Sql, "Order",
                    "The order in the central order table associated with this article order",
                    "", "orders"));
            addTitle(new Field("topic",
                    DataTypes.Integer));
            addField(new Field("project_type",
                    DataTypes.Integer));
            addField(new Field("project_description",
                    DataTypes.Varchar));
            addField(new Field("project_category",
                    DataTypes.Integer));
            addField(new Field("articles_length",
                    DataTypes.Integer));
            addField(new Field("number_of_articles",
                    DataTypes.Integer));
            addField(new Field("project_tone",
                    DataTypes.Integer));
            addField(new Field("project_purpose",
                    DataTypes.Varchar));
            addField(new Field("special_instructions",
                    DataTypes.Varchar));
        }
    }

    private class ArticleTitles extends ListDetailsViewMaker {

        public ArticleTitles(Component comp) {
            super(comp, "ArticleTitle", MVCClassType.ADMIN);
        }

        @Override
        void make() {
            addField(new Field("id",
                    DataTypes.PrimaryKey));
            addTitle(new Field("title",
                    DataTypes.Varchar));
        }
    }

    private class ArticleLengths extends ListDetailsViewMaker {

        public ArticleLengths(Component comp) {
            super(comp, "ArticleLength", MVCClassType.ADMIN);
        }

        @Override
        void make() {
            addField(new Field("id",
                    DataTypes.PrimaryKey));
            addTitle(new Field("art_length",
                    DataTypes.Varchar));
            addField(new Field("cost_factor",
                    DataTypes.Integer));
            addField(new Field("selected",
                    DataTypes.Integer));
            addField(new Field("index_number",
                    DataTypes.Integer));
            addField(new Field("published",
                    DataTypes.Integer));
        }
    }

    private class ArticleCategories extends ListDetailsViewMaker {

        public ArticleCategories(Component comp) {
            super(comp, "ArticleCategory", "ArticleCategories",
                    MVCClassType.ADMIN);
        }

        @Override
        void make() {
            addField(new Field("id",
                    DataTypes.PrimaryKey));
            addTitle(new Field("art_category",
                    DataTypes.Varchar));
        }
    }

    private class AcademicOrders extends ListDetailsViewMaker {

        public AcademicOrders(Component comp) {
            super(comp, "AcademicOrder", MVCClassType.ADMIN);
        }

        @Override
        void make() {
            addField(new Field("id",
                    DataTypes.PrimaryKey));
            addField(new Field("orderid",
                    DataTypes.Integer));
            addTitle(new Field("topic",
                    DataTypes.Varchar));
            addField(new Field("typeofdocument",
                    DataTypes.Integer));
            addField(new Field("spacing",
                    DataTypes.Integer));
            addField(new Field("number_of_words",
                    DataTypes.Integer));
            addField(new Field("academic_level",
                    DataTypes.Integer));
            addField(new Field("subject_area",
                    DataTypes.Integer));
            addField(new Field("style",
                    DataTypes.Integer));
            addField(new Field("no_of_src",
                    DataTypes.Integer));
        }
    }

    private class Orders extends ListDetailsViewMaker {

        public Orders(Component comp) {
            super(comp, "MyOrder", "MyOrders", "orders", "New Order",
                    "Edit Order Details", "List of Orders", MVCClassType.ADMIN);
        }

        @Override
        void make() {
            addField(new Field("orderid",
                    DataTypes.PrimaryKey));
            addField(new Field("customer_id",
                    DataTypes.Integer));
            addTitle(new Field("order_number",
                    DataTypes.Varchar));
            addField(new Field("placing_date",
                    DataTypes.TimeStamp));
            addField(new Field("cost_before_discount",
                    DataTypes.Double));
            addField(new Field("discount_amount",
                    DataTypes.Double));
            addField(new Field("deadline",
                    DataTypes.Date));
            addField(new Field("order_status",
                    DataTypes.Varchar));
            addField(new Field("topic",
                    DataTypes.Varchar));
            addField(new Field("cost_per_page",
                    DataTypes.Double));
            addField(new Field("urgency",
                    DataTypes.Integer));
            addField(new Field("order_desc",
                    DataTypes.Varchar));
            addField(new Field("attachment",
                    DataTypes.Varchar));
            addField(new Field("discount_code",
                    DataTypes.Varchar));
            addField(new Field("pref_writer",
                    DataTypes.Integer));
            addField(new Field("assigned_writer",
                    DataTypes.Integer));
            addField(new Field("total_amount",
                    DataTypes.Integer));
            addField(new Field("flags",
                    DataTypes.Varchar));
        }
    }

    private class NumberOfWords extends ListDetailsViewMaker {

        public NumberOfWords(Component comp) {
            super(comp, "NumberOfWord", MVCClassType.ADMIN);
        }

        @Override
        void make() {
            addField(new Field("id",
                    DataTypes.PrimaryKey,
                    "Id",
                    "Id",
                    "Id"));
            addTitle(new Field("numberofwords",
                    DataTypes.Varchar));
        }
    }

    private class OrderStyles extends ListDetailsViewMaker {

        public OrderStyles(Component comp) {
            super(comp, "OrderStyle", MVCClassType.ADMIN);
        }

        @Override
        void make() {
            addField(new Field("id",
                    DataTypes.PrimaryKey,
                    "id",
                    "id",
                    "id"));
            addTitle(new Field("style",
                    DataTypes.Varchar));
        }
    }

    private class LanguageStyles extends ListDetailsViewMaker {

        public LanguageStyles(Component comp) {
            super(comp, "LanguageStyle", MVCClassType.ADMIN);
        }

        @Override
        void make() {
            addField(new Field("id",
                    DataTypes.PrimaryKey,
                    "id",
                    "id",
                    "id"));
            addTitle(new Field("language_style",
                    DataTypes.Varchar));
        }
    }

    private class SentEmails extends ListDetailsViewMaker {

        public SentEmails(Component comp) {
            super(comp, "SentEmail", MVCClassType.ADMIN);
        }

        @Override
        void make() {
            addField(new Field("id",
                    DataTypes.PrimaryKey,
                    "id",
                    "id",
                    "id"));

            addField(new Field("user_id",
                    DataTypes.Varchar));

            addField(new Field("sent_date",
                    DataTypes.TimeStamp));

            addTitle(new Field("subject",
                    DataTypes.Varchar));

            addField(new Field("body",
                    DataTypes.Text));
        }
    }

    private class Spacings extends ListDetailsViewMaker {

        public Spacings(Component comp) {
            super(comp, "Spacing", MVCClassType.ADMIN);
        }

        @Override
        void make() {
            addField(new Field("id",
                    DataTypes.PrimaryKey,
                    "id",
                    "id",
                    "id"));
            addField(new Field("cost_factor",
                    DataTypes.Double));
            addField(new Field("pages_factor",
                    DataTypes.Double));
            addTitle(new Field("spacing",
                    DataTypes.Varchar));

        }
    }

    private class SubjectAreas extends ListDetailsViewMaker {

        public SubjectAreas(Component comp) {
            super(comp, "SubjectArea", "SubjectAreas", "subject_areas",
                    "New Subject Area", "Edit Subject Area Details",
                    "List of Subject Areas", MVCClassType.ADMIN);
        }

        @Override
        void make() {
            addField(new Field("id",
                    DataTypes.PrimaryKey,
                    "id",
                    "id",
                    "id"));
            addTitle(new Field("subject_name",
                    DataTypes.Varchar));
            addField(new Field("level",
                    DataTypes.Integer));
        }
    }

    private class TermsOfServices extends ListDetailsViewMaker {

        public TermsOfServices(Component comp) {
            super(comp, "TermsOfService", MVCClassType.ADMIN);
        }

        @Override
        void make() {
            addField(new Field("id",
                    DataTypes.PrimaryKey,
                    "id",
                    "id",
                    "id"));
            addTitle(new Field("title",
                    DataTypes.Varchar));
            addField(new Field("content",
                    DataTypes.Text));
            addField(new Field("assigned",
                    DataTypes.Integer));
        }
    }

    private class Urgencies extends ListDetailsViewMaker {

        public Urgencies(Component comp) {
            super(comp, "Urgency", "Urgencies", "urgencies",
                    "New Urgency Definition",
                    "Edit Urgency Definition", "List of Urgency Definitions",
                    MVCClassType.ADMIN);
        }

        @Override
        void make() {
            addField(new Field("id",
                    DataTypes.PrimaryKey,
                    "id",
                    "id",
                    "id"));
            addField(new Field("page_limit",
                    DataTypes.Integer));
            addField(new Field("cost_factor",
                    DataTypes.Double));
            addTitle(new Field("urgency",
                    DataTypes.Varchar));
        }
    }

    private class Writers extends ListDetailsViewMaker {

        public Writers(Component comp) {
            super(comp, "Writer", "Writers", "writers", "New Writer Editor",
                    "Edit Writer Details", "List of Writers", MVCClassType.ADMIN);
        }

        @Override
        void make() {
            addField(new Field("id",
                    DataTypes.PrimaryKey,
                    "id",
                    "id",
                    "id"));
            addField(new Field("writer_name",
                    DataTypes.Varchar, "Writer Name",
                    "Enter the name of the writer", "Please enter your name"));
            addTitle(
                    new Field("user_name",
                    DataTypes.Varchar, "User Name",
                    "The user name of the writer",
                    "Enter the name you wolud like other users to refer you by."));
            addField(new Field("email",
                    DataTypes.Varchar, "Email Address",
                    "Enter the email address of the writer",
                    "Enter your email address"));
        }
    }

    private class ClientMessages extends ListDetailsViewMaker {

        public ClientMessages(Component comp) {
            super(comp, "ClientMessage", "ClientMessages", "client_messages",
                    "New Message", "Message Editor", "All Messages",
                    MVCClassType.SITE);
        }

        @Override
        void make() {
            addField(new Field("id",
                    DataTypes.PrimaryKey));
            addTitle(new Field("topic",
                    DataTypes.Varchar, "Subject", "Subject of this message",
                    "Enter a subject"));
            addField(new Field("body",
                    DataTypes.Editor, "Body", "Body of this message",
                    "Enter a boby for your message"));
            addField(new Field("sender",
                    DataTypes.Integer, "Sender", "The sender of this message",
                    "The sender of this message", false, true, ""));
            Field f = new Field("receipient",
                    DataTypes.Integer, "Receipient",
                    "The receipient of this message",
                    "");
            f.setReadonly(true);
            f.setDisplayedSite(false);
            addField(f);
        }
    }

    private class ClientOrders extends ListDetailsViewMaker {

        public ClientOrders(Component comp) {
            super(comp, "ClientOrder", "ClientOrders", "client_orders",
                    "New Order", "Edit Order Details", "List of Orders",
                    MVCClassType.SITE);
        }

        @Override
        void make() {
            addField(new Field("orderid",
                    DataTypes.PrimaryKey));
            addField(new Field("topic",
                    DataTypes.Varchar, "Topic", "The topic of this order",
                    "Please specify the topic of this order"));
            addField(new Field("order_desc",
                    DataTypes.Editor, "Order Description", "Order description",
                    "Please enter a description for your order"));
            Field f = new Field("customer_id",
                    DataTypes.Integer, "Customer ID",
                    "The Customer associated with this order", "");
            f.setDisplayedSite(false);
            f.setReadonly(true);
            addField(f);
            addTitle(new Field("order_number",
                    DataTypes.Varchar, "Order Number",
                    "The order number of this order",
                    "The order number of this order", false, true, null));
            addField(new Field("placing_date",
                    DataTypes.TimeStamp, "Placing Date", "Placing Date",
                    "Placing Date"));
            addField(new Field("cost_before_discount",
                    DataTypes.Double, "Cost Before Discount",
                    "Cost before discount of this order",
                    "Cost before discount if discount is applicable.", false,
                    true, null));
            addField(new Field("discount_amount",
                    DataTypes.Double, "Discount Amount",
                    "Discount amount awarded",
                    "The discount amount deducted from your order", false, true,
                    null));
            addField(new Field("deadline",
                    DataTypes.Date, "Deadline",
                    "The urgency the client put on this order",
                    "Please specify the deadline of this order"));
            f = new Field("order_status",
                    DataTypes.Varchar, "Order Status",
                    "The status of this order", "");
            f.setDisplayedSite(false);
            addField(f);
            addField(new Field("cost_per_page",
                    DataTypes.Double, "Cost per page",
                    "The cost per page of this order based on settings",
                    "The cost you will pay per page", false, true, null));
            addField(new Field("attachment",
                    DataTypes.File, "Attachment", "Attachments uploaded",
                    "Upload an attachment if any", false, false, null));
            addField(new Field("discount_code",
                    DataTypes.Varchar, "Discount Code", "Discaount code",
                    "Please enter your discount code here if applicable", false,
                    false, null));
            f = new Field("pref_writer",
                    DataTypes.Sql, "Prefered writer", "Prefered writer",
                    "You may select from a list of our writers if you have a particular preference",
                    "writers", new String[]{
                        "'':''",
                        "[def]:''"
                    });
            f.setRequired(false);
            addField(f);
            f = new Field("assigned_writer",
                    DataTypes.Integer, "Assigned Writer",
                    "Writer assigned to write this order", "");
            f.setDisplayedSite(false);
            addField(f);
            addField(new Field("total_amount",
                    DataTypes.Double, "Total Amount", "Total cost of this order",
                    "The total cost of this order"));
            f = new Field("flags",
                    DataTypes.List, "Flags",
                    "You may flag this order to tell the system how to deal with it.",
                    "", new String[]{
                        "'':Ok",
                        "BLOCKED:Blocked",
                        "[def]:''"
                    });
            f.setDisplayedSite(false);
            addField(f);

        }
    }
}
