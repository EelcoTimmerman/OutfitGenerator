package outfitGenerator;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.simple.JSONObject;
import org.neo4j.driver.Record;
import com.google.gson.*;


@Path("/createoutfit")
public class CreateOutfit {
	private boolean useWeather;
	boolean custompreferences;
	private String outfitmessage;
	private String user;
	JSONObject output = new JSONObject();

	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createOutfit(@Context HttpServletRequest request, ItemData itemData) throws Exception {
		this.user =itemData.getOwner();
		if(itemData.getItem().equals("true")) {this.custompreferences = true;}
		if(!itemData.getColor().equals("-1")) {this.useWeather = true;}
		ArrayList<String[]> items = new ArrayList<>();
		output.put("useweather", this.useWeather);
		output.put("use preferences", custompreferences);
        try ( DBconnector db = new DBconnector( "bolt://localhost:7687", "neo4j", "Neo4j1" ) ){
        	db.setProposedClean(this.user);
        	if(custompreferences) {
            	createCustomOutfit( items, db);
            }else {
            	createDefaultOutfit( items, db);
            }
        }
        output.put("items",items.toArray());
    	output.put("outfitmessage", outfitmessage);   
		return Response.status(200).entity(output).build();

	}	
	
	@SuppressWarnings("unchecked")
	private void createDefaultOutfit(ArrayList<String[]> items ,DBconnector db) {
        try {     	
        	proposeItem( "T-shirt", items, db);
        	proposeItem( "Pants", items, db);
        	proposeItem( "Socks", items, db);
        	proposeItem( "Shoes", items, db);
    		output.put("status", "OK");
    		this.outfitmessage = "Roses are red, violets are blue, and because of that,"
    				+ " we selected a default outfit for you!";
        }catch(Exception e){
    		output.put("status", "not ok");
        	this.outfitmessage = "You need more (clean) clothes to create an outfit! For a default"
        			+ " outfit, you need at least a t-shirt, pants, socks and shoes.";         
        }

	}
	
	@SuppressWarnings("unchecked")
	private void createCustomOutfit(ArrayList<String[]> items,DBconnector db) {
        try {     	
        	//Record preferences = db.getPreferences();
        	proposeItem("T-shirt", items, db);
        	proposeItem("Pants", items, db);
        	proposeItem("Socks", items, db);
        	proposeItem("Shoes", items, db);
    		this.outfitmessage = "Roses are red, violets are blue, and because of that,"
    				+ " we selected a custom outfit for you!";
    		if(this.useWeather) {
    			adjustToWeather(items, db);
    		}
    		output.put("status", "OK");
        }catch(Exception e){
    		output.put("status", "not ok");
        	this.outfitmessage = "You need more (clean) clothes to create an outfit! For a default"
        			+ " outfit, you need at least a t-shirt, pants, socks and shoes.";         
        }

	}
	
	private void proposeItem(String type, ArrayList<String[]> items, DBconnector db) {
    	String[] item = {type, db.proposeItem(this.user,type) };
    	items.add(item);
	}
	

	
	private void adjustToWeather(ArrayList<String[]> items, DBconnector db) {
		int temp = db.getTemp();
		int comfyTemp = 20;
		if(temp< comfyTemp) {
			this.outfitmessage += " It is "+temp+" degrees outside, a bit cold for you, so we are trying"
					+ " to provide you with a warm and comfy outfit. ";
			try {
	        	proposeItem("Sweater",items, db);
			}catch(Exception e) {
				this.outfitmessage += "Unfortunately, you have no (clean) sweaters available. You will have "
						+ "to suffer in the cold. ";
			}
		}else {
			this.outfitmessage += " It is "+temp+" degrees outside, a bit warm for you, so we are trying "
					+ "to provide you with a fresh summer outfit. ";
		}

	}
	
}