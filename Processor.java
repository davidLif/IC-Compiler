
import java.util.List;



public class Processor {

	/* index of next statement to execute */
	private static int nextStatementIndex;
	
	/* statement list to execute */
	private static List<Statement> statementList;
	
	/*
	 * statmentList - list of statements to execute, by order or by jump
	 */
	public static void Process(List<Statement> statementList)
	{
		// init fields
		Processor.statementList = statementList;
		Processor.nextStatementIndex = 0;
	}
	
	/*
	 * set index of new statement to execute, by label.
	 * (useful for goto)
	 */
	
	public static void gotoLabel(int label)
	{
		
	}
	
	
	
}
