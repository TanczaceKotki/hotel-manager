package hotel;

import java.util.Date;

public class Interval {

    private Date begin;
    private Date end;

    public Interval (Date b, Date e)  {
        begin = b;
        end = e;
    }

    Date getBegin() {
        return begin;
    }

    Date getEnd() {
        return end;
    }

    public boolean collides(Interval other) {
        boolean caseA = end.after(other.getBegin()) && begin.before(other.getEnd());
        boolean caseB = begin.after(other.getEnd()) && end.before(other.getBegin());
        return caseA || caseB;
    }
}
