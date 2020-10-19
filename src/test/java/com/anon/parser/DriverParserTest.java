package com.anon.parser;

import com.anon.exception.DataException;
import com.anon.model.Trip;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

public class DriverParserTest {


    @Test(expected = NullPointerException.class)
    public void testNullLine() throws Exception {
        DriverParser dp = new DriverParser();
        dp.parseLine(null);
    }

    @Test
    public void testEmptyLine1() {
        DriverParser dp = new DriverParser();
        DataException d = assertThrows(DataException.class,
                () -> { dp.parseLine("");});
        assertTrue(d.getMessage().contains("Invalid data line"));
    }

    @Test
    public void testBlankLine() {
        DriverParser dp = new DriverParser();
        DataException d = assertThrows(DataException.class,
                () -> { dp.parseLine("   ");});
        assertTrue(d.getMessage().contains("Line has no data"));
    }

    @Test
    public void testBadLine() throws Exception {
        DriverParser dp = new DriverParser();
        DataException d = assertThrows(DataException.class,
                () -> { dp.parseLine("FOO!");});
        assertTrue(d.getMessage().contains("Invalid data line"));
    }

    @Test
    public void testDriverLine() throws Exception {
        DriverParser dp = new DriverParser();
        dp.parseLine("Driver Dan");
        Map<String, List<Trip>> trips = dp.getTrips();
        Set<String> drivers = dp.getDrivers();
        assertTrue(trips.keySet().size() == 1);
        assertTrue(trips.keySet().contains("Dan"));
        assertTrue(trips.get("Dan").size()==0);
        assertTrue(drivers.size() == 1);
        assertTrue(drivers.contains("Dan"));
    }

    @Test
    public void testMultipleDriverLines() throws Exception {
        DriverParser dp = new DriverParser();
        dp.parseLine("Driver Dan");
        dp.parseLine("Driver Dan2");
        Map<String, List<Trip>> trips = dp.getTrips();
        Set<String> drivers = dp.getDrivers();
        assertTrue(trips.keySet().size() == 2);
        assertTrue(trips.keySet().contains("Dan"));
        assertTrue(trips.keySet().contains("Dan2"));
        assertTrue(trips.get("Dan").size()==0);
        assertTrue(drivers.size() == 2);
        assertTrue(drivers.contains("Dan"));
        assertTrue(trips.keySet().contains("Dan2"));
        assertTrue(trips.get("Dan2").size()==0);
    }

    @Test
    public void testDuplicateDriverLines() throws Exception {
        DriverParser dp = new DriverParser();
        dp.parseLine("Driver Dan");
        DataException d = assertThrows(DataException.class,
                () -> { dp.parseLine("Driver Dan");});
        assertTrue(d.getMessage().contains("Driver 'Dan' is registered multiple times"));
    }

    @Test
    public void testDriverLineWithExtraData() throws Exception {
        DriverParser dp = new DriverParser();
        dp.parseLine("Driver Dan FOO");
        Map<String, List<Trip>> trips = dp.getTrips();
        Set<String> drivers = dp.getDrivers();
        assertTrue(trips.keySet().size() == 1);
        assertTrue(trips.keySet().contains("Dan"));
        assertTrue(trips.get("Dan").size()==0);
        assertTrue(drivers.size() == 1);
        assertTrue(drivers.contains("Dan"));
    }

    @Test
    public void testOrphanedTripLine() throws Exception {
        DriverParser dp = new DriverParser();
        DataException d = assertThrows(DataException.class,
                () -> { dp.parseLine("Trip Dan 01:00 02:00 1");});
        assertTrue(d.getMessage().contains("Trip details specified for driver 'Dan' but the driver is not registered"));
    }

    @Test
    public void testTripLine() throws Exception {
        DriverParser dp = new DriverParser();
        dp.parseLine("Driver Dan");
        dp.parseLine("Trip Dan 01:00 02:00 1");
        Map<String, List<Trip>> trips = dp.getTrips();
        Set<String> drivers = dp.getDrivers();
        assertTrue(trips.keySet().size() == 1);
        assertTrue(trips.keySet().contains("Dan"));
        assertTrue(trips.get("Dan").size()==1);
        assertTrue(trips.get("Dan").get(0).getDistance() == 1.0);
        assertTrue(drivers.size() == 1);
        assertTrue(drivers.contains("Dan"));
    }

    @Test
    public void test2TripLines() throws Exception {
        DriverParser dp = new DriverParser();
        dp.parseLine("Driver Dan");
        dp.parseLine("Trip Dan 01:00 02:00 1");
        dp.parseLine("Trip Dan 01:00 02:00 1");
        Map<String, List<Trip>> trips = dp.getTrips();
        Set<String> drivers = dp.getDrivers();
        assertTrue(trips.keySet().size() == 1);
        assertTrue(trips.keySet().contains("Dan"));
        assertTrue(trips.get("Dan").size()==2);
        assertTrue(drivers.size() == 1);
        assertTrue(drivers.contains("Dan"));
    }

