package com.vivek.wo.common.view.wheelpicker.widgets;

import android.content.Context;
import android.util.AttributeSet;

import com.vivek.wo.common.view.wheelpicker.WheelPicker;

import java.util.ArrayList;
import java.util.List;

/**
 * 小时选择器
 * <p>
 * Picker for Hour
 *
 * @author AigeStudio 2016-07-12
 * @version 1
 */
public class WheelHourPicker extends WheelPicker {
    private int mHourStart = 0;
    private int mSelectedHour;

    public WheelHourPicker(Context context) {
        this(context, null);
    }

    public WheelHourPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        updateHour();
    }

    private void updateHour() {
        List<Integer> data = new ArrayList<>();
        for (int i = mHourStart; i <= 23; i++) {
            data.add(i);
        }
        super.setData(data);
    }

    private void updateSelectedHour() {
        setSelectedItemPosition(mSelectedHour - mHourStart);
    }

    @Override
    public void setData(List data) {
        throw new UnsupportedOperationException("You can not invoke setData in WheelMonthPicker");
    }

    /**
     * 设置开始的小时
     *
     * @param hourStart
     */
    public void setHourStart(int hourStart) {
        mHourStart = hourStart;
        mSelectedHour = getCurrentHour();
        if (mSelectedHour < mHourStart) {
            mSelectedHour = mHourStart;
        }
        updateHour();
        updateSelectedHour();
    }

    /**
     * 获取设置的小时
     *
     * @return
     */
    public int getSelectedHour() {
        return mSelectedHour;
    }

    /**
     * 设置小时
     *
     * @param hour
     */
    public void setSelectedHour(int hour) {
        mSelectedHour = hour;
        updateSelectedHour();
    }

    /**
     * 获取当前选择的小时
     *
     * @return
     */
    public int getCurrentHour() {
        return Integer.valueOf(String.valueOf(getData().get(getCurrentItemPosition())));
    }
}