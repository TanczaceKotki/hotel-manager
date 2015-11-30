package hotel;

import java.util.Date;

public class Interval {

    protected Date begin;
    protected Date end;

    public Interval (Date b, Date e)  {
        begin = b;
        end = e;
    }

    public Date getBegin() {
        return begin;
    }

    public Date getEnd() {
        return end;
    }

    public boolean collides(Interval other) {
        boolean caseA = begin.compareTo(other.getBegin()) >= 0 && begin.compareTo(other.getEnd()) >= 0;
        boolean caseB = end.compareTo(other.getBegin()) <= 0 && end.compareTo(other.getEnd()) <= 0;
        return !(caseA || caseB);
    }

    public  boolean contains(Date date) {

        return date.compareTo(begin) >= 0 && date.compareTo(end) <= 0;

    }

}
