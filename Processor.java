
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class Processor {

	/* index of next statement to execute */
	private static int nextStatementIndex;
	
	/* statement list to execute */
	private static List<Statement> statementList;
	
	/* we map each label number to his statement index number in statementList */
	private static Map<Integer,Integer> labelIndexMap; 
	
	/* flag to indicate whether a command used a goto command */
	private static boolean goToHasTakenPlace = false;
	
	/* flag to indicate that a code 4 error has occurred, i.e. a program used an uninitialized variable */
	private static boolean variableErrorFlag = false;
	
	
	
	/*
	 * method interprets the code, represented by the given statementList
	 * statmentList - list of statements to execute, by order or by jump
	 */
	public static void Process(List<Statement> statementList)
	{
		// init required fields and data structures
		Processor.InitProcessor(statementList);
		
		/* execute the statements until we have reached the final one */
		while(nextStatementIndex < statementList.size())
		{
			/* execute the command inside the statement
			 */
			Statement currStatement = statementList.get(nextStatementIndex);
			try {
				currStatement.getCommand().execute();
			}
			catch (IllegalArgumentException e)
			{
				/* devision by zero was caught, report as error code 5*/
				Main.PrintError(labelIndexMap.get(currStatement.getLabel()) + 1, 5);
				return;
			}
			
			/* check code 4 error */
			if(variableErrorFlag)
			{
				/* report uninitialized variable error on current line */
				Main.PrintError(labelIndexMap.get(currStatement.getLabel()) + 1, 4);
				return;
			}
		
			
			/* check if goto jump was made */
			if(goToHasTakenPlace)
			{
				/* turn the flag off before moving to next statement */
				goToHasTakenPlace = false;
			}
			else
			{
				/* simple advance to next statement in list */
				++nextStatementIndex;
			}
			
		}
			
	}
	
	
	/* method initialized fields and data structures required for processing the code */
	private static void InitProcessor(List<Statement> statementList)
	{
		// init fields
		Processor.statementList = statementList;
		Processor.nextStatementIndex = 0;
		
		// init label to statement index map
		labelIndexMap = new HashMap<Integer,Integer>();
		initiateStatementLabelMap();
	}
	
	/*
	 * set index of new statement to execute, by label.
	 * (useful for goto commands)
	 */
	public static void gotoLabel(int label)
	{
		nextStatementIndex =  labelIndexMap.get(label);
		goToHasTakenPlace = true;
	}
	
	
	/* Initiate the map so each label number corresponds with the index of the statement in statementList*/
	private static void initiateStatementLabelMap()
	{
		for(int i =0; i < statementList.size(); i++)
		{
			labelIndexMap.put(statementList.get(i).getLabel(),i);	
		}	
	}
	

	
	/* 
	 * this method sets the error flag to true,
	 * i.e a variable was used without being initialized in current statement
	 */
	public static void turnOnErrorFlag()
	{
		variableErrorFlag = true;
	}

	
	
	
}
