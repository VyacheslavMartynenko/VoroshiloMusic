package com.music.voroshilo.fragment.enter;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.music.voroshilo.R;
import com.music.voroshilo.fragment.BaseFragment;

import butterknife.BindView;

public class EnterFragment extends BaseFragment {
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
        View view = super.onCreateView(inflater, container, savedInstanceState);
        int position = getArguments().getInt(POSITION);

        switch (position) {
            case 0:
                setUpFragment(getString(R.string.enter_text_first), Color.WHITE);
                break;
            case 1:
                setUpFragment(getString(R.string.enter_text_second), Color.BLUE);
                break;
            case 2:
                setUpFragment(getString(R.string.enter_text_third), Color.GRAY);
                break;
            default:
                break;
        }

        return view;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_enter;
    }

    public void setUpFragment(String text, int color) {
        enterTextView.setText(text);
        containerLayout.setBackgroundColor(color);
    }
}
