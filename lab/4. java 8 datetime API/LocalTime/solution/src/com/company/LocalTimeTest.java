package com.company;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;

/**
 * LocalTime API tests
 */
public class LocalTimeTest {
    public static void main(String[] args) {
        System.out.println(localTimeOfHourToMinute());
        System.out.println(localTimeOfHourToNanoSec());
        System.out.println(localTimeParse());
        System.out.println(localTimeWith());
        System.out.println(localTimePlus());
        System.out.println(localTimeMinus());
        System.out.println(localTimeMinusDuration());
        System.out.println(localDateIsBefore());
        System.out.println(localTimeTruncatedTo());
    }

    static LocalTime localTimeOfHourToMinute() {
        /**
         * Create a {@link LocalTime} of 23:07 by using {@link LocalTime#of}
         */
        return LocalTime.of(23, 07);
    }

    static LocalTime localTimeOfHourToNanoSec() {
        /**
         * Create a {@link LocalTime} of 23:07:03.1 by using {@link LocalTime#of}
         */
        LocalTime time =  LocalTime.of(23, 07, 03);
        time = time.plus(100, ChronoUnit.MILLIS);
        return time;
    }

    static LocalTime localTimeParse() {
        /**
         * Create a {@link LocalTime} of 23:07:03.1 from String by using {@link LocalTime#parse}
         */
        return LocalTime.parse("23:07:03.1");
    }

    static LocalTime localTimeWith() {
        LocalTime lt = DateAndTimes.LT_23073050;

        /**
         * Create a {@link LocalTime} from {@link lt} with hour 21
         * by using {@link LocalTime#withHour} or {@link LocalTime#with}
         */
        return LocalTime.from(lt).withHour(21);
    }

    static LocalTime localTimePlus() {
        LocalTime lt = DateAndTimes.LT_23073050;

        /**
         * Create a {@link LocalTime} from {@link lt} with 30 minutes later
         * by using {@link LocalTime#plusMinutes} or {@link LocalTime#plus}
         */
        return LocalTime.from(lt).plusMinutes(30);
    }

    static LocalTime localTimeMinus() {
        LocalTime lt = DateAndTimes.LT_23073050;

        /**
         * Create a {@link LocalTime} from {@link lt} with 3 hours before
         * by using {@link LocalTime#minusHours} or {@link LocalTime#minus}
         */
        return LocalTime.from(lt).minusHours(3);
    }


    static LocalTime localTimeMinusDuration() {
        LocalTime lt = DateAndTimes.LT_23073050;

        /**
         * Define a {@link Duration} of 3 hours 30 minutes and 20.2 seconds
         * Create a {@link LocalTime} subtracting the duration from {@link lt} by using {@link LocalTime#minus}
         */

        Duration duration1 = Duration.ZERO;
        duration1 = duration1.plusHours(3);
        duration1 = duration1.plusMinutes(30);
        duration1 = duration1.plusSeconds(20);
        duration1 = duration1.plusMillis(200);

        //duration2 instead of duration1 also works
        long milliSeconds = 200 + (20 * 1000) + (30 * 60 * 1000) + (3 * 60 * 60 * 1000);
        Duration duration2 = Duration.ofMillis(milliSeconds);

        return LocalTime.from(lt).minus(duration1);
    }

    static boolean localDateIsBefore() {
        LocalTime lt = DateAndTimes.LT_23073050;
        LocalTime lt2 = DateAndTimes.LT_12100000;
        /**
         * Check whether {@link lt2} is before {@link lt} or not
         * by using {@link LocalTime#isAfter} or {@link LocalTime#isBefore}
         */
        return LocalTime.from(lt2).isBefore(LocalTime.from(lt));
    }

    static LocalTime localTimeTruncatedTo() {
        LocalTime lt = DateAndTimes.LT_23073050;

        /**
         * Create a {@link LocalTime} from {@link lt} truncated to minutes by using {@link LocalTime#truncatedTo}
         */
        return LocalTime.from(lt).truncatedTo(ChronoUnit.MINUTES);
    }

    static class DateAndTimes {
        public static final LocalTime LT_23073050 = LocalTime.of(23, 7, 30, 500000000);
        public static final LocalTime LT_12100000 = LocalTime.of(12, 10);
    }

}
