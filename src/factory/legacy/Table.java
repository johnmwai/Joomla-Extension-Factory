package factory.legacy;

import com.fuscard.commons.FileWriter;
import factory.packager.VersionWrapper;

/**
 * <p/>
 * CONFIDENTIAL PROPRIETARY: FUSCARD<br/>Copyright (c) 2012 Fuscard. All rights
 * reserved.<br/> Creation time: Sep 27, 2012 -- 12:19:38 PM<br/>
 * <p/>
 * @author John Mwai
 */
public class Table extends DataSink {

    private final String tableName;
    private final Component parent;
    private boolean emitted = false;
    private String singular = "";
    private String plural = "";
    private String version = null;
    private String terminationVersion = null;
    private String installationName = null;
    private String root;

    public Table(String tableName, Record record, Component component) {
        super(record);
        if (component.hasTable(tableName)) {
            throw new IllegalArgumentException("The table name exists");
        }
        this.tableName = tableName;
        this.parent = component;
        installationName = "#__" + parent.getName().toLowerCase() + "_" + tableName.toLowerCase();
        setVersion(parent.getVersion());
    }

    public void emitCode(boolean isAdmin) {
        if (emitted) {
            return;
        }
        if (getPrimaryKey() == null) {
            throw new IllegalStateException(
                    "Table doesn't have a primary key. " + this);
        }

        emitted = true;
        String s;
        makeLanguageFile();
        String path = parent.makeFile(
                "admin.tables|" + getSingular().toLowerCase() + ".php");
        FileWriter fwt = parent.obtainWriter(path);
        fwt.pushLine("<?php");
        fwt.pushLine("/**");
        parent.writeHeader(fwt);
        fwt.pushLine("*/");
        fwt.pushLine("defined('_JEXEC') or die('Restricted access');");
        fwt.pushLine("jimport('joomla.database.table');");
        fwt.pushLine(
                "class " + FileWriter.capitalize(parent.getName()) + "Table" + getTableName() + " extends JTable");
        fwt.pushLine("{");
        fwt.pushLine("function __construct(&$db) ");
        fwt.pushLine("{");
        fwt.pushLine(
                "parent::__construct('" + getInstallationName() + "', '" + getPrimaryKey().getName() + "', $db);");
        fwt.pushLine("}");
        if (isAdmin) {
            s = "public function bind($array, $ignore = '') \n"
                    + "{\n"
                    + "if (isset($array['params']) && is_array($array['params'])) \n"
                    + "{\n"
                    + "$parameter = new JRegistry;\n"
                    + "$parameter->loadArray($array['params']);\n"
                    + "$array['params'] = (string)$parameter;\n"
                    + "}\n"
                    + "return parent::bind($array, $ignore);\n"
                    + "}\n"
                    + "public function load($pk = null, $reset = true) \n"
                    + "{\n"
                    + "if (parent::load($pk, $reset)) \n"
                    + "{\n"
                    + "$params = new JRegistry;\n"
                    + "$params->loadJSON($this->params);\n"
                    + "$this->params = $params;\n"
                    + "return true;\n"
                    + "}\n"
                    + "else\n"
                    + "{\n"
                    + "return false;\n"
                    + "}\n"
                    + "}";
            fwt.pushLine(s);
        }
        fwt.pushLine("protected function _getAssetName()");
        fwt.pushLine("{");
        fwt.pushLine("$k = $this->_tbl_key;");
        fwt.pushLine(
                "return '" + parent.getInstallationName() + "." + getSingular().toLowerCase() + ".'.(int) $this->$k;");
        fwt.pushLine("}");
        fwt.pushLine("protected function _getAssetTitle()");
        fwt.pushLine("{");
        fwt.pushLine("return $this->" + getTitleField().getName() + ";");
        fwt.pushLine("}");
        fwt.pushLine(
                "protected function _getAssetParentId($table = null, $id = null)");
        fwt.pushLine("{");
        fwt.pushLine("$asset = JTable::getInstance('Asset');");
        fwt.pushLine(
                "$asset->loadByName('" + parent.getInstallationName() + "');");
        fwt.pushLine("return $asset->id;");
        fwt.pushLine("}");
        fwt.pushLine("}");
        makeSqlUpdates();
    }

    private void makeSqlUpdates() {
        writeInstallSql();
    }

    private void writeInstallSql() {
        FileWriter fws = parent.getSqlUninstallAppendingFileWriter();
        writeUninstallSql(fws);
    }

    public void writeUninstallSql(FileWriter fws) {
        fws.pushLine("DROP TABLE IF EXISTS `" + getInstallationName() + "`;");
    }
    private boolean hasFields = false;

