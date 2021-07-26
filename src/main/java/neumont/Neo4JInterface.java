package neumont;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.Transaction;
import org.neo4j.driver.TransactionWork;

import static org.neo4j.driver.Values.parameters;

import java.util.Map;
import java.util.Map.Entry;

public class Neo4JInterface {
	private final Driver driver;
	
	//NOTE
	//The database that we've set up has a space before all employee names.
	//To properly query, always add a space before the "additionMatch" contents if querying for a name. 
	//EmployeeId and HireDate are unaffected.
	
	public Neo4JInterface(String uri, String user, String password) {
		driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
	}
	
	public void close() throws Exception {
		driver.close();
	}
	
	public void updateNode(final String databaseName, final String nodeType, final String propertyName, 
						   final String propertyMatch, final String additionMatch, final String addition) {
		
		try(Session session = driver.session()){
			String transactionString = session.writeTransaction(new TransactionWork<String>(){
				@Override
				public String execute(Transaction tx) {
					Result result = tx.run( "USE " + databaseName + " " +
											"MATCH (a:" + nodeType +" {" + propertyName +": ' "+ propertyMatch +"'})" + 
											" SET a." + additionMatch + " = '" + addition +"' " +
											"RETURN a." + additionMatch);
					
					return result.next().get(0).asString();
					
				}
			});
			
			System.out.println(transactionString);
			
		}catch(Exception e) {
			System.err.println(e.getMessage());
		}
		
	}
	
	public void readNode(final String databaseName, final String nodeType, final String propertyName, final String propertyMatch) {
		try(Session session = driver.session()){
			
			
			
			Result queryResult = session.run("USE " + databaseName +" " +
											 "MATCH (a:"+ nodeType +"{"+ propertyName +": '"+ propertyMatch +"'}) " + 
											 "RETURN a." + propertyName);
			
			while(queryResult.hasNext()) {
				Record row = queryResult.next();
				Map<String, Object> rowFormat = row.asMap();
				
				System.out.println(row.fields());
				
				
			}
			
		}catch(Exception e) {
			System.err.println(e.getMessage());
		}
	}
	
	
	//test code
	
    public void printGreeting( final String message )
    {
        try ( Session session = driver.session() )
        {
            String greeting = session.writeTransaction( new TransactionWork<String>()
            {
                @Override
                public String execute( Transaction tx )
                {
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
	
	
}
