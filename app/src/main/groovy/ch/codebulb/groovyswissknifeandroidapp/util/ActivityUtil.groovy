package ch.codebulb.groovyswissknifeandroidapp.util

import android.app.Activity
import android.content.Intent
import groovy.transform.CompileStatic
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

@CompileStatic
class ActivityUtil {
    @TypeChecked(TypeCheckingMode.SKIP)
    public static Intent intent(Activity activity, Class intentClass, Map<String, ?> extras) {
        Intent intent = activity.intent(intentClass)
        // prevent NullPointerException on null insertion
        /*
         * For unknown reasons, using Closure method on Map here would result in
         * "No signature of method exception" at runtime when app buildType is {minifyEnabled true}
         */
        for (String key : extras.keySet()) {
            if (extras[key] != null) {
                intent.putExtra(key, extras[key])
            }
        }
        return intent
    }
}
