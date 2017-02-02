package factory.legacy;

import com.fuscard.commons.FileWriter;
import factory.packager.VersionWrapper;
import java.util.HashMap;
import java.util.StringTokenizer;

/**
 * <p/>
 * CONFIDENTIAL PROPRIETARY: FUSCARD<br/>Copyright (c) 2012 Fuscard. All rights
 * reserved.<br/> Creation time: Sep 27, 2012 -- 12:19:56 PM<br/>
 * <p/>
 * @author John Mwai
 */
public class Field {

    public final String name;
    public final DataTypes type;
    private String descriptionAdmin;
    private String descriptionSite;
    private String displayName;
    private boolean required;
    private boolean readonly;
    private String _default;
    private Component parent;
    private HashMap<String, String> options = new HashMap<String, String>();
    private Table table;
    private String version = null;
    private String terminationVersion = null;
    private Table references = null;
    private Field valueField = null;
    private String referencesTableName = null;
    private String valueFieldName = null;
    private boolean displayedSite = true;

    public Field(String name, DataTypes type, boolean useSql) {
        if (!useSql && type == DataTypes.Sql) {
            throw new IllegalArgumentException(
                    "The constructor you invoked doesn't expect sql type.");
        }
        if (name == null || type == null) {
            throw new IllegalArgumentException(
                    "This constructor does not accept null arguments.\nName: " + name + " Type: " + type);
        }
        this.name = name;
        this.type = type;
        descriptionAdmin = name;
        descriptionSite = name;
        displayName = name;
        required = true;
        readonly = false;
        _default = null;
    }

    public Field(String name, DataTypes type) {
        this(name, type, false);
    }

    public Field(String name, DataTypes type, String displayName, String descriptionAdmin, String descriptionSite) {
        this(name, type, displayName, descriptionAdmin, descriptionSite,
                false);
    }

    private Field(String name, DataTypes type, String displayName, String descriptionAdmin, String descriptionSite, boolean useSql) {
        this(name, type, useSql);
        this.descriptionAdmin = descriptionAdmin;
        this.descriptionSite = descriptionSite;
        this.displayName = displayName;
    }

    public Field(String name, DataTypes type, String displayName, String descriptionAdmin, String descriptionSite, String[] options) {
        this(name, type, displayName, descriptionAdmin, descriptionSite);
        if (type != DataTypes.List && type != DataTypes.Sql && type != DataTypes.Params) {
            throw new IllegalArgumentException(
                    "This constructor expects data type to be LIST or SQL or PARAMS");
        }
        addOptions(options);
    }

    public Field(String name, String displayName, String descriptionAdmin, String descriptionSite, String positive, String negative) {
        this(name, DataTypes.List, displayName, descriptionAdmin,
                descriptionSite, new String[]{
                    "0:" + negative, "1:" + positive
                });
    }

    public final void addOptions(String[] options) {
        int j = 0;
        for (String s : options) {
            StringTokenizer st = new StringTokenizer(s, ":");
            if (st.countTokens() != 2) {
                throw new IllegalArgumentException(
                        s + " is not a valid option definition");
            }
            String s1 = unboxToken(st.nextToken());
            String s2 = unboxToken(st.nextToken());

            if ("[def]".equals(s1)) {
                _default = s2;
            } else {
                if ("[inc]".equals(s1)) {
                    s1 = ++j + "";
                }
                this.options.put(s1, s2);
            }
        }
    }

    private String unboxToken(String token) {
        if (token.matches("^'\\.*'$")) {
            token = token.substring(1, token.length() - 1);
        }
        return token;
    }

    public Field(String name, DataTypes type, String displayName, String descriptionAdmin, String descriptionSite, String referencesTableName, String valueField) {
        this(name, type, displayName, descriptionAdmin, descriptionSite,
                referencesTableName);
        this.valueFieldName = valueField;
    }

    public Field(String name, DataTypes type, String displayName, String descriptionAdmin, String descriptionSite, String referencesTableName) {
        this(name, type, displayName, descriptionAdmin, descriptionSite, true);
        if (type != DataTypes.Sql) {
            throw new IllegalArgumentException(
                    "This constructor expects the type to be SQL");
        }
        this.referencesTableName = referencesTableName;
    }

