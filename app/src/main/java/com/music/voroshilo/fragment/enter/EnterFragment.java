package com.music.voroshilo.fragment.enter;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.music.voroshilo.R;

import butterknife.BindView;

public class EnterFragment extends Fragment {
    public static final String POSITION = "position";

    @BindView(R.id.enter_text)
    TextView enterTextView;

    @BindView(R.id.container)
    RelativeLayout containerLayout;

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
                setUpFragment(getString(R.string.enter_text_first), null);
                break;
            case 1:
                setUpFragment(getString(R.string.enter_text_second), null);
                break;
            case 2:
                setUpFragment(getString(R.string.enter_text_third), null);
                break;
            default:
                break;
        }

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @SuppressWarnings("deprecation")
    public void setUpFragment(String text, Drawable drawable) {
        enterTextView.setText(text);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            containerLayout.setBackground(drawable);
        } else {
            containerLayout.setBackgroundDrawable(drawable);
        }
    }
}
