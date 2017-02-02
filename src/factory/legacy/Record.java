package factory.legacy;

import java.util.LinkedList;

/**
 * <p/>
 * CONFIDENTIAL PROPRIETARY: FUSCARD<br/>Copyright (c) 2012 Fuscard. All rights
 * reserved.<br/> Creation time: Sep 27, 2012 -- 12:26:36 PM<br/>
 * <p/>
 * @author John Mwai
 */
public class Record {

    private LinkedList<Field> fields = new LinkedList<Field>();
    private Field title = null;

    public void addField(Field field) throws Exception {
        if (field.getName() == null || "".equals(field.getName())) {
            throw new Exception("The name of the field is invalid");
        }
        if (field.getType() == DataTypes.PrimaryKey) {
            for (Field f : fields) {
                if (f.getType() == DataTypes.PrimaryKey) {
                    throw new Exception("This record already has a primary key\n");
                }
                if (f.getName() == null ? field.getName() == null : f.getName().equals(field.getName())) {
                    throw new Exception("This record already has a field by that name");
                }
            }
        }
        fields.add(field);
    }

    public boolean removeField(Field f) {
        return fields.remove(f);
    }

    public Field[] getFields() {
        return fields.toArray(new Field[0]);
    }

    public Field getPrimaryKey() {
        for (Field f : getFields()) {
            if (f.getType() == DataTypes.PrimaryKey) {
                return f;
            }
        }
        return null;
    }

    public Field getParams() {
        for (Field f : getFields()) {
            if (f.getType() == DataTypes.Params) {
                return f;
            }
        }
        return null;
    }

    public void setTitle(Field field) {
        if (!fields.contains(field)) {
            throw new IllegalStateException("This field is not contained in this record.");
        }
        title = field;
    }

    public Field getTitleField() {
        return title;
    }
}
