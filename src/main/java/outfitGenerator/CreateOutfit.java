package outfitGenerator;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
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
	public Response disPlayItems(@Context HttpServletRequest request, ItemData itemData) throws Exception {
		Gson gson = new Gson();
		String o=itemData.getOwner();
        ArrayList<Record> items = new ArrayList<>();
        try ( DBconnector db = new DBconnector( "bolt://localhost:7687", "neo4j", "Neo4j1" ) ){
        	items.add( db.getItem(o,"T-shirt") );
        	items.add( db.getItem(o,"Pants") );
        	items.add( db.getItem(o,"Socks") );
        	items.add( db.getItem(o,"Shoes") );
        }catch(Exception e){
    		JSONObject output = new JSONObject();
    		output.put("message", "You need more (clean) clothes to create an outfit!");         
    		String stringoutput = output.toJSONString();
        	return Response.status(200).entity(stringoutput).build();
        }
        String finalOutfit = gson.toJson(items);
		return Response.status(200).entity(finalOutfit).build();
	}
	
	
	
}