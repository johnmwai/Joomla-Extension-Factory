package factory.legacy;

import com.fuscard.commons.FileWriter;
import java.util.LinkedList;

/**
 * <p/>
 * CONFIDENTIAL PROPRIETARY: FUSCARD<br/>Copyright (c) 2012 Fuscard. All rights
 * reserved.<br/> Creation time: Sep 25, 2012 -- 9:27:18 PM<br/>
 * <p/>
 * @author John Mwai
 */
public abstract class MVCClass {

    protected String name;
    protected final MVCClassType type;
    protected final Component parent;
    protected final Config configuration;
    protected String className = "";
    protected String superClass = "";
    protected String parentFile = "";
    protected String folder = "";
    protected FileWriter fw;
    protected String file;
    protected String singular = "";
    protected String plural = "";
    protected String root = "";
    private LinkedList<ViewElementFacilitator> facilitators = new LinkedList<ViewElementFacilitator>();
    private boolean isDetails = false;
    private boolean isList = false;

    public MVCClass(String name, MVCClassType type, Component parent, Config config) {
        this.name = name;
        this.type = type;
        this.parent = parent;
        configuration = config;
    }

    public String getName() {
        return name;
    }

    protected void finalizePreparations() {
        switch (type) {
            case ADMIN:
                root = "admin";
                break;
            case SITE:
                root = "site";
                break;
            default:
                throw new IllegalStateException();
        }
        file = root + "." + folder + "|" + name + ".php";
        String index = root + "." + folder + "|index.html";
        file = parent.makeFile(file);
        parent.makeFile(index);
        fw = parent.obtainWriter(file.toLowerCase());
    }

    public String getSingular() {
        return singular;
    }

    public String getPlural() {
        return plural;
    }

    public void setPlural(String plural) {
        this.plural = plural;
    }

    public void setSingular(String singular) {
        this.singular = singular;
    }

    public void dumpCode() {
        finalizePreparations();
        fw.pushLine("<?php");
        fw.pushLine("/**");
        parent.writeHeader(fw);
        fw.pushLine("*/");
        fw.pushLine("defined('_JEXEC') or die('Restricted access');");
        fw.pushLine("jimport( 'joomla.application.component." + parentFile + "' );");
        fw.pushLine("class " + className + " extends " + superClass + " {");
        writeBody();
        fw.pushLine("}");
    }

    public abstract void writeBody();

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSite() {
        return type == MVCClassType.SITE;
    }
    public boolean isAdmin() {
        return type == MVCClassType.ADMIN;
    }

    public void injectFacilitator(ViewElementFacilitator facilitator) {
    }

    public boolean isDetails() {
        return isDetails;
    }

    public boolean isList() {
        return isList;
    }

    public void makeDetails() {
        if (isList) {
            throw new IllegalStateException("This class is a list and cannot be changed to details");
        }
        this.isDetails = true;
    }

    void makeList() {
        if (isDetails) {
            throw new IllegalStateException("This class is a details and cannot be changed to list");
        }
        this.isList = true;
    }

    public static class Config {

        protected final Type type;

        public Config(Type type) {
            this.type = type;
        }

        public enum Type {

            Plain,
            Form,
            Admin,
            List,
            IDE_Details,
            IDE_List
        }
    }
}
