package com.smart.himalaya.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.smart.himalaya.R;

public class ConfirmDialog extends Dialog {

    private View mGivUp;
    private View mCancelSub;
    private OnDialogActionClickListener mOnDialogActionClickListener = null;

    public ConfirmDialog(@NonNull Context context) {
        this(context,0);
    }

    public ConfirmDialog(@NonNull Context context, int themeResId) {
        this(context, true,null);
    }

    protected ConfirmDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_confirm);
        initView();
        initListener();
    }

    private void initListener() {
        mCancelSub.setOnClickListener(v -> {
            if (mOnDialogActionClickListener != null) {
                mOnDialogActionClickListener.onCancelSubClick();
                dismiss();
            }
        });
        mGivUp.setOnClickListener(v -> {
            if (mOnDialogActionClickListener != null) {
                mOnDialogActionClickListener.onGiveUpClick();
                dismiss();
            }
        });
    }

    private void initView() {
        mCancelSub = findViewById(R.id.dialog_cancel_sub_tv);
        mGivUp = findViewById(R.id.dialog_give_up_tv);
    }

    public void setOnDialogActionClickListener(OnDialogActionClickListener onDialogActionClickListener) {
        mOnDialogActionClickListener = onDialogActionClickListener;
    }

    public interface OnDialogActionClickListener{
        void onCancelSubClick();

        void onGiveUpClick();
    }
}
