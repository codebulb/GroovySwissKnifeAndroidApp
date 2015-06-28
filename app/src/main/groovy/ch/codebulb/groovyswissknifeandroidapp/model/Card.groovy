package ch.codebulb.groovyswissknifeandroidapp.model

import com.orm.SugarRecord
import groovy.beans.Bindable
import groovy.transform.AutoClone

@Bindable
@AutoClone
class Card extends SugarRecord<Card> {
    String text1
    String text2
    int correctGuesses = 0

    Card() {}

    Card(String text1, String text2) {
        this.text2 = text2
        this.text1 = text1
    }

    public String check(Card original) {
        if (text1 != original.text1) {
            return original.text1
        }
        if (text2 != original.text2) {
            return original.text2
        }
        return null;
    }

    public void next() {
        correctGuesses++
    }

    public Closure getText1Guessed() {
        return {text1 == null}
    }

    public Closure getText2Guessed() {
        return {text2 == null}
    }
}
