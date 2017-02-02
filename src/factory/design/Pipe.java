package factory.design;

import java.util.LinkedList;

/**
 * Pipe for organizing connections. Using Pipes one can bundle Connections so
 * that they are more manageable. A connection has to get in through on end and
 * get out through the other. A pipe is a box that can be resized only
 * longitudinally. 
 *
 * @author John Mwai
 */
public class Pipe extends Box {

    /**
     * Whether connections are visible in this Pipe.
     */
    private boolean showingConnections;
    /**
     * The list of connections that this Pipe has wrapped.
     */
    private LinkedList<Connection> connections;
}
