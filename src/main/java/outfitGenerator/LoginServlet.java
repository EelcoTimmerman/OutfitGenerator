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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.simple.JSONObject;
 

@Path("/login")
public class LoginServlet{  
    
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	protected Response doPost(HttpServletRequest request, LoginData login) throws ServletException, IOException {  
		JSONObject output = new JSONObject();
        String name=login.getUsername();  

		output.put("username", "u");
          
        String password=login.getPassword();  
		String stringoutput = output.toJSONString();

        return Response.status(200).entity(stringoutput).build();
    }  
}
