package com.sprtech.quickbite;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;


public class MessageFragment extends Fragment {
    View v;
    private MaterialButton chartBtn;
    private ProgressDialog loadingBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_message, container, false);
        chartBtn = v.findViewById(R.id.chartBtn);
        loadingBar = new ProgressDialog(getContext());
        loadingBar.setTitle("Loading");
        loadingBar.setMessage("Please wait...");
        loadingBar.setCanceledOnTouchOutside(true);
        chartBtn.setOnClickListener(view -> {
            loadingBar.show();
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                loadingBar.dismiss();
                Intent i = new Intent(getContext(), chart_activity.class);
                startActivity(i);
            }, 3000);
        });
        return v;
    }
}