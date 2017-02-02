package factory.legacy;

import factory.legacy.ViewElement;
import java.util.LinkedList;

/**
 * <p/>
 * CONFIDENTIAL PROPRIETARY: FUSCARD<br/>Copyright (c) 2012 Fuscard. All rights
 * reserved.<br/> Creation time: Sep 27, 2012 -- 12:05:35 PM<br/>
 * <p/>
 * @author John Mwai
 */
public class DataCollector extends ViewElement {

    private LinkedList<DataSink> sinks = new LinkedList<DataSink>();

    public void addSink(DataSink sink) {
        sinks.add(sink);
    }
}
