package com.music.voroshilo.dialog;

import android.content.Intent;
import android.net.Uri;
import android.widget.RatingBar;

import com.music.voroshilo.R;

import butterknife.BindView;
import butterknife.OnClick;

public class RatingDialogFragment extends BaseDialogFragment {
    private static final int MIN_RATING_MARKET = 4;

    @BindView(R.id.rating_bar)
    RatingBar ratingBar;

    @OnClick(R.id.rating_button)
    void rate() {
        int rating = (int) ratingBar.getRating();
        if (rating >= MIN_RATING_MARKET) {
            openAppInMarket();
        }
        dismissAllowingStateLoss();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_fragment_rating;
    }

    private void openAppInMarket() {
        //todo change to own - getPackageName
        final String appPackageName = "com.google.android.music";
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }
}
