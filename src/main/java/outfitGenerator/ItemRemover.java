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

@Path("/removeitem")
public class ItemRemover {

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response disPlayItems(@Context HttpServletRequest request, ItemData itemData) throws Exception {
        String o=itemData.getOwner();
        String i = itemData.getItem();
        String c = itemData.getColor();
        try ( DBconnector db = new DBconnector( "bolt://localhost:7687", "neo4j", "Neo4j1" ) ){
        	db.removeItem(o,i,c);
        }	
        JSONObject output = new JSONObject();
		output.put("owner", o);
		output.put("type", i);
		output.put("color", c);
        String stringoutput = output.toJSONString();
		return Response.status(200).entity(stringoutput).build();
	}
}
