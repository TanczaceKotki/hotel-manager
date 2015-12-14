package hotel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

public class IntervalTest {


    @Test
    public void testDatesSetting() throws Exception {

        //Correct data
        Calendar cal = Calendar.getInstance();
        cal.set(2015, 10, 1);
        Date b = cal.getTime();
        cal.set(2015, 10, 20);
        Date e = cal.getTime();
        Interval interval = new Interval(b, e);

        assertEquals(interval.getBegin(), b);
        assertEquals(interval.getEnd(), e);

        //Setting to < begin
        interval.setDates(e, b);
        assertEquals(interval.getBegin(), e);
        assertEquals(interval.getEnd(), e);

    }

    @Test
    public void testCollides() throws Exception {

        Calendar cal = Calendar.getInstance();
        cal.set(2015, 10, 10);
        Date b = cal.getTime();
        cal.set(2015, 10, 20);
        Date e = cal.getTime();
        Interval interval = new Interval(b, e);

        //Not colliding
        cal.set(2015, 9, 30);
        b = cal.getTime();
        cal.set(2015, 10, 9);
        e = cal.getTime();
        Interval beforeInterval = new Interval(b, e);

        cal.set(2015, 10, 25);
        b = cal.getTime();
        cal.set(2015, 11, 30);
        e = cal.getTime();
        Interval afterInterval = new Interval(b, e);

        assertFalse(interval.collides(beforeInterval));
        assertFalse(interval.collides(afterInterval));

        //Colliding
        //Begin date between intersectingInterval boundaries
        cal.set(2015, 10, 8);
        b = cal.getTime();
        cal.set(2015, 10, 15);
        e = cal.getTime();
        Interval intersectingInterval = new Interval(b, e);
        assertTrue(interval.collides(intersectingInterval));

        //End date between intersectingInterval boundaries
        cal.set(2015, 10, 16);
        b = cal.getTime();
        cal.set(2016, 1, 30);
        e = cal.getTime();
        intersectingInterval = new Interval(b, e);
        assertTrue(interval.collides(intersectingInterval));

        //IntersectingInterval "inside" interval
        cal.set(2015, 10, 11);
        b = cal.getTime();
        cal.set(2016, 10, 19);
        e = cal.getTime();
        intersectingInterval = new Interval(b, e);
        assertTrue(interval.collides(intersectingInterval));

        //Self colliding
        assertTrue(interval.collides(interval));

    }

    @Test
    public void testContains() throws Exception {

        Calendar cal = Calendar.getInstance();
        cal.set(2015, 10, 10);
        Date b = cal.getTime();
        cal.set(2015, 10, 20);
        Date e = cal.getTime();
        Interval interval = new Interval(b, e);

        //Contains
        cal.set(2015, 10, 20);
        Date between = cal.getTime();

        assertTrue(interval.contains(b));
        assertTrue(interval.contains(e));
        assertTrue(interval.contains(between));

        //Does not contain
        //Contains
        cal.set(2015, 9, 12);
        Date dateBefore = cal.getTime();
        cal.set(2015, 10, 22);
        Date dateAfter = cal.getTime();

        assertFalse(interval.contains(dateBefore));
        assertFalse(interval.contains(dateAfter));


    }


}