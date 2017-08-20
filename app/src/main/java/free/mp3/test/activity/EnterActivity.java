package free.mp3.test.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.IntentCompat;
import android.support.v4.view.ViewPager;
import android.widget.Button;

import butterknife.BindView;
import butterknife.OnClick;
import free.mp3.test.R;
import free.mp3.test.adapter.MusicPageAdapter;
import free.mp3.test.model.networking.DataBody;
import free.mp3.test.util.preferences.UserPreferences;

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
            if (UserPreferences.getInstance().getAdNetTutorial() != DataBody.NO) {
                showAd();
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
}