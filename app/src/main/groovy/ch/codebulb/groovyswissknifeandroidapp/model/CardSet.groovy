package ch.codebulb.groovyswissknifeandroidapp.model

import com.orm.SugarRecord
import groovy.beans.Bindable

@Bindable
class CardSet extends SugarRecord<CardSet> {
    String template1
    String template2
    ObservableList cards = [] as ObservableList
}
