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

@Path("/createuser")
public class CreateUser{  
    
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response doPost(@Context HttpServletRequest request, LoginData login) throws ServletException, Exception {  
        String name=login.getUsername();
        String password=login.getPassword();
        String city = login.getCity();
		JSONObject output = new JSONObject();
        try ( DBconnector db = new DBconnector( "bolt://localhost:7687", "neo4j", "Neo4j1" ) ){
        	boolean b = db.getUser(name);
    		if(b == true) {
            	output.put("message", "The user already exists");
    		}else {
            	db.createUser(name, password, city);
        		output.put("message2", "User created successfully");  
        		String stringoutput = output.toJSONString();
                return Response.status(200).entity(stringoutput).build();
    		}

        }
		String stringoutput = output.toJSONString();
        return Response.status(200).entity(stringoutput).build();

    }  
}