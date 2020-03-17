package outfitGenerator;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Session;
import org.neo4j.driver.Result;
import org.neo4j.driver.Transaction;
import org.neo4j.driver.TransactionWork;
import static org.neo4j.driver.Values.parameters;

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
    
    public void addItem( final String item, final String color ){
    	//final String itemQ = "\""+ item + "\"";
    	final String theName = "'Shikar Dhawan'";
        try ( Session session = driver.session() ){
        	String Clothing = session.writeTransaction( new TransactionWork<String>(){
                    @Override
                    public String execute( Transaction tx ){
                    	Result result = tx.run( "CREATE (Dhawan:player{name:'theName' , YOB: 1990})");                   	
                        return result.single().get( 0 ).asString();
                    }
            } );
        }

    }
    

    public void addPerson(String name, String color){
        // Sessions are lightweight and disposable connection wrappers.
        try (Session session = driver.session()){
        	//session.writeTransaction(tx -> tx.run("MERGE (a:Person {name: $x})", parameters("x", name)));
            // Wrapping a Cypher Query in a Managed Transaction provides atomicity
            // and makes handling errors much easier.
            // Use `session.writeTransaction` for writes and `session.readTransaction` for reading data.
            // These methods are also able to handle connection problems and transient errors using an automatic retry mechanism.
        	//session.writeTransaction(tx -> tx.run("CREATE (n:ClothingPiece {type: $x})", parameters("x", name)));
        	session.writeTransaction(tx -> tx.run("CREATE (n:ClothingPiece {type: $x, color:$c})",
        			parameters("x", name, "c", color )));

        
        }
    }
    
    public void deleteEntireCloset() {
        try (Session session = driver.session()){
            session.writeTransaction(tx -> tx.run("DELETE (n:ClothingPiece {type: $x})"));          
   
        }
    }

    


}