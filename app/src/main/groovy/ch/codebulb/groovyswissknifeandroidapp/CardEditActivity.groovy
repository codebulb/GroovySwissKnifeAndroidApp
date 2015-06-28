package ch.codebulb.groovyswissknifeandroidapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import ch.codebulb.groovyswissknifeandroidapp.model.Card
import ch.codebulb.groovyswissknifeandroidapp.model.CardSet
import ch.codebulb.groovyswissknifeandroidapp.observer.TextViewBindings
import codebulb.ch.groovyswissknifeandroidapp.R
import com.arasthel.swissknife.SwissKnife
import com.arasthel.swissknife.annotations.Extra
import com.arasthel.swissknife.annotations.OnClick
import groovy.transform.CompileStatic

import static ch.codebulb.groovyswissknifeandroidapp.observer.ViewAttribute.hint
import static ch.codebulb.groovyswissknifeandroidapp.observer.ViewAttribute.text

@CompileStatic
public class CardEditActivity extends AppCompatActivity {
    @Extra
    Long id

    CardSet cardSet = CardSet.listAll(CardSet)[0]
    Card card = new Card()

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_edit);
        // This must be called for injection of views and callbacks to take place
        SwissKnife.inject(this);
        SwissKnife.loadExtras(this)
        if (id != null) {
            card = Card.findById(Card, id)
        }
        initListener()
    }

    private void initListener() {
        TextViewBindings.bind(card, [
                text1       : text(findViewById(R.id.text1)),
                text2       : text(findViewById(R.id.text2)),
                correctGuesses: text(findViewById(R.id.correctGuesses)),
        ])
        TextViewBindings.bind(cardSet, [
                template1: hint(findViewById(R.id.text1)),
                template2: hint(findViewById(R.id.text2))
        ])
    }

    @OnClick(R.id.saveButton)
    public void save() {
        card.save()
        setResult(RESULT_OK)
        finish()
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_card_edit, menu);
        return true;
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
