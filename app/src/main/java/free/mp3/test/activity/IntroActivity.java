package free.mp3.test.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.lang.ref.WeakReference;

import free.mp3.test.R;
import free.mp3.test.model.networking.DataBody;
import free.mp3.test.networking.request.SettingsRequest;
import free.mp3.test.util.preferences.UserPreferences;

public class IntroActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        int isFirstLaunch = UserPreferences.getInstance().isFirstLaunch();
        SettingsRequest.requestSettings(isFirstLaunch, new SettingsRequestCallback(this));
    }

    private void showNewActivity() {
        Class<?> activityClass = UserPreferences.getInstance().getTutorialStatus() == DataBody.NO ? MainActivity.class : EnterActivity.class;
        Intent intent = new Intent(this, activityClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private static class SettingsRequestCallback implements SettingsRequest.SettingsCallback {
        WeakReference<IntroActivity> introActivityWeakReference;

        SettingsRequestCallback(IntroActivity introActivity) {
            this.introActivityWeakReference = new WeakReference<>(introActivity);
        }

        @Override
        public void onSuccess(DataBody data) {
            UserPreferences.getInstance().setAdNetType(data.getAdNetType());
            UserPreferences.getInstance().setAdNetTutorial(data.getAdNetTutorial());
            UserPreferences.getInstance().setAdNetDownload(data.getAdNetTutorial());
            UserPreferences.getInstance().setAdNetPlay(data.getAdNetPlay());
            UserPreferences.getInstance().setAdNetSearch(data.getAdNetSearch());
            UserPreferences.getInstance().setAdNetBanner(data.getAdNetBanner());
            UserPreferences.getInstance().setPopupStatus(data.getPopupStatus());
            UserPreferences.getInstance().setPopupText(data.getPopupText());
            UserPreferences.getInstance().setPopupUrl(data.getPopupUrl());
            UserPreferences.getInstance().setBurstStatus(data.getBurstStatus());
            UserPreferences.getInstance().setBurstText(data.getBurstText());
            UserPreferences.getInstance().setBurstUrl(data.getBurstUrl());
            UserPreferences.getInstance().setAppodealKey(data.getAppodealKey());
            UserPreferences.getInstance().setStartappKey(data.getStartappKey());
            UserPreferences.getInstance().setTutorialStatus(data.getTutorialStatus());
            UserPreferences.getInstance().setMusicUrl(data.getMusicUrl());

            UserPreferences.getInstance().setIsFirstLaunch();
            IntroActivity introActivity = introActivityWeakReference.get();
            if (introActivity != null) {
                introActivity.showNewActivity();
            }
        }

        @Override
        public void onError(Throwable throwable) {
            Log.e("onResponse: ", Log.getStackTraceString(throwable));

            IntroActivity introActivity = introActivityWeakReference.get();
            if (introActivity != null) {
                introActivity.showNewActivity();
            }
        }
    }
}
