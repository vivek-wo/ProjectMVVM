package com.vivek.wo.common.dialog;

import android.content.Context;
import android.view.Gravity;

import androidx.annotation.NonNull;

import com.vivek.wo.common.R;

public class BottomSheetDialog extends BaseDialog {

    public BottomSheetDialog(@NonNull Context context) {
        super(context);
        setWindowGravity(Gravity.BOTTOM);
        setWindowAnimations(R.style.Bottom_Sheet_Animations_style);
    }
}
