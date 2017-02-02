package factory.design;

import java.util.LinkedList;

/**
 *
 * @author John Mwai
 */
public class EventProducer<W extends PropertyChangeEvent> implements EventProducerInterface<W>{
    private LinkedList<PropertyChangeListener> listeners = new LinkedList<>();

    @Override
    public void addPropertyChangeListener(PropertyChangeListener<W> listener) {
        if(!listeners.contains(listener)){
            listeners.add(listener);
        }
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener<W> listener) {
        listeners.remove(listener);
    }

    @Override
    public void firePropertyChangeEvent(W event) {
        for(PropertyChangeListener listener : listeners){
            listener.propertyChanged(event);
        }
    }
}
