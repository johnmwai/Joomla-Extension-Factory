package factory.legacy;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p/>
 * CONFIDENTIAL PROPRIETARY: FUSCARD<br/>Copyright (c) 2012 Fuscard. All rights
 * reserved.<br/> Creation time: Sep 29, 2012 -- 4:45:10 PM<br/>
 * <p/>
 * @author John Mwai
 */
public class Regex {

//    static String order =
//            "`orderid` int(11) NOT NULL AUTO_INCREMENT,\n"
//            + "`topic` varchar(100) DEFAULT NULL,\n"
//            + "`customer_id` int(11) NOT NULL,\n"
//            + "`order_number` varchar(1024) DEFAULT NULL,\n"
//            + "`placing_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,\n"
//            + "`cost_before_discount` double(11,2) NOT NULL DEFAULT '0.00',\n"
//            + "`discount_amount` double(11,2) NOT NULL DEFAULT '0.00',\n"
//            + "`deadline` datetime NOT NULL DEFAULT '9999-12-31 23:59:59',\n"
//            + "`order_status` varchar(1024) NOT NULL DEFAULT 'PENDING',\n"
//            + "`cost_per_page` double(11,2) NOT NULL DEFAULT '0.00',\n"
//            + "`urgency` int(11) NOT NULL,\n"
//            + "`order_desc` varchar(1000) DEFAULT NULL,\n"
//            + "`attachment` varchar(1000) DEFAULT NULL,\n"
//            + "`discount_code` varchar(100) DEFAULT NULL,\n"
//            + "`pref_writer` int(11) DEFAULT NULL,\n"
//            + "`assigned_writer` int(11) DEFAULT NULL,\n"
//            + "`total_amount` int(11) NOT NULL,\n"
//            + "`catid` int(11) NOT NULL DEFAULT '0',\n"
//            + "`params` text NOT NULL,\n"
//            + "`flags` varchar(1024) NOT NULL DEFAULT 'ORDER_OK',\n"
//            + "`ip_address` varchar(1024) DEFAULT NULL,\n";
//    static String article =
//            "`id` int(11) NOT NULL auto_increment,\n"
//            + "`project_type` int(11) NOT NULL,\n"
//            + "`project_description` varchar(10000) NOT NULL default '',\n"
//            + "`project_category` int(11) NOT NULL,\n"
//            + "`articles_length` int(11) NOT NULL,\n"
//            + "`number_of_articles` int(11) NOT NULL default '1',\n"
//            + "`project_tone` int(11) NOT NULL,\n"
//            + "`project_purpose` varchar(1000) NOT NULL,\n"
//            + "`special_instructions` varchar(1000) NOT NULL default '',\n";
//    static String academic =
//            "`id` int(11) NOT NULL AUTO_INCREMENT,\n"
//            + "`orderid` int(11) NOT NULL, -- foreign key"
//            + "`typeofdocument` int(11) NOT NULL,\n"
//            + "`spacing` int(11) NOT NULL,\n"
//            + "`number_of_words` int(11) NOT NULL,\n"
//            + "`academic_level` int(11) NOT NULL,\n"
//            + "`subject_area` int(11) NOT NULL,\n"
//            + "`style` int(11) NOT NULL,\n"
//            + "`no_of_src` int(11) NOT NULL,\n"
//            + "`pref_lang_stl` int(11) NOT NULL,\n";
    static String dataTable =
            "`id` int(11) NOT NULL auto_increment COMMENT 'my primary key',\n"
            + "`tone` varchar(1000) NOT NULL default '',\n"
            + "`catid` int(11) default NULL,\n"
            + "`params` text,\n".toUpperCase();
    static private String sarea = "( 1, 'Literature and Language' ),\n"
            + "( 2, 'English Literature' ),\n"
            + "( 2, 'American Literature' ),\n"
            + "( 2, 'Antique Literature' ),\n"
            + "( 2, 'Asian Literature' ),\n"
            + "( 2, 'Linguistics' ),\n"
            + "( 2, 'Shakespeare' ),\n"
            + "( 2, 'English' ),\n"
            + "( 1, 'Art' ),\n"
            + "( 2, 'Paintings' ),\n"
            + "( 2, 'Architecture' ),\n"
            + "( 2, 'Drama' ),\n"
            + "( 2, 'Theatre' ),\n"
            + "( 2, 'Dance' ),\n"
            + "( 2, 'Movies' ),\n"
            + "( 2, 'Design Analysis' ),\n"
            + "( 2, 'Music Studies' ),\n"
            + "( 2, 'Advanced Art' ),\n"
            + "( 1, 'Social Sciences' ),\n"
            + "( 2, 'Psychology' ),\n"
            + "( 2, 'Sociology' ),\n"
            + "( 2, 'Social Issues' ),\n"
            + "( 2, 'Ethics' ),\n"
            + "( 2, 'Ethnic and Area Studies' ),\n"
            + "( 2, 'Gender' ),\n"
            + "( 2, 'Human Sexuality' ),\n"
            + "( 2, 'Philosophy' ),\n"
            + "( 2, 'Political Science' ),\n"
            + "( 2, 'Government' ),\n"
            + "( 2, 'Public Administration' ),\n"
            + "( 2, 'Methodology' ),\n"
            + "( 2, 'International Relations' ),\n"
            + "( 2, 'Globalization' ),\n"
            + "( 2, 'Labor Studies' ),\n"
            + "( 1, 'History' ),\n"
            + "( 2, 'American History' ),\n"
            + "( 2, 'African-American Studies' ),\n"
            + "( 2, 'Native-American Studies' ),\n"
            + "( 2, 'Latin-American Studies' ),\n"
            + "( 2, 'Canadian Studies' ),\n"
            + "( 2, 'Asian Studies' ),\n"
            + "( 2, 'West European Studies' ),\n"
            + "( 2, 'East European Studies' ),\n"
            + "( 2, 'Holocaust Studies' ),\n"
            + "( 2, 'Women Studies' ),\n"
            + "( 1, 'Law' ),\n"
            + "( 2, 'Legal Issues' ),\n"
            + "( 2, 'Criminology' ),\n"
            + "( 1, 'Mathematics and Economics' ),\n"
            + "( 2, 'Mathematics' ),\n"
            + "( 2, 'Business' ),\n"
            + "( 2, 'Economics' ),\n"
            + "( 2, 'Management' ),\n"
            + "( 2, 'HR Management' ),\n"
            + "( 2, 'Marketing' ),\n"
            + "( 2, 'Investment' ),\n"
            + "( 2, 'Company Analysis' ),\n"
            + "( 2, 'Finance' ),\n"
            + "( 2, 'Accounting' ),\n"
            + "( 2, 'Case Study' ),\n"
            + "( 2, 'E-Commerce' ),\n"
            + "( 2, 'Logistics' ),\n"
            + "( 2, 'Trade' ),\n"
            + "( 1, 'Technology' ),\n"
            + "( 2, 'Science' ),\n"
            + "( 2, 'Engineering' ),\n"
            + "( 2, 'Mechanical Engineering' ),\n"
            + "( 2, 'Civil Engineering' ),\n"
            + "( 2, 'Aviation' ),\n"
            + "( 2, 'Aeronautics' ),\n"
            + "( 2, 'Computer Science' ),\n"
            + "( 2, 'Internet' ),\n"
            + "( 2, 'Programming' ),\n"
            + "( 2, 'IT Management' ),\n"
            + "( 2, 'Modern Technologies' ),\n"
            + "( 2, 'Web Design' ),\n"
            + "( 1, 'Nature' ),\n"
            + "( 2, 'Geography' ),\n"
            + "( 2, 'Geology and Geophysics' ),\n"
            + "( 2, 'Archeology' ),\n"
            + "( 2, 'Ecology' ),\n"
            + "( 2, 'Environmental Issues' ),\n"
            + "( 2, 'Description/Analysis of Place/Territory' ),\n"
            + "( 2, 'Agricultural Studies' ),\n"
            + "( 2, 'Astronomy' ),\n"
            + "( 1, 'Education' ),\n"
            + "( 2, 'Pedagogy' ),\n"
            + "( 2, 'Education Theories' ),\n"
            + "( 2, 'Teacher''s Career' ),\n"
            + "( 2, 'Child' ),\n"
            + "( 2, 'Youth Issues' ),\n"
            + "( 2, 'Application Essay' ),\n"
            + "( 1, 'Health and Medicine' ),\n"
            + "( 2, 'Nutrition' ),\n"
            + "( 2, 'Sport' ),\n"
            + "( 2, 'Healthcare' ),\n"
            + "( 2, 'Aging' ),\n"
            + "( 2, 'Alternative Medicine' ),\n"
            + "( 2, 'Pharmacology' ),\n"
            + "( 2, 'Nursing' ),\n"
            + "( 1, 'Communications and Media' ),\n"
            + "( 2, 'Journalism' ),\n"
            + "( 2, 'Public Relations' ),\n"
            + "( 2, 'Advertising' ),\n"
            + "( 2, 'Information Campaign' ),\n"
            + "( 2, 'Communication Strategies' ),\n"
            + "( 1, 'Religion and Theology' ),\n"
            + "( 1, 'Life Sciences' ),\n"
            + "( 2, 'Physics' ),\n"
            + "( 2, 'Chemistry' ),\n"
            + "( 2, 'Biology' ),\n"
            + "( 2, 'Anthropology' ),\n"
            + "( 1, 'Tourism' ),\n"
            + "( 1, 'Creative writing' ),\n"
            + "( 2, 'Personal Experience Essay' ),\n"
            + "( 2, 'Personal Interpretation of fiction story/essay' ),\n";
    static private String countries = "('Afghanistan ', '93'),\n"
            + "('Albania ', '355'),\n"
            + "('Algeria ', '213'),\n"
            + "('American Samoa ', '1 684'),\n"
            + "('Andorra ', '376'),\n"
            + "('Angola ', '244'),\n"
            + "('Anguilla ', '1 264'),\n"
            + "('Antarctica (Australian bases) ', '6721'),\n"
            + "('Antigua and Barbuda ', '1 268'),\n"
            + "('Argentina ', '54'),\n"
            + "('Armenia ', '374'),\n"
            + "('Aruba ', '297'),\n"
            + "('Ascension ', '247'),\n"
            + "('Australia ', '61'),\n"
            + "('Austria ', '43'),\n"
            + "('Azerbaijan ', '994'),\n"
            + "('Bahamas ', '1 242'),\n"
            + "('Bahrain ', '973'),\n"
            + "('Bangladesh ', '880'),\n"
            + "('Barbados ', '1 246'),\n"
            + "('Belarus ', '375'),\n"
            + "('Belgium ', '32'),\n"
            + "('Belize ', '501'),\n"
            + "('Benin ', '229'),\n"
            + "('Bermuda ', '1 441'),\n"
            + "('Bhutan ', '975'),\n"
            + "('Bolivia ', '591'),\n"
            + "('Bosnia and Herzegovina ', '387'),\n"
            + "('Botswana ', '267'),\n"
            + "('Brazil ', '55'),\n"
            + "('British Indian Ocean Territory ', '246'),\n"
            + "('British Virgin Islands ', '1 284'),\n"
            + "('Brunei ', '673'),\n"
            + "('Bulgaria ', '359'),\n"
            + "('Burkina Faso ', '226'),\n"
            + "('Burundi ', '257'),\n"
            + "('Cambodia ', '855'),\n"
            + "('Cameroon ', '237'),\n"
            + "('Canada ', '1'),\n"
            + "('Cape Verde ', '238'),\n"
            + "('Cayman Islands ', '1 345'),\n"
            + "('Central African Republic ', '236'),\n"
            + "('Chad ', '235'),\n"
            + "('Chile ', '56'),\n"
            + "('China ', '86'),\n"
            + "('Colombia ', '57'),\n"
            + "('Comoros ', '269'),\n"
            + "('Congo, Democratic Republic of the ', '243'),\n"
            + "('Congo, Republic of the ', '242'),\n"
            + "('Cook Islands ', '682'),\n"
            + "('Costa Rica ', '506'),\n"
            + "('Cote d''Ivoire ', '225'),\n"
            + "('Croatia ', '385'),\n"
            + "('Cuba ', '53'),\n"
            + "('CuraÃ§ao ', '599'),\n"
            + "('Cyprus ', '357'),\n"
            + "('Czech Republic ', '420'),\n"
            + "('Denmark ', '45'),\n"
            + "('Djibouti ', '253'),\n"
            + "('Dominica ', '1 767'),\n"
            + "('Dominican Republic ', '1 809, 1 829, 1 849'),\n"
            + "('East Timor ', '670'),\n"
            + "('Ecuador ', '593'),\n"
            + "('Egypt ', '20'),\n"
            + "('El Salvador ', '503'),\n"
            + "('Equatorial Guinea ', '240'),\n"
            + "('Eritrea ', '291'),\n"
            + "('Estonia ', '372'),\n"
            + "('Ethiopia ', '251'),\n"
            + "('Falkland Islands ', '500'),\n"
            + "('Faroe Islands ', '298'),\n"
            + "('Fiji ', '679'),\n"
            + "('Finland ', '358'),\n"
            + "('France ', '33'),\n"
            + "('French Guiana ', '594'),\n"
            + "('French Polynesia ', '689'),\n"
            + "('Gabon ', '241'),\n"
            + "('Gambia ', '220'),\n"
            + "('Gaza Strip ', '970'),\n"
            + "('Georgia ', '995'),\n"
            + "('Germany ', '49'),\n"
            + "('Ghana ', '233'),\n"
            + "('Gibraltar ', '350'),\n"
            + "('Greece ', '30'),\n"
            + "('Greenland ', '299'),\n"
            + "('Grenada ', '1 473'),\n"
            + "('Guadeloupe ', '590'),\n"
            + "('Guam ', '1 671'),\n"
            + "('Guatemala ', '502'),\n"
            + "('Guinea ', '224'),\n"
            + "('Guinea-Bissau ', '245'),\n"
            + "('Guyana ', '592'),\n"
            + "('Haiti ', '509'),\n"
            + "('Honduras ', '504'),\n"
            + "('Hong Kong ', '852'),\n"
            + "('Hungary ', '36'),\n"
            + "('Iceland ', '354'),\n"
            + "('India ', '91'),\n"
            + "('Indonesia ', '62'),\n"
            + "('Iraq ', '964'),\n"
            + "('Iran ', '98'),\n"
            + "('Ireland (Eire) ', '353'),\n"
            + "('Israel ', '972'),\n"
            + "('Italy ', '39'),\n"
            + "('Jamaica ', '1 876'),\n"
            + "('Japan ', '81'),\n"
            + "('Jordan ', '962'),\n"
            + "('Kazakhstan ', '7'),\n"
            + "('Kenya ', '254'),\n"
            + "('Kiribati ', '686'),\n"
            + "('Kuwait ', '965'),\n"
            + "('Kyrgyzstan ', '996'),\n"
            + "('Laos ', '856'),\n"
            + "('Latvia ', '371'),\n"
            + "('Lebanon ', '961'),\n"
            + "('Lesotho ', '266'),\n"
            + "('Liberia ', '231'),\n"
            + "('Libya ', '218'),\n"
            + "('Liechtenstein ', '423'),\n"
            + "('Lithuania ', '370'),\n"
            + "('Luxembourg ', '352'),\n"
            + "('Macau ', '853'),\n"
            + "('Macedonia, Republic of ', '389'),\n"
            + "('Madagascar ', '261'),\n"
            + "('Malawi ', '265'),\n"
            + "('Malaysia ', '60'),\n"
            + "('Maldives ', '960'),\n"
            + "('Mali ', '223'),\n"
            + "('Malta ', '356'),\n"
            + "('Marshall Islands ', '692'),\n"
            + "('Martinique ', '596'),\n"
            + "('Mauritania ', '222'),\n"
            + "('Mauritius ', '230'),\n"
            + "('Mayotte ', '262'),\n"
            + "('Mexico ', '52'),\n"
            + "('Micronesia, Federated States of ', '691'),\n"
            + "('Moldova ', '373'),\n"
            + "('Monaco ', '377'),\n"
            + "('Mongolia ', '976'),\n"
            + "('Montenegro ', '382'),\n"
            + "('Montserrat ', '1 664'),\n"
            + "('Morocco ', '212'),\n"
            + "('Mozambique ', '258'),\n"
            + "('Myanmar ', '95'),\n"
            + "('Namibia ', '264'),\n"
            + "('Nauru ', '674'),\n"
            + "('Netherlands ', '31'),\n"
            + "('Netherlands Antilles ', '599'),\n"
            + "('Nepal ', '977'),\n"
            + "('New Caledonia ', '687'),\n"
            + "('New Zealand ', '64'),\n"
            + "('Nicaragua ', '505'),\n"
            + "('Niger ', '227'),\n"
            + "('Nigeria ', '234'),\n"
            + "('Niue ', '683'),\n"
            + "('Norfolk Island ', '6723'),\n"
            + "('North Korea ', '850'),\n"
            + "('Northern Ireland ', '44 28'),\n"
            + "('Northern Mariana Islands ', '1 670'),\n"
            + "('Norway ', '47'),\n"
            + "('Oman ', '968'),\n"
            + "('Pakistan ', '92'),\n"
            + "('Palau ', '680'),\n"
            + "('Panama ', '507'),\n"
            + "('Papua New Guinea ', '675'),\n"
            + "('Paraguay ', '595'),\n"
            + "('Peru ', '51'),\n"
            + "('Philippines ', '63'),\n"
            + "('Poland ', '48'),\n"
            + "('Portugal ', '351'),\n"
            + "('Puerto Rico ', '1 787, 1 939'),\n"
            + "('Qatar ', '974'),\n"
            + "('Reunion ', '262'),\n"
            + "('Romania ', '40'),\n"
            + "('Russia ', '7'),\n"
            + "('Rwanda ', '250'),\n"
            + "('Saint-BarthÃ©lemy ', '590'),\n"
            + "('Saint Helena ', '290'),\n"
            + "('Saint Kitts and Nevis ', '1 869'),\n"
            + "('Saint Lucia ', '1 758'),\n"
            + "('Saint Martin (French side) ', '590'),\n"
            + "('Saint Pierre and Miquelon ', '508'),\n"
            + "('Saint Vincent and the Grenadines ', '1 784'),\n"
            + "('Samoa ', '685'),\n"
            + "('Sao Tome and Principe ', '239'),\n"
            + "('Saudi Arabia ', '966'),\n"
            + "('Senegal ', '221'),\n"
            + "('Serbia ', '381'),\n"
            + "('Seychelles ', '248'),\n"
            + "('Sierra Leone ', '232'),\n"
            + "('Sint Maarten (Dutch side) ', '1 721'),\n"
            + "('Singapore ', '65'),\n"
            + "('Slovakia ', '421'),\n"
            + "('Slovenia ', '386'),\n"
            + "('Solomon Islands ', '677'),\n"
            + "('Somalia ', '252'),\n"
            + "('South Africa ', '27'),\n"
            + "('South Korea ', '82'),\n"
            + "('South Sudan ', '211'),\n"
            + "('Spain ', '34'),\n"
            + "('Sri Lanka ', '94'),\n"
            + "('Sudan ', '249'),\n"
            + "('Suriname ', '597'),\n"
            + "('Swaziland ', '268'),\n"
            + "('Sweden ', '46'),\n"
            + "('Switzerland ', '41'),\n"
            + "('Syria ', '963'),\n"
            + "('Taiwan ', '886'),\n"
            + "('Tajikistan ', '992'),\n"
            + "('Tanzania ', '255'),\n"
            + "('Thailand ', '66'),\n"
            + "('Togo ', '228'),\n"
            + "('Tokelau ', '690'),\n"
            + "('Tonga ', '676'),\n"
            + "('Trinidad and Tobago ', '1 868'),\n"
            + "('Tunisia ', '216'),\n"
            + "('Turkey ', '90'),\n"
            + "('Turkmenistan ', '993'),\n"
            + "('Turks and Caicos Islands ', '1 649'),\n"
            + "('Tuvalu ', '688'),\n"
            + "('Uganda ', '256'),\n"
            + "('Ukraine ', '380'),\n"
            + "('United Arab Emirates ', '971'),\n"
            + "('United Kingdom ', '44'),\n"
            + "('United States of America ', '1'),\n"
            + "('Uruguay ', '598'),\n"
            + "('Uzbekistan ', '998'),\n"
            + "('Vanuatu ', '678'),\n"
            + "('Venezuela ', '58'),\n"
            + "('Vietnam ', '84'),\n"
            + "('U.S. Virgin Islands ', '1 340'),\n"
            + "('Wallis and Futuna ', '681'),\n"
            + "('West Bank ', '970'),\n"
            + "('Yemen ', '967'),\n"
            + "('Zambia ', '260');";
    static private String doctypes = "('Essay', 10, 'page' , 275  , 100  ),\n"
            + "('Term Paper', 10 , 'page'  , 275  , 100  ),\n"
            + "('Research Paper', 10 , 'page'  , 275  , 100  ),\n"
            + "('Coursework', 10 , 'page'  , 275  , 100  ),\n"
            + "('Book Report', 10 , 'page'  , 275  , 100  ),\n"
            + "('Book Review', 10 , 'page'  , 275  , 100  ),\n"
            + "('Movie Review',10 , 'page'  , 275  , 100  ),\n"
            + "('Thesis', 10 , 'page'  , 275  , 100  ),\n"
            + "('Thesis Proposal', 10 , 'page'  , 275  , 100  ),\n"
            + "('Research Proposal', 10 , 'page'  , 275  , 100  ),\n"
            + "('Dissertation', 10 , 'page'  , 275  , 100  ),\n"
            + "('Dissertation chapter - Abstract', 15 , 'page'  , 275  , 100  ),\n"
            + "('Dissertation chapter - Introduction', 15 , 'page'  , 275  , 100  ),\n"
            + "('Dissertation chapter - Literature Review', 15 , 'page'  , 275  , 100  ),\n"
            + "('Dissertation chapter - Methodology', 15 , 'page'  , 275  , 100  ),\n"
            + "('Dissertation chapter - Results', 15 , 'page'  , 275  , 100  ),\n"
            + "('Dissertation chapter - Discussion', 15 , 'page'  , 275  , 100  ),\n"
            + "('Dissertation Service - Editing', 15 , 'page'  , 275  , 100  ),\n"
            + "('Dissertaton Services - Proofreading', 15 , 'page'  , 275  , 100  ),\n"
            + "('Formatting', 10 , 'page'  , 275  , 100  ),\n"
            + "('Lab Report', 15 , 'page'  , 275  , 100  ),\n"
            + "('Math Problem', 18 , 'page'  , 275  , 100  ),\n"
            + "('Reaction Paper', 10 , 'page'  , 275  , 100  ),\n"
            + "('PowerPoint', 5 , 'slide'  , 275  , 100  ),\n"
            + "('Annotated Bibliography', 10 , 'page'  , 275  , 100  ),\n"
            + "('Research Proposal', 10 , 'page'  , 275 , 100 );";
    private static String urgencies = "('1 Month', 10, 1 ),\n"
            + "('3 Weeks', 10,  2 ),\n"
            + "('2 Weeks', 10,  3 ),\n"
            + "('10 Days', 10,  4 ),\n"
            + "('7 Days', 10,  5 ),\n"
            + "('5 Days', 10,  6 ),\n"
            + "('4 Days', 10,  7 ),\n"
            + "('3 Days', 10,  8 ),\n"
            + "('48 hours', 10,  9 ),\n"
            + "('24 hours', 10,  10 ),\n"
            + "('12 hours', 10,  11 ),\n"
            + "('6 hours', 10,  12 );";
    static Pattern p = Pattern.compile("`(.*)`.*\n");
    static Pattern d = Pattern.compile(
            "\\(\\s*"
            + "'([\\w\\d '\\-]+)'\\s*,"
            + "\\s*(\\d+)\\s*,"
            // + "\\s*'([\\w\\d '\\-]+)'\\s*,"
            + "\\s*(\\d+)\\s*,"
            // + "\\s*(\\d+)\\s*"
            + "\\)\\s*[,;]?\n?");

