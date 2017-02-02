package factory.legacy;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * <p/>
 * CONFIDENTIAL PROPRIETARY: FUSCARD<br/>Copyright (c) 2012 Fuscard. All rights
 * reserved.<br/> Creation time: Oct 2, 2012 -- 5:01:51 PM<br/>
 * <p/>
 * @author John Mwai
 */
public class MyFileReader {

    BufferedReader inputStream = null;

    public MyFileReader(String file) throws IOException {
        inputStream = new BufferedReader(new FileReader(file));
    }

    public String readLine() {
        try {
            return inputStream.readLine();
        } catch (IOException ex) {
            throw new IllegalStateException(ex.getMessage());
        }
    }

    @Override
    protected void finalize() throws Throwable {
        inputStream.close();
        super.finalize();
    }
}
