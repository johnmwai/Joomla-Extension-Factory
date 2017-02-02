package factory.design;

/**
 *
 * @author John Mwai
 */
public interface EventProducerInterface<W extends PropertyChangeEvent> {

    /**
     * Add a listener to listen to some event
     *
     * @param listener The listener to add
     */
    public void addPropertyChangeListener(PropertyChangeListener<W> listener);

    /**
     * Remove a listener
     *
     * @param listener The listener to remove
     */
    public void removePropertyChangeListener(PropertyChangeListener<W> listener);

    /**
     * Fire property change event to all listeners.
     *
     * @param event The event to fire.
     */
    public void firePropertyChangeEvent(W event);
}
