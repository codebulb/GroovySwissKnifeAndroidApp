package ch.codebulb.groovyswissknifeandroidapp.observer

import android.view.View
import android.widget.EditText
import android.widget.TextView
import groovy.transform.CompileStatic
import groovy.transform.TupleConstructor

@CompileStatic
@TupleConstructor
class ViewAttribute {
    View view
    Type type

    public TextView getAsTextView() {
        return view in TextView ? (TextView)view : null
    }

    public EditText getAsEditTextView() {
        return view in EditText ? (EditText)view : null
    }

    public static ViewAttribute text(View view) {
        return new ViewAttribute(view, Type.text())
    }

    public static ViewAttribute enabled(View view) {
        return new ViewAttribute(view, Type.enabled())
    }

    public static ViewAttribute hint(View view) {
        return new ViewAttribute(view, Type.hint())
    }

    public static class Type {
        public static Type text() {
            new Type(true, { ViewAttribute viewAttribute, Object value ->
                viewAttribute.asTextView.text = value != null ? value.toString() : ''
                // set cursor
                if (viewAttribute.asEditTextView) {
                    (viewAttribute.asEditTextView).selection = viewAttribute.asEditTextView.text.length()
                }
            })
        }

        public static Type enabled() {
            new Type(false, { ViewAttribute viewAttribute, Object value ->
                viewAttribute.view.enabled = (boolean) value
                if (!viewAttribute.view.enabled) {
                    viewAttribute.view.clearFocus()
                }
            })
        }

        public static Type hint() {
            new Type(false, { ViewAttribute viewAttribute, Object value ->
                viewAttribute.asTextView.hint = value.toString()
            })
        }

        boolean updateModelText
        Closure updateView

        private Type(boolean updateModelText, Closure updateView) {
            this.updateModelText = updateModelText
            this.updateView = updateView
        }
    }
}
