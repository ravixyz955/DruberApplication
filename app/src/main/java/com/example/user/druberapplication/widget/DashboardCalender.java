package com.example.user.druberapplication.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.user.druberapplication.R;
import com.example.user.druberapplication.helper.PreferenceHelper;
import com.example.user.druberapplication.helper.TimeUtil;
import com.example.user.druberapplication.listener.CalenderDayClickListener;
import com.example.user.druberapplication.network.model.DayContainerModel;
import com.example.user.druberapplication.network.model.Event;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class DashboardCalender extends LinearLayout implements View.OnClickListener {

    private static final String TAG = "CalenderEvent";

    private static final String[] MONTH_NAMES = {"January", "February", "March", "April",
            "May", "June", "July", "August",
            "September", "October", "November", "December"};
    private static final String TEXT_EVENT = " TEXT";
    private static final String COLOR_EVENT = " COLOR";
    private static final String CUSTOM_GREY = "#a0a0a0";
    // Default component
    private static final int DEFAULT_BACKGROUND_COLOR = Color.parseColor("#F5F5F5");
    private static final int DEFAULT_SELECTED_DAY_COLOR = Color.BLACK;
    private static final int DEFAULT_CURRENT_MONTH_DAY_COLOR = Color.parseColor("#303F9F");
    private static final int DEFAULT_OFF_MONTH_DAY_COLOR = Color.parseColor(CUSTOM_GREY);
    private static final int DEFAULT_SELECTOR_COLOR = Color.parseColor("#D81B60");
    private static final int DEFAULT_TEXT_COLOR = Color.parseColor("#808080");
    private LinearLayout[] weeks;
    private TextView[] days;
    private LinearLayout[] daysContainer;
    private TextView[] eventsTextViewList;
    private TextView textViewMonthName;
    private Calendar mCalendar;
    private Context mContext;
    private List<DayContainerModel> dayContainerList;
    private String mToday;
    //selected color element
    private TextView mSelectedTexView;
    private LinearLayout mSelectedLinearLayout;
    private int mPreviousColor;
    private PreferenceHelper preferenceHelper;
    private CalenderDayClickListener mCalenderDayClickListener;
    // View component
    private View mainView;
    private int mBackgroundColor;
    private int mSelectorColor;
    private int mSelectedDayTextColor;
    private int mCurrentMonthDayColor;
    private int mOffMonthDayColor;
    private int mMonthTextColor;
    private int mWeekNameColor;
    private Drawable nextButtonDrawable;
    private Drawable prevButtonDrawable;


    public DashboardCalender(Context context) {
        super(context);
    }

    public DashboardCalender(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;

        initAttrs(attrs);

        initView(context);

        preferenceHelper = PreferenceHelper.init(context);
    }

    public static double round(float d) {
        DecimalFormat df = new DecimalFormat("#.#");
        df.setRoundingMode(RoundingMode.FLOOR);
        return Double.valueOf(df.format(d));
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        LinearLayout weekOneLayout = findViewById(R.id.calendar_week_1);
        LinearLayout weekTwoLayout = findViewById(R.id.calendar_week_2);
        LinearLayout weekThreeLayout = findViewById(R.id.calendar_week_3);
        LinearLayout weekFourLayout = findViewById(R.id.calendar_week_4);
        LinearLayout weekFiveLayout = findViewById(R.id.calendar_week_5);
        LinearLayout weekSixLayout = findViewById(R.id.calendar_week_6);

        Button buttonPrevious = findViewById(R.id.button_previous);
        Button buttonNext = findViewById(R.id.button_next);

        textViewMonthName = findViewById(R.id.text_view_month_name);

        // week container
        LinearLayout linearLayoutWeak = findViewById(R.id.linear_layout_week);

        weeks = new LinearLayout[6];
        days = new TextView[6 * 7];
        eventsTextViewList = new TextView[6 * 7];
        daysContainer = new LinearLayout[6 * 7];

        weeks[0] = weekOneLayout;
        weeks[1] = weekTwoLayout;
        weeks[2] = weekThreeLayout;
        weeks[3] = weekFourLayout;
        weeks[4] = weekFiveLayout;
        weeks[5] = weekSixLayout;

        mCalendar = Calendar.getInstance();


        mToday = mCalendar.get(Calendar.DATE) + " " + MONTH_NAMES[mCalendar.get(Calendar.MONTH)] + " " + mCalendar.get(Calendar.YEAR);

        Log.d(TAG, "Today, " + mToday);

        buttonPrevious.setOnClickListener(this);
        buttonNext.setOnClickListener(this);

        // set view element
        mainView.setBackgroundColor(mBackgroundColor);

        textViewMonthName.setTextColor(mMonthTextColor);

        for (int i = 0; i < linearLayoutWeak.getChildCount(); i++) {
            TextView textViewWeek = (TextView) linearLayoutWeak.getChildAt(i);
            if (i != 0)
                textViewWeek.setTextColor(Color.parseColor("#303F9F"));
            else
                textViewWeek.setTextColor(Color.parseColor("#f92b19"));
        }

        if (nextButtonDrawable != null) {
            buttonNext.setBackground(nextButtonDrawable);
        }
        if (prevButtonDrawable != null) {
            buttonPrevious.setBackground(prevButtonDrawable);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button_next) {
            gotoNextMonth();
        } else if (view.getId() == R.id.button_previous) {
            gotoPreviousMonth();
        }
    }


    @SuppressLint("SetTextI18n")
    private void initCalender(int selectedYear, int selectedMonth) {

        dayContainerList = new ArrayList<>();

        mCalendar.set(selectedYear, selectedMonth, 1);

        textViewMonthName.setText(MONTH_NAMES[selectedMonth] + ", " + selectedYear);

        int daysInCurrentMonth = mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        int index = 0;

        int firstDayOfCurrentMonth = mCalendar.get(Calendar.DAY_OF_WEEK);

        int previousLeftMonthDays = firstDayOfCurrentMonth - 1;

        int nextMonthDays = 42 - (daysInCurrentMonth + previousLeftMonthDays);

        // now set previous date

        if (previousLeftMonthDays != 0) {
            int prevMonth;
            int prevYear;
            if (selectedMonth > 0) {
                mCalendar.set(selectedYear, selectedMonth - 1, 1);
                prevMonth = selectedMonth - 1;
                prevYear = selectedYear;
            } else {
                mCalendar.set(selectedYear - 1, 11, 1);
                prevMonth = 11;
                prevYear = selectedYear - 1;
            }

            int previousMonthTotalDays = mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);

            mCalendar.set(prevYear, prevMonth, (previousMonthTotalDays - previousLeftMonthDays));
            int prevCurrentDay = mCalendar.get(Calendar.DATE);
            prevCurrentDay++;
            for (int i = 0; i < previousLeftMonthDays; i++) {
                days[index].setText(String.valueOf(prevCurrentDay));
                days[index].setTextColor(mOffMonthDayColor);

                final DayContainerModel model = new DayContainerModel();
                model.setIndex(index);
                model.setYear(prevYear);
                model.setMonthNumber(prevMonth);
                model.setMonth(MONTH_NAMES[prevMonth]);
                model.setDay(prevCurrentDay);
                String date = model.getDay() + " " + model.getMonth() + " " + model.getYear();
                model.setDate(date);
                model.setTimeInMillisecond(TimeUtil.getTimestamp(date));

                Event event = getEvent(model.getTimeInMillisecond());
                model.setEvent(event);
                model.setHaveEvent(event != null);

                if (model.isHaveEvent()) {
                    eventsTextViewList[index].setText(Objects.requireNonNull(event).getEventText());
                    eventsTextViewList[index].setVisibility(VISIBLE);
                    eventsTextViewList[index].setTextColor(event.getEventColor());
                } else {
                    eventsTextViewList[index].setVisibility(INVISIBLE);
                }

                dayContainerList.add(model);

                final int finalIndex = index;
                days[index].setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (mSelectedTexView != null) {
                            mSelectedLinearLayout.setBackgroundResource(android.R.color.transparent);
                            mSelectedTexView.setTextColor(mPreviousColor);
                        }
                        mPreviousColor = mOffMonthDayColor;
                        mSelectedTexView = days[finalIndex];
                        daysContainer[finalIndex].setBackgroundResource(R.drawable.drawable_rectangular);

                        Drawable backgroundDrawable = DrawableCompat.wrap(daysContainer[finalIndex].getBackground()).mutate();
                        DrawableCompat.setTint(backgroundDrawable, mSelectorColor);

                        mSelectedLinearLayout = daysContainer[finalIndex];
                        days[finalIndex].setTextColor(mSelectedDayTextColor);

                        if (mCalenderDayClickListener != null) {
                            mCalenderDayClickListener.onGetDay(model);
                        }
                    }
                });

                prevCurrentDay++;
                index++;
            }
        }

        // now set current month date

        mCalendar.set(selectedYear, selectedMonth, 1);

        for (int i = 1; i <= daysInCurrentMonth; i++) {
            days[index].setText(String.valueOf(i));
            mCalendar.set(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), i);
            if (mCalendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                days[index].setTextColor(Color.RED);
            } else {
                days[index].setTextColor(mCurrentMonthDayColor);
            }
            final DayContainerModel model = new DayContainerModel();
            model.setIndex(index);
            model.setYear(selectedYear);
            model.setMonthNumber(selectedMonth);
            model.setMonth(MONTH_NAMES[selectedMonth]);
            model.setDay(i);
            String date = model.getDay() + " " + model.getMonth() + " " + model.getYear();
            model.setDate(date);
            model.setTimeInMillisecond(TimeUtil.getTimestamp(date));

            Event event = getEvent(model.getTimeInMillisecond());
            model.setEvent(event);
            model.setHaveEvent(event != null);

            if (mToday.equals(model.getDate())) {
                if (mSelectedTexView != null) {
                    mSelectedLinearLayout.setBackgroundResource(android.R.color.transparent);
                    mSelectedTexView.setTextColor(mPreviousColor);
                }
                mPreviousColor = mCurrentMonthDayColor;
                mSelectedTexView = days[index];

                daysContainer[index].setBackgroundResource(R.drawable.drawable_rectangular);

                Drawable backgroundDrawable = DrawableCompat.wrap(daysContainer[index].getBackground()).mutate();
                DrawableCompat.setTint(backgroundDrawable, mSelectorColor);

                mSelectedLinearLayout = daysContainer[index];
                days[index].setTextColor(mSelectedDayTextColor);
            }

            if (model.isHaveEvent()) {
                eventsTextViewList[index].setText(Objects.requireNonNull(event).getEventText());
                eventsTextViewList[index].setVisibility(VISIBLE);
                eventsTextViewList[index].setTextColor(event.getEventColor());
                eventsTextViewList[index].invalidate();
            } else {
                eventsTextViewList[index].setVisibility(INVISIBLE);
                eventsTextViewList[index].invalidate();
            }

            dayContainerList.add(model);

            final int finalIndex = index;
            days[index].setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "Hello");

                    if (mSelectedTexView != null) {
                        mSelectedLinearLayout.setBackgroundResource(android.R.color.transparent);
                        mSelectedTexView.setTextColor(mPreviousColor);
                    }
                    mPreviousColor = mCurrentMonthDayColor;
                    mSelectedTexView = days[finalIndex];
                    daysContainer[finalIndex].setBackgroundResource(R.drawable.drawable_rectangular);

                    Drawable backgroundDrawable = DrawableCompat.wrap(daysContainer[finalIndex].getBackground()).mutate();
                    DrawableCompat.setTint(backgroundDrawable, mSelectorColor);

                    mSelectedLinearLayout = daysContainer[finalIndex];
                    days[finalIndex].setTextColor(mSelectedDayTextColor);

                    if (mCalenderDayClickListener != null) {
                        mCalenderDayClickListener.onGetDay(model);
                    }
                }
            });

            index++;
        }

        int nextYear;
        int nextMonth;
        if (selectedMonth < 11) {
            nextYear = selectedYear;
            nextMonth = selectedMonth + 1;
        } else {
            nextYear = selectedYear + 1;
            nextMonth = 0;
        }

        // now set rest of day
        for (int i = 1; i <= nextMonthDays; i++) {
            days[index].setText(String.valueOf(i));
            days[index].setTextColor(mOffMonthDayColor);

            final DayContainerModel model = new DayContainerModel();
            model.setIndex(index);
            model.setYear(nextYear);
            model.setMonthNumber(nextMonth);
            model.setMonth(MONTH_NAMES[nextMonth]);
            model.setDay(i);
            String date = model.getDay() + " " + model.getMonth() + " " + model.getYear();
            model.setDate(date);
            model.setTimeInMillisecond(TimeUtil.getTimestamp(date));

            Event event = getEvent(model.getTimeInMillisecond());
            model.setEvent(event);
            model.setHaveEvent(event != null);

            if (model.isHaveEvent()) {
                eventsTextViewList[index].setText(event.getEventText());
                eventsTextViewList[index].setVisibility(VISIBLE);
                eventsTextViewList[index].setTextColor(event.getEventColor());
                eventsTextViewList[index].invalidate();
            } else {
                eventsTextViewList[index].setVisibility(INVISIBLE);
                eventsTextViewList[index].invalidate();
            }

            dayContainerList.add(model);

            final int finalIndex = index;
            days[index].setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "Hello");
                    if (mSelectedTexView != null) {
                        mSelectedTexView.setTextColor(mPreviousColor);
                        mSelectedLinearLayout.setBackgroundResource(android.R.color.transparent);
                    }
                    mPreviousColor = mOffMonthDayColor;
                    mSelectedTexView = days[finalIndex];
                    mSelectedLinearLayout = daysContainer[finalIndex];
                    daysContainer[finalIndex].setBackgroundResource(R.drawable.drawable_rectangular);

                    Drawable backgroundDrawable = DrawableCompat.wrap(daysContainer[finalIndex].getBackground()).mutate();
                    DrawableCompat.setTint(backgroundDrawable, mSelectorColor);

                    days[finalIndex].setTextColor(mSelectedDayTextColor);

                    if (mCalenderDayClickListener != null) {
                        mCalenderDayClickListener.onGetDay(model);
                    }
                }
            });

            index++;
        }

    }

    private void gotoNextMonth() {
        int year = mCalendar.get(Calendar.YEAR);
        int month = mCalendar.get(Calendar.MONTH);

        if (month < 11) {
            mCalendar.set(year, month + 1, 1);
        } else {
            mCalendar.set(year + 1, 0, 1);
        }

        initCalender(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH));
    }

    private void gotoPreviousMonth() {
        int year = mCalendar.get(Calendar.YEAR);
        int month = mCalendar.get(Calendar.MONTH);

        if (month > 0) {
            mCalendar.set(year, month - 1, 1);
        } else {
            mCalendar.set(year - 1, 11, 1);
        }

        initCalender(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH));
    }

    private void initDaysInCalender(LayoutParams buttonParams, Context context) {
        int engDaysArrayCounter = 0;
        LayoutParams percentParams = new LayoutParams(132, 132);
        percentParams.setMargins(5, 20, 0, 5);

        for (int weekNumber = 0; weekNumber < 6; ++weekNumber) {
            for (int dayInWeek = 0; dayInWeek < 7; ++dayInWeek) {

                LinearLayout linearLayout = new LinearLayout(context);
                linearLayout.setLayoutParams(buttonParams);
                linearLayout.setOrientation(VERTICAL);
                linearLayout.setGravity(Gravity.CENTER);
                linearLayout.setBackground(getResources().getDrawable(R.drawable.drawable_rectangular));

                final TextView day = new TextView(context);
                day.setBackgroundColor(Color.TRANSPARENT);
                day.setTextSize(12);
                day.setPadding(0, 4, 12, 0);
                day.setGravity(Gravity.END);
                day.setSingleLine();

                TextView textView = new TextView(context);
                textView.setLayoutParams(percentParams);
                textView.setBackground(getResources().getDrawable(R.drawable.drawable_circle));
                textView.setTextColor(Color.BLACK);
                textView.setGravity(Gravity.CENTER);
                textView.setTextSize(12);
                textView.setPadding(12, 12, 12, 12);
                textView.setSingleLine();

                linearLayout.addView(day);
                linearLayout.addView(textView);

                days[engDaysArrayCounter] = day;
                eventsTextViewList[engDaysArrayCounter] = textView;

                weeks[weekNumber].addView(linearLayout);

                daysContainer[engDaysArrayCounter] = linearLayout;

                engDaysArrayCounter++;
            }
        }
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CalenderEvent);

        mBackgroundColor = typedArray.getColor(R.styleable.CalenderEvent_calender_background, DEFAULT_BACKGROUND_COLOR);

        mSelectorColor = typedArray.getColor(R.styleable.CalenderEvent_selector_color, DEFAULT_SELECTOR_COLOR);

        mSelectedDayTextColor = typedArray.getColor(R.styleable.CalenderEvent_selected_day_text_color, DEFAULT_SELECTED_DAY_COLOR);

        mCurrentMonthDayColor = typedArray.getColor(R.styleable.CalenderEvent_current_month_day_color, DEFAULT_CURRENT_MONTH_DAY_COLOR);

        mOffMonthDayColor = typedArray.getColor(R.styleable.CalenderEvent_off_month_day_color, DEFAULT_OFF_MONTH_DAY_COLOR);

        mMonthTextColor = typedArray.getColor(R.styleable.CalenderEvent_month_color, DEFAULT_TEXT_COLOR);

        mWeekNameColor = typedArray.getColor(R.styleable.CalenderEvent_week_name_color, DEFAULT_TEXT_COLOR);

        nextButtonDrawable = typedArray.getDrawable(R.styleable.CalenderEvent_next_icon);

        prevButtonDrawable = typedArray.getDrawable(R.styleable.CalenderEvent_previous_icon);

        typedArray.recycle();
    }

    private void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            mainView = inflater.inflate(R.layout.layout_dashboard_calender, this);
        }
    }

    private LayoutParams getdaysLayoutParams() {
        LayoutParams buttonParams = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        buttonParams.weight = 1;
        return buttonParams;
    }

    private DayContainerModel getDaysContainerModel(String date) {
        if (dayContainerList != null) {
            for (DayContainerModel model : dayContainerList) {
                if (model.getDate().equals(date)) {
                    return model;
                }
            }
        }
        return null;
    }

    private Event getEvent(long time) {
        String date = TimeUtil.getDate(time);

        String textKey = date + TEXT_EVENT;
        String colorKey = date + COLOR_EVENT;

        String eventText = preferenceHelper.readString(textKey);

        if (eventText != null) {
            int color = preferenceHelper.readInteger(colorKey);
            if (color == 0) {
                color = DEFAULT_TEXT_COLOR;
            }
            return new Event(time, eventText, color);
        }
        return null;
    }

    public void addEvent(Event event) {
        if (event == null) return;
        String date = TimeUtil.getDate(event.getTime());

        String textKey = date + TEXT_EVENT;
        String colorKey = date + COLOR_EVENT;

        preferenceHelper.write(textKey, event.getEventText());
        preferenceHelper.write(colorKey, event.getEventColor());

        DayContainerModel model = getDaysContainerModel(date);
        if (model != null) {
            model.setHaveEvent(true);
            model.setEvent(event);
        }
    }

    public void removeEvent(Event event) {
        if (event == null) return;
        String date = TimeUtil.getDate(event.getTime());
        String textKey = date + TEXT_EVENT;
        String colorKey = date + COLOR_EVENT;
        preferenceHelper.remove(textKey);
        preferenceHelper.remove(colorKey);

        DayContainerModel model = getDaysContainerModel(date);
        if (model != null) {
            model.setHaveEvent(false);
            model.setEvent(null);
        }
    }

    public void initCalderItemClickCallback(CalenderDayClickListener listener) {
        this.mCalenderDayClickListener = listener;
    }
}