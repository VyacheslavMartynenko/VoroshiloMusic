package mp3.music.download.downloadmp3.downloadmusic.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import butterknife.BindView;
import mp3.music.download.downloadmp3.downloadmusic.R;

public class PolicyActivity extends BaseActivity {
    public static final String URL = "url";

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
        if (getIntent() != null) {
            String url = getIntent().getStringExtra(URL);
            policyWebView.setWebChromeClient(new WebChromeClient());
            policyWebView.getSettings().setJavaScriptEnabled(true);
            policyWebView.loadUrl(url);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        policyWebView.onResume();
    }

    @Override
    protected void onPause() {
        policyWebView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (policyWebView != null) {
            policyWebView.loadUrl("about:blank");
        }
        super.onDestroy();
    }
}