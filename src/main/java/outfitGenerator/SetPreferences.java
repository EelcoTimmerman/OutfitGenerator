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
import org.neo4j.driver.Record;

@Path("/preferences")
public class SetPreferences{  
    
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response doPost(@Context HttpServletRequest request, ItemData data) throws ServletException, Exception {  
        String user=data.getOwner();
        String prim=data.getItem();
        String sec = data.getColor();
        String temp = data.getTemp();
		JSONObject output = new JSONObject();
		output.put("temp", temp);
		output.put("prim", prim);
		output.put("sec", sec);
		output.put("user", user);

        try ( DBconnector db = new DBconnector( "bolt://localhost:7687", "neo4j", "Neo4j1" ) ){
        	db.setPreferences(user, temp, prim, sec);
        }
		String stringoutput = output.toJSONString();
        return Response.status(200).entity(stringoutput).build();

    }  
}
