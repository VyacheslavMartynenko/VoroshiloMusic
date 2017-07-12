package free.mp3.musicdownloadmp3.dialog;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.RatingBar;

import free.mp3.musicdownloadmp3.R;
import free.mp3.musicdownloadmp3.util.preferences.UserPreferences;

import butterknife.BindView;
import butterknife.OnClick;

public class RatingDialogFragment extends BaseDialogFragment {
    private static final int MIN_RATING_MARKET = 4;
    private static final String MARKET_URL = "free.mp3.musicdownloadmp3.market";

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
        UserPreferences.getInstance().setIsAppRated();
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