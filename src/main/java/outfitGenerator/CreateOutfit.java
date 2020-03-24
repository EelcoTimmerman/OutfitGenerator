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
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createOutfit(@Context HttpServletRequest request, ItemData itemData) throws Exception {
		Gson gson = new Gson();
		String o=itemData.getOwner();
		ArrayList<String[]> items = new ArrayList<>();
		JSONObject output = new JSONObject();

        try ( DBconnector db = new DBconnector( "bolt://localhost:7687", "neo4j", "Neo4j1" ) ){
        	db.setProposedClean(o);
        	String[] tshirt = {"T-shirt", db.proposeItem(o,"T-shirt") };
        	items.add(tshirt);
        	String[] pants = {"Pants", db.proposeItem(o,"Pants") };
        	items.add(pants);
        	String[] socks = {"Socks", db.proposeItem(o,"Socks") };
        	items.add(socks);
        	String[] shoes = {"Shoes", db.proposeItem(o,"Shoes") };
        	items.add(shoes);
        }catch(Exception e){
    		output.put("message", "You need more (clean) clothes to create an outfit!");         
    		String stringoutput = output.toJSONString();
        	return Response.status(200).entity(stringoutput).build();
        }
		output.put("message", "OK");
		output.put("outfitmessage", "Roses are red, violets are blue, and because of that,"
				+ " we selected the perfect outfit for you!");
        output.put("items",items.toArray());
		return Response.status(200).entity(output).build();
	}	
	
}