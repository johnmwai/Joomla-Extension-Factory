package factory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p/>
 * CONFIDENTIAL PROPRIETARY: FUSCARD<br/>Copyright (c) 2012 Fuscard. All rights
 * reserved.<br/> Creation time: Oct 24, 2012 -- 1:06:42 PM<br/>
 *
 * @author John Mwai
 */
public class Scrap {

    public static void main(String[] args) {
        Pattern p = Pattern.compile("\\s*\\w*\\s*=\"(?:[^\"]|\\\\\")*\"(?:\\s|\\n)*");
        String s = "<![CDATA[\n" +
"    \n" +
"    \n" +
"                    <div id=\"headline\" style=\"color: #333333; font-family: Arial, Helvetica, sans-serif; font-size: 12px; text-align: start;\">\n" +
"            <h2 style=\"margin: 0.1em 0px; color: #c88039; font-size: 1.33em;\">&nbsp;<span style=\"font-size: 1.33em;\">Welcome to BraviaCart</span></h2>\n" +
"            </div>\n" +
"            <div id=\"messageBox\" style=\"color: #333333; font-family: Arial, Helvetica, sans-serif; font-size: 12px; text-align: start;\">&nbsp;</div>\n" +
"            <div id=\"main\" style=\"line-height: 1.5; color: #333333; font-family: Arial, Helvetica, sans-serif; font-size: 12px; text-align: start;\">\n" +
"            <div class=\"layout1\" style=\"clear: both; overflow: visible;\">\n" +
"            <p>BraviaCart is Joomla 1.6,1.7,2.5 native component. It adds e-commerce<br /> ability to to worlds most popular cms. It is not designed for selling goods. It is meant for <br /> selling services such as article writing, academic writing, resumes, and business documents<br /> writing services. It currently supports two payment gateways: Paypal and Swreg. This is first of<br /> its kind in the market.</p>\n" +
"            <table style=\"width: 605px; height: 5px;\" border=\"0\">\n" +
"            <tbody>\n" +
"            <tr>\n" +
"            <td style=\"background-color: #dfb4a4;\">&nbsp;Releases</td>\n" +
"            <td style=\"background-color: #dfb4a4;\"></td>\n" +
"            </tr>\n" +
"            </tbody>\n" +
"            </table>\n" +
"            <br />\n" +
"            <p style=\"margin: 1em 0px;\"><strong>Changes in the latest edition</strong></p>\n" +
"            <p style=\"color: #333333; font-family: Arial, Helvetica, sans-serif; font-size: 12px; line-height: 18px; text-align: start; margin: 1em 0px;\"><span style=\"color: #008080;\"><strong>version 1.0.39</strong></span></p>\n" +
"            <ol style=\"color: #333333; font-family: Arial, Helvetica, sans-serif; font-size: 12px; line-height: 18px; text-align: start; list-style-type: lower-greek;\">\n" +
"            <li style=\"margin-top: 1em; margin-right: 0px; margin-bottom: 1em;\"><span style=\"line-height: 1.5;\">Minor bug fix</span></li>\n" +
"            </ol>\n" +
"            <p style=\"margin-top: 1em; margin-right: 0px; margin-bottom: 1em;\"><strong><span style=\"line-height: 1.5; color: #000000;\">Earlier changes</span></strong></p>\n" +
"            <p style=\"color: #333333; font-family: Arial, Helvetica, sans-serif; font-size: 12px; line-height: 18px; text-align: start; margin: 1em 0px;\"><span style=\"color: #008080;\"><strong>version 1.0.37</strong></span></p>\n" +
"            <ol style=\"color: #333333; font-family: Arial, Helvetica, sans-serif; font-size: 12px; line-height: 18px; text-align: start; list-style-type: lower-greek;\">\n" +
"            <li style=\"margin-top: 1em; margin-right: 0px; margin-bottom: 1em;\"><span style=\"line-height: 1.5;\">Minor bug fix</span></li>\n" +
"            </ol>\n" +
"            <p style=\"color: #333333; font-family: Arial, Helvetica, sans-serif; font-size: 12px; line-height: 18px; text-align: start; margin: 1em 0px;\"><span style=\"color: #008080;\"><strong>version 1.0.36</strong></span></p>\n" +
"            <ol style=\"list-style-type: lower-greek;\">\n" +
"            <li style=\"margin-top: 1em; margin-right: 0px; margin-bottom: 1em;\"><span style=\"line-height: 1.5;\">The ipn handler falgs orders as paid</span></li>\n" +
"            </ol>\n" +
"            <table style=\"font-family: Arial, Helvetica, sans-serif; width: 605px; height: 5px;\" border=\"0\">\n" +
"            <tbody>\n" +
"            <tr>\n" +
"            <td style=\"background-color: #c4c4cf;\"></td>\n" +
"            <td style=\"background-color: #dedee3;\">&nbsp;<span style=\"text-decoration: underline;\"><a href=\"http://www.mambocreative.com\"><span style=\"color: #808080; text-decoration: underline;\">Mambo Creative</span></a></span></td>\n" +
"            </tr>\n" +
"            </tbody>\n" +
"            </table>\n" +
"            </div>\n" +
"            </div>\n" +
"\n" +
"    ]]>";
        Matcher m = p.matcher(s);
//        m.region(366, s.length());
        while (m.find()) {
            System.out.println("Found from " + m.start() + " to " + m.end() + " : " + m.group());
//            System.out.println("Group 1: " + m.group(1));
//            System.out.println("Group 2: " + m.group(2));
//            System.out.println("Group 3: " + m.group(3));
//            System.out.println("Group 4: " + m.group(4));
        }
    }
}
