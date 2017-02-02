package factory.design;

/**
 * A property Change event
 * @author John Mwai
 * @param <T> The Type of the property discriminator e.g. an enumeration type
 * @param <U> The type of the objects that produce these events
 * @param <V> The Type of values that these events can carry
 */
public class PropertyChangeEvent<T, U, V> {
    /**
     * The type of event this is
     */
    private T type;
    /**
     * The object that generated the event
     */
    private U source;
    /**
     * The old value of a property
     */
    private V oldValue;
    /**
     * The new value of a property.
     */
    private V newValue;

    public PropertyChangeEvent(T type, U source, V oldValue,
            V newValue) {
        this.type = type;
        this.source = source;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public PropertyChangeEvent() {
    }

    /**
     * @return the type
     */
    public T getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(T type) {
        this.type = type;
    }

    /**
     * @return the source
     */
    public U getSource() {
        return source;
    }

    /**
     * @param source the source to set
     */
    public void setSource(U source) {
        this.source = source;
    }

    /**
     * @return the oldValue
     */
    public V getOldValue() {
        return oldValue;
    }

    /**
     * @param oldValue the oldValue to set
     */
    public void setOldValue(V oldValue) {
        this.oldValue = oldValue;
    }

    /**
     * @return the newValue
     */
    public V getNewValue() {
        return newValue;
    }

    /**
     * @param newValue the newValue to set
     */
    public void setNewValue(V newValue) {
        this.newValue = newValue;
    }
    
    
}
