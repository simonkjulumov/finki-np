package com.company;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.*;


class DateConverterExtensions {
    public static Date localDateTimeToDateConverter(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDateTime dateToLocalDateTimeConverter(Date date) {
        return new java.sql.Timestamp(date.getTime()).toLocalDateTime();
    }

    public static LocalDateTime dateToLocalDateTimeConverterV2(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }
}

class Measurement implements Comparable<Measurement> {
    private float temperature;
    private float wind;
    private float humidity;
    private float visibility;
    private LocalDateTime date;

    public Measurement(float temperature, float wind, float humidity, float visibility, Date date) {
        this.temperature = temperature;
        this.wind = wind;
        this.humidity = humidity;
        this.visibility = visibility;
        this.date = DateConverterExtensions.dateToLocalDateTimeConverterV2(date);
    }

    @Override
    public int compareTo(Measurement measurement) {
        return getDate().compareTo(measurement.getDate());
    }

    @Override
    public String toString() {
        //date.format(DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss uuuu")
        Date dateString = DateConverterExtensions.localDateTimeToDateConverter(date);
        return String.format("%.1f %.1f km/h %.1f%s %.1f km %s", temperature, wind, humidity, "%", visibility, dateString);
    }

    public float getTemperature() {
        return temperature;
    }

    public float getWind() {
        return wind;
    }

    public float getHumidity() {
        return humidity;
    }

    public float getVisibility() {
        return visibility;
    }

    public LocalDateTime getDate() {
        return date;
    }
}

class WeatherStation {
    private int days;
    private List<Measurement> measurements;
    private int secondsUntilAcceptNewMeasurement;

    public WeatherStation(int days) {
        this.days = days;
        this.measurements = new LinkedList<>();
        this.secondsUntilAcceptNewMeasurement = 150;
    }

    public void addMeasurement(float temperature, float wind, float humidity, float visibility, Date date) {
        LocalDateTime measurementDate = DateConverterExtensions.dateToLocalDateTimeConverterV2(date);

        //избиши ги Measurements постари од N дена од новиот Measurement
        measurements.removeIf(x -> x.getDate().plusDays(days).isBefore(measurementDate));

        if(measurements.isEmpty()){
            measurements.add(new Measurement(temperature, wind, humidity, visibility, date));
        }
        else{
            Measurement latestMeasurement = getLatestMeasurement();
            if(latestMeasurement != null && latestMeasurement.getDate().plusSeconds(secondsUntilAcceptNewMeasurement).isBefore(measurementDate)) {
                measurements.add(new Measurement(temperature, wind, humidity, visibility, date));
            }
        }

    }

    public int total() {
        return measurements.size();
    }

    public void status(Date from, Date to) {
        LocalDateTime fromDate = DateConverterExtensions.dateToLocalDateTimeConverterV2(from);
        LocalDateTime toDate = DateConverterExtensions.dateToLocalDateTimeConverterV2(to);

        double sum = 0.0;
        List<Measurement> measurementsBetweenDates = new LinkedList<>();
        for(Measurement measurement : measurements) {
            if((measurement.getDate().isEqual(fromDate) || measurement.getDate().isAfter(fromDate)) && (measurement.getDate().isEqual(toDate) || measurement.getDate().isBefore(toDate))) {
                measurementsBetweenDates.add(measurement);
                sum += measurement.getTemperature();
            }
        }

        if(measurementsBetweenDates.isEmpty()) {
            throw new RuntimeException();
        }

        Collections.sort(measurementsBetweenDates);
        measurementsBetweenDates.stream().forEach(System.out::println);
        double averageTemperature = sum / measurementsBetweenDates.size();
        System.out.println(String.format("Average temperature: %.2f", averageTemperature));
    }

    private Measurement getLatestMeasurement() {
        return measurements.get(total() - 1);
    }
}

public class WeatherStationTest {
    public static void main(String[] args) throws ParseException {
        Scanner scanner = new Scanner(System.in);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        int n = scanner.nextInt();
        scanner.nextLine();
        WeatherStation ws = new WeatherStation(n);
        while (true) {
            String line = scanner.nextLine();
            if (line.equals("=====")) {
                break;
            }
            String[] parts = line.split(" ");
            float temp = Float.parseFloat(parts[0]);
            float wind = Float.parseFloat(parts[1]);
            float hum = Float.parseFloat(parts[2]);
            float vis = Float.parseFloat(parts[3]);
            line = scanner.nextLine();
            Date date = df.parse(line);
            ws.addMeasurement(temp, wind, hum, vis, date);
        }
        String line = scanner.nextLine();
        Date from = df.parse(line);
        line = scanner.nextLine();
        Date to = df.parse(line);
        scanner.close();
        System.out.println(ws.total());
        try {
            ws.status(from, to);
        } catch (RuntimeException e) {
            System.out.println(e);
        }
    }
}

// vashiot kod ovde