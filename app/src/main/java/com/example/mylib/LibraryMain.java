package com.example.mylib;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.Calendar;
import java.util.Collection;

public class LibraryMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        MaterialCalendarView materialCalendarView = (MaterialCalendarView) findViewById(R.id.calendarView);


//       캘린더 첫날과 마지막 날 지정
        materialCalendarView.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2017, 0, 1))
                .setMaximumDate(CalendarDay.from(2030, 11, 31))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();


//      주말과 오늘 날짜 색깔 지정
        final OneDayDecorator oneDayDecorator = new OneDayDecorator();
        materialCalendarView.addDecorators(
                new SundayDecorator(),
                new SaturdayDecorator(),
                oneDayDecorator);


//       글 작성한 날에 점 표시 (미구현)
        Collection<CalendarDay> calendarDays = null;
        CalendarDay calendarDay = new CalendarDay();
        calendarDays.add(calendarDay);
//        materialCalendarView.addDecorator(new EventDecorator(Color.RED, calendarDays);


//        내 서재 > 설정
        Button btn_lib_setting = (Button)findViewById(R.id.btn_lib_setting);
        btn_lib_setting.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), LibrarySetting.class);
                        startActivity(intent);
                    }
                }
        );
    }
}
