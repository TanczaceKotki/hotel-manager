package hotel;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({

        IntervalTest.class,
        DiscountsTest.class,
        RoomTest.class,
        PersonTest.class,
        ReservationTest.class,
        HotelTest.class

})
public class HotelTestSuite {

}
