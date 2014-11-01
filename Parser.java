
import java.util.ArrayList;
import java.util.List;


public class Parser {
	
	private static String curLineMistake=null;//This string holds the description off the last mistake found in the code
	public static List<String> errorMassages=new ArrayList<String>();//This list holds all the error massages we got while parsing.
	public static List<Integer> errorLines=new ArrayList<Integer>();//This list holds all the number of lines which were unvalid.
	
	// Method parses the given program and returns a list of statements.
	// If there is an error in this code, the function returns null.
	public static List<Statement> parseProgram()
	{
		List<Statement> statementList=new ArrayList<Statement>();//This is the statement list- a representation of the program.
		int currLabel = -1; //Variable to maintain last seen label, to ensure monotone growth.
		int lineNumber=-1;
		boolean errorInCode=false;//this variable assigned "true" if there was at least one mistake in the code.
		boolean endedCorrctly=false;
		String errorStart="In label:";
		Token curTokenParsing = TokenGenerator.currentToken; // fetch token reference from lexer.
		
		//
		for ( TokenGenerator.advanceToNextToken() ;curTokenParsing.getTokenType()!=TokenType.EOF; TokenGenerator.advanceToNextToken(),++lineNumber){
			
			//Check for label in the beginning of The line and set it.
			if (curTokenParsing.getTokenType()!=TokenType.NUM){
				//check for end.should be [sapce][EOF]
				if (curTokenParsing.getTokenType()==TokenType.SPACE){
					TokenGenerator.advanceToNextToken();
					if (curTokenParsing.getTokenType()==TokenType.EOF){
						endedCorrctly=true;
						break;
					}
				}
				errorInCode=true;
				errorLines.add(lineNumber);
				errorMassages.add(errorStart + "prev" + currLabel +":there is no label number.\n");
				continue;
			}
			if(currLabel >= Integer.parseInt(curTokenParsing.getRep())){
				errorInCode=true;
				errorLines.add(lineNumber);
				errorMassages.add(errorStart + currLabel +": this label number is greater than the previous one.\n");
				continue;
			}
			else{
				currLabel=Integer.parseInt(curTokenParsing.getRep());
			}
			TokenGenerator.advanceToNextToken(); // advance to next token
			
			if (curTokenParsing.getTokenType()!=TokenType.SPACE){
				errorInCode=true;
				errorLines.add(lineNumber);
				errorMassages.add(errorStart + currLabel +": no space after label number.\n");
				continue;
			}
			TokenGenerator.advanceToNextToken();
			if (curTokenParsing.getTokenType()!=TokenType.COLON){
				errorInCode=true;
				errorLines.add(lineNumber);
				errorMassages.add(errorStart + currLabel +": no colon after label number.\n");
				continue;
			}
			TokenGenerator.advanceToNextToken();
			
			if (curTokenParsing.getTokenType()!=TokenType.SPACE){
				errorInCode=true;
				errorLines.add(lineNumber);
				errorMassages.add(errorStart + currLabel +": no space after label colon.\n");
				continue;
			}
			
			//Get first command token and handle
			TokenGenerator.advanceToNextToken();
			ICommand lineCommands=parseCommand();
			
			if (lineCommands==null){ // failed to parse
				errorInCode=true;
				errorLines.add(lineNumber);
				errorMassages.add(errorStart + currLabel + ": "+curLineMistake +"\n");
				continue;
			}
			
			//check [SPACE][SEM-COL][NEWLINE][SPACE] (older)
			// check [NEWLINE]
			TokenGenerator.advanceToNextToken();
			if (curTokenParsing.getTokenType()!=TokenType.NEWLINE){
				errorInCode=true;
				errorLines.add(lineNumber);
				errorMassages.add(errorStart + currLabel +": an incorrectline end- should be [SPACE][SEM-COL][NEWLINE][SPACE].\n");
				continue;
			}
			/*TokenGenerator.advanceToNextToken();
			if (curTokenParsing.getTokenType()!=TokenType.SPACE){
				err = true;
				break;
			}
			TokenGenerator.advanceToNextToken();
			if (curTokenParsing.getTokenType()!=TokenType.SEMCOL){
				err = true;
				break;
			}
			TokenGenerator.advanceToNextToken();
			if (curTokenParsing.getTokenType()!=TokenType.NEWLINE){
				err = true;
				break;
			}
			TokenGenerator.advanceToNextToken();
			if (curTokenParsing.getTokenType()!=TokenType.SPACE){
				err = true;
				break;
			}*/
			
			// otherwise, valid statement
			statementList.add(new Statement(currLabel,lineCommands));
			
		}
		
		endedCorrctly=true;
		if (errorInCode || !endedCorrctly){
			if(!endedCorrctly){
				errorLines.add(++lineNumber);
				errorMassages.add("an incorrect ending to the program.Should be [space][EOF].");
			}
			return null;
		}
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
			curLineMistake="no such type of command exist";
			return null; /* should not happen */
		}

