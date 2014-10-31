
import java.util.ArrayList;
import java.util.List;


public class Parser {

	private static Token curTokenParsing=null; //current token being proccessed
	
	
	
	// Method parses the given program and returns a list of statements.
	public static List<Statement> parseProgram()
	{
		
		
		/* 
		 * there is a slight problem when you call getAndAdvance and i have already closed the file after EOF
		 * most likely will cause run time error. same problem with the first getAndAdvance, the token will be null
		 * you gotta call advance first 
		 * also, we can just reuse the same token (instead of allocating a new one each time, thats stupid)
		 * so, do something like this
		 * 
		 * curTokenParsing = getToken();
		 * and from this point, use advanceToNextToken() ONLY, and use curTokenParsing again (in place change, no new assignment needed)
		 * please use advanceToNextToken() before calling getToken()
		 * I could return ERROR as a token, but that would imply ERROR is a somewhat important keyword in the language itself
		 * instead, advance will return true on error
		 * so use: if(advanceToNextToken) { // print err, return null; // }
		 * sorry for the mess
		 */
		
		
		List<Statement> statementList=new ArrayList<Statement>();//This is the statement list- a representation of the program.
		int currLabel = -1; //Variable to maintain last seen label, to ensure monotone growth.
			
			for (curTokenParsing=TokenGenerator.getAndAdvance();  curTokenParsing.getTokenType()!=TokenType.EOF;  curTokenParsing=TokenGenerator.getAndAdvance()){
				
			//Check for label in the beginning of The line and set it.
			if (curTokenParsing.getTokenType()!=TokenType.NUM){
				// Syntax error and out
			}
			if(currLabel >= Integer.parseInt(curTokenParsing.getRep())){
				// Syntax error and out
			}
			else{
				currLabel=Integer.parseInt(curTokenParsing.getRep());
			}
				
				//Get first command token and handle
				curTokenParsing=TokenGenerator.getAndAdvance();
				statementList.add(new Statement(currLabel,parseCommand()));
			}
		return statementList;
	}
	
	  //This function call the right function to parse the command
		private static ICommand parseCommand(){
			ICommand curCommand=null;//holds the "root" command for this line
			switch(curTokenParsing.getTokenType()){
			case IF:
				curCommand=ifParsing();
				break;
			case ASSIGN:
				break;
			case GOTO:
				break;
			case PRINT:
				break;
			default:
				// Syntax error and out
			}
			return curCommand;
		}
		
		//This function builds the if command tree and check pattern
		
		/* 
		 * david, use Variable.GetVar(Token.getRep()) instead of tokenToVar
		 * 
		 * */
		private static Commands.ifCommand ifParsing(){
			
			curTokenParsing=TokenGenerator.getAndAdvance();
			if (curTokenParsing.getTokenType()!=TokenType.LPAR){
				// Syntax error and out
			}
			
			curTokenParsing=TokenGenerator.getAndAdvance();
			if (curTokenParsing.getTokenType()!=TokenType.VAR){
				// Syntax error and out
			}
			
			
			//Variable leftVar=curTokenParsing.tokenToVar();
			
			curTokenParsing=TokenGenerator.getAndAdvance();
			if (curTokenParsing.getTokenType()!=TokenType.BOOLOP){
				// Syntax error and out
			}
			Operation boolOp=Operation.valueOf(curTokenParsing.getRep());
			
			curTokenParsing=TokenGenerator.getAndAdvance();
			if (curTokenParsing.getTokenType()!=TokenType.VAR){
				// Syntax error and out
			}
			//Variable rightVar=curTokenParsing.tokenToVar();
			
			curTokenParsing=TokenGenerator.getAndAdvance();
			if (curTokenParsing.getTokenType()!=TokenType.RPAR){
				// Syntax error and out
			}
			
			curTokenParsing=TokenGenerator.getAndAdvance();
			ICommand ifTrueCommand=parseCommand();
			
			//return new Commands.ifCommand(leftVar,rightVar,boolOp,ifTrueCommand);
			return null; 
		}
	
}
