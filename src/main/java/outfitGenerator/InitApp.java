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
import org.eclipse.jetty.http.HttpStatus;

@Path("start")
public class InitApp extends HttpServlet {
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response initialize(@Context HttpServletRequest request) {
		
		HttpSession session= request.getSession(true);
		org.json.simple.JSONObject obj = new org.json.simple.JSONObject(); 
		obj.put("success", false);
		obj.put("message", "PEBKAC");		  				
		
		String output = obj.toString();//new JSONResultProcessor().createJSONResponse(mancala);
		
		return Response.status(200).entity(output).build();
	}


}
