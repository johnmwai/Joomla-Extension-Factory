package design;

import factory.design.Box;
import factory.design.PropertyChangeListener;

/**
 *
 * @author John Mwai
 */
public class Test {

    public static void main(String[] args) {
        Box box = new Box();
        box.addPropertyChangeListener(
                new PropertyChangeListener<Box.BoxPropertyChangeEvent>() {
            @Override
            public void propertyChanged(Box.BoxPropertyChangeEvent event) {
                Box.BoxPropertyType type = event.getType();
                Box source = event.getSource();
                Object newValue = event.getNewValue();
                Object oldValue = event.getOldValue();
            }
        });
    }
}
