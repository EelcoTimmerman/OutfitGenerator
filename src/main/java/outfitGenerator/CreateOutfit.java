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



@Path("/createoutfit")
public class CreateOutfit {
	
	@SuppressWarnings("unchecked")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createOutfit(@Context HttpServletRequest request, ItemData data) throws Exception {
		CreateOutfitHelper helper = new CreateOutfitHelper(data);
		JSONObject output = null;
		try {
			output = helper.createOutfit();
		}catch(Exception e) {
			output.put("outfitmessage", "Something has gone terribly wrong,"
					+ " we are not able to provide you with an outfit");
		}
		return Response.status(200).entity(output).build();
	}	
	
}