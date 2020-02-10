package com.company;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;

/**
 * LocalDateTime tests
 */
public class LocalDateTimeTest {

    public static void main(String[] args) {
        System.out.println(localDateTimeOf());
        System.out.println(localDateTimeParse());
        System.out.println(localTimeWith());
        System.out.println(localDatePlusMinus());
        System.out.println(localDateTimeFormat());
        System.out.println(toLocalDateAndTime());
        System.out.println(toLocalDateTime());
    }

    static LocalDateTime localDateTimeOf() {
        /**
         * Create a {@link LocalDateTime} of 2015-06-20 23:07:30 by using {@link LocalDateTime#of}
         */
        LocalDate date = LocalDate.of(2015, 06, 20);
        LocalTime time = LocalTime.of(23, 07, 30);
        return LocalDateTime.of(date, time);
    }

    static LocalDateTime localDateTimeParse() {
        /**
         * Create a {@link LocalDateTime} of 2015-06-20 23:07:30 by using {@link LocalDateTime#parse}
         */

        //The default date pattern is DateTimeFormatter.ISO_LOCAL_DATE_TIME which is yyyy-MM-ddThh:mm:ss.
        return LocalDateTime.parse("2015-06-20T23:07:30", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    static LocalDateTime localTimeWith() {
        LocalDateTime ldt = DateAndTimes.LDT_20150618_23073050;

        /**
         * Create a {@link LocalDateTime} from {@link ldt}
         * with first day of the next month and also truncated to hours.
         */
        return LocalDateTime.from(ldt).with(TemporalAdjusters.firstDayOfNextMonth()).truncatedTo(ChronoUnit.HOURS);
    }

    static LocalDateTime localDatePlusMinus() {
        LocalDateTime ldt = DateAndTimes.LDT_20150618_23073050;

        /**
         * Create a {@link LocalDateTime} from {@link ldt} with 10 month later and 5 hours before
         * by using {@link LocalDateTime#plus*} or {@link LocalDateTime#minus*}
         */
        return LocalDateTime.from(ldt).plusMonths(10).plusHours(-5);
    }

    static String localDateTimeFormat() {
        LocalDateTime ldt = DateAndTimes.LDT_20150618_23073050;

        /**
         * Format {@link ldt} to a {@link String} as "2015_06_18_23_07_30"
         * by using {@link LocalDateTime#format} and {@link DateTimeFormatter#ofPattern}
         */
        return LocalDateTime.from(ldt).format(DateTimeFormatter.ofPattern("2015_06_18_23_07_30"));
    }

    static String toLocalDateAndTime() {
        LocalDateTime ldt = DateAndTimes.LDT_20150618_23073050;

        /**
         * Create a {@link LocalDate} and a {@link LocalTime} from {@link ldt}
         * by using {@link LocalDateTime#toLocalDate} and {@link LocalDateTime#toLocalTime}
         */
        LocalDate localDate = LocalDate.parse(ldt.format(DateTimeFormatter.ISO_LOCAL_DATE));
        LocalTime localTime = LocalTime.parse(ldt.format(DateTimeFormatter.ISO_LOCAL_TIME));
        return localDate.toString() + localTime.toString();
    }

    static String toLocalDateTime() {
        LocalDate ld = DateAndTimes.LD_20150618;
        LocalTime lt = DateAndTimes.LT_23073050;

        /**
         * Create two equal {@link LocalDateTime} from {@link ld} and {@link lt}
         * by using {@link LocalDate#atTime} and {@link LocalTime#atDate}
         */
        LocalDateTime localDateTime1 = LocalDateTime.from(ld.atTime(lt));
        LocalDateTime localDateTime2 = LocalDateTime.from(lt.atDate(ld));
        return localDateTime1.toString() + " " + localDateTime2.toString();
    }

    static class DateAndTimes {
        public static final LocalDate LD_20150618 = LocalDate.of(2015, 6, 18);
        public static final LocalTime LT_23073050 = LocalTime.of(23, 7, 30, 500000000);
        public static final LocalDateTime LDT_20150618_23073050 = LocalDateTime.of(2015, 6, 18, 23, 7, 30, 500000000);
    }
}
