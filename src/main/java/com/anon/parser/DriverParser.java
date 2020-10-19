package com.anon.parser;

import com.anon.model.Trip;
import com.anon.exception.DataException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DriverParser {

    Logger log = LogManager.getLogger(DriverParser.class);

    private Set<String> drivers = new HashSet<String>();
    private Map<String, List<Trip>> trips = new HashMap<String,List<Trip>>();
    private final static String timePattern = "HH:mm";
    private SimpleDateFormat format = new SimpleDateFormat(timePattern);

    protected Map<String, List<Trip>> getTrips() {
        return trips;
    }

    protected Set<String> getDrivers() {
        return drivers;
    }

    public Map<String, List<Trip>> parse(InputStream is) throws DataException,
            ParseException, IOException {
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            isr = new InputStreamReader(is);
            br = new BufferedReader((isr));
            parse(br);
            return trips;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {/*swallow*/}
            }
            if(isr != null) {
                try {
                    isr.close();
                } catch (IOException e) {/*swallow*/}
            }
        }
    }

    protected void parse(BufferedReader br) throws DataException,
            ParseException, IOException {
        String line;
        while ((line = br.readLine()) != null) {
            parseLine(line);
        }
    }

    protected void parseLine(String line) throws DataException,
            ParseException {
        String[] fields = line.split(" ");
        if(fields.length==0) {
            throw new DataException("Line has no data.");
        } else if(fields[0].equals("Driver")) {
            parseDriver(fields);
        } else if(fields[0].equals("Trip")) {
            parseTrip(line, fields);
        } else {
            throw new DataException("Invalid data line: " + line);
        }
    }

    private void parseTrip(String line, String[] fields) throws DataException, ParseException {
        //SimpleDateFormat will ignore some types of bad data...
        //use a pattern as well to ensure no bad data
        if(fields.length<5) {
            throw new DataException("Incomplete trip field: " + line);
        }
        validateDates(fields[2], fields[3]);
        Trip t = new Trip(fields[1], format.parse(fields[2]),
                format.parse(fields[3]), Float.parseFloat(fields[4]));
        if(fields[4].length()>20) {
            //arbitrary, but we don't need Float to go to infinity/overflow
            throw new DataException("Distance must be < 20 chars: " + fields[4]);
        }
        if(!drivers.contains(fields[1])) {
            throw new DataException("Trip details specified for driver '" +
                    fields[1] + "' but the driver is not registered.");
        }
        List list = trips.get(fields[1]);
        if(list==null) {
            list = new ArrayList<Trip>();
            trips.put(fields[1], list);
        }
        list.add(t);
    }

    private void parseDriver(String[] fields) throws DataException {
        if(drivers.contains(fields[1])) {
            throw new DataException("Driver '" + fields[1] +
                    "' is registered multiple times.");
        } else {
            drivers.add(fields[1]);
            if(trips.get(fields[1]) == null) {
                trips.put(fields[1], new ArrayList<Trip>());
            }
        }
    }

    protected void validateDates(String...dates) throws DataException {
        for(String s : dates) {
            if(s.length() != 5 ||
               !Character.isDigit(s.charAt(0)) ||
               !Character.isDigit(s.charAt(1)) ||
               !Character.isDigit(s.charAt(3)) ||
               !Character.isDigit(s.charAt(4)) ||
                    s.charAt(2) != ':') {
                throw new DataException("Date is not formatted correctly: " + s);
            }

        }
    }
}
