package factory.legacy;

/**
 * <p/>
 * CONFIDENTIAL PROPRIETARY: FUSCARD<br/>Copyright (c) 2012 Fuscard. All rights
 * reserved.<br/> Creation time: Sep 27, 2012 -- 12:20:36 PM<br/>
 * <p/>
 * @author John Mwai
 */
public class DataSink {

    protected final Record record;

    public DataSink(Record record) {
        if (record == null) {
            throw new IllegalArgumentException("A datasink cannot have a null record");
        }
        this.record = record;
    }

    public Field[] retrieve(Query query) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void store(Field[] fields) {
    }
}
