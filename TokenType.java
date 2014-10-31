

/*
 * TokenType represents all the possible tokens read from input:
 * 		VAR- variable [a-z]
 * 		NUM- valid number
 * 		BINO- binary operation: +, *, -, \ 
 * 		COLON- colon :
 * 		ENDLINE- semicolon followed by a new line char ;\n 
 * 		RPAR- right bracket )
 * 		LPAR - left bracket (
 * 		IF - if
 * 		BOOLOP- < or > or <= or >= or == or !=
 * 		PRINT- print command
 * 		GOTO- goto command
 * 		ASSIGN- assignment command :=
 * 		EOF - EOF
 * 		
 */

public enum TokenType {
	VAR, NUM, BINOP, COLON, NEWLINE, RPAR, LPAR,
	IF, BOOLOP, PRINT, GOTO, ASSIGN, EOF, SPACE, SEMCOL;
	
	
	/* method returns TokenType by suiting string representation */
	public static TokenType getTypeByString(String rep)
	{
		if(rep.equals(";"))
			return SEMCOL;
		if(rep.equals(  ":"))
			return COLON;
		if(rep.equals(  "if"))
			return IF;
		if(rep.equals(  "print"))
			return PRINT;
		if(rep.equals(  "\n"))
			return NEWLINE;
		if(rep.equals(  "("))
			return LPAR;
		if(rep.equals( " "))
			return SPACE;
		if(rep.equals( ")"))
			return RPAR;
		if(rep.equals("goto"))
			return GOTO;
		if(rep.equals(":="))
			return ASSIGN;
		if(rep.equals("EOF"))
			return EOF;
		char c = rep.charAt(0);
		if(Character.isAlphabetic(c))
			return VAR;
		if(Character.isDigit(c))
			return NUM;
		if(c == '*' || c == '+' || c == '-' || c == '\\')
			return BINOP;
		// no other option
		return BOOLOP;
	}
	
	public String ToString()
	{
		switch(this)
		{
		case VAR:
			return "VAR";
		case NUM:
			return "NUM";
		case BINOP:
			return "BINOP";
		case COLON:
			return "COLON";
		case NEWLINE:
			return "NEWLINE";
		case SPACE:
			return "SPACE";
		case SEMCOL:
			return "SEMCOL";
		case RPAR:
			return "RPAR";
		case LPAR:
			return "LPAR";
		case IF:
			return "IF";
		case PRINT:
			return "PRINT";
		case GOTO:
			return "GOTO";
		case ASSIGN:
			return "ASSIGN";
		case EOF:
			return "EOF";
		case BOOLOP:
			return "BOOLOP";
			
		}
		return null;
		
					
	}
}

