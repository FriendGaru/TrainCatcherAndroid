package com.friendgaru.traincatcherandroid;

import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.friendgaru.traincatcherandroid.trainCatcher.LookupResults;
import com.friendgaru.traincatcherandroid.trainCatcher.Schedule;
import com.friendgaru.traincatcherandroid.trainCatcher.TrainCatcher;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String LOOKUP_MESSAGE = "com.friendgaru.traincatcherandroid.LOOKUP";

    public static final int WEEKDAYS = R.raw.weekdays;
    public static final int SATURDAYS = R.raw.saturdays;
    public static final int HOLIDAYS = R.raw.holidays;
    public static final int WALKTIME = 6;
    public static final int WIGGLE_TIME = 3;
    public static final List<String> GOOD_DESTINATIONS = new ArrayList<String>(
            Arrays.asList( "宮", "川", "池", "浦", "赤"));

    TrainCatcher tc;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        buildTC();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void buildTC(){
        TrainCatcher tc = new TrainCatcher(WALKTIME, WIGGLE_TIME);
        try (
                InputStream weekdaysIS = getResources().openRawResource(WEEKDAYS);
                InputStream saturdaysIS = getResources().openRawResource(SATURDAYS);
                InputStream holidaysIS = getResources().openRawResource(HOLIDAYS);
        ){

            Schedule schedWeekdays = Schedule.buildSchedule(weekdaysIS);
            Schedule schedSaturdays = Schedule.buildSchedule(saturdaysIS);
            Schedule schedHolidays = Schedule.buildSchedule(holidaysIS);
            tc.setAllSchedules(schedWeekdays, schedSaturdays, schedHolidays);
            tc.setGoodDestinations(GOOD_DESTINATIONS);

            this.tc = tc;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void doLookup(View view){
        TextView lookupResultsText = findViewById(R.id.lookupResultsView);
        LookupResults results = this.tc.getLookupNow();
        lookupResultsText.setText(results.toString());
        //lookupResultsText.setText("yay");

    }
}
