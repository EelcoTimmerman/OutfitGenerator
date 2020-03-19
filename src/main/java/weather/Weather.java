package weather;

import java.io.IOException;

import net.aksingh.owmjapis.DailyForecast;
import net.aksingh.owmjapis.OpenWeatherMap;


public class Weather {
	  public static final void main(String[] args) {
	    boolean isMetric = true;
	    String owmApiKey = "ba1623a2a70847dd9879a79a7b16cd4e"; 
	    String weatherCity = "Brisbane, AU";
	    byte forecastDays = 3;
	    OpenWeatherMap.Units units = (isMetric) ?
	    		OpenWeatherMap.Units.METRIC :
	    			OpenWeatherMap.Units.IMPERIAL;
	    OpenWeatherMap owm = new OpenWeatherMap(units, owmApiKey);
	    try {
	      DailyForecast forecast = owm.dailyForecastByCityName(weatherCity, forecastDays);
	      System.out.println("Weather for: " + forecast.getCityInstance().getCityName());
	      int numForecasts = forecast.getForecastCount();
	      for (int i = 0; i < numForecasts; i++) {
	        DailyForecast.Forecast dayForecast = forecast.getForecastInstance(i);
	        DailyForecast.Forecast.Temperature temperature = dayForecast.getTemperatureInstance();
	        System.out.println("\t" + dayForecast.getDateTime());
	        System.out.println("\tTemperature: " + temperature.getMinimumTemperature() +
	            " to " + temperature.getMaximumTemperature() + "\n");
	      }
	    }
	    catch (IOException e) {
	      e.printStackTrace();
	    }
	  }
	}