		return curCommand;
	}
	
	// This function builds the if command tree and check pattern
	// note that upon entry token is set to IF
	// returns null if parsing fails
	private static Commands.ifCommand ifParsing(){
		String correctFormat="The correct format should be - if (var boolOp var) command.";
		
		Token curTokenParsing = TokenGenerator.currentToken; // fetch token reference from lexer
		TokenGenerator.advanceToNextToken();                 // advance to next token
		if (curTokenParsing.getTokenType()!=TokenType.LPAR){
			curLineMistake="after if there should be a left bracket."+correctFormat;
			return null;
		}
		
		TokenGenerator.advanceToNextToken();
		if (curTokenParsing.getTokenType()!=TokenType.VAR){
			curLineMistake="after the left bracket should come a variable."+ correctFormat;
			return null;
		}
		Variable leftVar=Variable.GetVar(curTokenParsing.getRep()); // fetch variable
		
		TokenGenerator.advanceToNextToken();
		if (curTokenParsing.getTokenType()!=TokenType.SPACE){
			curLineMistake="after the first variable should come a space."+correctFormat;
			return null;
		}
		
		TokenGenerator.advanceToNextToken();
		if (curTokenParsing.getTokenType()!=TokenType.BOOLOP){
			curLineMistake="after the first variable and a space should come the boolean expression."+correctFormat;
			return null;
		}
		Operation boolOp = Operation.getOpByString(curTokenParsing.getRep());
		
		TokenGenerator.advanceToNextToken();
		if (curTokenParsing.getTokenType()!=TokenType.SPACE){
			curLineMistake="after the boolean expression should come a space."+correctFormat;
			return null;
		}
		
		TokenGenerator.advanceToNextToken();
		if (curTokenParsing.getTokenType()!=TokenType.VAR){
			curLineMistake="after the boolean expression and a space should come the second variable."+correctFormat;
			return null;
		}
		Variable rightVar=Variable.GetVar(curTokenParsing.getRep());
		
		TokenGenerator.advanceToNextToken();
		if (curTokenParsing.getTokenType()!=TokenType.RPAR){
			curLineMistake="after the second variable should come the right bracket."+correctFormat;
			return null;
		}
		
		// check last space after if structure
		TokenGenerator.advanceToNextToken();
		if (curTokenParsing.getTokenType()!=TokenType.SPACE){
			curLineMistake="after the right bracket should come a space."+correctFormat;
			return null;
		}
		
		// parse resulting command
		TokenGenerator.advanceToNextToken();
		ICommand ifTrueCommand=parseCommand();
		
		if (ifTrueCommand==null){
			curLineMistake="after the right bracket and a space should come a valid command."+correctFormat +"\tThe command wasn't valid because"+ curLineMistake;
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
			curLineMistake="after the keyword goto there should be a space";
			return null;
		}
		TokenGenerator.advanceToNextToken();
		if (curTokenParsing.getTokenType()!= TokenType.NUM){
			curLineMistake="after the keyword goto the label number to which the program will jump should be indicated";
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
			curLineMistake="in an assignment command, a space should come after the variable.";
			return null;
		}
		
		TokenGenerator.advanceToNextToken();                 // advance to next token
		if (curTokenParsing.getTokenType()!=TokenType.ASSIGN){
			curLineMistake="a command that starts with variable shold be a assignment command, and after the variable := should appear.";
			return null;
		}
		
		TokenGenerator.advanceToNextToken();                 // advance to next token
		if (curTokenParsing.getTokenType()!=TokenType.SPACE){
			curLineMistake="after the := should come a space.";
			return null;
		}
		// try to parse expression
		TokenGenerator.advanceToNextToken();
		IExpression rootExp=parseExp();
		
		if(rootExp == null){ // failed to parse expression
			curLineMistake="failed to parse the expression in the assignment command." +curLineMistake;
			return null;
		}
			
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
			curLineMistake="after the print keyword there should be a left bracket.";
			return null;
		}
		
		// try to parse expression to print
		TokenGenerator.advanceToNextToken();
		toPrint = parseExp();
		if(toPrint == null){
			return null;
		}
		
		TokenGenerator.advanceToNextToken();                 // advance to next token
		if (curTokenParsing.getTokenType()!=TokenType.RPAR){
			curLineMistake="failed to parse the expression in the print command." +curLineMistake;
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
				curLineMistake="there should be space after a binary operation.";
				return null;
			}
			// first expression
			TokenGenerator.advanceToNextToken();
			exp = parseExp();
			if(exp == null) {
				return null; // parsing error
			}
			rootBinOp.setFirstExp(exp);
			
			TokenGenerator.advanceToNextToken();
			if (curTokenParsing.getTokenType()!=TokenType.SPACE){
				curLineMistake="after the binary operation and the first expression, there should be a space.";
				return null;
			}
			
			// second expression
			TokenGenerator.advanceToNextToken();
			exp = parseExp();
			if(exp == null) {
				return null; // parsing error
			}
			rootBinOp.setSecondExp(exp);
	
			root=rootBinOp;
			break;
		default:
			curLineMistake="an valid expression.";
			return null;
		}
		
		return root;
	}
	
}
