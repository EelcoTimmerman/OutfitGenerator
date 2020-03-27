package outfitGenerator;

import java.util.ArrayList;

import org.json.simple.JSONObject;

public class CreateOutfitHelper {
	JSONObject output = new JSONObject();
	private boolean useWeather;
	boolean custompreferences;
	private String outfitmessage;
	private String user;
	private String motivation;
	ArrayList<String[]> items = new ArrayList<>();

	@SuppressWarnings("unchecked")
	public CreateOutfitHelper(ItemData data) {
		this.user =data.getOwner();
		if(data.getItem().equals("true")) {this.custompreferences = true;}
		if(!data.getColor().equals("-1")) {this.useWeather = true;}
		output.put("useweather", this.useWeather);
		output.put("use preferences", custompreferences);
		this.motivation = data.getTemp().toLowerCase();
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject createOutfit() throws Exception{
        try ( DBconnector db = new DBconnector( "bolt://localhost:7687", "neo4j", "Neo4j1" ) ){
        	db.setProposedClean(this.user);
        	if(custompreferences) {
            	createCustomOutfit(db);
            }else {
            	createDefaultOutfit(db);
            }
        }
		output.put("items", this.items);
		this.outfitmessage += " We hope you are satisfied with your "+motivation+" outfit,"
				+ "and wish you a pleasant day.";
		output.put("outfitmessage", this.outfitmessage);
        return this.output;
	}
	
	@SuppressWarnings("unchecked")
	private void createDefaultOutfit(DBconnector db) {
        try {     	
        	proposeItem( "T-shirt", db);
        	proposeItem( "Pants", db);
        	proposeItem( "Socks", db);
        	proposeItem( "Shoes", db);
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
	private void createCustomOutfit(DBconnector db) {
        try {     	
        	String[] preferences = db.getPreferences(this.user);
        	proposeItem("T-shirt", db, preferences);
        	proposeItem("Pants", db, preferences);
        	proposeItem("Socks", db, preferences);
        	proposeItem("Shoes", db, preferences);
    		this.outfitmessage = "Roses are red, violets are blue, and because of that,"
    				+ " we selected a custom outfit for you!";
    		this.outfitmessage += " Your preferences are as follows. Fav color:"
    				+ " "+preferences[0]+", second color: "+preferences[1]+". You feel comfy only "
    						+ "when it is "+preferences[2]+" degrees.";
    		if(this.useWeather) {
    			adjustToWeather(items, db, preferences[2]);
    		}
    		output.put("status", "OK");
        }catch(Exception e){
    		output.put("status", "not ok");
        	this.outfitmessage = "You need more (clean) clothes to create an outfit! For a default"
        			+ " outfit, you need at least a t-shirt, pants, socks and shoes.";         
        }

	}
	
	private void proposeItem(String type, DBconnector db) {
    	String[] item = {type, db.proposeItem(this.user,type) };
    	items.add(item);
	}
	
	private void proposeItem(String type, DBconnector db, String[] preferences) {
		String[] item = new String[2];
		item[0]= type;
		try {
			item[1] = db.proposeItem(this.user, type, preferences[0]);
		}catch(Exception e) {
			try {
				item[1] = db.proposeItem(this.user, type, preferences[1]);
			}catch(Exception ee) {
				item[1] = db.proposeItem(this.user, type);
			}
		}	
		items.add(item);	 
	}
	
	private void adjustToWeather(ArrayList<String[]> items, DBconnector db, String comfyTemp) {
		int temp = db.getTemp();
		int comfyTempInt = Integer.parseInt(comfyTemp);
		if(temp< comfyTempInt) {
			this.outfitmessage += " It is "+temp+" degrees outside, a bit cold for you, so we are trying"
					+ " to provide you with a warm and comfy outfit. ";
			try {
	        	proposeItem("Sweater", db);
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
