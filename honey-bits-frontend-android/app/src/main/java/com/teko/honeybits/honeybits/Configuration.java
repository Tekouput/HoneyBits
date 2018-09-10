package com.teko.honeybits.honeybits;

import android.support.v4.app.FragmentManager;

import com.teko.honeybits.honeybits.dialogs.AuthenticateDialog;

public class Configuration {

    public static FragmentManager fragmentManager;

    public static void PromptAuth(){
        AuthenticateDialog authenticateDialog = new AuthenticateDialog();
        authenticateDialog.show(fragmentManager, "AuthenticateDialogFragment");
    }
}
