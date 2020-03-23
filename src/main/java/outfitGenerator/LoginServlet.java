package outfitGenerator;
import java.io.IOException;  
import java.io.PrintWriter;  
import javax.servlet.ServletException;  
import javax.servlet.http.HttpServlet;  
import javax.servlet.http.HttpServletRequest;  
import javax.servlet.http.HttpServletResponse;  
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.simple.JSONObject;
 

@Path("/login")
public class LoginServlet{  
    
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response doPost(@Context HttpServletRequest request, LoginData login) throws ServletException, Exception {  
        String name=login.getUsername();
        String password=login.getPassword();  
		JSONObject output = new JSONObject();

        try ( DBconnector db = new DBconnector( "bolt://localhost:7687", "neo4j", "Neo4j1" ) ){
    		if(db.checkPassword(name, password)) {
            	output.put("message", "Password correct.");
            	output.put("check", "true");
            	output.put("username", name);

    		}else {
            	output.put("message", "No matching data with current username and password");
            	output.put("check", "false");

    		}
        }
		String stringoutput = output.toJSONString();
        return Response.status(200).entity(stringoutput).build();
    }  
}
