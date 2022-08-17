package dk.kvalitetsit.ihexdsapi.utility;


import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class UtilityTest {
    public static void main(String[] args) {
        waiter(2000);
    }
    private static int timeDiff;

    public static void waiter(long time) {
        LocalTime now = LocalTime.now();
        LocalTime tempStart = now;
        LocalTime end = now.plus(time, ChronoUnit.MILLIS);

        while (now.isBefore(end)) {
            now = LocalTime.now();
            // Huh måske bruges Snakke om
            setTimeDiff(-1 * ((tempStart.getSecond() - now.getSecond()) * 1000));

        }
    }

    public static int getTimeDiff() {
        return timeDiff;
    }

    public static void setTimeDiff(int timeDiff) {
        UtilityTest.timeDiff = timeDiff;
    }
}
