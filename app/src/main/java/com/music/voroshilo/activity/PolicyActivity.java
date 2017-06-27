package com.music.voroshilo.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.webkit.WebView;

import com.music.voroshilo.R;

import butterknife.BindView;

public class PolicyActivity extends BaseActivity {

    @BindView(R.id.policy_web_view)
    WebView policyWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        policyWebView.loadUrl("http://audiko.net/privacy.html");
    }

    @Override
    protected void onResume() {
        super.onResume();
        policyWebView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        policyWebView.onPause();
    }
}