package com.music.voroshilo.fragment.enter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.music.voroshilo.R;

import butterknife.BindView;

public class EnterFragment extends Fragment {
    public static final String POSITION = "position";

    @BindView(R.id.enter_button)
    Button enterButton;

    @BindView(R.id.enter_text)
    TextView enterTextView;

    public static EnterFragment newInstance(int position) {

        Bundle args = new Bundle();
        args.putInt(POSITION, position);

        EnterFragment fragment = new EnterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int position = getArguments().getInt(POSITION);

        switch (position) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            default:
                break;
        }

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void setUpFragment() {
        enterButton.setText();
        enterTextView.setText();
    }
}
