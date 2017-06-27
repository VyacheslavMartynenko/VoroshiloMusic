package com.music.voroshilo.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;

import butterknife.ButterKnife;

abstract public class BaseDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder resultDialogBuilder = new AlertDialog.Builder(getActivity());
        View dialogView = View.inflate(getActivity(), getLayoutId(), null);

        ButterKnife.bind(this, dialogView);
        resultDialogBuilder.setView(dialogView);

        return resultDialogBuilder.create();
    }

    @LayoutRes
    protected abstract int getLayoutId();
}
