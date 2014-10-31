
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class Processor {

	/* index of next statement to execute */
	private static int nextStatementIndex;
	
	/* statement list to execute */
	private static List<Statement> statementList;
	
	private static Map<Integer,Integer> labelIndexMap; /* we map each label number to his statement index number */

	/*
	 * statmentList - list of statements to execute, by order or by jump
	 */
	public Processor(List<Statement> statementList)
	{
		// init fields
		Processor.statementList = statementList;
		Processor.nextStatementIndex = 0;
		labelIndexMap = new HashMap<Integer,Integer>();
		initiateStatementLabelMap();
	}
	
	public static void increaseIndex()
	{
		 nextStatementIndex++;
	}

	
	
	
	/* Initiate the map so each label number corresponds with the index of the statement */
	public void initiateStatementLabelMap()
	{
		for(int i =0; i < statementList.size(); i++)
		{
			labelIndexMap.put(statementList.get(i).getLabel(),i);	
		}	
	}
	
	/*
	 * set index of new statement to execute, by label.
	 * (useful for goto)
	 */
	
	public static void gotoLabel(int label)
	{
		nextStatementIndex =  getIndexFromLine(label);
		
	}
	
	/* given  a label number - returns the statement index that corresponds with it */
	public static int getIndexFromLine(int line)
	{
		return labelIndexMap.get(line);
	}
	
	
	/*process the statements*/
	public void process()
	{
		while ( nextStatementIndex <= statementList.size() - 1)
		{
			statementList.get(nextStatementIndex).getCommand().execute();
		}
		
		
		
	}

	
	
	
}
