

/*
 * TokenType represents all the possible tokens read from input:
 * 		VAR- variable [a-z]
 * 		NUM- valid number
 * 		BINO- binary operation: +, *, -, \ 
 * 		COLON- colon :
 * 		
 * 		RPAR- right bracket )
 * 		LPAR - left bracket (
 * 		IF - if
 * 		BOOLOP- < or > or <= or >= or == or !=
 * 		PRINT- print command
 * 		GOTO- goto command
 * 		ASSIGN- assignment command :=
 * 		EOF - EOF
 * 		SPACE- simple one char space
 * 		NEWLINE - \n
 * 		SEMCOL - ;
 */

public enum TokenType {
	VAR, NUM, BINOP, COLON, RPAR, LPAR, NEWLINE, SEMCOL, 
	IF, BOOLOP, PRINT, GOTO, ASSIGN, EOF, SPACE, INVALID;

	
	/* method returns TokenType by suiting string representation */
	public static TokenType getTypeByString(String rep)
	{
		if(rep.equals(";"))
			return SEMCOL;
		if(rep.equals(  ":"))
			return COLON;
		if(rep.equals(TokenGenerator.ifStr))
			return IF;
		if(rep.equals(TokenGenerator.printStr))
			return PRINT;
		if(rep.equals(  "("))
			return LPAR;
		if(rep.equals( " "))
			return SPACE;
		if(rep.equals( ")"))
			return RPAR;
		if(rep.equals(TokenGenerator.gotoStr))
			return GOTO;
		if(rep.equals(":="))
			return ASSIGN;
		if(rep.equals("EOF"))
			return EOF;
	
		
		char c = rep.charAt(0);
		if( c == '\n')
			return NEWLINE;
		if(c >= 'a' && c <= 'z')
			return VAR;
		if(Character.isDigit(c))
			return NUM;
		if(c == '*' || c == '+' || c == '-' || c == '\\')
			return BINOP;
		// no other option
		return BOOLOP;
	}
	

}

