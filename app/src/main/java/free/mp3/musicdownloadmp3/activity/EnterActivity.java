package free.mp3.musicdownloadmp3.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.IntentCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.Button;

import free.mp3.musicdownloadmp3.R;
import free.mp3.musicdownloadmp3.adapter.MusicPageAdapter;
import free.mp3.musicdownloadmp3.model.networking.DataBody;
import free.mp3.musicdownloadmp3.networking.request.SettingsRequest;
import free.mp3.musicdownloadmp3.util.preferences.UserPreferences;

import butterknife.BindView;
import butterknife.OnClick;

public class EnterActivity extends BaseActivity {

    @BindView(R.id.enter_view_pager)
    ViewPager enterViewPager;

    @BindView(R.id.enter_button)
    Button enterButton;

    @OnClick(R.id.enter_button)
    public void setCurrentPate() {
        int position = enterViewPager.getCurrentItem();
        switch (position) {
            case 0:
            case 1:
                enterViewPager.setCurrentItem(++position);
                break;
            case 2:
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            setEnterButtonText(position);
            showAd();
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestSettings();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);

        enterViewPager.setAdapter(new MusicPageAdapter(getSupportFragmentManager()));
        enterViewPager.addOnPageChangeListener(pageChangeListener);
        setEnterButtonText(enterViewPager.getCurrentItem());
    }

    private void setEnterButtonText(int position) {
        switch (position) {
            case 0:
            case 1:
                enterButton.setText(R.string.next);
                break;
            case 2:
                enterButton.setText(R.string.open_application);
                break;
            default:
                break;
        }
    }

    private void requestSettings() {
        new SettingsRequest().requestSettings(new SettingsRequest.SettingsCallback() {
            @Override
            public void onSuccess(DataBody data) {
                UserPreferences.getInstance().setBustStatus(data.getBurstStatus());
                UserPreferences.getInstance().setMarketUrl(data.getBurstUrl());
                UserPreferences.getInstance().setAdStatus(data.getNetType());
                UserPreferences.getInstance().setPopUpUrl(data.getPopupUrl());
                UserPreferences.getInstance().setPopUpStatus(data.getPopup());
            }

            @Override
            public void onError(Throwable throwable) {
                Log.e("onResponse: ", Log.getStackTraceString(throwable));
            }
        });
    }
}