package com.vivek.wo.common.view.wheelpicker.widgets;

import android.content.Context;
import android.util.AttributeSet;

import com.vivek.wo.common.view.wheelpicker.WheelPicker;

import java.util.ArrayList;
import java.util.List;

/**
 * 月份选择器
 * <p>
 * Picker for Months
 *
 * @author AigeStudio 2016-07-12
 * @version 1
 */
public class WheelMonthPicker extends WheelPicker {
    private int mMonthStart = 1, mMonthEnd = 12;
    private int mSelectedMonth;

    public WheelMonthPicker(Context context) {
        this(context, null);
    }

    public WheelMonthPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        updateMonth();
    }

    private void updateMonth() {
        List<Integer> data = new ArrayList<>();
        for (int i = mMonthStart; i <= mMonthEnd; i++) {
            data.add(i);
        }
        super.setData(data);
    }

    private void updateSelectedMonth() {
        setSelectedItemPosition(mSelectedMonth - mMonthStart);
    }

    @Override
    public void setData(List data) {
        throw new UnsupportedOperationException("You can not invoke setData in WheelMonthPicker");
    }

    /**
     * 获取设置的月份
     *
     * @return
     */
    public int getSelectedMonth() {
        return mSelectedMonth;
    }

    /**
     * 设置月份
     *
     * @param month
     */
    public void setSelectedMonth(int month) {
        mSelectedMonth = month;
        updateSelectedMonth();
    }

    /**
     * 设置开始的月份
     *
     * @param monthStart
     */
    public void setMonthStart(int monthStart) {
        if (mMonthStart == monthStart) {
            return;
        }
        mMonthStart = monthStart;
        mSelectedMonth = getCurrentMonth();
        if (mSelectedMonth < mMonthStart) {
            mSelectedMonth = mMonthStart;
        }
        updateMonth();
        updateSelectedMonth();
    }

    /**
     * 获取当前月份
     *
     * @return
     */
    public int getCurrentMonth() {
        return Integer.valueOf(String.valueOf(getData().get(getCurrentItemPosition())));
    }
}