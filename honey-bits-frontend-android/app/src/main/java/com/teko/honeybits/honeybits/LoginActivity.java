package com.teko.honeybits.honeybits;

import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.teko.honeybits.honeybits.API.Authentication.LoginHandler;

import java.util.Objects;
import java.util.zip.Inflater;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Objects.requireNonNull(getSupportActionBar()).setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(getResources().getColor(android.R.color.white))
        );
        View view = getLayoutInflater().inflate(R.layout.custom_action_bar_title, new LinearLayout(this), false);
        ((TextView) view.findViewById(R.id.tvTitle)).setText(R.string.sign_in);
        getSupportActionBar().setCustomView(view);

    }

    public void signIn(View view) {

        String email = ((EditText)findViewById(R.id.email)).getText().toString();
        String password = ((EditText)findViewById(R.id.password)).getText().toString();

        Log.i("Credentials: ", email + " @ " +  password);

        LoginHandler loginHandler = new LoginHandler(this);
        loginHandler.performLogin(email, password);
    }

    public void forgotPassword(View view) {
    }

    public void continueGoogle(View view) {
    }

    public void continueFacebook(View view) {
    }

    public void registerCall(View view) {
    }
}
