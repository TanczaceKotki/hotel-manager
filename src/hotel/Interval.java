package hotel;

import java.util.Calendar;
import java.util.Date;

public class Interval {

    protected Date begin;
    protected Date end;
    public Calendar from = Calendar.getInstance();
    public Calendar to = Calendar.getInstance();
    public static TimeIgnoringComparator comparator;


    static {
        comparator = new TimeIgnoringComparator();

    }

    public Interval (Date b, Date e)  {
        from = Calendar.getInstance();
        to = Calendar.getInstance();
        setDates(b, e);
    }

    void setDates(Date b, Date e) {
        begin = b;
        end = e;
        from.setTime(begin);
        to.setTime(end);
    }

    public Date getBegin() {
        return begin;
    }

    public Date getEnd() {
        return end;
    }

    public boolean collides(Interval other) {

        //boolean caseA = begin.compareTo(other.getBegin()) >= 0 && begin.compareTo(other.getEnd()) >= 0;
        boolean caseA = comparator.compare(from, other.to) > 0 && comparator.compare(to, other.to) > 0;
        //boolean caseB = end.compareTo(other.getBegin()) <= 0 && end.compareTo(other.getEnd()) <= 0;
        boolean caseB = comparator.compare(from, other.from) < 0 && comparator.compare(to, other.from) < 0;
        return !(caseA || caseB);
    }

    public  boolean contains(Date date) {
        Calendar tmp = Calendar.getInstance();
        tmp.setTime(date);
        return comparator.compare(tmp, from) >= 0 && comparator.compare(tmp, to) <= 0;

    }

}
