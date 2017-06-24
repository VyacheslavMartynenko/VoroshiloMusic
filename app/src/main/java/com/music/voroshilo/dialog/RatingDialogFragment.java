package com.music.voroshilo.dialog;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.RatingBar;

import com.music.voroshilo.R;

import butterknife.BindView;
import butterknife.OnClick;

public class RatingDialogFragment extends BaseDialogFragment {
    private static final int MIN_RATING_MARKET = 4;
    private static final String MARKET_URL = "com.music.voroshilo.market";

    public static RatingDialogFragment newInstance(String url) {

        Bundle args = new Bundle();
        args.putString(MARKET_URL, url);

        RatingDialogFragment fragment = new RatingDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

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
        Bundle arguments = getArguments();
        if (arguments != null) {
            String marketUrl = arguments.getString(MARKET_URL);
            if (marketUrl != null) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(marketUrl)));
            }
        }
    }
}