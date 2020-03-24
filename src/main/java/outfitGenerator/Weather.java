package outfitGenerator;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.simple.JSONObject;
import net.aksingh.owmjapis.CurrentWeather;
import outfitGenerator.ItemData;
import net.aksingh.owmjapis.OpenWeatherMap;

@Path("/weather")
public class Weather {
	  	  
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response  getWeather(@Context HttpServletRequest request, ItemData weather) throws ServletException, IOException {
	    String owmApiKey = "ba1623a2a70847dd9879a79a7b16cd4e"; 
	    String cityRaw = weather.getItem();
	    String city = cityRaw+", NL";
	    OpenWeatherMap.Units units = OpenWeatherMap.Units.METRIC;
	    OpenWeatherMap owm = new OpenWeatherMap(units, owmApiKey);
	    float temp = -1;
	    float rain = 0;
	    float clouds = -1;
		JSONObject output = new JSONObject();

	    try {
		  CurrentWeather cw =  owm.currentWeatherByCityName(city);
		  if(cw.getRainInstance() != null) { rain = cw.getRainInstance().getRain();}
		  if(cw.getCloudsInstance() != null) {clouds = cw.getCloudsInstance().getPercentageOfClouds();}
		  if(cw.getMainInstance() != null) { temp = cw.getMainInstance().getTemperature();}
		  try(DBconnector db = new DBconnector( "bolt://localhost:7687", "neo4j", "Neo4j1" ) ){
			  db.setWeather(temp,clouds, rain);
		  }catch(Exception e) {
			  
		  }
	    }catch (IOException e) {
	    	e.printStackTrace();
	    	output.put("message", "No weather data available.");
	    }
		if(temp>20) {
			output.put("weathermessage", "It's a warm day today, so to make you feel comfortable"
					+ " we have selected a summer outfit for you!");
		}else {
			output.put("weathermessage", "It's a bit cold today, so to make you feel comfy " + 
					"we have selected a nice and warm outfit for you!");
		}
		output.put("rain", rain);
		output.put("clouds", clouds);         
		output.put("temp", temp);  
		output.put("city", city);         
		String stringoutput = output.toJSONString();	    
	    return Response.status(200).entity(stringoutput).build();
	  }
//<script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script> 
//<script src="//geodata.solutions/includes/countrystatecity.js"></script>
//    <select name="country" class="countries order-alpha presel-NL" id="countryId" v-model="country">
//    <option value="">Select Country</option>
//</select>
//<select name="state" class="states order-alpha" id="stateId" v-model="state">
//    <option value="">Select State</option>
//</select>
//<select name="city" class="cities order-alpha" id="cityId" v-model="city">
//    <option value="">Select City</option>
//</select>
	  
}

