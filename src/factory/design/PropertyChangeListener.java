package factory.design;

/**
 * Interface for things that need to be notified about a certain event
 * @author John Mwai
 */
public interface PropertyChangeListener<V extends PropertyChangeEvent> {
    public void propertyChanged(V event);
}
