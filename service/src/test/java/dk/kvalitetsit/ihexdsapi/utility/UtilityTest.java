package dk.kvalitetsit.ihexdsapi.utility;


import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class UtilityTest {


    public static void waiter(long time) {
        LocalTime now = LocalTime.now();
        LocalTime end = now.plus(time, ChronoUnit.MILLIS);

        while (now.isBefore(end)) {
            now = LocalTime.now();


        }
    }

}
