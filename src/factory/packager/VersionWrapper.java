package factory.packager;

import java.util.StringTokenizer;

/**
 * <p/>
 * CONFIDENTIAL PROPRIETARY: FUSCARD<br/>Copyright (c) 2012 Fuscard. All rights
 * reserved.<br/> Creation time: Sep 25, 2012 -- 7:31:33 AM<br/>
 * <p/>
 * @author John Mwai
 */
public class VersionWrapper {

    public static final int GREATER_THAN = 1;//Later version
    public static final int EQUAL = 0;//Same version
    public static final int LESS_THAN = -1;//Earlier version
    
    public static final int EARLIER = -1;
    public static final int LATER = 1;
    public static final int SAME = 0;
    private int[] parts;

    public VersionWrapper(String version) {
        setVersion(version);
    }

    public final void setVersion(String version) {
        if (version == null || !version.matches("\\d+(?:\\.\\d+)*")) {
            throw new IllegalArgumentException((version == null ? "null":"the string '" + version + "'") + " is not a version");
        }
        StringTokenizer tokenizer = new StringTokenizer(version, ".");
        int[] components = new int[tokenizer.countTokens()];
        int i = 0;
        while (tokenizer.hasMoreTokens()) {
            components[i++] = Integer.parseInt(tokenizer.nextToken());
            if (components[i - 1] < 0) {
                throw new IllegalArgumentException("Version strings may not contain negative numbers");
            }
        }
        parts = components;
        trim();
    }

    public String getVersion() {
        String res = String.valueOf(parts[0]);
        for (int i = 1; i < parts.length; i++) {
            res += "." + parts[i];
        }
        return res;
    }

    private void trim() {
        int trailing = 0;
        for (int i = parts.length - 1; i >= 0; i--) {
            if (parts[i] > 0) {
                break;
            }
            trailing++;
        }
        int l = parts.length - trailing;
        if (parts.length > 2 && l <= 2) {
            l = 2;
        }
        int[] temp = new int[l];
        System.arraycopy(parts, 0, temp, 0, temp.length);
        parts = temp;
    }

    private void extend(int length) {
        if (length <= parts.length) {
            return;
        }
        int[] temp = new int[length];
        System.arraycopy(parts, 0, temp, 0, parts.length);
        parts = temp;
    }

    /**
     * Returns {@link #GREATER_THAN GREATER_THAN} if the argument is greater than this, {@link #EQUAL EQUAL} if they are
     * equal and {@link #LESS_THAN LESS_THAN} otherwise
     * <p/>
     * @param vw A version wrapper to compare
     * @return a constant to show whether the argument is less equal or greater
     *         than this version wrapper
     */
    public int compare(VersionWrapper vw) {
        int i;
        for (i = 0; i < vw.parts.length; i++) {
            if (i >= parts.length) {
                return GREATER_THAN;
            }
            if (vw.parts[i] > parts[i]) {
                return GREATER_THAN;
            } else if (vw.parts[i] < parts[i]) {
                return LESS_THAN;
            }
        }
        if (vw.parts.length == parts.length) {
            return EQUAL;
        }
        return LESS_THAN;
    }

    public static int comp(String v1, String v2) {
        VersionWrapper vw1 = new VersionWrapper(v1);
        VersionWrapper vw2 = new VersionWrapper(v2);
        return vw1.compare(vw2);
    }

    /**
     * Makes the next version from the version string
     * <p/>
     * @param part the part of the version string to increment
     */
    public void increment(int part) {
        if (part == -1) {
            parts[parts.length - 1]++;
        } else {
            extend(part + 1);
            parts[part]++;
            if (parts.length - part > 1) {
                for (int i = part + 1; i < parts.length; i++) {
                    parts[i] = 0;
                }
            }
        }

        trim();
    }
}
