package com.vivek.wo.common.view.wheelpicker.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import com.vivek.wo.common.view.wheelpicker.WheelPicker;

import java.util.ArrayList;
import java.util.List;

/**
 * 年份选择器
 * <p>
 * Picker for Years
 *
 * @author AigeStudio 2016-07-12
 * @version 1
 */
public class WheelYearPicker extends WheelPicker {
    private int mYearStart = 1000, mYearEnd = 3000;
    private int mSelectedYear;

    public WheelYearPicker(Context context) {
        this(context, null);
    }

    public WheelYearPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        updateYears();
    }

    private void updateYears() {
        List<Integer> data = new ArrayList<>();
        for (int i = mYearStart; i <= mYearEnd; i++) {
            data.add(i);
        }
        super.setData(data);
    }

    private void updateSelectedYear() {
        setSelectedItemPosition(mSelectedYear - mYearStart);
    }

    public void setData(List data) {
        throw new UnsupportedOperationException("You can not invoke setData in WheelYearPicker");
    }

    /**
     * 设置开始和结束的年份
     *
     * @param start
     * @param end
     */
    public void setYearFrame(int start, int end) {
        if (start > end) {
            Log.d("WheelYearPicker", "Set YearFrame must start <= end");
            return;
        }
        mYearStart = start;
        mYearEnd = end;
        mSelectedYear = getCurrentYear();
        if (mSelectedYear < mYearStart || mSelectedYear > mYearEnd) {
            mSelectedYear = mYearStart;
        }
        updateYears();
        updateSelectedYear();
    }

    /**
     * 获取开始的年份
     *
     * @return
     */
    public int getYearStart() {
        return mYearStart;
    }

    /**
     * 设置开始年份
     *
     * @param start
     */
    public void setYearStart(int start) {
        mYearStart = start;
        mSelectedYear = getCurrentYear();
        if (mSelectedYear < mYearStart) {
            mSelectedYear = mYearStart;
        }
        updateYears();
        updateSelectedYear();
    }

    /**
     * 获取结束的年份
     *
     * @return
     */
    public int getYearEnd() {
        return mYearEnd;
    }

    /**
     * 设置结束的年份
     *
     * @param end
     */
    public void setYearEnd(int end) {
        mYearEnd = end;
        updateYears();
    }

    /**
     * 获取设置的年份
     *
     * @return
     */
    public int getSelectedYear() {
        return mSelectedYear;
    }

    /**
     * 设置年份
     *
     * @param year
     */
    public void setSelectedYear(int year) {
        if (year < mYearStart || year > mYearEnd) {
            Log.d("WheelYearPicker", "Set selectedYear must >= " + mYearStart + " and =< " + mYearEnd);
            return;
        }
        mSelectedYear = year;
        updateSelectedYear();
    }

    /**
     * 获取当前的年份
     *
     * @return
     */
    public int getCurrentYear() {
        return Integer.valueOf(String.valueOf(getData().get(getCurrentItemPosition())));
    }
}