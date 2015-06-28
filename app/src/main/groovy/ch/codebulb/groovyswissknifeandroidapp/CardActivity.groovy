package ch.codebulb.groovyswissknifeandroidapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import ch.codebulb.groovyswissknifeandroidapp.model.Card
import ch.codebulb.groovyswissknifeandroidapp.model.CardSet
import ch.codebulb.groovyswissknifeandroidapp.observer.TextViewBindings
import ch.codebulb.groovyswissknifeandroidapp.util.ViewUtil
import codebulb.ch.groovyswissknifeandroidapp.R
import com.arasthel.swissknife.SwissKnife
import com.arasthel.swissknife.annotations.OnClick
import groovy.transform.CompileStatic

import static ch.codebulb.groovyswissknifeandroidapp.observer.ViewAttribute.*

@CompileStatic
public class CardActivity extends AppCompatActivity {
    CardSet cardSet = CardSet.listAll(CardSet)[0]
    List<Card> cards = Card.listAll(Card)
    Card card
    Card guessedCard

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        // This must be called for injection of views and callbacks to take place
        SwissKnife.inject(this);
        [findViewById(R.id.text1), findViewById(R.id.text2)].each {
            ViewUtil.addKeyboardOnFocusChanged(this, it)
        }
        loadCard()
    }

    private void loadCard() {
        card = Card.findById(Card, cards[new Random().nextInt(cards.size())].id)
        guessedCard = card.clone()

        if (new Random().nextBoolean()) {
            guessedCard.text1 = null
        } else {
            guessedCard.text2 = null
        }
        initListener()
        ViewUtil.findFirstChild(findViewById(R.id.card_layout), {View view -> view.enabled}).requestFocus()
    }

    private void initListener() {
        TextViewBindings.bind(guessedCard, [
                text1       : text(findViewById(R.id.text1)),
                text2       : text(findViewById(R.id.text2)),
                text1Guessed: enabled(findViewById(R.id.text1)),
                text2Guessed: enabled(findViewById(R.id.text2))
        ])
        TextViewBindings.bind(cardSet, [
                template1: hint(findViewById(R.id.text1)),
                template2: hint(findViewById(R.id.text2))
        ])
    }

    @OnClick(R.id.okButton)
    public void guess() {
        String checked = guessedCard.check(card)
        if (checked != null) {
            toast("Correct answer was: $checked").show()
        }
        else {
            card.correctGuesses++
            card.save()
        }
        // reset focus
        findViewById(R.id.text1).requestFocus()
        loadCard()
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
