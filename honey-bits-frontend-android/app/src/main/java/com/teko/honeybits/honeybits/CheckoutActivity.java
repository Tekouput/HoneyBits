package com.teko.honeybits.honeybits;
import android.support.v7.app.AppCompatActivity;

import com.stripe.android.model.Card;
import com.stripe.android.view.CardInputWidget;
import com.stripe.android.Stripe;
import com.stripe.android.model.Token;

public class CheckoutActivity extends AppCompatActivity {
    CardInputWidget mCardInputWidget = (CardInputWidget) findViewById(R.id.card_input_widget);

  /*  Card card = mCardInputWidget.getCard();
        if (card == null) {
        // Do not continue token creation.
    }

       */

}
