package outfitGenerator;
import org.json.simple.JSONObject;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Record;
import org.neo4j.driver.Session;
import org.neo4j.driver.Result;
import org.neo4j.driver.Transaction;
import org.neo4j.driver.TransactionWork;
import static org.neo4j.driver.Values.parameters;
import com.google.gson.*;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


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
        // Sessions are lightweight and disposable connection wrappers.
        try (Session session = driver.session()){
        	//session.writeTransaction(tx -> tx.run("MERGE (a:Person {name: $x})", parameters("x", name)));
            // Wrapping a Cypher Query in a Managed Transaction provides atomicity
            // and makes handling errors much easier.
            // Use `session.writeTransaction` for writes and `session.readTransaction` for reading data.
            // These methods are also able to handle connection problems and transient errors using an automatic retry mechanism.
        	//session.writeTransaction(tx -> tx.run("CREATE (n:ClothingPiece {type: $x})", parameters("x", name)));
        	session.writeTransaction(tx -> tx.run("CREATE (n:ClothingPiece {type: $t, color:$c, owner: $o})",
        			parameters("t", type, "c", color, "o", owner)));       
        }
    }
    
    public String getItemsFromOwner(String owner) {
		ArrayList<Record> items = new ArrayList<>();
		String newItem = null;
    	try (Session session = driver.session()){
            // A Managed Transaction transactions are a quick and easy way to wrap a Cypher Query.
            // The `session.run` method will run the specified Query.
            // This simpler method does not use any automatic retry mechanism.
            Result result = session.run("MATCH (n:ClothingPiece { owner: $o }) RETURN n",
            		parameters("o", owner));
            Gson gson = new Gson();
//            Record record = result.next();
//            newItem = gson.toJson(record);
            
            while (result.hasNext()){
                Record record = result.next();
        		items.add(record);         

                
                // Values can be extracted from a record by index or name.
                //System.out.println(record.get("name").asString());
            }
             newItem = gson.toJson(items);

            
        }
		return newItem;
    }

    


}