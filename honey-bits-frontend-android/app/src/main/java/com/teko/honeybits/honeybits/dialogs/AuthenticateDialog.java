package com.teko.honeybits.honeybits.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

import com.teko.honeybits.honeybits.LoginActivity;
import com.teko.honeybits.honeybits.R;
import com.teko.honeybits.honeybits.RegisterActivity;

public class AuthenticateDialog extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_authenticate, null);

        builder.setView(view);

        view.findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(builder.getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        view.findViewById(R.id.register_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(builder.getContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        return builder.create();
    }
}
