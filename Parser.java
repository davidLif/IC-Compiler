
import java.util.ArrayList;
import java.util.List;


public class Parser {

	/* 
	 * this list will contain the built statement list, each item in the list
	 * is a statement!
	 */
	private static List<Statement> statementList;
	
	/* variable to maintain last seen label, to ensure monotone growth */
	private static int currLabel = -1; 
	
	/* method parses the given program and returns a list of statements */
	public static List<Statement> parseProgram()
	{
		statementList = new ArrayList<Statement>();
		return statementList;
	}
	
}
