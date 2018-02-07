package com.friendgaru.traincatcherandroid.trainCatcher;
public class Departure implements Comparable<Departure>{
		
		int time; //Time of departure as minutes from midnight
		String dest; //Destination of departure 
		
		Departure(int time, String dest){
			this.time = time;
			this.dest = dest;
		}
		
		int getTime() {
			return this.time;
		}
		
		int getHour() {
			return this.time / 60;
		}
		
		int getMin() {
			return this.time % 60;
		}
		
		String getDest() {
			return this.dest;
		}
		
		@Override
		public String toString() {
			String out = TrainCatcher.timeToStr(this.time) + " " + this.dest;
			return out;
		}

		@Override
		public int compareTo(Departure otherDep) {
			
			return this.getTime() - otherDep.getTime();
		}
	}