    @Test
    public void test4TripLines() throws Exception {
        DriverParser dp = new DriverParser();
        dp.parseLine("Driver Dan");
        dp.parseLine("Trip Dan 01:00 02:00 1");
        dp.parseLine("Trip Dan 01:00 02:00 1");
        dp.parseLine("Trip Dan 01:00 02:00 1");
        dp.parseLine("Trip Dan 01:00 02:00 1");
        Map<String, List<Trip>> trips = dp.getTrips();
        Set<String> drivers = dp.getDrivers();
        assertTrue(trips.keySet().size() == 1);
        assertTrue(trips.keySet().contains("Dan"));
        assertTrue(trips.get("Dan").size()==4);
        assertTrue(drivers.size() == 1);
        assertTrue(drivers.contains("Dan"));
    }

    @Test
    public void testMultipleDriversAndLines() throws Exception {
        DriverParser dp = new DriverParser();
        dp.parseLine("Driver Dan");
        dp.parseLine("Driver Dan2");
        dp.parseLine("Driver Dan3");
        dp.parseLine("Driver Dan4");
        dp.parseLine("Trip Dan 01:00 02:00 1");
        dp.parseLine("Trip Dan 01:00 02:00 1");
        dp.parseLine("Trip Dan2 01:00 02:00 1");
        dp.parseLine("Trip Dan3 01:00 02:00 1");
        dp.parseLine("Trip Dan2 01:00 02:00 1");
        dp.parseLine("Trip Dan3 01:00 02:00 1");
        dp.parseLine("Trip Dan2 01:00 02:00 1");
        dp.parseLine("Trip Dan3 01:00 02:00 1");
        Map<String, List<Trip>> trips = dp.getTrips();
        Set<String> drivers = dp.getDrivers();
        assertTrue(trips.keySet().size() == 4);
        assertTrue(trips.keySet().contains("Dan"));
        assertTrue(trips.keySet().contains("Dan2"));
        assertTrue(trips.keySet().contains("Dan3"));
        assertTrue(trips.keySet().contains("Dan4"));
        assertTrue(trips.get("Dan").size()==2);
        assertTrue(trips.get("Dan2").size()==3);
        assertTrue(trips.get("Dan3").size()==3);
        assertTrue(trips.get("Dan4").size()==0);
        assertTrue(drivers.size() == 4);
        assertTrue(drivers.contains("Dan"));
        assertTrue(drivers.contains("Dan2"));
        assertTrue(drivers.contains("Dan3"));
        assertTrue(drivers.contains("Dan4"));
    }

    @Test
    public void testTripLineWithExtraData() throws Exception {
        DriverParser dp = new DriverParser();
        dp.parseLine("Driver Dan FOO");
        dp.parseLine("Trip Dan 01:00 02:00 1 fasfdasdf");
        Map<String, List<Trip>> trips = dp.getTrips();
        Set<String> drivers = dp.getDrivers();
        assertTrue(trips.keySet().size() == 1);
        assertTrue(trips.keySet().contains("Dan"));
        assertTrue(trips.get("Dan").size()==1);
        assertTrue(drivers.size() == 1);
        assertTrue(drivers.contains("Dan"));
    }

    @Test
    public void testTripLineWithBadData1() throws Exception {
        DriverParser dp = new DriverParser();
        dp.parseLine("Driver Dan");
        assertThrows(DataException.class,
                () -> { dp.parseLine("Trip Dan 01:0E 02:00 1");});
    }

    @Test
    public void testTripLineWithBadData2() throws Exception {
        DriverParser dp = new DriverParser();
        dp.parseLine("Driver Dan");
        assertThrows(DataException.class,
                () -> { dp.parseLine("Trip Dan 01:00EXTRA 02:00 1");});
    }

    @Test
    public void testTripLineWithBadData3() throws Exception {
        DriverParser dp = new DriverParser();
        dp.parseLine("Driver Dan");
        assertThrows(DataException.class,
                () -> { dp.parseLine("Trip Dan 01:00 02:00EXTRA 1");});
    }

    @Test
    public void testTripLineWithBadData4() throws Exception {
        DriverParser dp = new DriverParser();
        dp.parseLine("Driver Dan");
        assertThrows(DataException.class,
                () -> { dp.parseLine("Trip Dan 01:00 2:00 1");});
    }

    @Test
    public void testTripLineWithBadData5() throws Exception {
        DriverParser dp = new DriverParser();
        dp.parseLine("Driver Dan");
        assertThrows(NumberFormatException.class,
                () -> { dp.parseLine("Trip Dan 01:00 02:00 1r");});
    }

    @Test
    public void testTripLineWithMissingData() throws Exception {
        DriverParser dp = new DriverParser();
        dp.parseLine("Driver Dan");
        assertThrows(DataException.class,
                () -> { dp.parseLine("Trip Dan 01:00 02:00");});
    }

    @Test
    public void testTripLineWithOverflow() throws Exception {
        DriverParser dp = new DriverParser();
        dp.parseLine("Driver Dan");
        assertThrows(DataException.class,
                () -> { dp.parseLine("Trip Dan 01:00 02:00 40000000000000000000000000000000000000000");});
    }
}
