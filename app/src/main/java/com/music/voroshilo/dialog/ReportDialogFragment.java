package com.music.voroshilo.dialog;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.music.voroshilo.R;
import com.music.voroshilo.application.MusicApplication;
import com.music.voroshilo.networking.ApiBuilder;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportDialogFragment extends BaseDialogFragment {

    @BindView(R.id.name_edit_text)
    TextInputEditText nameEditText;

    @BindView(R.id.message_edit_text)
    TextInputEditText messageEditText;

    @OnClick(R.id.report_button)
    void sendReport() {
        String fullName = nameEditText.getText().toString();
        String message = messageEditText.getText().toString();
        ApiBuilder.getApiService().reportSong(fullName, message).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    new Handler(Looper.getMainLooper())
                            .post(() -> Toast.makeText(MusicApplication.getInstance().getApplicationContext(),
                                    R.string.report_complete_message, Toast.LENGTH_SHORT).show());
                }
                ReportDialogFragment.this.dismissAllowingStateLoss();
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Log.e(ReportDialogFragment.this.getClass().getSimpleName(), Log.getStackTraceString(t));
                ReportDialogFragment.this.dismissAllowingStateLoss();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_fragment_report;
    }
}
