package com.friendgaru.traincatcherandroid.trainCatcher;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TrainCatcher {
	
	//java.util.Calendar treats Sunday as 1 and Saturday as 7
	public static final String[] WEEKDAYS = new String[] {"", "Sunday", "Monday", "Tuesday", "Wednesday", 
			"Thursday", "Friday", "Saturday"};
	
	int walkTime;
	int wiggleTime;
	Schedule[] schedules; //index = day of the week
	List<String> goodDestinations = new ArrayList<String>();
	
	public TrainCatcher (int walkTime, int wiggleTime){
		this.walkTime = walkTime;
		this.wiggleTime = wiggleTime;
		this.schedules = new Schedule[8]; //index 0 means nothing
	}
	
	public void setGoodDestinations(List<String> goodDestinations) {
		this.goodDestinations = goodDestinations;
	}
	
	private boolean isDepDestinationOkay(Departure dep){
		if (this.goodDestinations.size() <= 0 || this.goodDestinations.contains(dep.getDest())) {
			return true;
		} else {
			return false;
		}
		
	}
	
	public static String timeToStr(int time) {
		/*
		 * Takes an int of minutes from midnight and returns a nice HH:MM string
		 * Uses 24 hour style
		 */
		int hour = time / 60;
		int min = time % 60;
		
		String out = String.format("%02d:%02d", hour, min);
		return out;
	}
	
	public static String timeDayToStr(int time, int day) {
		String out = WEEKDAYS[day] + " " + timeToStr(time);
		return out;
		
	}
	
	public void setSchedule (Schedule sched, int weekday) {
		this.schedules[weekday] = sched;
	}

	public void setAllSchedules(Schedule weekdays, Schedule saturdays, Schedule holidays) {
		this.schedules[1] = holidays;
		this.schedules[2] = weekdays;
		this.schedules[3] = weekdays;
		this.schedules[4] = weekdays;
		this.schedules[5] = weekdays;
		this.schedules[6] = weekdays;
		this.schedules[7] = saturdays;
	}

	/*
	public void setSchedsByFileLoc(String weekdaysLoc, String saturdaysLoc, String holidaysLoc) {
		Schedule weekdays = Schedule.buildSchedule(weekdaysLoc);
		Schedule saturdays = Schedule.buildSchedule(saturdaysLoc);
		Schedule holidays = Schedule.buildSchedule(holidaysLoc);
		this.setAllSchedules(weekdays, saturdays, holidays);
	}
	*/
	
	public LookupResults getLookup(int time, int day) {
		LookupResults lookup = new LookupResults(time, day);
		Schedule activeSched = this.schedules[day];
		List<Departure> depList = activeSched.getDepartures();
		
		List<Departure> goodTrains = new ArrayList<Departure>();
		List<Departure> maybeTrains = new ArrayList<Departure>();
		List<Departure> badTrains = new ArrayList<Departure>();
		
		for (int i = 0; i < depList.size(); i++) {
			Departure dep = depList.get(i);
			if (dep.getTime() < time || !this.isDepDestinationOkay(dep)) {
				continue;
			} else if (dep.getTime() < time + this.walkTime) {
				badTrains.add(dep);
			} else if (dep.getTime() < time + this.walkTime + this.wiggleTime) {
				maybeTrains.add(dep);
			} else {
				goodTrains.add(dep);
			}
			
			if (goodTrains.size() >= 2) {
				break;
			}
		}
		
		lookup.setBad(badTrains);
		lookup.setMaybe(maybeTrains);
		lookup.setGood(goodTrains);
		
		return lookup;
	}
	
	public LookupResults getLookupNow() {
		Calendar now = Calendar.getInstance();
		int time = now.get(Calendar.HOUR_OF_DAY) * 60 + now.get(Calendar.MINUTE);
		int day = now.get(Calendar.DAY_OF_WEEK);
		return this.getLookup(time, day);
	}


}
