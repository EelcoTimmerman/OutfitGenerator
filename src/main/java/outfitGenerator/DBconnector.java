package outfitGenerator;
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
        	session.writeTransaction(tx -> tx.run("CREATE (n:ClothingPiece {type: $t, color:$c, owner: $o, state: 'clean'})",
        			parameters("t", type, "c", color, "o", owner)));       
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

    public void removeItem(String owner, String item, String color) {
    	try (Session session = driver.session()){
            session.run("MATCH (n:ClothingPiece { owner: $o, type: $t, color: $c }) DELETE n",
            		parameters("o", owner, "t", item, "c", color));
    	}
    }
    
    public Record getItem(String owner, String item) {
    	try (Session session = driver.session()){
    		Result result = session.run("MATCH (n:ClothingPiece { owner: $o, type: $t}) RETURN n",
            		parameters("o", owner, "t", item));
    		return (Record) result.next();
    	}
    }


}