    private void writeFieldSql(FileWriter fws, Field f) {
        String s = f.getSqlDefinition();
        if (hasFields) {
            fws.format(",%n\t%s", s);
        } else {
            fws.format("\t%s", s);
        }
        hasFields = true;
    }

    public void writeSql(FileWriter fws, boolean isInstall) {
        fws.pushLine("DROP TABLE IF EXISTS `" + getInstallationName() + "` ;\n");
        fws.pushLine(
                "CREATE TABLE IF NOT EXISTS `" + getInstallationName() + "` (\n");

        hasFields = false;
        for (Field f : getFields()) {
            if (!isInstall) {
                int comp = VersionWrapper.comp(version, f.getVersion());
                if (comp == VersionWrapper.SAME) {
                    writeFieldSql(fws, f);
                }
                if (comp == VersionWrapper.EARLIER) {
                    throw new IllegalStateException(this.toString());
                }
            } else {
                writeFieldSql(fws, f);
            }
        }
        Field pk = getPrimaryKey();
        int comp = 10;
        if (pk != null) {
            comp = VersionWrapper.comp(version, pk.getVersion());
        }
        if (pk != null && (isInstall || comp == VersionWrapper.SAME)) {
            String pre = hasFields ? ",\n" : "\n";
            fws.pushLine(
                    pre + "\nPRIMARY KEY (`" + pk.getName() + "`)\n"
                    + ") ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ; -- dump complete for  " + getInstallationName());
        } else {
            fws.format(");%n");
        }
        fws.format("%n%n%n%n");
    }

    public void writeSql(FileWriter fws) {
        writeSql(fws, false);
    }

    public String getPlural() {
        return plural;
    }

    public String getSingular() {
        return singular;
    }

    public void setPlural(String plural) {
        this.plural = plural;
    }

    public void setSingular(String singular) {
        this.singular = singular;
    }

    public String getName() {
        return tableName;
    }

    public Field getPrimaryKey() {
        return record.getPrimaryKey();
    }

    public Field getTitleField() {
        return record.getTitleField();
    }

    public Record getRecord() {
        return record;
    }

    public Field[] getFields() {
        return record.getFields();
    }

    public String getInstallationName() {
        return installationName;
    }

    private String getTableName() {
        return FileWriter.capitalize(getSingular());
    }

    private void makeLanguageFile() {

        FileWriter fw_ini = parent.obtainWriter(parent.makeFile(
                root + ".language.en-GB|en-GB." + parent.getInstallationName() + ".ini",
                true), true);

        for (Field f : getRecord().getFields()) {
            fw_ini.pushLine(parent.getInstallationName().toUpperCase()
                    + "_DATABASE_COLUMN_" + tableName.toUpperCase() + "_NAME_"
                    + f.getName().toUpperCase()
                    + "=\"" + f.getDisplayName() + "\"");
            fw_ini.pushLine(parent.getInstallationName().toUpperCase()
                    + "_DATABASE_COLUMN_" + tableName.toUpperCase() + "_DESCS_"
                    + f.getName().toUpperCase()
                    + "=\"" + f.getDescriptionSite() + "\"");
            fw_ini.pushLine(parent.getInstallationName().toUpperCase()
                    + "_DATABASE_COLUMN_" + tableName.toUpperCase() + "_DESCA_"
                    + f.getName().toUpperCase()
                    + "=\"" + f.getDescriptionAdmin() + "\"");
        }
    }

    public final void setVersion(String version) {
        try {
            VersionWrapper vw = new VersionWrapper(version);
        } catch (Exception e) {
            try {
                VersionWrapper vw = new VersionWrapper(parent.getVersion());
                version = vw.getVersion();
            } catch (Exception e1) {
                throw new IllegalStateException("Cannot set the version");
            }
        }
        for (Field f : getFields()) {
            f.setVersion(version);
        }
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    public String getTerminationVersion() {
        return terminationVersion;
    }

    public void setTerminationVersion(String terminationVersion) {
        this.terminationVersion = terminationVersion;
    }

    public void setInstallationName(String installationName) {
        this.installationName = installationName;
    }

    @Override
    public String toString() {
        String res = "\n****************\n";
        res += "Name: " + getName() + "\n";
        res += "Installation name: " + getInstallationName() + "\n";
        res += "Initiation version: " + version + "\n";
        res += "Termination version: " + getTerminationVersion() + "\n";
        res += "Fields: \n";
        for (Field f : getRecord().getFields()) {
            res += f.toString();
        }
        res += "End table\n***********\n";
        return res;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public Field getFieldByName(String valueFieldName) {
        for (Field f : getFields()) {
            if (f.name == null ? valueFieldName == null : f.name.equals(
                    valueFieldName)) {
                return f;
            }
        }
        return null;
    }
}
