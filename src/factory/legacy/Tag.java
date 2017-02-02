package factory.legacy;

import factory.legacy.Script;

/**
 * <p/>
 * CONFIDENTIAL PROPRIETARY: FUSCARD<br/>Copyright (c) 2012 Fuscard. All rights
 * reserved.<br/> Creation time: Oct 12, 2012 -- 12:04:51 PM<br/>
 * <p/>
 * @author John Mwai
 */
public class Tag extends Script {

    private final String start;
    private final String finish;

    public Tag(String start, String finish) {
        this.start = start;
        this.finish = finish;
    }

    public void create(String content){
        push(start);
        push(content);
        push(finish);
    }

    @Override
    public void push(String line) {
        line = line.trim();
        super.push(line);
    }

    public static class PHP extends Tag {

        public PHP() {
            super("<?php", "?>");
        }
        
        private static class Block {
            final private String str, stp;

            public Block(String str, String stp) {
                this.str = str;
                this.stp = stp;
            }
            
            void create(){
                
            }
        }
    }
}
