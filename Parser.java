
import java.util.ArrayList;
import java.util.List;


public class Parser {
	
	
	
	// Method parses the given program and returns a list of statements.
	public static List<Statement> parseProgram()
	{
		List<Statement> statementList=new ArrayList<Statement>();//This is the statement list- a representation of the program.
		int currLabel = -1; //Variable to maintain last seen label, to ensure monotone growth.
		int lineCounter = 0;   // variable to count lines
		Token curTokenParsing = TokenGenerator.currentToken; // fetch token reference from lexer
		for ( TokenGenerator.advanceToNextToken() ; curTokenParsing.getTokenType()!=TokenType.EOF; TokenGenerator.advanceToNextToken(), ++lineCounter){
			
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
			TokenGenerator.advanceToNextToken(); // advance to next token
			
			if (curTokenParsing.getTokenType()!=TokenType.SPACE){
				// Syntax error and out
				return null;
			}
			TokenGenerator.advanceToNextToken();
			if (curTokenParsing.getTokenType()!=TokenType.COLON){
				// Syntax error and out
				return null;
			}
			TokenGenerator.advanceToNextToken();
			
			if (curTokenParsing.getTokenType()!=TokenType.SPACE){
				// Syntax error and out
				return null;
			}
			
			//Get first command token and handle
			TokenGenerator.advanceToNextToken();
			ICommand lineCommands=parseCommand();
			
			if (lineCommands==null){ // failed to parse
				return null;
			}
			
			//check [SPACE][SEM-COL][NEWLINE][SPACE]
			TokenGenerator.advanceToNextToken();
			if (curTokenParsing.getTokenType()!=TokenType.SPACE){
				// Syntax error and out
				return null;
			}
			TokenGenerator.advanceToNextToken();
			if (curTokenParsing.getTokenType()!=TokenType.SEMCOL){
				// Syntax error and out
				return null;
			}
			TokenGenerator.advanceToNextToken();
			if (curTokenParsing.getTokenType()!=TokenType.NEWLINE){
				// Syntax error and out
				return null;
			}
			TokenGenerator.advanceToNextToken();
			if (curTokenParsing.getTokenType()!=TokenType.SPACE){
				// Syntax error and out
				return null;
			}
			// otherwise, valid statement
			statementList.add(new Statement(currLabel,lineCommands));
			
		}
		
		
		/* add final check here */
		
		
		return statementList;
	}
	
	// This function call the right function to parse the command
	// returns null on parsing failure
	private static ICommand parseCommand(){
		ICommand curCommand=null;//holds the "root" command for this line
		Token curTokenParsing = TokenGenerator.currentToken; // fetch token reference from lexer
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
			return null; /* should not happen */
		}

