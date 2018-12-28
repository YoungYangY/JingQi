package com.example.yang.jingqi;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.yang.jingqi.calendar.CalendarAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.spinner)
    BetterSpinner betterSpinner;
    @BindView(R.id.spinner2)
    BetterSpinner betterSpinner2;
    @BindView(R.id.spinner3)
    TextView last_day;
    @BindView(R.id.jisuan_before)
    LinearLayout jisuan_before;
    @BindView(R.id.jisuan_after)
    LinearLayout jisuan_after;
    @BindView(R.id.flipper)
    ViewFlipper flipper;
    @BindView(R.id.smalltool_pailuanqi_canlendar_text)
    LinearLayout smalltool_pailuanqi_canlendar_text;
    @BindView(R.id.smalltool_pailuanqi_tag)
    ImageView smalltool_pailuanqi_tag;
    @BindView(R.id.smalltool_pailuanqi_left)
    ImageView smalltool_pailuanqi_left;
    @BindView(R.id.smalltool_pailuanqi_right)
    ImageView smalltool_pailuanqi_right;
    @BindView(R.id.smalltool_pailuanqi_text)
    TextView smalltool_pailuanqi_text;

    private GestureDetector gestureDetector;
    private CalendarAdapter calV;
    private UnscrollableGridView gridView = null;
    private int jumpMonth = 0; // 每次滑动，增加或减去一个月,默认为0（即显示当前月）
    private int jumpYear = 0; // 滑动跨越一年，则增加或者减去一年,默认为0(即当前年)
    private int year_c = 0;
    private int month_c = 0;
    private int day_c = 0;
    private String currentDate = "";
    private boolean canLeft = true, canRight = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, new String[]{"25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38"});

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, new String[]{"5", "6", "7", "8"});

        betterSpinner.setAdapter(adapter2);
        betterSpinner2.setAdapter(adapter);
        gestureDetector = new GestureDetector(this, new MyGestureListener());

    }


    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            int gvFlag = 0; // 每次添加gridview到viewflipper中时给的标记
            if (e1.getX() - e2.getX() > 120 && canLeft && e1.getY() - e2.getY() < 50) {
                // 像左滑动
                enterNextMonth(gvFlag);
                return true;
            } else if (e1.getX() - e2.getX() < -120 && canRight && e1.getY() - e2.getY() < 50) {
                // 向右滑动
                enterPrevMonth(gvFlag);
                return true;
            }
            return false;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        gestureDetector.onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }


    @OnClick({R.id.smalltool_but, R.id.smalltool_but2, R.id.spinner3, R.id.smalltool_pailuanqi_left, R.id.smalltool_pailuanqi_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.smalltool_but:
                if ("".equals(betterSpinner.getText().toString())) {
                    Toast.makeText(this, "输入不能为空哦", Toast.LENGTH_SHORT).show();
                    break;
                } else if ("".equals(betterSpinner2.getText().toString())) {
                    Toast.makeText(this, "输入不能为空哦", Toast.LENGTH_SHORT).show();
                    break;
                } else if ("".equals(last_day.getText().toString())) {
                    Toast.makeText(this, "输入不能为空哦", Toast.LENGTH_SHORT).show();
                    break;
                }
                dosome(betterSpinner2.getText().toString(), betterSpinner.getText().toString(), last_day.getText().toString());
                jisuan_before.setVisibility(View.GONE);
                jisuan_after.setVisibility(View.VISIBLE);
                smalltool_pailuanqi_canlendar_text.setVisibility(View.VISIBLE);
                smalltool_pailuanqi_tag.setVisibility(View.VISIBLE);
                break;
            case R.id.smalltool_but2:
                betterSpinner.setText("");
                betterSpinner2.setText("");
                last_day.setText("");
                jumpMonth = 0;
                jumpYear = 0;
                smalltool_pailuanqi_right.setVisibility(View.VISIBLE);
                smalltool_pailuanqi_left.setVisibility(View.INVISIBLE);
                canLeft = true;
                canRight = false;
                smalltool_pailuanqi_canlendar_text.setVisibility(View.GONE);
                jisuan_after.setVisibility(View.GONE);
                jisuan_before.setVisibility(View.VISIBLE);
                smalltool_pailuanqi_tag.setVisibility(View.GONE);
                break;
            case R.id.spinner3:
                showTimeSelectorDialog();
                break;
            case R.id.smalltool_pailuanqi_left:
                enterPrevMonth(0);
                break;
            case R.id.smalltool_pailuanqi_right:
                enterNextMonth(0);
                break;
        }
    }

    private void showTimeSelectorDialog() {
        TimeSelector timeSelector = new TimeSelector(this, new TimeSelector.ResultHandler() {
            @Override
            public void handle(String time) {
                last_day.setText(time);
            }
        }, "2000-01-01 00:00", "2020-01-01 00:00");

        timeSelector.setIsLoop(false);
        timeSelector.setMode(TimeSelector.MODE.YMD);
        timeSelector.show();
    }

    private String dosome(String spinner, String spinner2, String currentDate) {

//        Date date = new Date();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
//        currentDate = sdf.format(date); // 当期日期
//        currentDate = "2017-5-2"; // 当期日期
        year_c = Integer.parseInt(currentDate.split("-")[0]);
        month_c = Integer.parseInt(currentDate.split("-")[1]);
        day_c = Integer.parseInt(currentDate.split("-")[2]);

        flipper = (ViewFlipper) findViewById(R.id.flipper);
        flipper.removeAllViews();
        calV = new CalendarAdapter(this, getResources(), jumpMonth, jumpYear, year_c, month_c, day_c, spinner, spinner2, true);
        addGridView();
        gridView.setAdapter(calV);
        flipper.addView(gridView, 0);
        addTextToTopTextView(smalltool_pailuanqi_text);

        return null;
    }


    private void addGridView() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT);
        // 取得屏幕的宽度和高度
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int Width = display.getWidth();
        int Height = display.getHeight();

        gridView = new UnscrollableGridView(this);
        gridView.setNumColumns(7);
        gridView.setColumnWidth(40);
        // gridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
        if (Width == 720 && Height == 1280) {
            gridView.setColumnWidth(40);
        }
        gridView.setGravity(Gravity.CENTER_VERTICAL);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        // 去除gridView边框
        gridView.setVerticalSpacing(1);
        gridView.setHorizontalSpacing(1);
        gridView.setOnTouchListener(new View.OnTouchListener() {
            // 将gridview中的触摸事件回传给gestureDetector

            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                return gestureDetector.onTouchEvent(event);
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                // TODO Auto-generated method stub
                // 点击任何一个item，得到这个item的日期(排除点击的是周日到周六(点击不响应))
                int startPosition = calV.getStartPositon();
                int endPosition = calV.getEndPosition();
                if (startPosition <= position + 7 && position <= endPosition - 7) {
                    String scheduleDay = calV.getDateByClickItem(position).split("\\.")[0]; // 这一天的阳历
                    // String scheduleLunarDay =
                    // calV.getDateByClickItem(position).split("\\.")[1];
                    // //这一天的阴历
                    String scheduleYear = calV.getShowYear();
                    String scheduleMonth = calV.getShowMonth();
//                    Toast.makeText(baseActivity, scheduleYear + "-" + scheduleMonth + "-" + scheduleDay, Toast.LENGTH_SHORT).show();
                    // Toast.makeText(CalendarActivity.this, "点击了该条目",
                    // Toast.LENGTH_SHORT).show();
                }
            }
        });
        gridView.setLayoutParams(params);
    }

    /**
     * 移动到下一个月
     *
     * @param gvFlag
     */
    private void enterNextMonth(int gvFlag) {
        addGridView(); // 添加一个gridView
        jumpMonth++; // 下一个月

        calV = new CalendarAdapter(this, this.getResources(), jumpMonth, jumpYear, year_c, month_c, day_c, betterSpinner2.getText().toString(), betterSpinner.getText().toString(), false);
        gridView.setAdapter(calV);
        addTextToTopTextView(smalltool_pailuanqi_text); // 移动到下一月后，将当月显示在头标题中
        gvFlag++;
        flipper.addView(gridView, gvFlag);
        flipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_in));
        flipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_out));
        flipper.showNext();
        flipper.removeViewAt(0);

        smalltool_pailuanqi_left.setVisibility(View.VISIBLE);
        smalltool_pailuanqi_right.setVisibility(View.INVISIBLE);
        canLeft = false;
        canRight = true;
    }

    /**
     * 移动到上一个月
     *
     * @param gvFlag
     */
    private void enterPrevMonth(int gvFlag) {
        addGridView(); // 添加一个gridView
        jumpMonth--; // 上一个月

        calV = new CalendarAdapter(this, this.getResources(), jumpMonth, jumpYear, year_c, month_c, day_c, betterSpinner2.getText().toString(), betterSpinner.getText().toString(), true);
        gridView.setAdapter(calV);
        gvFlag++;
        addTextToTopTextView(smalltool_pailuanqi_text); // 移动到上一月后，将当月显示在头标题中
        flipper.addView(gridView, gvFlag);

        flipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_in));
        flipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_out));
        flipper.showPrevious();
        flipper.removeViewAt(0);

        smalltool_pailuanqi_right.setVisibility(View.VISIBLE);
        smalltool_pailuanqi_left.setVisibility(View.INVISIBLE);
        canLeft = true;
        canRight = false;
    }

    public void addTextToTopTextView(TextView view) {
        StringBuffer textDate = new StringBuffer();
        // draw = getResources().getDrawable(R.drawable.top_day);
        // view.setBackgroundDrawable(draw);
        textDate.append(calV.getShowYear()).append("年").append(calV.getShowMonth()).append("月").append("\t");
        view.setText(textDate);
    }

}
