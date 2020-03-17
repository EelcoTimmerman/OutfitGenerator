package outfitGenerator;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
//import org.eclipse.jetty.http.HttpStatus;

import org.json.simple.JSONObject;

@Path("/additem")
public class itemAdder {
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response receiveItem(@Context HttpServletRequest request, ItemData itemData) throws Exception {
        String i=itemData.getItem();
        String c=itemData.getColor();
        try ( DBconnector db = new DBconnector( "bolt://localhost:7687", "neo4j", "Neo4j1" ) ){
            //db.addItem(i, c);
            //db.printGreeting("joo");
        	db.addPerson(i,c);
        }
		
		JSONObject output = new JSONObject();
		output.put("color", c);         
		String stringoutput = output.toJSONString();
		
		return Response.status(200).entity(stringoutput).build();
	}


}
