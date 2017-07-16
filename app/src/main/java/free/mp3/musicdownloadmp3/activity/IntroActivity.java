package free.mp3.musicdownloadmp3.activity;

import android.os.Bundle;
import android.widget.ProgressBar;

import butterknife.BindView;
import free.mp3.musicdownloadmp3.R;

public class IntroActivity extends BaseActivity {

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
    }
}
