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

%{
	
	private StringBuffer stringBuilder = new StringBuffer(); // will hold string literals
	private int string_line_start;                           // will hold the the beginning of the string location
	private int string_column_start;
	
	/* return new token of type string */
	private Token getStringToken( String value)
	{
		return new Token(sym.STRING_LITERAL, Token.STRING_TAG, value, string_line_start + 1, string_column_start + 1);
	}
	
	private void resetString()
	{
		// reset buffer
		this.stringBuilder.setLength(0);
		// save beginning of the string location
		this.string_line_start = yyline;
		this.string_column_start = yycolumn;
	}
	
	/* return new token of type id (with tag) */
	private Token getToken(int id, String tag)
	{
		return new Token(id, tag, yytext(), yyline + 1, yycolumn + 1);
		
	}
	/* return new token of type id (without tag) 
	   use this method when tag equals value
	*/
	private Token getToken(int id)
	{
		String value = yytext();
		return new Token(id, value, value, yyline + 1, yycolumn + 1);
		
	}
	

%}


/* define Integer literal */
PosInt = [1-9][0-9]*
Integer = [-]?({PosInt} | 0)

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
WhiteSpace = " " | "\t" | {LineSeperator}

/* define all the characters that can appear in a string
   all the characters between 32 (space) up to 126(~) except double quote "(34) and \ (42)
*/
StringChar = [ !#-Z] | "[" | "]" | "^" |[_-~]


/* state for string parsing */
%state STRING
/* state for complex comment parsing */
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
<YYINITIAL> {ClassID}  {   return getToken(sym.CLASSID, Token.CLASS_TAG); }
<YYINITIAL> {OtherID}  {   return getToken(sym.ID, Token.ID_TAG);         }

/* process integers */
<YYINITIAL> {Integer}  {   
							try {
									// try to parse the number to Integer (signed int has the same range as IC spec)
									Integer.parseInt(yytext());
									return getToken(sym.INTEGER, Token.INTEGER_TAG);  
									
							} catch (NumberFormatException e) {
								throw new LexicalError("Error: number " + yytext() +" is not an Integer\n");
							}
							   
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
<YYINITIAL> ">"       { return getToken(sym.GREATERTHAN); }
<YYINITIAL> ">="       { return getToken(sym.GREATERTHANEQ); }
<YYINITIAL> "=="       { return getToken(sym.EQ); }
<YYINITIAL> "!="       { return getToken(sym.NOTEQ); }
<YYINITIAL> "&&"       { return getToken(sym.AND); }
<YYINITIAL> "||"       { return getToken(sym.OR); }
<YYINITIAL> "!"       { return getToken(sym.NOT); }
<YYINITIAL> "="       { return getToken(sym.ASSIGN); }

/* process string literal  */
<YYINITIAL> \"       { 
								this.resetString();  // reset buffer and location fields
								yybegin(STRING);     // parse string
					  }
/* process comment (not inline) */
<YYINITIAL> "/*"                 { yybegin(COMMENT); }

/* process inline comment */
<YYINITIAL> {InlineComment}      { /* skip */}
					  
/* process white spaces */
<YYINITIAL> {WhiteSpace}         { /* skip */ }


<COMMENT> {
	"*/"						{ 	/*  comment closed */
									yybegin(YYINITIAL);  } 
	[^]							{   /* ignore  */  }
	<<EOF>>                     {   /* comment was not closed */
									throw new LexicalError("Comment was not closed!\n");
								}
}

<STRING> {
  \"                             { yybegin(YYINITIAL); 
                                   return getStringToken(stringBuilder.toString());
                                 }
  {StringChar}+                  { stringBuilder.append(yytext()); }
  \\t                            { stringBuilder.append("\t"); }
  \\n                            { stringBuilder.append("\n"); }
  \\\"                           { stringBuilder.append("\""); }
  \\\\                           { stringBuilder.append("\\"); }
  [^]                            { throw new LexicalError("String literal was not closed properly, line: " + (yyline+1) + " column: " + (yycolumn +1)); }
  <<EOF>>                        {
									// string was not closed
									throw new LexicalError("String literal:" + stringBuilder.toString() 
											+ "was not closed\n");
								}
}

[^]                             { throw new LexicalError("Invalid char: "+ yytext() +" line:"+ (yyline+1) + " column: " + (yycolumn+1)); }