    private static void parseData() {
        Matcher m = d.matcher(urgencies);
        System.out.println("TABLE_DATA_INIT urgencies");
        while (m.find()) {
            int l = m.groupCount();
            System.out.println("urgency;" + m.group(1)
                    + "\t page_limit;" + m.group(2)
                    + "\t cost_factor;" + m.group(3)
//                    + "\t words_per_page;" + m.group(4)
//                    + "\t page_limit;" + m.group(5)
                    );
        }
        System.out.println("TABLE_DATA_TERMINATE");
    }
    static Pattern p2 = Pattern.compile(
            "`(\\w*)`"//NAME
            + "\\s*(\\w*)"//TYPE
            + "\\s*(\\(\\d+\\))?"//SIZE
            + "(?:\\s*(NOT\\s+NULL\\s+(DEFAULT\\s+([\\w'\\-]*+)|(AUTO_INCREMENT))|(DEFAULT\\s+NULL)))?.*\n");

    public static void main(String args[]) {
//       doSomeStuff();
//        parseSql();
        parseData();
    }
    private static final int FIELD_NAME = 1;
    private static final int FIELD_TYPE = 2;
    private static final int DEFAULT = 4;
    private static final int AUTO_INCREMENT = 7;
    private static final int DEFAULT_NULL = 8;

