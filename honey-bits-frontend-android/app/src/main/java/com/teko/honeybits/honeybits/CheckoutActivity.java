package com.teko.honeybits.honeybits;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.view.CardInputWidget;
import com.stripe.android.Stripe;
import com.stripe.android.model.Token;

import static java.security.AccessController.getContext;

public class CheckoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        final Context context = this;
        final CardInputWidget mCardInputWidget = (CardInputWidget) findViewById(R.id.card_input_widget);





        findViewById(R.id.paybutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Card card = mCardInputWidget.getCard();
                Stripe stripe = new Stripe(context, "pk_test_HvNbBkFnhEzG0blC1adFF8ZN");

                stripe.createToken(card,
                        new TokenCallback() {
                            public void onSuccess(Token token) {
                                // Send token to your server
                                System.out.print(token);

                            }
                            public void onError(Exception error) {
                                // Show localized error message
                                System.out.print(error);

                            }
                        });
            }


        });


    }
}










