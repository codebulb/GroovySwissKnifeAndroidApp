package ch.codebulb.groovyswissknifeandroidapp.util

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.view.inputmethod.InputMethodManager
import groovy.transform.CompileStatic

@CompileStatic
class ViewUtil {
    private ViewUtil() {}

    // based on http://stackoverflow.com/a/8395263
    public static List<View> findChildren(ViewParent parent, Closure<Boolean> predicate={true}) {
        List<View> ret = []
        View nextChild
        for(int i=0; i<((ViewGroup)parent).getChildCount(); ++i) {
            nextChild = ((ViewGroup)parent).getChildAt(i)
            if (predicate(nextChild)) {
                ret << nextChild
            }
        }
        return ret
    }

    // based on http://stackoverflow.com/a/8395263
    public static View findFirstChild(View parent, Closure<Boolean> predicate={true}) {
        View nextChild
        for(int i=0; i<((ViewGroup)parent).getChildCount(); ++i) {
            nextChild = ((ViewGroup)parent).getChildAt(i)
            if (predicate(nextChild)) {
                return nextChild
            }
        }
    }

    public static void addKeyboardOnFocusChanged(Activity activity, View view) {
        view.setOnFocusChangeListener { View v, boolean hasFocus ->
            if (hasFocus) {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            } else {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }
}
