
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Parser {
	
	private static boolean errorInCode=false;//this variable assigned "true" if there was at least one mistake (of all types 1-3) in the code.
	private static boolean possibleGoToError=false;  //this variable gets true if the current line we parsing has an goto command which points to a label we haven't discovered yet
											        
	
	public static List<Integer> codeOneErrors=new ArrayList<Integer>();       // lines with code one error(s)
	public static List<Integer> codeThreeErrors=new ArrayList<Integer>();     // lines with code three error(s)
	public static List<Integer> codeTwoErrors=new ArrayList<Integer>();   //this holds all the lines of goto commands which jump to labels that don't exist.
	
	private static List<Integer[]> qlabels=new ArrayList<Integer[]>();// all the goto label commands that MAY jump to labels that don't exist
	                                                                  // format: list of tuples (line, label to jump to)
	private static Set<Integer> labelSet=new HashSet<Integer>();// this set holds all the labels we saw till now in the parsing.
	private static int qLabel;                                  // if current command is a goto, this variable will hold the jump to label
	
	
	/* method handles syntax error (code 1) 
	 * adds the line to the proper lists, and removes from other lists if needed
	 * the label, if was added, is also removed.
	 * (if label was not added to labelSet, specifiy label as null )
	 */
	
	private static void setSyntaxError(Integer line, Integer label)
	{
		/* add error code 1 */
		addError(line, 1);
		/* now, remove other errors, and remove label from set*/
		if(codeTwoErrors.contains(line)) codeTwoErrors.remove(line);
		if(codeThreeErrors.contains(line)) codeTwoErrors.remove(line);
		if(label != null ) // label was set
			labelSet.remove(label);
	}
	
	// Method parses the given program and returns a list of statements.
	// If there is an error [1-3] in the given code, the function returns null.
	public static List<Statement> parseProgram()
	{
		List<Statement> statementList=new ArrayList<Statement>();//This is the statement list- a representation of the program.
		int lastCorrectLabel = -1; //Variable to maintain last (correct) seen label, to ensure monotone growth.
		int lineNumber = 1;
		
		Token curTokenParsing = TokenGenerator.currentToken; // fetch token reference from lexer.
		
		for (; curTokenParsing.getTokenType()!=TokenType.EOF; ++lineNumber){
			
			
			if(lineNumber != 1){
				/* check if we are not on NEWLINE token, meaning that, previous iteration could not find NEWLINE token
				 * if so, we will try to find the next newline (if possible) and continue parsing
				 * also note that, if lineNumber == 1 we did not have a previous iteration.
				 */
				for(;curTokenParsing.getTokenType()!= TokenType.NEWLINE && curTokenParsing.getTokenType()!=TokenType.EOF;
						TokenGenerator.advanceToNextToken());
				if(curTokenParsing.getTokenType() == TokenType.EOF)
				{
					// could not find next line.
					break;
				}
			}
			
			TokenGenerator.advanceToNextToken();
			if (curTokenParsing.getTokenType()==TokenType.EOF){
				//check if the previous line was the last command
				//or if the program is an empty program (valid program)
				break;
			}
			
			//Check for label in the beginning of The line
			if (curTokenParsing.getTokenType()!=TokenType.NUM){
				addError(lineNumber, 1);
				continue;
			}
			int newLabel = Integer.parseInt(curTokenParsing.getRep());
			
			TokenGenerator.advanceToNextToken();
			if (curTokenParsing.getTokenType()!=TokenType.SPACE){
				addError(lineNumber,1);
				continue;
			}
			
			TokenGenerator.advanceToNextToken();
			if (curTokenParsing.getTokenType()!=TokenType.COLON){
				addError(lineNumber,1);
				continue;
			}
			
			TokenGenerator.advanceToNextToken();
			if (curTokenParsing.getTokenType()!=TokenType.SPACE){
				addError(lineNumber,1);
				continue;
			}
			
			//Get first command token and handle
			TokenGenerator.advanceToNextToken();
			ICommand lineCommands= parseCommand();
			
			
			if (lineCommands==null){ // failed to parse, error 1
				addError(lineNumber,1);
				continue;
			}
			if (possibleGoToError){
				//there's a possible go to error in this line
				//qLabel contains the jump to label of the goto command
				qlabels.add(new Integer[]{lineNumber,qLabel});
				/* reset go to error flag */
				possibleGoToError= false;
			}
			
			
			// finally, check [NEWLINE]
			TokenGenerator.advanceToNextToken();
			if (curTokenParsing.getTokenType()!=TokenType.NEWLINE){
				addError(lineNumber,1);
				
				/* the goto command is always in the end, and if now the line seperator is missing this whole line is 
				 * invalid by code 1, so, we do not report other errors like invalid jumps
				 */
				
				if (!qlabels.isEmpty() && qlabels.get(qlabels.size()-1)[0] == lineNumber) {
					qlabels.remove(qlabels.size()-1);
				}
				
				continue;
			}
			
			/* if reached here, the line is valid by code 1*/
			
			labelSet.add(newLabel);
			
			/* check for error 3 (monotone labels) */
			if(lastCorrectLabel >= newLabel){
				addError(lineNumber, 3);
				
			}
			else{
				/* otherwise, set current label as max seen yet */
				lastCorrectLabel= newLabel;
			
			}
			
			// otherwise, valid statement( may have invalid goto, if there are we delete them later).
			statementList.add(new Statement(newLabel, lineCommands));
			
		}
		if (checkGotoCommandLabels()){
			errorInCode=true;
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
		TokenGenerator.advanceToNextToken(); // advance to next token
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
			/* at this point, it is a possible goto error, however, we haven't seen all labels yet
			 * we will make the final check in the end
			 */
			possibleGoToError= true;
			qLabel=Integer.parseInt(curTokenParsing.getRep());
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
	
	private static void addError(int line , int code){
		switch(code){
		case 1:
			codeOneErrors.add(line);
			break;
		case 2:
			codeTwoErrors.add(line);
			break;
		case 3:
			codeThreeErrors.add(line);
			break;
		}
		errorInCode=true;
	}
	
	//Check for all the goto commands label that we couldn't verify before, do those labels exist.
	//if any error found,return true. else return false.
	private static boolean checkGotoCommandLabels(){
		boolean errorFound=false;
		for (int i=0;i<qlabels.size();i++){
			if(!labelSet.contains(qlabels.get(i)[1])){
				errorFound=true;
				addError(qlabels.get(i)[0], 2);
			}
		}
		return errorFound;
	}
	
}