    private static void parseSql() {
        Matcher matnt = p2.matcher(dataTable);
        while (matnt.find()) {
            int l = matnt.groupCount();
            for (int i = 0; i <= l; i++) {
                String gr = matnt.group(i);
                if (gr != null) {
                    if (i == FIELD_NAME) {
                        name = gr.toLowerCase();
                    }
                    if (i == FIELD_TYPE) {
                        if (gr != null) {
                            if (l >= AUTO_INCREMENT && matnt.group(
                                    AUTO_INCREMENT) != null) {
                                gr = "PRIMARY_KEY";
                            }
                        }
                        initiateField(gr);
                    }
                    if (i == DEFAULT) {
                        if (gr != null) {
                            if (l >= DEFAULT_NULL && matnt.group(DEFAULT_NULL) != null) {
                                terminateField(true);
                                break;
                            }
                        }
                    }
                }

            }
            if (fieldInitiated) {
                terminateField(false);
            }
        }
        System.out.println();
    }
    private static Field field = null;
    private static String name = null;
    private static boolean fieldInitiated = false;

    static private void terminateField(boolean notNull) {
        field.setRequired(!notNull);
        outPutData();
        fieldInitiated = false;
    }

    static private void outPutData() {
        /*
         * addField(new Field("id",//Name to use in programs
         * DataTypes.PrimaryKey,//Data type "Id",//Display name
         * "Id",//Description in admin "Id")
         */
        if ("params".equals(field.getName()) | "catid".equals(field.getName())) {
            //These fields are not added
        } else if (field.getType() == DataTypes.PrimaryKey) {
            w_("addField(new Field(\"" + field.getName() + "\",//Name to use in programs");
            w_("DataTypes.PrimaryKey,//Data type");
            w_("\"" + field.getName() + "\",//Display name");
            w_("\"" + field.getName() + "\",//Description in admin");
            w_("\"" + field.getName() + "\"));//Description in site");

        } else {
            w_("addField(new Field(\"" + field.getName() + "\",//Name to use in programs");
            w_("DataTypes." + field.getType().name() + "//Data type");
            w_("));");
        }
    }

    static private void w_(String s) {
        System.out.println(s);
    }

    static void initiateField(String type) {
        if (fieldInitiated) {
            throw new IllegalStateException(
                    "The current field is not terminated");
        }
        fieldInitiated = true;
        type = type.toUpperCase();
        DataTypes t;
        if ("VARCHAR".equals(type)) {
            t = DataTypes.Varchar;
        } else if ("INT".equals(type)) {
            t = DataTypes.Integer;
        } else if ("PRIMARY_KEY".equals(type)) {
            t = DataTypes.PrimaryKey;
        } else if ("DOUBLE".equals(type)) {
            t = DataTypes.Double;
        } else if ("TEXT".equals(type)) {
            t = DataTypes.Text;
        } else if ("TIMESTAMP".equals(type)) {
            t = DataTypes.TimeStamp;
        } else if ("DATETIME".equals(type)) {
            t = DataTypes.Date;
        } else {
            throw new IllegalStateException("Unsupported field type : " + type);
        }
        field = new Field(name, t);
    }
}
