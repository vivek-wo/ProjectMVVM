package com.vivek.wo.common.view.wheelpicker.widgets;


import android.content.Context;
import android.util.AttributeSet;

import com.vivek.wo.common.view.wheelpicker.WheelPicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 日期选择器
 * <p>
 * Picker for Day
 *
 * @author AigeStudio 2016-07-12
 * @version 1
 */
public class WheelDayPicker extends WheelPicker {
    private Calendar mCalendar;

    private int mDayStart = 1, mDayEnd;
    private int mYear, mMonth;
    private int mSelectedDay;

    public WheelDayPicker(Context context) {
        this(context, null);
    }

    public WheelDayPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        mCalendar = Calendar.getInstance();
        mYear = mCalendar.get(Calendar.YEAR);
        mMonth = mCalendar.get(Calendar.MONTH);
        updateDays();
    }

    private void updateDays() {
        mCalendar.set(Calendar.YEAR, mYear);
        mCalendar.set(Calendar.MONTH, mMonth);
        int days = mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        mDayEnd = days;
        //开始日期大于结束日期为参数异常，正常时间下不会出现
        if (mDayStart > mDayEnd) {
            throw new IllegalArgumentException("Day Start parameter error in current Year and Month");
        }
        List<Integer> data = new ArrayList<>();
        for (int i = mDayStart; i <= days; i++) {
            data.add(i);
        }
        super.setData(data);
    }

    private void updateSelectedDay() {
        setSelectedItemPosition(mSelectedDay - mDayStart);
    }

    @Override
    public void setData(List data) {
        throw new UnsupportedOperationException("You can not invoke setData in WheelDayPicker");
    }

    /**
     * 获取设置的日期
     *
     * @return
     */
    public int getSelectedDay() {
        return mSelectedDay;
    }

    /**
     * 设置日期
     *
     * @param day
     */
    public void setSelectedDay(int day) {
        mSelectedDay = day;
        updateSelectedDay();
    }

    /**
     * 获取当前的日期
     *
     * @return
     */
    public int getCurrentDay() {
        return Integer.valueOf(String.valueOf(getData().get(getCurrentItemPosition())));
    }

    /**
     * 设置年月
     *
     * @param year
     * @param month
     */
    public void setYearAndMonth(int year, int month) {
        setYearAndMonth(year, month, 1);
    }

    /**
     * 设置年月和开始的日期
     *
     * @param year
     * @param month
     * @param dayStart
     */
    public void setYearAndMonth(int year, int month, int dayStart) {
        mYear = year;
        mMonth = month - 1;
        mDayStart = dayStart;
        mSelectedDay = getCurrentDay();
        //前一次选择的日期小于开始日期
        if (mSelectedDay < mDayStart) {
            mSelectedDay = mDayStart;
        }
        updateDays();
        //前一次选择的日期大于结束日期
        if (mSelectedDay > mDayEnd) {
            mSelectedDay = mDayEnd;
        }
        updateSelectedDay();
    }

    /**
     * 获取设置的年
     *
     * @return
     */
    public int getYear() {
        return mYear;
    }

    /**
     * 设置年
     *
     * @param year
     */
    public void setYear(int year) {
        setYearAndMonth(year, mMonth + 1);
    }

    /**
     * 获取设置的月
     *
     * @return
     */
    public int getMonth() {
        return mMonth;
    }

    /**
     * 设置月
     *
     * @param month
     */
    public void setMonth(int month) {
        setYearAndMonth(mYear, month);
    }
}