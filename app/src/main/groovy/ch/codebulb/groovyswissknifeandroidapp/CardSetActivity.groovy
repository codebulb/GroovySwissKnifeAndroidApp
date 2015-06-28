package ch.codebulb.groovyswissknifeandroidapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import ch.codebulb.groovyswissknifeandroidapp.builder.TableBuilder
import ch.codebulb.groovyswissknifeandroidapp.model.Card
import ch.codebulb.groovyswissknifeandroidapp.model.CardSet
import ch.codebulb.groovyswissknifeandroidapp.observer.TableBinding
import ch.codebulb.groovyswissknifeandroidapp.observer.TextViewBindings
import ch.codebulb.groovyswissknifeandroidapp.util.ActivityUtil
import codebulb.ch.groovyswissknifeandroidapp.R
import com.arasthel.swissknife.SwissKnife
import com.arasthel.swissknife.annotations.OnClick
import groovy.transform.CompileStatic

import static ch.codebulb.groovyswissknifeandroidapp.observer.ViewAttribute.text

@CompileStatic
public class CardSetActivity extends AppCompatActivity {
    CardSet cardSet

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_set);
        // This must be called for injection of views and callbacks to take place
        SwissKnife.inject(this);
        initModel()
    }

    private void initModel() {
        List<CardSet> cardSets = CardSet.listAll(CardSet)
        if (!cardSets.empty) {
            cardSet = cardSets[0]
        }
        else {
            cardSet = new CardSet()
        }

        cardSet.cards = Card.listAll(Card).sort {
            -it.correctGuesses
        } as ObservableList
        initListener()
        findViewById(R.id.addCardButton).requestFocus()
    }

    private void initListener() {
        // misc properties
        TextViewBindings.bind(cardSet, [
                template1: text(findViewById(R.id.cardset_template1)),
                template2: text(findViewById(R.id.cardset_template2)),
        ])

        // card list
        View table = findViewById(R.id.card_table_layout)
        TableBinding.bind(cardSet.cards, table) { Card card ->
            TableBuilder.linearLayout(this,
                    TableBuilder.button(this, "X", {
                        card.delete()
                        cardSet.cards.remove(card)
                    }),
                    TableBuilder.text(this, "($card.correctGuesses) $card.text1:\n$card.text2", {
                        startActivity(ActivityUtil.intent(this, CardEditActivity, [
                                id: card.id
                        ]))
                    })
            )
        }
    }

    @OnClick(R.id.saveButton)
    public void save() {
        cardSet.save()
    }

    @OnClick(R.id.addCardButton)
    public void addNewCard() {
        startActivity(intent(CardEditActivity))
    }

    // Coming back from addNewCard
    @Override
    protected void onResume() {
        super.onResume()
        initModel()
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