    public Field(String name, DataTypes type, String displayName, String descriptionAdmin, String descriptionSite, String referencesTableName, String[] options) {
        this(name, type, displayName, descriptionAdmin, descriptionSite,
                referencesTableName);
        addOptions(options);
    }

    public Field(String name, DataTypes type, String displayName, String descriptionAdmin, String descriptionSite, boolean required, boolean readonly, String _default) {
        this(name, type, displayName, descriptionAdmin, descriptionSite);
        this.required = required;
        this.readonly = readonly;
        this._default = _default;
    }

    public String getTerminationVersion() {
        return terminationVersion;
    }

    public void setTerminationVersion(String terminationVersion) {
        this.terminationVersion = terminationVersion;
    }

    public void setOptions(HashMap<String, String> options) {
        this.options = options;
    }

    public HashMap<String, String> getOptions() {
        return options;
    }

    public void addOption(String key, String value) {
        options.put(key, value);
    }

    public String getDescriptionSite() {
        return descriptionSite;
    }

    public void setDescriptionSite(String descriptionSite) {
        this.descriptionSite = descriptionSite;
    }

    public void setDescriptionAdmin(String description) {
        this.descriptionAdmin = description;
    }

    public String getDefault() {
        return _default;
    }

    public void setDefault(String _default) {
        this._default = _default;
    }

