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

@Path("/displayitem")
public class DisplayItems {

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response disPlayItems(@Context HttpServletRequest request, ItemData itemData) throws Exception {
        String o=itemData.getOwner();
        String output = null;
        try ( DBconnector db = new DBconnector( "bolt://localhost:7687", "neo4j", "Neo4j1" ) ){
        	output = db.getItemsFromOwner(o);
        }	
		return Response.status(200).entity(output).build();
	}
}
