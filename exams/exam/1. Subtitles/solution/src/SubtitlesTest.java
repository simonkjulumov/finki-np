import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.regex.Pattern;

public class SubtitlesTest {
    public static void main(String[] args) {
        Subtitles subtitles = new Subtitles();
        int n = subtitles.loadSubtitles(System.in);
        System.out.println("+++++ ORIGINIAL SUBTITLES +++++");
        subtitles.print();
        int shift = n * 37;
        shift = (shift % 2 == 1) ? -shift : shift;
        System.out.println(String.format("SHIFT FOR %d ms", shift));
        subtitles.shift(shift);
        System.out.println("+++++ SHIFTED SUBTITLES +++++");
        subtitles.print();
    }
}

// Вашиот код овде
class Subtitle {
    private int orderNumber;
    private String duration;
    private LocalTime start;
    private LocalTime finish;
    private String text;

    public Subtitle(int orderNumber, String duration, String text) {
        this.orderNumber = orderNumber;
        this.duration = duration;
        this.text = text;
        setStart(duration);
        setFinish(duration);
    }

    public void shiftTimeNegative(int ms) {
        start = start.plus(ms, ChronoUnit.MILLIS);
        finish = finish.plus(ms, ChronoUnit.MILLIS);
    }

    public void shiftTimePositive(int ms) {
        start = start.plus(ms, ChronoUnit.MILLIS);
        finish = finish.plus(ms, ChronoUnit.MILLIS);
    }

    private void setStart(String duration){
        String formatted = duration.replaceAll(Pattern.quote(","), ":");
        String startTime = formatted.split(Pattern.quote(" "))[0];
        start = LocalTime.parse(startTime, DateTimeFormatter.ofPattern("HH:mm:ss:SSS"));
    }

    private void setFinish(String duration){
        String formatted = duration.replaceAll(Pattern.quote(","), ":");
        String finishTime = formatted.split(Pattern.quote(" "))[2];
        finish = LocalTime.parse(finishTime, DateTimeFormatter.ofPattern("HH:mm:ss:SSS"));
    }

    @Override
    public String toString() {
        String startTime = start.toString().replaceAll(Pattern.quote("."), ",");
        String endTime = finish.toString().replaceAll(Pattern.quote("."), ",");
        if(startTime.length() <= 8) {
            startTime = startTime.concat(",000");
        }
        if(endTime.length() <= 8) {
            endTime = endTime.concat(",000");
        }
        return String.format("%d\n%s --> %s\n%s",
                orderNumber,
                startTime,
                endTime,
                text);
    }
}

class Subtitles {
    private ArrayList<Subtitle> subtitles;

    public Subtitles() {
        subtitles = new ArrayList<>();
    }

    public int loadSubtitles(InputStream inputStream) {
        Scanner scanner = new Scanner(new InputStreamReader(inputStream));
        while(scanner.hasNextLine()){
            String n = scanner.nextLine().trim();
            if(!n.isEmpty()){
                int orderNumber = Integer.parseInt(n);
                String duration = scanner.nextLine().trim();
                String words = null;
                StringBuilder text = new StringBuilder();
                while(true) {
                    if(scanner.hasNextLine()){
                        words = scanner.nextLine();
                        if(words.isEmpty())
                            break;
                        text.append(words + "\n");
                    }
                    else{
                        break;
                    }
                }
                subtitles.add(new Subtitle(orderNumber, duration, text.toString()));
            }
        }

        return subtitles.size();
    }

    public void print() {
        subtitles.stream().forEach(System.out::println);
    }

    public void shift(int ms) {
        if(ms < 0) {
            subtitles.stream().forEach(x -> x.shiftTimeNegative(ms));
        }
        else if(ms > 0) {
            subtitles.stream().forEach(x -> x.shiftTimePositive(ms));
        }
    }
}