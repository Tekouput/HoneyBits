package com.teko.honeybits.honeybits.fragments.you.admin.shop;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teko.honeybits.honeybits.R;

public class PoliciesSettingsAdmin extends Fragment {

    public PoliciesSettingsAdmin() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_policies_settings_admin, container, false);
        return rootView;
    }
}
