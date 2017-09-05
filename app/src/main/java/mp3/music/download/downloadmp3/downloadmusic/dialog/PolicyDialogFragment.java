package mp3.music.download.downloadmp3.downloadmusic.dialog;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.util.Log;
import android.widget.Toast;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;

import butterknife.BindView;
import butterknife.OnClick;
import mp3.music.download.downloadmp3.downloadmusic.R;
import mp3.music.download.downloadmp3.downloadmusic.activity.BaseActivity;
import mp3.music.download.downloadmp3.downloadmusic.application.MusicApplication;
import mp3.music.download.downloadmp3.downloadmusic.networking.NetworkBuilder;
import mp3.music.download.downloadmp3.downloadmusic.util.DeviceInformationUtil;
import mp3.music.download.downloadmp3.downloadmusic.util.preferences.UserPreferences;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PolicyDialogFragment extends BaseDialogFragment {
    private static final String SONG_NAME = "music_name";
    private static final String VIDEO_ID = "video_id";

    private String songName;

    public static PolicyDialogFragment newInstance(String songName, String videoId) {

        Bundle args = new Bundle();
        args.putString(SONG_NAME, songName);
        args.putString(VIDEO_ID, videoId);

        PolicyDialogFragment fragment = new PolicyDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.name_edit_text)
    TextInputEditText nameEditText;

    @BindView(R.id.message_edit_text)
    TextInputEditText messageEditText;

    @OnClick(R.id.report_button)
    void sendReport() {
        String url = UserPreferences.getInstance().getReportUrl();
        String deviceId = DeviceInformationUtil.getDeviceUniqueID((BaseActivity) getActivity());
        String fullName = nameEditText.getText().toString();
        String message = messageEditText.getText().toString();
        String videoId = "";
        StringBuilder stringBuilderMessage = new StringBuilder();
        stringBuilderMessage.append(message);
        Bundle arguments = getArguments();
        if (arguments != null) {
            songName = arguments.getString(SONG_NAME, "");
            videoId = arguments.getString(VIDEO_ID, "");
            stringBuilderMessage.append(" ");
            stringBuilderMessage.append(songName);
        }
        NetworkBuilder.getApiService().reportSong(url, deviceId, fullName, stringBuilderMessage.toString(), videoId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        BaseActivity baseActivity = MusicApplication.getInstance().getCurrentActivity();
                        if (baseActivity != null && baseActivity.isVisible()) {
                            Toast.makeText(MusicApplication.getInstance().getApplicationContext(),
                                    R.string.report_complete_message, Toast.LENGTH_SHORT).show();
                            Answers.getInstance().logCustom(new CustomEvent("Report send").putCustomAttribute("Song name", songName));

                        }
                    });
                }
                PolicyDialogFragment.this.dismissAllowingStateLoss();
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Log.e(PolicyDialogFragment.this.getClass().getSimpleName(), Log.getStackTraceString(t));
                PolicyDialogFragment.this.dismissAllowingStateLoss();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_fragment_report;
    }
}
