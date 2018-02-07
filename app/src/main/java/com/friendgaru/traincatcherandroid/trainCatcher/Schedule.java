package com.friendgaru.traincatcherandroid.trainCatcher;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;

public class Schedule {
	List<Departure> departureList;
	List<String> destinations;
	
	Schedule(List<Departure> departureList){
		this.departureList = departureList;
		Collections.sort(departureList);
		this.setDestinations();
	}
	
	@RequiresApi(api = Build.VERSION_CODES.KITKAT)
	public static Schedule buildSchedule(InputStream inputStream){
		try {
			//InputStream inputStream = new FileInputStream(fileName);
			//InputStream inputStream = Schedule.class.getResourceAsStream(fileName);
            InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line;
            
            List<Departure> departures = new ArrayList<Departure>();
            
            int currentHour = -1;
            boolean firstLine = true;
            while ((line = bufferedReader.readLine()) != null) {
            	
            	if(firstLine) {
            		//The BOM thing for this file is screwy,
            		//So, just throw away the first line
            		firstLine = false;
            		continue;
            	}
            	
            	
            	//Trim whitespace and throw out empty lines
            	//Treat lines that start with "*" as comments and toss them, too
            	line = line.trim();
                if (line.equals("") || line.substring(0, 1).equals("*")) {
                	continue;
                }
                
                
                //If a line is an int, it starts a new hour,
                //If not, that means it's another departure for the current hour
                boolean isInt = false;
                try {
                Integer.parseInt(line);
                 //line is a valid integer!
                 isInt = true;
                } catch (NumberFormatException e) {
                	
                	//e.printStackTrace();
                }
                
                if (isInt) {
                	currentHour = Integer.parseInt(line);
                	
                } else {
                	//String trainType = line.substring(1, 3); //Not bothering with train types right now
                	String destination = line.substring(4);
                	String minLine = bufferedReader.readLine();
                	
                	try {
                		int minutes = Integer.parseInt(minLine.trim());
                		departures.add(new Departure(currentHour * 60 + minutes, destination));
                	} catch (NumberFormatException e) {
                		//Minute line bad!
                	}
                	
                	
                }
                
            }
            reader.close();
            
            return new Schedule(departures);
 
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
	}

	public String niceSchedule() {
		StringJoiner out = new StringJoiner("\n");
		
		int currentHour = -1;
		String currentLine = "Schedule";
		
		int depHour;
		int depMin;
		String depDest;
		
		for (int i = 0; i < departureList.size(); i++) {
			depHour = departureList.get(i).getHour();
			depMin = departureList.get(i).getMin();
			depDest = departureList.get(i).getDest();
			
			if (depHour > currentHour) {
				currentHour = depHour;
				out.add(currentLine);
				currentLine = String.format("%02d", depHour) + "   ";
			}
			
			currentLine += " " + String.format("%02d", depMin) + " " + depDest;
			
		}
		out.add(currentLine);
		
		return out.toString();
	}
	
	public List<Departure> getDepartures(){
		return this.departureList;
	}
	
	private void setDestinations(){
		this.destinations = new ArrayList<String>();
		for (int i = 0; i < this.departureList.size(); i++) {
			Departure dep = this.departureList.get(i);
			if (!this.destinations.contains(dep.getDest())){
				this.destinations.add(dep.getDest());
			}
		}
		Collections.sort(this.destinations);
	}
	
	public List<String> getDestinations(){
		return this.destinations;
	}
}
