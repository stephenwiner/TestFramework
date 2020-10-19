package com.anon.driver;

import com.anon.model.DriverSummary;
import com.anon.model.Trip;
import com.anon.parser.DriverParser;

import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class DrivingSummary {

    private InputStream input;
    private DriverParser parser;
    private OutputStream output;

    public static void main(String[] args) throws Exception {
        DrivingSummary ds = new DrivingSummary(System.in, new DriverParser(), System.out);
        ds.generateSummaryReport();
    }

    public DrivingSummary(InputStream input, DriverParser parser, OutputStream output) {
        this.input = input;
        this.parser = parser;
        this.output = output;
    }

    public void generateSummaryReport() throws Exception {
        Map<String, List<Trip>> initialSummary = parser.parse(input);
        List<DriverSummary> aggregatedSummaries = aggregate(initialSummary);
        sortSummaries(aggregatedSummaries);
        printSummaries(aggregatedSummaries, output);
    }

    protected List<DriverSummary> aggregate(Map<String, List<Trip>> initialSummary) {
        List<DriverSummary> aggregatedSummaries = new ArrayList<DriverSummary>();
        Iterator<String> i = initialSummary.keySet().iterator();
        while (i.hasNext()) {
            String name = i.next();
            List<Trip> tripList = initialSummary.get(name);
            if (tripList == null) {
                aggregatedSummaries.add(new DriverSummary(name, 0, 0));
            } else {
                aggregatedSummaries.add(computeTripSummary(name, tripList));
            }
        }
        return aggregatedSummaries;
    }

    protected DriverSummary computeTripSummary(String name, List<Trip> trips) {
        double rollingVelocityMinutes = 0.0;
        long rollingMinutes = 0;
        double rollingDistance = 0.0;
        for(Trip t: trips) {
            long elapsedMinutes = getMinutes(t.getStart(), t.getEnd());
            double velocity = t.getDistance() * 60 / elapsedMinutes;
            if(velocity>5 && velocity <100){
                rollingVelocityMinutes += velocity * elapsedMinutes;
                rollingMinutes += elapsedMinutes;
                rollingDistance += t.getDistance();
            }
        }
        DriverSummary sum = new DriverSummary(name,
                (int) Math.round(rollingDistance),
                (int) Math.round(rollingVelocityMinutes / rollingMinutes));
        return sum;
    }

    protected long getMinutes(Date start, Date end) {
        return TimeUnit.MINUTES.convert(end.getTime() - start.getTime(),
                                        TimeUnit.MILLISECONDS);
    }

    protected void sortSummaries(List<DriverSummary> summaries) {
        Collections.sort(summaries, new Comparator<DriverSummary>() {
            public int compare(DriverSummary a, DriverSummary b) {
                return b.getDistance() - a.getDistance();
            }
        });
    }

    protected void printSummaries(List<DriverSummary> summaries, OutputStream out) {
        PrintStream ps = new PrintStream(out);
        for(DriverSummary s:summaries) {
            ps.print(s.getName() +": " + s.getDistance());
            if(s.getDistance()==0) {
                ps.print(" miles");
            } else if(s.getDistance()==1){
                ps.print(" mile @ " + s.getAverageSpeed() + " mph");
            } else {
                ps.print(" miles @ " + s.getAverageSpeed() + " mph");
            }
            ps.println();
        }
        ps.flush();
    }
}


