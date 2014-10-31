

/*
 * TokenType represents all the possible tokens read from input:
 * 		VAR- variable [a-z]
 * 		NUM- valid number
 * 		BINO- binary operation: +, *, -, \ 
 * 		COLON- colon :
 * 		ENDLINE- semi-colon followed by a new line char ;\n 
 * 		RPAR- right bracket )
 * 		LPAR - left bracket (
 * 		IF - if
 * 		BOOLOP- < or > or <= or >= or == or !=
 * 		PRINT- print command
 * 		GOTO- goto command
 * 		ASSIGN- assignment command :=
 * 		EOF - eof
 * 		
 */

public enum TokenType {
	VAR, NUM, BINOP, COLON, ENDLINE, RPAR, LPAR,
	IF, BOOLOP, PRINT, GOTO, ASSIGN, EOF;
	
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
		case ENDLINE:
			return "ENDLINE";
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
		return super.toString();
		
					
	}
}

