package Kolokviumski2.VremenskaPrognozaEmbeddedObservable;

import java.util.Observer;
import java.util.Scanner;
import java.util.Observable;

class MyObservable extends Observable {
    public void register(Observer o) {
        addObserver(o);
    }

    public void remove(Observer o) {
        deleteObserver(o);
    }

}

class WeatherDispatcher extends MyObservable {
    private float temperature;
    private float humidity;
    private float pressure;

    public WeatherDispatcher() {

    }

    public void setMeasurements(float temperature, float humidity, float pressure) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        measurementsChanged();
    }

    private void measurementsChanged() {
        setChanged();
        notifyObservers();
    }

    public float getTemperature() {
        return temperature;
    }

    public float getHumidity() {
        return humidity;
    }

    public float getPressure() {
        return pressure;
    }
}

class CurrentConditionsDisplay implements Observer {
    Observable observable;
    private float temperature;
    private float humidity;

    public CurrentConditionsDisplay(Observable observale) {
        this.observable = observale;
        this.observable.addObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof WeatherDispatcher) {
            WeatherDispatcher weatherDispatcher = (WeatherDispatcher) o;
            this.temperature = weatherDispatcher.getTemperature();
            this.humidity = weatherDispatcher.getHumidity();
            System.out.println(toString());
        }
    }

    @Override
    public String toString() {
        return String.format("Temperature: %.1fF\nHumidity: %.1f%%\n", temperature, humidity);
    }
}

class ForecastDisplay implements Observer {
    private float currentPressure;
    private float lastPressure;
    private Observable observable;

    public ForecastDisplay(Observable observable) {
        this.observable = observable;
        this.observable.addObserver(this);
        this.currentPressure = 0;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof WeatherDispatcher) {
            WeatherDispatcher wd = (WeatherDispatcher) o;
            this.lastPressure = currentPressure;
            currentPressure = wd.getPressure();
            System.out.println(toString());
        }
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
            pres = "Coller";
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