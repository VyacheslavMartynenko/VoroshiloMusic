package mp3.music.download.downloadmp3.downloadmusic.dialog;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import mp3.music.download.downloadmp3.downloadmusic.R;
import mp3.music.download.downloadmp3.downloadmusic.util.preferences.UserPreferences;

public class RatingDialogFragment extends BaseDialogFragment {
    private static final int MIN_RATING_MARKET = 4;
    private static final String MARKET_URL = "mp3.music.download.downloadmp3.downloadmusic";

    public static RatingDialogFragment newInstance(String url) {

        Bundle args = new Bundle();
        args.putString(MARKET_URL, url);

        RatingDialogFragment fragment = new RatingDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.rating_bar)
    RatingBar ratingBar;

    @BindView(R.id.text_view_rating)
    TextView textView;

    @OnClick(R.id.rating_button)
    void rate() {
        int rating = (int) ratingBar.getRating();
        if (rating >= MIN_RATING_MARKET) {
            openAppInMarket();
        }
        UserPreferences.getInstance().setIsAppRated();
        dismissAllowingStateLoss();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        String text = UserPreferences.getInstance().getPopupText();
        if (text != null) {
            textView.setText(text);
        }
        return view;
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