package factory.legacy;

import java.util.LinkedList;

/**
 * <p/>
 * CONFIDENTIAL PROPRIETARY: FUSCARD<br/>Copyright (c) 2012 Fuscard. All rights
 * reserved.<br/> Creation time: Sep 27, 2012 -- 11:56:23 AM<br/>
 * <p/>
 * @author John Mwai
 */
public class DataCollection extends Capability {

    LinkedList<DataCollector> collectors = new LinkedList<DataCollector>();
    DataCollector defaultDataCollector = null;

    public void addCollector(DataCollector dcr) {
        collectors.add(dcr);
    }

    public void setDefault(DataCollector dataCollector) {
        defaultDataCollector = dataCollector;
    }

    @Override
    public void setEntryTransition(ViewElement v1, ViewElement v2, TransitionProcessor tp) {
        if (collectors.isEmpty() || collectors.size() == 1) {
            throw new IllegalStateException("You can only set transitions when there are more than one datacollectors");
        }
        DataCollector dc1 = (DataCollector) v1;
        DataCollector dc2 = (DataCollector) v2;
        
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void addTransition(DataCollector email, DataCollector personalDetails, TransitionProcessor email_to_personal) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
