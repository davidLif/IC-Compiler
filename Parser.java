
import java.util.ArrayList;
import java.util.List;


public class Parser {
	
	private static Token curTokenParsing=null; //current token being proccessed
	
	// Method parses the given program and returns a list of statements.
	public static List<Statement> parseProgram()
	{
		List<Statement> statementList=new ArrayList<Statement>();//This is the statement list- a representation of the program.
		int currLabel = -1; //Variable to maintain last seen label, to ensure monotone growth.
		
		for (curTokenParsing=TokenGenerator.getAndAdvance();curTokenParsing.getTokenType()!=TokenType.EOF;curTokenParsing=TokenGenerator.getAndAdvance()){
			
			//Check for label in the beginning of The line and set it.
			if (curTokenParsing.getTokenType()!=TokenType.NUM){
				// Syntax error and out
				return null;
			}
			if(currLabel >= Integer.parseInt(curTokenParsing.getRep())){
				// Syntax error and out
				return null;
			}
			else{
				currLabel=Integer.parseInt(curTokenParsing.getRep());
			}
			curTokenParsing=TokenGenerator.getAndAdvance();
			if (curTokenParsing.getTokenType()!=TokenType.COLON){
				// Syntax error and out
				return null;
			}
			
			//Get first command token and handle
			curTokenParsing=TokenGenerator.getAndAdvance();
			ICommand lineCommands=parseCommand();
			if (lineCommands==null){
				return null;
			}
			
			//check ;\n
			curTokenParsing=TokenGenerator.getAndAdvance();
			if (curTokenParsing.getTokenType()!=TokenType.ENDLINE){
				// Syntax error and out
				return null;
			}
			statementList.add(new Statement(currLabel,lineCommands));
			
			//advance to next line
			curTokenParsing=TokenGenerator.getAndAdvance();
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
		case VAR:
			curCommand=assignParsing();
			break;
		case GOTO:
			curCommand=gotoParsing();
			break;
		case PRINT:
			curCommand=printParsing();
			break;
		default:
			return null;
		}
		if (curCommand==null){
			return null;
		}
		return curCommand;
	}
	
	//This function builds the if command tree and check pattern
	private static Commands.ifCommand ifParsing(){
		
		curTokenParsing=TokenGenerator.getAndAdvance();
		if (curTokenParsing.getTokenType()!=TokenType.LPAR){
			// Syntax error and out
			return null;
		}
		
		curTokenParsing=TokenGenerator.getAndAdvance();
		if (curTokenParsing.getTokenType()!=TokenType.VAR){
			// Syntax error and out
			return null;
		}
		Variable leftVar=Variable.GetVar(curTokenParsing.getRep());
		
		curTokenParsing=TokenGenerator.getAndAdvance();
		if (curTokenParsing.getTokenType()!=TokenType.BOOLOP){
			// Syntax error and out
			return null;
		}
		Operation boolOp=Operation.valueOf(curTokenParsing.getRep());
		
		curTokenParsing=TokenGenerator.getAndAdvance();
		if (curTokenParsing.getTokenType()!=TokenType.VAR){
			// Syntax error and out
			return null;
		}
		Variable rightVar=Variable.GetVar(curTokenParsing.getRep());
		
		curTokenParsing=TokenGenerator.getAndAdvance();
		if (curTokenParsing.getTokenType()!=TokenType.RPAR){
			// Syntax error and out
			return null;
		}
		
		curTokenParsing=TokenGenerator.getAndAdvance();
		ICommand ifTrueCommand=parseCommand();
		if (ifTrueCommand==null){
			// Syntax error and out
			return null;
		}
		
		return new Commands.ifCommand(leftVar,rightVar,boolOp,ifTrueCommand);
	}
	
	//This function builds the goto command tree and check pattern
	private static Commands.gotoCommand gotoParsing(){
		curTokenParsing=TokenGenerator.getAndAdvance();
		if (curTokenParsing.getTokenType()!=TokenType.NUM){
			// Syntax error and out
			return null;
		}
		return new Commands.gotoCommand(new Number(Integer.parseInt(curTokenParsing.getRep())));
	}
	
	//This function builds the goto command tree and check pattern
	private static Commands.assignCommand assignParsing(){
		
		Variable toAssign=Variable.GetVar(curTokenParsing.getRep());
			
		curTokenParsing=TokenGenerator.getAndAdvance();
		if (curTokenParsing.getTokenType()!=TokenType.ASSIGN){
			// Syntax error and out
			return null;
		}
			
		curTokenParsing=TokenGenerator.getAndAdvance();
		IExpression rootExp=parseExp();
			
		return new Commands.assignCommand(toAssign,rootExp);
	}
		
	private static Commands.printCommand printParsing(){
		curTokenParsing=TokenGenerator.getAndAdvance();
		return new Commands.printCommand(parseExp());
	}
		
		//This function builds the AST tree , FOR THE CURR TOKEN AS A ROOT. 
	private static IExpression parseExp(){
		IExpression root=null;
		switch(curTokenParsing.getTokenType()){
		case NUM:
			root=new Number(Integer.parseInt(curTokenParsing.getRep()));
			break;
		case VAR:
			root=Variable.GetVar(curTokenParsing.getRep());
			break;
		case BINOP:
			ComplexExpression rootBinOp=new ComplexExpression(Operation.valueOf(curTokenParsing.getRep()));
			curTokenParsing=TokenGenerator.getAndAdvance();
			rootBinOp.setFirstExp(parseExp());
			if (rootBinOp.firstExp==null){
				return null;
			}
			curTokenParsing=TokenGenerator.getAndAdvance();
			rootBinOp.setSecondExp(parseExp());
			if (rootBinOp.secExp==null){
				return null;
			}
			root=rootBinOp;
			break;
		default:
			// Syntax error and out
			return null;
		}
		if(root==null){
			return null;
		}
		return root;
	}
	
}
