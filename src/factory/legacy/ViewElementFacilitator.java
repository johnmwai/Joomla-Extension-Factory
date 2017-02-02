package factory.legacy;

import com.fuscard.commons.FileWriter;
import java.util.LinkedList;

/**
 * <p/>
 * CONFIDENTIAL PROPRIETARY: FUSCARD<br/>Copyright (c) 2012 Fuscard. All rights
 * reserved.<br/> Creation time: Sep 27, 2012 -- 3:42:42 PM<br/>
 * <p/>
 * @author John Mwai
 */
public class ViewElementFacilitator {

    private final View view;
    public final LinkedList<Capability> capabilities = new LinkedList<Capability>();

    public ViewElementFacilitator(View view) {
        this.view = view;
    }

    public void injectScriptsToView(FileWriter fw) {
    }

    public void gatherData() {
    }

    public void addCapability(Capability c) {
        capabilities.add(c);
    }

    public void emitFunctions(Class type, FileWriter fw) {
        if ("Model".equals(Class.class.getSimpleName())) {
        } else if ("Controller".equals(Class.class.getSimpleName())) {
        }
    }

    public void makeFiles() {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
