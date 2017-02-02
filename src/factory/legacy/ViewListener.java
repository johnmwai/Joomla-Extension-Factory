package factory.legacy;

import com.fuscard.commons.FileWriter;

/**
 * <p/>
 * CONFIDENTIAL PROPRIETARY: FUSCARD<br/>Copyright (c) 2012 Fuscard. All rights
 * reserved.<br/> Creation time: Sep 27, 2012 -- 3:24:11 PM<br/>
 * <p/>
 * @author John Mwai
 */
public interface ViewListener {

    public void onBeforeWrite(FileWriter fw);

    public void writeComments(FileWriter fw);

    public void onWriteHeaders(FileWriter fw);
}
