package neumont;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Is this enough of a project eclipse?");
		
		Neo4JInterface neoInterface = new Neo4JInterface("bolt://localhost:7687", "interface", "blah");
		
		//neoInterface.readNode("records", "Employee", "FirstName", " VERA");
		
		neoInterface.updateNode("records", "Employee", "FirstName", "VERA", "LastName", " WILL");
		
		//neoInterface.printGreeting("This is a greeting");
	}

}
