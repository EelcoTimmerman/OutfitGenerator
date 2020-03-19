package outfitGenerator;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.simple.JSONObject;

@Path("/acceptoutfit")
public class AcceptOutfit {

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response acceptOutfit(@Context HttpServletRequest request, ItemData itemData) throws Exception {
		String o=itemData.getOwner();
		try ( DBconnector db = new DBconnector( "bolt://localhost:7687", "neo4j", "Neo4j1" ) ){
        	db.moveItemsToBasket(o);
        }catch(Exception e){
    		JSONObject output = new JSONObject();
    		output.put("message", "Not able to move outfit to laundry basket");         
    		String stringoutput = output.toJSONString();
        	return Response.status(200).entity(stringoutput).build();
        }
		JSONObject output = new JSONObject();
		output.put("", "");         
		String stringoutput = output.toJSONString();
    	return Response.status(200).entity(stringoutput).build();
	}
	
	
}
