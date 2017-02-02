package factory.legacy;

import com.fuscard.commons.FileWriter;
import java.util.HashMap;

/**
 * <p/>
 * CONFIDENTIAL PROPRIETARY: FUSCARD<br/>Copyright (c) 2012 Fuscard. All rights
 * reserved.<br/> Creation time: Oct 7, 2012 -- 4:03:17 PM<br/>
 * <p/>
 * @author John Mwai
 */
public class TableRecord {

    private HashMap<String, Boolean> fields = new HashMap<String, Boolean>();
    private Table table;
    String values = "";
    private FileWriter fw;

    public TableRecord(Table table, Record record) {
        for (Field f : record.getFields()) {
            if (f.type != DataTypes.PrimaryKey) {
                fields.put(f.name, false);
            }
        }
        this.table = table;
    }

    public void initiate(FileWriter fw) {
        this.fw = fw;
        String insert = "INSERT INTO " + table.getInstallationName() + "(";
        boolean comma = false;
        for (String s : fields.keySet()) {
            insert += (comma ? "," : "");
            comma = true;
            insert += "`" + s + "`";
        }
        insert += ") VALUES ";
        fw.pushLine(insert);
    }

    public void putRow(HashMap<String, String> h) {
        for (String s : h.keySet()) {
            if (fields.get(s) == null) {
                throw new IllegalArgumentException(
                        "This table record contains no such field\nTable: '" + table.getName() + "' Field: '" + s + "'");
            }
        }
        values += ("".equals(values) ? "\n(" : ",\n(");
        boolean comma = false;
        for (String s : fields.keySet()) {
            String r = h.get(s);
            if (r == null) {
                r = "";
            }
            values += (comma ? "," : "");
            comma = true;
            values += "'" + r + "'";
        }
        values += ")";
    }

    public void terminate() {
        values += ";";
        fw.pushLine(values);
    }
}
