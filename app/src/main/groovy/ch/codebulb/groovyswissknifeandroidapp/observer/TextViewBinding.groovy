package ch.codebulb.groovyswissknifeandroidapp.observer

import android.text.Editable
import android.text.TextWatcher
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode
import groovyjarjaropenbeans.PropertyChangeEvent
import groovyjarjaropenbeans.PropertyChangeListener

class TextViewBinding {
    private TextViewBinding() {}

    public static void bind(Object model, String modelPropertyName, ViewAttribute view) {
        initView(model, modelPropertyName, view)
        initModelListener(model, modelPropertyName, view)
        initViewListener(model, modelPropertyName, view)
    }

    private static void initView(Object model, String modelPropertyName, ViewAttribute viewAttribute) {
        Object property = model[modelPropertyName]
        if (property in Closure) {
            property = ((Closure)property)()
        }
        viewAttribute.type.updateView.call(viewAttribute, property)
    }

    @TypeChecked(TypeCheckingMode.SKIP)
    private static void initModelListener(Object model, String modelPropertyName, ViewAttribute viewAttribute) {
        Object property = model[modelPropertyName]
        if (property in Closure) {
            property = ((Closure)property).call()
        }
        model.addPropertyChangeListener { PropertyChangeEvent event ->
            viewAttribute.type.updateView.call(viewAttribute, property)
        }
    }

    @TypeChecked(TypeCheckingMode.SKIP)
    private static void initViewListener(Object model, String modelPropertyName, ViewAttribute viewAttribute) {
        if (!viewAttribute.type.updateModelText) {
            return
        }
        viewAttribute.asTextView.addTextChangedListener(new TextWatcher() {
            @Override
            void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            void afterTextChanged(Editable s) {
                List<PropertyChangeListener> propertyChangeListeners = model.propertyChangeListeners as List
                /*
                 * For unknown reasons, using PropertyChangeListener#each(Closure) here would result in
                 *
                 * AndroidRuntime? FATAL EXCEPTION: main
                 * b.b.cg: No signature of method: java.util.ArrayList.each() is applicable for argument types:
                 * (ch.codebulb.groovyswissknifeandroidapp.observer.TextViewBinding$1$_afterTextChanged_closure1)
                 * values: [ch.codebulb.groovyswissknifeandroidapp.observer.TextViewBinding$1$_afterTextChanged_closure1@41dfed48]
                 *
                 * at runtime when app buildType is {minifyEnabled true}
                 */
                for (PropertyChangeListener it : propertyChangeListeners) {
                    model.removePropertyChangeListener(it)
                }
                model[modelPropertyName] = s.toString()
                for (PropertyChangeListener it : propertyChangeListeners) {
                    model.addPropertyChangeListener(it)
                }
            }
        })
    }
}
