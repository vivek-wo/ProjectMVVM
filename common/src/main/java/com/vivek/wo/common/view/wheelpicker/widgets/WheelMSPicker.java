package com.vivek.wo.common.view.wheelpicker.widgets;

import android.content.Context;
import android.util.AttributeSet;

import com.vivek.wo.common.view.wheelpicker.WheelPicker;

import java.util.ArrayList;
import java.util.List;

/**
 * 分、秒选择器
 * <p>
 * Picker for Minute and Second
 *
 * @author AigeStudio 2016-07-12
 * @version 1
 */
public class WheelMSPicker extends WheelPicker {
    private int mMSStart = 0;
    private int mSelectedMS;

    public WheelMSPicker(Context context) {
        this(context, null);
    }

    public WheelMSPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        updateMS();
        mSelectedMS = 0;//默认分、秒
        updateSelectedMS();
    }

    private void updateMS() {
        List<Integer> data = new ArrayList<>();
        for (int i = mMSStart; i <= 59; i++) {
            data.add(i);
        }
        super.setData(data);
    }

    private void updateSelectedMS() {
        setSelectedItemPosition(mSelectedMS - mMSStart);
    }

    @Override
    public void setData(List data) {
        throw new UnsupportedOperationException("You can not invoke setData in WheelMonthPicker");
    }

    /**
     * 设置分或者秒的开始时间
     *
     * @param msStart
     */
    public void setMSStart(int msStart) {
        mMSStart = msStart;
        mSelectedMS = getCurrentMS();
        if (mSelectedMS < mMSStart) {
            mSelectedMS = mMSStart;
        }
        updateMS();
        updateSelectedMS();
    }

    /**
     * 获取设置的分或者秒
     *
     * @return
     */
    public int getSelectedMS() {
        return mSelectedMS;
    }

    /**
     * 设置选择的分或者秒
     *
     * @param ms
     */
    public void setSelectedMS(int ms) {
        mSelectedMS = ms;
        updateSelectedMS();
    }

    /**
     * 获取当前的分或者秒
     *
     * @return
     */
    public int getCurrentMS() {
        return Integer.valueOf(String.valueOf(getData().get(getCurrentItemPosition())));
    }
}