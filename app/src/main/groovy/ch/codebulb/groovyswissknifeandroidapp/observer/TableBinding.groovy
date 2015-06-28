package ch.codebulb.groovyswissknifeandroidapp.observer

import android.view.View
import android.view.ViewGroup
import groovy.transform.CompileStatic
import groovyjarjaropenbeans.PropertyChangeEvent
import groovyjarjaropenbeans.PropertyChangeListener

@CompileStatic
class TableBinding {
    private TableBinding() {}

    public static void bind(ObservableList model, View parentView, Closure<? extends View> createNewView) {
        initView(model, (ViewGroup)parentView, createNewView)
        initModelListener(model, (ViewGroup)parentView, createNewView)
    }

    private static void initView(ObservableList model, ViewGroup parentView, Closure<? extends View> createNewView) {
        // clean children
        parentView.removeAllViews()
        model.each {
            parentView.addView(createNewView(it))
        }
    }

    public static void initModelListener(ObservableList model, ViewGroup parentView, Closure<? extends View> createNewView) {
        model.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            void propertyChange(PropertyChangeEvent changeEvent) {
                if (!(changeEvent in ObservableList.ElementEvent)) {
                    return // ignore size() changes and the like
                }
                ObservableList.ElementEvent event = (ObservableList.ElementEvent)changeEvent
                switch(event) {
                    case ObservableList.ElementAddedEvent:
                        parentView.addView(createNewView(event.newValue), event.index)
                    case ObservableList.ElementUpdatedEvent:
                        parentView.removeViewAt(event.index)
                        parentView.addView(createNewView(event.newValue), event.index)
                    case ObservableList.ElementRemovedEvent:
                        parentView.removeViewAt(event.index)
                }
            }
        })
    }
}