    public void setReadonly(boolean readonly) {
        this.readonly = readonly;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getDescriptionAdmin() {
        return descriptionAdmin;
    }

    public String getName() {
        return name;
    }

    public DataTypes getType() {
        return type;
    }

    public boolean isReadonly() {
        return readonly;
    }

    public boolean isRequired() {
        return required;
    }

    public Component getParent() {
        return parent;
    }

    public void setParent(Component parent) {
        this.parent = parent;
    }

    public void setReferences(Table references) {
        this.references = references;
    }

    public Table getReferences() {
        return references != null ? references : obtainTable();
    }

    public void setValueField(Field valueField) {
        if (references == null) {
            throw new IllegalArgumentException(
                    "Please set the references table before attempting to set the valuefield\n"
                    + "If you use the constructor that accepts a table and a field name, the exception will be thrown at build time if the field doesnt exist.");
        }
        for (Field f : references.getFields()) {
            if (valueField == f) {
                this.valueField = valueField;
                return;
            }
        }
        throw new IllegalArgumentException(
                "This method expects a field that is in the references table");
    }

    private Table obtainTable() {
        references = parent.getTableByName(referencesTableName);
        if (references != null) {
            if (valueFieldName != null) {
                valueField = references.getFieldByName(valueFieldName);
            } else {
                valueField = references.getTitleField();
            }
        }
        return references;
    }

    public void writeFormField(FileWriter fw, boolean admin) {
        if (!admin && !isDisplayedSite()) {
            return;
        }
        String sname = name,
                stype = null,
                slabel = parent.getInstallationName().toUpperCase() + "_DATABASE_COLUMN_" + table.getName().toUpperCase() + "_NAME_" + name.toUpperCase(),
                sxtn = null,
                sdesc = admin ? parent.getInstallationName().toUpperCase() + "_DATABASE_COLUMN_" + table.getName().toUpperCase() + "_DESCA_" + name.toUpperCase()
                : parent.getInstallationName().toUpperCase() + "_DATABASE_COLUMN_" + table.getName().toUpperCase() + "_DESCS_" + name.toUpperCase(),
                sclass = "input",
                srequired = isRequired() ? "true" : null,
                sql = null,
                svaluefield = null;

        boolean soptions = false, canRender = true;
        switch (type) {
            case PrimaryKey:
                stype = "hidden";
                slabel = null;
                sdesc = null;
                sclass = null;
                srequired = null;
                break;
            case Text:
                stype = "textarea";
                break;
            case Date:
                stype = "calendar";
                break;
            case Double:
            case Integer:
                stype = "text";
                sclass += " validate-numeric";
                break;
            case Email:
                stype = "text";
                sclass += " validate-email";
                break;
            case File:
                stype = "file";
                break;
            case Password:
                stype = "password";
                break;
            case Varchar:
                stype = "text";
                break;
            case List:
                stype = "list";
                soptions = true;
                break;
            case Sql:
                stype = "sql";
                if (getReferences() == null) {
                    throw new IllegalStateException(
                            "Cannot make an sql form field without the references table");
                }
                svaluefield = valueField == null ? references.getTitleField().name : valueField.name;
                sql = "SELECT " + references.getPrimaryKey().name + " AS value, " + svaluefield + " FROM " + references.getInstallationName();
                soptions = true;
                break;
            case Params:
                stype = "list";
                soptions = true;
                break;
            case Category:
                sxtn = parent.getInstallationName();
                stype = "category";
                break;
            case TimeStamp:
                canRender = false;
                break;
            case Editor:
                stype = "editor";
                break;
            default:
                throw new IllegalStateException(
                        "Unsuppored field type: " + type.name());
        }
        if (canRender) {
            fw.pushLine("<field");
            if (sname != null) {
                fw.pushLine("name=\"" + sname + "\"");
            }
            if (stype != null) {
                fw.pushLine("type=\"" + stype + "\"");
            }
            if (slabel != null) {
                fw.pushLine("label=\"" + slabel + "\"");
            }
            if (readonly) {
                fw.pushLine("readonly=\"true\"");
            }
            if (sdesc != null) {
                fw.pushLine("description=\"" + sdesc + "\"");
            }
            if (sclass != null) {
                fw.pushLine("class=\"" + sclass + "\"");
            }
            if (sxtn != null) {
                fw.pushLine("extension=\"" + sxtn + "\"");
            }
            if (srequired != null) {
                fw.pushLine("required=\"" + srequired + "\"");
            }
            if (sql != null) {
                fw.pushLine("query=\"" + sql + "\"");
            }
            if (svaluefield != null) {
                fw.pushLine("value_field=\"" + svaluefield + "\"");
            }
            if (soptions) {
                fw.pushLine(">");

                for (String s : ScriptAssistance.sortStrings(
                        options.keySet(), false)) {
                    fw.pushLine(
                            "<option value=\"" + s + "\">" + options.get(s) + "</option>");
                }
                fw.pushLine("</field>");
            } else {
                fw.pushLine("/>");
            }
        }
    }

    public void writeSql(FileWriter fws) {

        fws.pushLine(getSqlDefinition());
    }

    public String getSqlDefinition() {
        String typeString = "";
        String defStr = "''";
        boolean isPrimaryKey = false;
        switch (type) {
            case Params:
            case Editor:
            case Text:
                typeString = "TEXT";
                break;
            case Date:
                typeString = "DATETIME";
                defStr = "'0000-00-00 00:00:00'";
                break;
            case Double:
                typeString = "DOUBLE(11,2)";
                defStr = "'0'";
                break;
            case Email:
            case File:
            case Password:
            case List:
            case Varchar:
                typeString = "VARCHAR(1000)";
                break;
            case PrimaryKey:
                isPrimaryKey = true;
            case Category:
            case Integer:
            case Sql:
                typeString = "INT(11)";
                defStr = "'0'";
                break;
            case TimeStamp:
                typeString = "TIMESTAMP";
                defStr = "CURRENT_TIMESTAMP";
                break;
            default:
                throw new IllegalStateException(
                        "Unsuppored field type: " + type.name());
        }
        String _null = "NOT NULL DEFAULT ";
        if (!isPrimaryKey) {
            if (_default != null) {
                defStr = _default;
            }
            if (!required) {
                _null = "DEFAULT NULL";
            } else {
                if (!"".equals(defStr)) {
                    _null += defStr;
                } else {
                    _null += "''";
                }
            }
        } else {
            _null = "NOT NULL AUTO_INCREMENT";
        }

        if (type == DataTypes.TimeStamp) {
            _null = "NOT NULL DEFAULT CURRENT_TIMESTAMP";
        }
        return "`" + name + "` " + typeString + " " + _null;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setTable(Table t) {
        try {
            VersionWrapper vw = new VersionWrapper(version);
        } catch (Exception e) {
            version = t.getVersion();
        }
        this.table = t;
    }

    public Table getTable() {
        return table;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    @Override
    public String toString() {
        String res = "";
        res += "Name: " + getName() + "\n";
        res += "Sql: " + getSqlDefinition() + "\n";
        res += "Version: " + getVersion() + "\n";
        res += "Termination Version: " + getTerminationVersion() + "\n";
        return res;
    }

    public void setDisplayedSite(boolean b) {
        this.displayedSite = b;
    }

    public boolean isDisplayedSite() {
        return displayedSite;
    }
}
