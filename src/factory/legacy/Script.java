package factory.legacy;

/**
 * <p/>
 * CONFIDENTIAL PROPRIETARY: FUSCARD<br/>Copyright (c) 2012 Fuscard. All rights
 * reserved.<br/> Creation time: Oct 12, 2012 -- 12:07:27 PM<br/>
 * <p/>
 * @author John Mwai
 */
public class Script {

    private String content = "";
    private int indent = 0;

    public void nl() {
        content += "\n";
    }

    public void push(String line) {
        for (int i = 0; i < indent; i++) {
            content += "\t";
        }
        content += line;
        nl();
    }

    public String getContent() {
        return content;
    }

    public void indent(Direction dir) {
        switch (dir) {
            case Less:
                indent = indent > 0 ? indent-- : 0;
                break;
            case More:
                indent = indent >= 0 ? indent++ : 0;
                break;
        }
    }

    public static enum Direction {

        More,
        Less
    }
}
