package com.teko.honeybits.honeybits;

import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.zip.Inflater;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(getResources().getColor(android.R.color.white))
        );
        View view = getLayoutInflater().inflate(R.layout.custom_action_bar_title, new LinearLayout(this), false);
        ((TextView) view.findViewById(R.id.tvTitle)).setText(R.string.sign_in);
        getSupportActionBar().setCustomView(view);
    }
}
