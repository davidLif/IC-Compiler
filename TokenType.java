

/*
 * TokenType represents all the possible tokens read from input:
 * 		VAR- variable [a-z]
 * 		NUM- valid number
 * 		BINO- binary operation: +, *, -, \ 
 * 		COLON- colon :
 * 		SEMCOL- semi colon ; 
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
	VAR, NUM, BINOP, COLON, SEMCOL, RPAR, LPAR,
	IF, BOOLOP, PRINT, GOTO, ASSIGN, EOF
}

