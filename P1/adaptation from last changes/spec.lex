import java_cup.runtime.*;

%%
%cup
%type Token
%line
%column
%public 
%class Lexer
%function next_token
%scanerror LexicalError


/* 
	----	NOTE:    
	a numberic value that is bigger than 2^31 will result in a lexical error (cannot be an Integer)
	
*/

%{
	/* 
		return new token of type/tag id 
		the matched value will be fetched using yytext()
	*/
	private Token getToken(int tag)
	{
		String value = yytext();
		return new Token(tag, value, yyline + 1, yycolumn + 1);
		
	}
	
	/* 
		return new token of type/tag id, with given line and column values
		the matched value will be fetched using yytext()
	*/
	private Token getToken(int tag, int line, int column)
	{
		String value = yytext();
		return new Token(tag, value, line, column);
		
	}
	
	/* error message to print on lexical error */
	private String getErrorMessage()
	{
		return String.format("%d: Lexical error: %s", yyline + 1, yytext());
	}
%}


/* define Integer literal */
PosInt = [1-9][0-9]*
Integer = ({PosInt} | 0)

/* define identifiers */
Letter_  = [a-zA-Z_]
ClassID = [A-Z] ({Letter_} | [0-9])*
OtherID   = [a-z] ({Letter_} | [0-9])*

/* define inline comment */
ContentChar = [^\r\n]
LineSeperator = \n | \r | \r\n
// inline comment may end with an EOF 
InlineComment = "//" {ContentChar}* {LineSeperator}?

/* define white spaces */
WhiteSpace = [ \t] | {LineSeperator}

/* define all the characters that can appear in a string
   all the characters between 32 (space) up to 126(~) except double quote "(34) and \ (42)
*/
StringChar = [ !#-Z] | "[" | "]" | "^" |[_-~]
String = \" ({StringChar} | "\\"n | "\\"t | \\\" | \\\\ )* \"


/* state for comment (not inline) parsing */
%state COMMENT

%eofval{
			return getToken(sym.EOF); 
%eofval}

%%
/* process reserved keywords */
<YYINITIAL> "extends" { return getToken(sym.EXTENDS); }
<YYINITIAL> "static"  { return getToken(sym.STATIC); }
<YYINITIAL> "class"   { return getToken(sym.CLASS); }
<YYINITIAL> "void"   { return getToken(sym.VOID); }
<YYINITIAL> "int"   { return getToken(sym.INT); }
<YYINITIAL> "boolean"   { return getToken(sym.BOOL); }
<YYINITIAL> "return"   { return getToken(sym.RETURN); }
<YYINITIAL> "if"   { return getToken(sym.IF); }
<YYINITIAL> "else"   { return getToken(sym.ELSE); }
<YYINITIAL> "while"   { return getToken(sym.WHILE); }
<YYINITIAL> "break"   { return getToken(sym.BREAK); }
<YYINITIAL> "continue"   { return getToken(sym.CONTINUE); }
<YYINITIAL> "this"   { return getToken(sym.THIS); }
<YYINITIAL> "new"   { return getToken(sym.NEW); }
<YYINITIAL> "length"   { return getToken(sym.LENGTH); }
<YYINITIAL> "true"   { return getToken(sym.TRUE); }
<YYINITIAL> "false"   { return getToken(sym.FALSE); }
<YYINITIAL> "null"   { return getToken(sym.NULL); }
<YYINITIAL> "string"   { return getToken(sym.STRING); }

/* process identifies */
<YYINITIAL> {ClassID}  {   return getToken(sym.CLASSID); }
<YYINITIAL> {OtherID}  {   return getToken(sym.ID);         }

/* process integers */
<YYINITIAL> {Integer}  {   
							try {
									// try to parse the number to Integer (signed int has the same range as IC spec)
									// add "-" for maximum range (worst case, we dont know if 2^31 is valid or not without the minus)
									Integer.parseInt("-" + yytext());
									return getToken(sym.INTEGER);  
									
							} catch (NumberFormatException e) {
							
								// we know for sure that this numeric expression cannot be an integer (not even a negative one)
								throw new LexicalError(getErrorMessage());

							}
							   
						}
						
/* process string literal*/
<YYINITIAL> {String}    {
							String val = yytext();
							// fetch token, but set column index as the column of last " 
							return getToken(sym.STRING_LITERAL, yyline + 1, (yycolumn + 1) + val.length() - 1);
						}
						
/* punctuation */	
<YYINITIAL> "("       { return getToken(sym.LP); }
<YYINITIAL> ")"       { return getToken(sym.RP); }
<YYINITIAL> "}"       { return getToken(sym.CRP); }
<YYINITIAL> "{"       { return getToken(sym.CLP); }
<YYINITIAL> "]"       { return getToken(sym.SQUARERB); }
<YYINITIAL> "["       { return getToken(sym.SQUARELB); }
<YYINITIAL> "."       { return getToken(sym.DOT); }
<YYINITIAL> ";"       { return getToken(sym.SEMCOL); }
<YYINITIAL> ","       { return getToken(sym.COMMA); }

/* operators */
<YYINITIAL> "+"       { return getToken(sym.ADD); }
<YYINITIAL> "-"       { return getToken(sym.MINUS); }
<YYINITIAL> "*"       { return getToken(sym.MULT); }
<YYINITIAL> "/"       { return getToken(sym.DIV); }
<YYINITIAL> "%"       { return getToken(sym.MOD); }
<YYINITIAL> "<"       { return getToken(sym.LESSTHAN); }
<YYINITIAL> "<="       { return getToken(sym.LESSTHANEQ); }
<YYINITIAL> ">"        { return getToken(sym.GREATERTHAN); }
<YYINITIAL> ">="       { return getToken(sym.GREATERTHANEQ); }
<YYINITIAL> "=="       { return getToken(sym.EQ); }
<YYINITIAL> "!="       { return getToken(sym.NOTEQ); }
<YYINITIAL> "&&"       { return getToken(sym.AND); }
<YYINITIAL> "||"       { return getToken(sym.OR); }
<YYINITIAL> "!"       { return getToken(sym.NOT); }
<YYINITIAL> "="       { return getToken(sym.ASSIGN); }


/* process comment (not inline) */
<YYINITIAL> "/*"                 { 
									yybegin(COMMENT); }

/* process inline comment */
<YYINITIAL> {InlineComment}      { /* skip */}
					  
/* process white spaces */
<YYINITIAL> {WhiteSpace}         { /* skip */ }


<COMMENT> {
	"*/"						{ 	/*  comment closed */
									yybegin(YYINITIAL);  } 
	[^]							{   /* ignore  */  }
	<<EOF>>                     {   /* comment was not closed */
									throw new LexicalError(getErrorMessage());
								}
}



[^]                             { throw new LexicalError(getErrorMessage()); }
