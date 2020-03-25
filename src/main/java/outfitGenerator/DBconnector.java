package outfitGenerator;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Record;
import org.neo4j.driver.Session;
import org.neo4j.driver.Result;
import org.neo4j.driver.Transaction;
import org.neo4j.driver.TransactionWork;
import org.neo4j.driver.Value;

import static org.neo4j.driver.Values.parameters;
import com.google.gson.*;
import java.util.ArrayList;



public class DBconnector implements AutoCloseable{
    private final Driver driver;

    public DBconnector( String uri, String user, String password ){
        driver = GraphDatabase.driver( uri, AuthTokens.basic( user, password ) );
    }


    @Override
    public void close() throws Exception{
        driver.close();
    }

    public void printGreeting( final String message){
        try ( Session session = driver.session() ){
            String greeting = session.writeTransaction( new TransactionWork<String>(){
                @Override
                public String execute( Transaction tx ){
                    Result result = tx.run( "CREATE (a:Greeting) " +
                                                     "SET a.message = $message " +
                                                     "RETURN a.message + ', from node ' + id(a)",
                            parameters( "message", message ) );
                    return result.single().get( 0 ).asString();
                }
            } );
            System.out.println( greeting );
        }
    }
    

    public void addItem(String type, String color, String owner){
        try (Session session = driver.session()){
        	//session.writeTransaction(tx -> tx.run("CREATE (n:ClothingPiece {type: $x})", parameters("x", name)));
        	session.run("match(m:User{username: $o}) CREATE (n:ClothingPiece{"
        			+ "type: $t, color:$c, owner: m.username, state: 'clean'})<-[:owns]-(m)"        			        			
        			,parameters("t", type, "c", color, "o", owner));       
        }
    }
    
    public String getItemsFromOwner(String owner) {
		ArrayList<Record> items = new ArrayList<>();
		String newItem = null;
    	try (Session session = driver.session()){
            Result result = session.run("MATCH (n:ClothingPiece { owner: $o }) RETURN n",
            		parameters("o", owner));
            Gson gson = new Gson();
            while (result.hasNext()){
                Record record = result.next();
        		items.add(record);         
            }
             newItem = gson.toJson(items);          
        }
		return newItem;
    }
    
    public String getCity(String owner) {
		String newItem;
    	try (Session session = driver.session()){
            Result result = session.run("MATCH (n:User { username: $o }) RETURN n.city",
            		parameters("o", owner));
            Gson gson = new Gson();
            Record record = result.next();
            newItem = gson.toJson(record);          
        }
		return newItem;
    }

    public void removeItem(String owner, String item, String color) {
    	try (Session session = driver.session()){
            session.run("MATCH (n:ClothingPiece { owner: $o, type: $t, color: $c }) DETACH DELETE n",
            		parameters("o", owner, "t", item, "c", color));
    	}
    }
    
    public String proposeItem(String owner, String item) {
    	String finalResult = null;
    	try (Session session = driver.session()){
    		Result result = session.run("MATCH (n:ClothingPiece { owner: $o, type: $t, state: 'clean'})"
    				+ "SET n.state = 'proposed' RETURN n.color LIMIT 1", parameters("o", owner, "t", item));
    		finalResult = result.next().get("n.color").toString();
    		finalResult = finalResult.substring(1, finalResult.length()-1);
    		return finalResult;
    	}

    }
    
    public void setProposedClean(String owner) {
    	try (Session session = driver.session()){
            session.run("MATCH (n:ClothingPiece { owner: $o, state: 'proposed'})"
            		+ " SET  n.state = 'clean'", parameters("o", owner));
    	}catch(Exception e){
    		
    	}
    }
    
    public void doLaundry(String owner) {
    	try (Session session = driver.session()){
            session.run("MATCH (n:ClothingPiece { owner: $o, state: 'dirty'})"
            		+ " SET  n.state = 'clean'", parameters("o", owner));
            session.run("MATCH (n:ClothingPiece { owner: $o, state: 'proposed'})"
            		+ " SET  n.state = 'clean'", parameters("o", owner));
    	}
    }
    
    public void moveItemsToBasket(String owner) {
    	try (Session session = driver.session()){
    		session.run("MATCH (n:ClothingPiece { owner: $o, state: 'proposed'})"
    				+ "SET n.state = 'dirty' RETURN n" , parameters("o", owner));
    	}
    }

    public boolean getUser(String user) {
    	try (Session session = driver.session()){
    		Result r = session.run("MATCH (n:User{username: $u}) RETURN n", parameters("u", user)); 
    		if(r.hasNext()) { return true;}
    	}
    	return false;
    }
    
    public boolean checkPassword(String user, String password) {
    	try (Session session = driver.session()){
    		Result r = session.run("MATCH (n:User{username: $u, password: $p}) RETURN n"
    				, parameters("u", user,"p", password)); 
    		if(r.hasNext()) { return true;}
    	}
    	return false;
    }
    
    public void createUser(String user, String password, String city) {
    	try (Session session = driver.session()){
        	session.writeTransaction(tx -> tx.run("CREATE (n:User {username: $u, password: $p, city: $c})",
        			parameters("u", user, "p", password, "c", city))); 
    	}
    }
    
    public void setWeather(float temp, float clouds, float rain) {
    	try (Session session = driver.session()){
    		session.run("MATCH (n:Weather) SET n = { temp: $t, clouds: $w, rain: $r }",
        			parameters("t", temp, "w", clouds, "r", rain)); 
    	}
    }
    
    
}