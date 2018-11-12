package com.teko.honeybits.honeybits;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.Console;
import java.io.IOException;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {

    TextView name;
    TextView password;
    TextView password_confirmation;
    TextView first_name;
    TextView last_name;
    Context context;
    View parentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        context = this;
        parentLayout = ((ViewGroup) this
                .findViewById(android.R.id.content)).getChildAt(0);

        Objects.requireNonNull(getSupportActionBar()).setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(getResources().getColor(android.R.color.white))
        );
        View view = getLayoutInflater().inflate(R.layout.custom_action_bar_title, new LinearLayout(this), false);
        ((TextView) view.findViewById(R.id.tvTitle)).setText(R.string.register);
        getSupportActionBar().setCustomView(view);

        name = findViewById(R.id.email);
        password = findViewById(R.id.password);
        password_confirmation = findViewById(R.id.password_confirm);
        first_name = findViewById(R.id.first_name);
        last_name = findViewById(R.id.last_name);


        findViewById(R.id.register_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new RegisterUser().execute();
            }
        });
    }

    public class RegisterUser extends AsyncTask<Object, Void, Object>{

        @Override
        protected Object doInBackground(Object[] objects) {
            OkHttpClient client = new OkHttpClient();

            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody body = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("email", name.getText().toString())
                    .addFormDataPart("password", password.getText().toString())
                    .addFormDataPart("password_confirmation", password_confirmation.getText().toString())
                    .addFormDataPart("first_name", first_name.getText().toString())
                    .addFormDataPart("last_name", last_name.getText().toString())
                    .addFormDataPart("sex", "male")
                    .addFormDataPart("birthday", "03-06-1997")
                    .build();
            Request request = new Request.Builder()
                    .url("http://104.248.61.12/users")
                    .post(body)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build();

            try {
                return client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            Response response = (Response) o;
            if (response.code() == 200) {
                Intent i = context.getPackageManager()
                        .getLaunchIntentForPackage(context.getPackageName());
                assert i != null;
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(i);
            } else {
                Snackbar.make(parentLayout, "User already exist", Snackbar.LENGTH_LONG)
                        .setAction("CLOSE", null)
                        .setActionTextColor(parentLayout.getResources().getColor(android.R.color.holo_red_light ))
                        .show();
            }
        }
    }
}
