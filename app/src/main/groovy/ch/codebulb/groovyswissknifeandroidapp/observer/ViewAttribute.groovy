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
        return new ViewAttribute(view, Type.text)
    }

    public static ViewAttribute enabled(View view) {
        return new ViewAttribute(view, Type.enabled)
    }

    public static ViewAttribute hint(View view) {
        return new ViewAttribute(view, Type.hint)
    }

    /*
     * Should be an enum, but for unknown reasons, using an enum results in
     *
     * AndroidRuntime? FATAL EXCEPTION: main
     * java.lang.ExceptionInInitializerError
     * Caused by: java.lang.IllegalArgumentException: This class has been compiled with a super class which is
     * binary incompatible with the current super class found on classpath. You should recompile this class with the new version.
     *
     *  at runtime when app buildType is {minifyEnabled true}
     */
    @TupleConstructor
    public static class Type {
        private static final Type text = text()
        private static final Type enabled = enabled()
        private static final Type hint = hint()

        private static Type text() {
            new Type(true, { ViewAttribute viewAttribute, Object value ->
                viewAttribute.asTextView.text = value != null ? value.toString() : ''
                // set cursor
                if (viewAttribute.asEditTextView) {
                    (viewAttribute.asEditTextView).selection = viewAttribute.asEditTextView.text.length()
                }
            })
        }

        private static Type enabled() {
            new Type(false, { ViewAttribute viewAttribute, Object value ->
                viewAttribute.view.enabled = (boolean) value
                if (!viewAttribute.view.enabled) {
                    viewAttribute.view.clearFocus()
                }
            })
        }

        private static Type hint() {
            new Type(false, { ViewAttribute viewAttribute, Object value ->
                viewAttribute.asTextView.hint = value.toString()
            })
        }

        boolean updateModelText
        Closure updateView
    }
}
