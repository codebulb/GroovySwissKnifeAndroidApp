package ch.codebulb.groovyswissknifeandroidapp.observer

import groovy.transform.CompileStatic

@CompileStatic
class TextViewBindings {
    private TextViewBindings() {}

    public static void bind(def model, Map<String, ViewAttribute> mapping) {
        mapping.each {String propertyName, ViewAttribute viewAttribute ->
            TextViewBinding.bind(model, propertyName, viewAttribute)
        }
    }
}
