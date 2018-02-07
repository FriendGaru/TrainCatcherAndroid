package com.friendgaru.traincatcherandroid.trainCatcher;

import java.util.List;
import java.util.stream.Collectors;

public class LookupResults {
		
	int time;
	int day;
	List<Departure> goodTrains;
	List<Departure> maybeTrains;
	List<Departure> badTrains;
	
	LookupResults(int time, int day){
		this.time = time;
		this.day = day;
	}
	
	public void setGood(List<Departure> deps) {
		goodTrains = deps;
	}
	
	public void setMaybe(List<Departure>  deps) {
		maybeTrains = deps;
	}
	
	public void setBad(List<Departure>  deps) {
		badTrains = deps;
	}
	
	@Override
	public String toString() {
		String out = TrainCatcher.timeDayToStr(this.time, this.day);
		if (badTrains.size() > 0) {
			out += "\nBad: ";
			for (int i = 0; i < badTrains.size();i++) {
				out += " " + badTrains.get(i).toString();
			}	
		}
		if (maybeTrains.size() > 0) {
			out += "\nMaybe: ";
			for (int i = 0; i < maybeTrains.size();i++) {
				out += " " + maybeTrains.get(i).toString();
			}
		}
		if (goodTrains.size() > 0) {
			out += "\nGood: " ;
			for (int i = 0; i < goodTrains.size();i++) {
				out += " " + goodTrains.get(i).toString();
			}
		}
		
		return out;
	}

}