		return curCommand;
	}
	
	// This function builds the if command tree and check pattern
	// note that upon entry token is set to IF
	// returns null if parsing fails
	private static Commands.ifCommand ifParsing(){
		
		Token curTokenParsing = TokenGenerator.currentToken; // fetch token reference from lexer
		TokenGenerator.advanceToNextToken();                 // advance to next token
		if (curTokenParsing.getTokenType()!=TokenType.LPAR){
			// Syntax error and out
			return null;
		}
		
		TokenGenerator.advanceToNextToken();
		if (curTokenParsing.getTokenType()!=TokenType.VAR){
			// Syntax error and out
			return null;
		}
		Variable leftVar=Variable.GetVar(curTokenParsing.getRep()); // fetch variable
		
		TokenGenerator.advanceToNextToken();
		if (curTokenParsing.getTokenType()!=TokenType.SPACE){
			// Syntax error and out
			return null;
		}
		
		TokenGenerator.advanceToNextToken();
		if (curTokenParsing.getTokenType()!=TokenType.BOOLOP){
			// Syntax error and out
			return null;
		}
		Operation boolOp = Operation.getOpByString(curTokenParsing.getRep());
		
		TokenGenerator.advanceToNextToken();
		if (curTokenParsing.getTokenType()!=TokenType.SPACE){
			// Syntax error and out
			return null;
		}
		
		TokenGenerator.advanceToNextToken();
		if (curTokenParsing.getTokenType()!=TokenType.VAR){
			// Syntax error and out
			return null;
		}
		Variable rightVar=Variable.GetVar(curTokenParsing.getRep());
		
		TokenGenerator.advanceToNextToken();
		if (curTokenParsing.getTokenType()!=TokenType.RPAR){
			// Syntax error and out
			return null;
		}
		
		// check last space after if structure
		TokenGenerator.advanceToNextToken();
		if (curTokenParsing.getTokenType()!=TokenType.SPACE){
			// Syntax error and out
			return null;
		}
		
		// parse resulting command
		TokenGenerator.advanceToNextToken();
		ICommand ifTrueCommand=parseCommand();
		
		if (ifTrueCommand==null){
			// Syntax error and out
			return null;
		}
		
		return new Commands.ifCommand(leftVar,rightVar,boolOp,ifTrueCommand);
	}
	
	// This function builds the goto command tree and checks pattern
	// note that upon entry token is set to GOTO
	// returns null on failure
	private static Commands.gotoCommand gotoParsing(){
		Token curTokenParsing = TokenGenerator.currentToken; // fetch token reference from lexer
		TokenGenerator.advanceToNextToken();                 // advance to next token
		
		if (curTokenParsing.getTokenType()!= TokenType.SPACE){
			// Syntax error and out
			return null;
		}
		TokenGenerator.advanceToNextToken();
		if (curTokenParsing.getTokenType()!= TokenType.NUM){
			// Syntax error and out
			return null;
		}
		return new Commands.gotoCommand(new Number(Integer.parseInt(curTokenParsing.getRep())));
	}
	
	// This function builds the assignment command tree and checks pattern
	// note that upon entry token is set to VAR
	// returns null on failure
	private static Commands.assignCommand assignParsing(){
		Token curTokenParsing = TokenGenerator.currentToken; // fetch token reference from lexer
		
		Variable toAssign= Variable.GetVar(curTokenParsing.getRep()); // get variable
			
		TokenGenerator.advanceToNextToken();                 // advance to next token
		if (curTokenParsing.getTokenType()!=TokenType.SPACE){
			// Syntax error and out
			return null;
		}
		
		TokenGenerator.advanceToNextToken();                 // advance to next token
		if (curTokenParsing.getTokenType()!=TokenType.ASSIGN){
			// Syntax error and out
			return null;
		}
		
		TokenGenerator.advanceToNextToken();                 // advance to next token
		if (curTokenParsing.getTokenType()!=TokenType.SPACE){
			// Syntax error and out
			return null;
		}
		// try to parse expression
		TokenGenerator.advanceToNextToken();
		IExpression rootExp=parseExp();
		
		if(rootExp == null) // failed to parse expression
			return null;
			
		return new Commands.assignCommand(toAssign,rootExp);
	}
	
	// This function builds the print command tree and checks pattern
	// note that upon entry token is set to PRINT
	// returns null on failure
	private static Commands.printCommand printParsing(){
		Token curTokenParsing = TokenGenerator.currentToken; // fetch token reference from lexer
		IExpression toPrint = null;
		TokenGenerator.advanceToNextToken();                 // advance to next token
		if (curTokenParsing.getTokenType()!=TokenType.LPAR){
			// Syntax error and out
			return null;
		}
		
		// try to parse expression to print
		TokenGenerator.advanceToNextToken();
		toPrint = parseExp();
		if(toPrint == null) return null;
		
		TokenGenerator.advanceToNextToken();                 // advance to next token
		if (curTokenParsing.getTokenType()!=TokenType.RPAR){
			// Syntax error and out
			return null;
		}
		return new Commands.printCommand(toPrint);
	}
		
	// this method parses expression, starting with current token
	private static IExpression parseExp(){
		Token curTokenParsing = TokenGenerator.currentToken; // fetch token reference from lexer
		IExpression root=null;
		
		switch(curTokenParsing.getTokenType()){
		case NUM:
			root=new Number(Integer.parseInt(curTokenParsing.getRep()));
			break;
		case VAR:
			root=Variable.GetVar(curTokenParsing.getRep());
			break;
		case BINOP:
			// fetch binary operator
			ComplexExpression rootBinOp = new ComplexExpression(Operation.getOpByString(curTokenParsing.getRep()));
			// fetch two expressions
			IExpression exp;
			TokenGenerator.advanceToNextToken();
			if (curTokenParsing.getTokenType()!=TokenType.SPACE){
				// Syntax error and out
				return null;
			}
			// first expression
			TokenGenerator.advanceToNextToken();
			exp = parseExp();
			if(exp == null) return null; // parsing error
			rootBinOp.setFirstExp(exp);
			
			TokenGenerator.advanceToNextToken();
			if (curTokenParsing.getTokenType()!=TokenType.SPACE){
				// Syntax error and out
				return null;
			}
			
			// second expression
			TokenGenerator.advanceToNextToken();
			exp = parseExp();
			if(exp == null) return null; // parsing error
			rootBinOp.setSecondExp(exp);
	
			root=rootBinOp;
			break;
		default:
			// Syntax error and out
			return null;
		}
		
		return root;
	}
	
}
