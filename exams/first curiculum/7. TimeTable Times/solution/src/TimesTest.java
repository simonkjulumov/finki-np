import java.io.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class TimesTest {

    public static void main(String[] args) {
        TimeTable timeTable = new TimeTable();
        try {
            timeTable.readTimes(System.in);
        } catch (UnsupportedFormatException e) {
            System.out.println("UnsupportedFormatException: " + e.getMessage());
        } catch (InvalidTimeException e) {
            System.out.println("InvalidTimeException: " + e.getMessage());
        }
        System.out.println("24 HOUR FORMAT");
        timeTable.writeTimes(System.out, TimeFormat.FORMAT_24);
        System.out.println("AM/PM FORMAT");
        timeTable.writeTimes(System.out, TimeFormat.FORMAT_AMPM);
    }

}

enum TimeFormat {
    FORMAT_24, FORMAT_AMPM
}

class UnsupportedFormatException extends Exception {
    private String format;
    public UnsupportedFormatException(String format) {
        this.format = format;
    }

    @Override
    public String getMessage() {
        return format;
    }
}

class InvalidTimeException extends Exception {
    private String format;
    public InvalidTimeException(String format) {
        this.format = format;
    }

    @Override
    public String getMessage() {
        return format;
    }
}

class TimeTable {
    private List<String> times;

    public TimeTable() {
        times = new ArrayList<>();
    }

    public void readTimes(InputStream inputStream) throws UnsupportedFormatException, InvalidTimeException {
        Scanner scanner = new Scanner(new InputStreamReader(inputStream));
        while(scanner.hasNextLine()){
            String line = scanner.nextLine();
            String[] info = line.split(Pattern.quote(" "));
            for(String time : info) {
                if(time.matches("[0-9]{1,2}:[0-9]{1,2}")){
                    validateTime(time, time.split(Pattern.quote(":")));
                    times.add(formatTime(time, ":"));
                }
                else if(time.matches("[0-9]{1,2}\\.[0-9]{1,2}")) {
                    validateTime(time, time.split(Pattern.quote(".")));
                    times.add(formatTime(time.replace(".", ":"), ":"));
                }
                else{
                    throw new UnsupportedFormatException(time);
                }
            }
        }
        scanner.close();
    }

    public void writeTimes(OutputStream outputStream, TimeFormat timeFormat) {
        PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(outputStream));
        Collections.sort(times);
        if(timeFormat == TimeFormat.FORMAT_24){
            for(String time : times) {
                printWriter.println(time);
            }
        }
        else if(timeFormat == TimeFormat.FORMAT_AMPM) {
            for(String time : times) {
                printWriter.println(hoursToAMPMFormat(time));
            }
        }
        printWriter.flush();
    }

    private String hoursToAMPMFormat(String time) {
        int hours = Integer.parseInt(time.trim().split(":")[0]);
        String minutes = time.trim().split(":")[1];

        String newTime = null;
        String t = null;
        switch(hours) {
            case 0:
                t = String.format("%d:%s", 12, minutes);
                newTime = String.format("%s %s", t, "AM");
                break;
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
                t = String.format(" %d:%s", hours, minutes);
                newTime = String.format("%s %s", t, "AM");
                break;
            case 10:
            case 11:
                t = String.format("%d:%s", hours, minutes);
                newTime = String.format("%s %s", t, "AM");
                break;
            case 12:
                t = String.format("%d:%s", hours, minutes);
                newTime = String.format("%s %s", t, "PM");
                break;
            case 13:
                t = String.format(" %d:%s", 1, minutes);
                newTime = String.format("%s %s", t, "PM");
                break;
            case 14:
                t = String.format(" %d:%s", 2, minutes);
                newTime = String.format("%s %s", t, "PM");
                break;
            case 15:
                t = String.format(" %d:%s", 3, minutes);
                newTime = String.format("%s %s", t, "PM");
                break;
            case 16:
                t = String.format(" %d:%s", 4, minutes);
                newTime = String.format("%s %s", t, "PM");
                break;
            case 17:
                t = String.format(" %d:%s", 5, minutes);
                newTime = String.format("%s %s", t, "PM");
                break;
            case 18:
                t = String.format(" %d:%s", 6, minutes);
                newTime = String.format("%s %s", t, "PM");
                break;
            case 19:
                t = String.format(" %d:%s", 7, minutes);
                newTime = String.format("%s %s", t, "PM");
                break;
            case 20:
                t = String.format(" %d:%s", 8, minutes);
                newTime = String.format("%s %s", t, "PM");
                break;
            case 21:
                t = String.format(" %d:%s", 9, minutes);
                newTime = String.format("%s %s", t, "PM");
                break;
            case 22:
                t = String.format("%d:%s", 10, minutes);
                newTime = String.format("%s %s", t, "PM");
                break;
            case 23:
                t = String.format("%d:%s", 11, minutes);
                newTime = String.format("%s %s", t, "PM");
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + hours);
        }
        return newTime;
    }

    private String formatTime(String time, String quote) {
        String[] numbers = time.split(Pattern.quote(quote));
        int hours = Integer.parseInt(numbers[0]);
        int minutes = Integer.parseInt(numbers[1]);
        String hoursString = hours < 10
                ? String.format(" %d", hours)
                : String.format("%d", hours);

        String minutesString = minutes < 10
                ? String.format("0%d", minutes)
                : String.format("%d", minutes);

        return String.format("%s%s%s", hoursString, quote, minutesString);
    }

    private void validateTime(String time, String[] numbers) throws InvalidTimeException {
        int hours = Integer.parseInt(numbers[0]);
        int minutes = Integer.parseInt(numbers[1]);

        if(hours < 0 || hours > 23) {
            throw new InvalidTimeException(time);
        }
        if(minutes < 0 || minutes > 59) {
            throw new InvalidTimeException(time);
        }
    }
}