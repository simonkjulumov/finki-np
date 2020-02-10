package com.company;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class F1Test {

    public static void main(String[] args) {
        F1Race f1Race = new F1Race();
        f1Race.readResults(System.in);
        f1Race.printSorted(System.out);
    }
}

class Driver implements  Comparable<Driver> {
    private String name;
    private List<LocalTime> laps;
    private LocalTime bestLap;

    public Driver(String name, String... laps) {
        this.name = name;
        this.laps = new ArrayList<>();
        this.bestLap = LocalTime.MAX;
        for(String lap : laps) {
            LocalTime latestLap = formattedLocalTime(lap);
            this.laps.add(latestLap);
            if(latestLap.isBefore(bestLap)) {
                bestLap = latestLap;
            }
        }

    }

    @Override
    public int compareTo(Driver driver) {
        return getBestLap().compareTo(driver.getBestLap());
    }

    @Override
    public String toString() {
        return String.format("%-10s%10s\n", name, getBestLap().format(DateTimeFormatter.ofPattern("m:ss:nnn")));
    }

    public String getName() {
        return name;
    }

    private LocalTime getBestLap() {
        //We can do this if we dont check for best lap in constructor
        //Collections.sort(laps);
        //return laps.get(0);
        return bestLap;
    }

    private LocalTime formattedLocalTime(String lap) {
        //lap = 1:34:456 - minutes:seconds:nanoseconds
        String[] numbers = lap.split(":");
        int minutes = Integer.parseInt(numbers[0]);
        int seconds = Integer.parseInt(numbers[1]);
        int nanoseconds = Integer.parseInt(numbers[2]);

        return LocalTime.of(0, minutes, seconds, nanoseconds);
    }
}

class F1Race {
    // vashiot kod ovde
    private List<Driver> drivers;

    public F1Race() {
        drivers = new ArrayList<>();
    }

    public void readResults(InputStream in) {
        Scanner scanner = new Scanner(new InputStreamReader((in)));
        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] info = line.split(" ");
            String[] laps = new String[] { info[1], info[2], info[3] };
            drivers.add(new Driver(info[0], laps));
        }
    }

    public void printSorted(OutputStream out) {
        Collections.sort(drivers);
        PrintWriter printWriter = new PrintWriter(out);

        for(int i = 0; i < drivers.size(); i++){
            printWriter.print(i + 1 + ". " + drivers.get(i).toString());
        }
        printWriter.close();
    }

}