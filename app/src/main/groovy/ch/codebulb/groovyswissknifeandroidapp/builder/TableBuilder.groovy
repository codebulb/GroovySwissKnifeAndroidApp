package ch.codebulb.groovyswissknifeandroidapp.builder

import android.app.Activity
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import groovy.transform.CompileStatic

@CompileStatic
class TableBuilder {
    public static LinearLayout linearLayout(Activity activity, TextView... children) {
        LinearLayout row = new LinearLayout(activity);
        row.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        row.setOrientation(LinearLayout.HORIZONTAL);
        children.each { View it ->
            row.addView(it)
        }
        return row
    }

    public static TextView text(Activity activity, String text, Closure onClickListener) {
        return textView(activity, text, onClickListener, new TextView(activity)) as TextView
    }

    public static Button button(Activity activity, String text, Closure onClickListener) {
        return textView(activity, text, onClickListener, new Button(activity)) as Button
    }

    private static View textView(Activity activity, String text, Closure onClickListener, TextView view) {
        view.text = text
        view.setTextAppearance(activity, android.R.style.TextAppearance_Small);
        view.onClickListener = onClickListener as View.OnClickListener
        return view
    }
}
