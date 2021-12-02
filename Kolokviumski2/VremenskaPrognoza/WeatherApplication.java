package Kolokviumski2.VremenskaPrognoza;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

interface Subject {
    public void register(Observer o);

    public void remove(Observer o);

    public void notifyObservers();
}

interface Observer {
    public void update(float temperature, float humidity, float pressure);
}

class WeatherDispatcher implements Subject {
    List<Observer> observers;
    private float temperature;
    private float humidity;
    private float pressure;

    public WeatherDispatcher() {
        this.observers = new LinkedList<>();
        this.temperature = 0;
        this.humidity = 0;
        this.pressure = 0;
    }

    @Override
    public void register(Observer o) {
        if (this.observers.contains(o))
            return;
        if (o instanceof CurrentConditionsDisplay)
            this.observers.add(0, o);
        else {
            this.observers.add(o);
        }

    }

    public void setMeasurements(float temperature, float humidity, float pressure) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        notifyObservers();

    }

    @Override
    public void remove(Observer o) {
        this.observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        this.observers.forEach(o -> o.update(temperature, humidity, pressure));
    }
}

class CurrentConditionsDisplay implements Observer {
    private WeatherDispatcher weatherDispatcher;
    private float temperature;
    private float humidity;

    public CurrentConditionsDisplay(WeatherDispatcher weatherDispatcher) {
        this.weatherDispatcher = weatherDispatcher;
        this.weatherDispatcher.register(this);
        this.temperature = 0;
        this.humidity = 0;
    }

    @Override
    public void update(float temperature, float humidity, float pressure) {
        this.temperature = temperature;
        this.humidity = humidity;
        System.out.println(toString());
    }

    @Override
    public String toString() {
        return String.format("Temperature: %.1fF\nHumidity: %.1f%%", temperature, humidity);
    }
}

class ForecastDisplay implements Observer {
    private float currentPressure;
    private float lastPressure;
    private WeatherDispatcher weatherDispatcher;

    public ForecastDisplay(WeatherDispatcher weatherDispatcher) {
        this.weatherDispatcher = weatherDispatcher;
        this.weatherDispatcher.register(this);
        this.currentPressure = 0;
    }

    @Override
    public void update(float temperature, float humidity, float pressure) {
        lastPressure = currentPressure;
        currentPressure = pressure;
        System.out.println(toString());
    }

    @Override
    public String toString() {
        String pres = "";
        if (currentPressure > lastPressure) {
            pres = "Improving";
        }
        if (currentPressure == lastPressure) {
            pres = "Same";
        }
        if (currentPressure < lastPressure) {
            pres = "Cooler";
        }
        return String.format("Forecast: %s\n", pres);
    }
}

public class WeatherApplication {

    public static void main(String[] args) {
        WeatherDispatcher weatherDispatcher = new WeatherDispatcher();

        CurrentConditionsDisplay currentConditions = new CurrentConditionsDisplay(weatherDispatcher);
        ForecastDisplay forecastDisplay = new ForecastDisplay(weatherDispatcher);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] parts = line.split("\\s+");
            weatherDispatcher.setMeasurements(Float.parseFloat(parts[0]), Float.parseFloat(parts[1]), Float.parseFloat(parts[2]));
            if (parts.length > 3) {
                int operation = Integer.parseInt(parts[3]);
                if (operation == 1) {
                    weatherDispatcher.remove(forecastDisplay);
                }
                if (operation == 2) {
                    weatherDispatcher.remove(currentConditions);
                }
                if (operation == 3) {
                    weatherDispatcher.register(forecastDisplay);
                }
                if (operation == 4) {
                    weatherDispatcher.register(currentConditions);
                }

            }
        }
    }
}