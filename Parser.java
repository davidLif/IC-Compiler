
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Parser {
	
	public static List<Integer> errorMassages=new ArrayList<Integer>();//This list holds all the error massages we got while parsing.
	public static List<Integer> errorLines=new ArrayList<Integer>();//This list holds all the number of lines which were unvalid.
	private static boolean gotoError=false;
	private static Set<Integer> labelSet=new HashSet<Integer>();
	
	// Method parses the given program and returns a list of statements.
	// If there is an error in this code, the function returns null.
	public static List<Statement> parseProgram()
	{
		List<Statement> statementList=new ArrayList<Statement>();//This is the statement list- a representation of the program.
		int currLabel = -1; //Variable to maintain last seen label, to ensure monotone growth.
		int lineNumber=1;
		boolean errorInCode=false;//this variable assigned "true" if there was at least one mistake in the code.
		Token curTokenParsing = TokenGenerator.currentToken; // fetch token reference from lexer.
		
		for (;curTokenParsing.getTokenType()!=TokenType.EOF;++lineNumber){
			gotoError=false;
			if(lineNumber!=1){
				for(;curTokenParsing.getTokenType()!=TokenType.NEWLINE;TokenGenerator.advanceToNextToken());//advance from error to next line (if there was error)
			}
			if(TokenGenerator.advanceToNextToken()){//advance to the start of the next line
				errorLines.add(lineNumber);
				errorMassages.add(1);
				return null;
			}
			if (curTokenParsing.getTokenType()==TokenType.EOF){
				break;
			}
			
			
			//Check for label in the beginning of The line and set it.
			if (curTokenParsing.getTokenType()!=TokenType.NUM){
				errorInCode=true;
				errorLines.add(lineNumber);
				errorMassages.add(1);
				continue;
			}
			if(currLabel >= Integer.parseInt(curTokenParsing.getRep())){
				errorInCode=true;
				errorLines.add(lineNumber);
				errorMassages.add(3);
				continue;
			}
			else{
				currLabel=Integer.parseInt(curTokenParsing.getRep());
				labelSet.add(currLabel);
			}
			if(TokenGenerator.advanceToNextToken()){//advance
				errorLines.add(lineNumber);
				errorMassages.add(1);
				return null;
			}
			
			if (curTokenParsing.getTokenType()!=TokenType.SPACE){
				errorInCode=true;
				errorLines.add(lineNumber);
				errorMassages.add(1);
				continue;
			}
			
			if(TokenGenerator.advanceToNextToken()){//advance
				errorLines.add(lineNumber);
				errorMassages.add(1);
				return null;
			}
			if (curTokenParsing.getTokenType()!=TokenType.COLON){
				errorInCode=true;
				errorLines.add(lineNumber);
				errorMassages.add(1);
				continue;
			}
			
			if(TokenGenerator.advanceToNextToken()){//advance
				errorLines.add(lineNumber);
				errorMassages.add(1);
				return null;
			}
			if (curTokenParsing.getTokenType()!=TokenType.SPACE){
				errorInCode=true;
				errorLines.add(lineNumber);
				errorMassages.add(1);
				continue;
			}
			
			//Get first command token and handle
			TokenGenerator.advanceToNextToken();
			ICommand lineCommands=parseCommand();
			
			if (lineCommands==null){ // failed to parse
				errorInCode=true;
				errorLines.add(lineNumber);
				if (gotoError){
					errorMassages.add(3);
				}
				else{
					errorMassages.add(1);
				}
				continue;
			}
			
			//check [SPACE][SEM-COL][NEWLINE][SPACE] (older)
			// check [NEWLINE]
			if(TokenGenerator.advanceToNextToken()){//advance
				errorLines.add(lineNumber);
				errorMassages.add(1);
				return null;
			}
			if (curTokenParsing.getTokenType()!=TokenType.NEWLINE){
				errorInCode=true;
				errorLines.add(lineNumber);
				errorMassages.add(1);
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
		
		if (errorInCode){
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
			return null; /* should not happen */
		}

		return curCommand;
	}
	
	// This function builds the if command tree and check pattern
	// note that upon entry token is set to IF
	// returns null if parsing fails
	private static Commands.ifCommand ifParsing(){
		
		Token curTokenParsing = TokenGenerator.currentToken; // fetch token reference from lexer
		if(TokenGenerator.advanceToNextToken()){//advance
			return null;
		}                // advance to next token
		if (curTokenParsing.getTokenType()!=TokenType.LPAR){
			return null;
		}
		
		TokenGenerator.advanceToNextToken();
		if (curTokenParsing.getTokenType()!=TokenType.VAR){
			return null;
		}
		Variable leftVar=Variable.GetVar(curTokenParsing.getRep()); // fetch variable
		
		TokenGenerator.advanceToNextToken();
		if (curTokenParsing.getTokenType()!=TokenType.SPACE){
			return null;
		}
		
		TokenGenerator.advanceToNextToken();
		if (curTokenParsing.getTokenType()!=TokenType.BOOLOP){
			return null;
		}
		Operation boolOp = Operation.getOpByString(curTokenParsing.getRep());
		
		TokenGenerator.advanceToNextToken();
		if (curTokenParsing.getTokenType()!=TokenType.SPACE){
			return null;
		}
		
		TokenGenerator.advanceToNextToken();
		if (curTokenParsing.getTokenType()!=TokenType.VAR){
			return null;
		}
		Variable rightVar=Variable.GetVar(curTokenParsing.getRep());
		
		TokenGenerator.advanceToNextToken();
		if (curTokenParsing.getTokenType()!=TokenType.RPAR){
			return null;
		}
		
		// check last space after if structure
		TokenGenerator.advanceToNextToken();
		if (curTokenParsing.getTokenType()!=TokenType.SPACE){
			return null;
		}
		
		// parse resulting command
		TokenGenerator.advanceToNextToken();
		ICommand ifTrueCommand=parseCommand();
		
		if (ifTrueCommand==null){
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
			return null;
		}
		TokenGenerator.advanceToNextToken();
		if (curTokenParsing.getTokenType()!= TokenType.NUM){
			return null;
		}
		
		if(!labelSet.contains(Integer.parseInt(curTokenParsing.getRep()))){
			gotoError=true;
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
			return null;
		}
		
		TokenGenerator.advanceToNextToken();                 // advance to next token
		if (curTokenParsing.getTokenType()!=TokenType.ASSIGN){
			return null;
		}
		
		TokenGenerator.advanceToNextToken();                 // advance to next token
		if (curTokenParsing.getTokenType()!=TokenType.SPACE){
			return null;
		}
		// try to parse expression
		TokenGenerator.advanceToNextToken();
		IExpression rootExp=parseExp();
		
		if(rootExp == null){ // failed to parse expression
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
			return null;
		}
		
		return root;
	}
	
}
