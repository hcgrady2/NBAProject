package com.hcworld.nbalive.UI.activity;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.hcworld.nbalive.R;
import com.hcworld.nbalive.UI.base.BaseSwipeBackCompatActivity;
import com.hcworld.nbalive.event.DateStateChange;
import com.hcworld.nbalive.http.api.RequestCallback;
import com.hcworld.nbalive.http.api.tencent.TencentService;
import com.hcworld.nbalive.http.match.Matchs;
import com.hcworld.nbalive.utils.DateUtils;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateLongClickListener;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import org.greenrobot.eventbus.EventBus;
import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.List;

import butterknife.BindView;

/**
 * Created by hcw on 2019/1/15.
 * Copyright©hcw.All rights reserved.
 */

public class CalendarActivity extends BaseSwipeBackCompatActivity   implements OnDateSelectedListener, OnMonthChangedListener, OnDateLongClickListener {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("EEE, d MMM yyyy");
    private static final DateTimeFormatter NORMAL_FORMATTER = DateTimeFormatter.ofPattern("MM月dd日 E");
    private static final DateTimeFormatter QUERY_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");



    @BindView(R.id.calendarView)
    MaterialCalendarView widget;

    @BindView(R.id.textView)
    TextView textView;

    @BindView(R.id.common_toolbar)
    Toolbar toolbar;

    private String date,week;
    private String nowTime;


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_basic_calendar;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void initViewsAndEvents() {
        widget.setOnDateChangedListener(this);
        widget.setOnDateLongClickListener(this);
        widget.setOnMonthChangedListener(this);

        //Setup initial text
        textView.setText("No Selection");
        //toolbar
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);



        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();//返回
            }
        });

        //CollapsingToolbarLayout
       // toolbar.set
        date = DateUtils.format(System.currentTimeMillis(), "MM月dd日");
        week = DateUtils.format(System.currentTimeMillis(), "E");
        toolbar.setTitle(date + " ," + week);
        widget.setSelectedDate(LocalDate.now());
        toolbar.inflateMenu(R.menu.calender_menu);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    //这里应该刷新
                    //eventbus 刷新
                    case R.id.calender_select:
                     // Toast.makeText(CalendarActivity.this, widget.getCurrentDate().getDate().toString(), Toast.LENGTH_SHORT).show();
                        String queryDate = QUERY_FORMATTER.format(widget.getSelectedDate().getDate());
                        EventBus.getDefault().post(new DateStateChange(queryDate));
                        Log.i("TestDateDemo", "Event: " + queryDate);

                        finish();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onDateLongClick(@NonNull MaterialCalendarView materialCalendarView, @NonNull CalendarDay calendarDay) {
      //  final String text = String.format("%s is available", NORMAL_FORMATTER.format(calendarDay.getDate()));
     //   Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView materialCalendarView, @NonNull CalendarDay calendarDay, boolean selected) {
        nowTime = NORMAL_FORMATTER.format(calendarDay.getDate());
        toolbar.setTitle(nowTime);
        requestMatchs(QUERY_FORMATTER.format(calendarDay.getDate()),false);
        }

    @Override
    public void onMonthChanged(MaterialCalendarView materialCalendarView, CalendarDay calendarDay) {
      //  Toast.makeText(this, calendarDay.getDate().toString(), Toast.LENGTH_SHORT).show();
        //   getSupportActionBar().setTitle(FORMATTER.format(calendarDay.getDate()));
    }

/*
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.stats_menu, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.calender_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void requestMatchs(String date, boolean isRefresh) {
        TencentService.getMatchsByDate(date, isRefresh, new RequestCallback<Matchs>() {
            @Override
            public void onSuccess(Matchs matchs) {
              //  list.clear();
                List<Matchs.MatchsDataBean.MatchesBean> mList = matchs.getData().matches;
                if (mList != null && !mList.isEmpty()) {
                    textView.setText("共 " +  mList.size()  +" 场比赛");
                }
              //  complete();
            }

            @Override
            public void onFailure(String message) {
                //complete();
                textView.setText("查询失败！");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
