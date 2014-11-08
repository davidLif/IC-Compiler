import java_cup.runtime.*;

%%
%cup
%type Token
%line
%column

%{
	private StringBuffer stringBuilder = new StringBuffer(); // will hold string literals
	private int string_line_start;
	private int string_column_start;
	
	/* return new token of type string */
	private Token getStringToken(int id, String value)
	{

		return new Token(id, Token.STRING_TAG, value, string_line_start + 1, string_column_start + 1);
		
	}
	
	private void resetString()
	{
		this.stringBuilder.setLength(0);
		// save beginning of the string location
		this.string_line_start = yyline;
		this.string_column_start = yycolumn;
	}
	
	/* return new token of type id  */
	private Token getToken(int id)
	{
		String tag = getTokenTag(id, yytext());
		return new Token(id, tag, yytext(), yyline + 1, yycolumn + 1);
		
	}
	
	
	/* retrieve tag by id and value */
	private String getTokenTag(int id, String value)
	{
		
		String tag;
		switch(id)
		{
			case sym.ID: 
				tag = Token.ID_TAG;
				break;
			case sym.CLASSID:
				tag = Token.CLASS_TAG;
				break;
			case sym.STRING:
				tag = Token.STRING_TAG;
				break;
			case sym.INT:
				tag = Token.INTEGER_TAG;
				break;
			default:
				// if non of the above, set value as tag
				tag = value;
				break;
		}
		return tag;
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

/* define string valid characters 
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

/* process identifies */
<YYINITIAL> {ClassID}  {   return getToken(sym.CLASSID); }
<YYINITIAL> {OtherID}  {   return getToken(sym.ID);      }

/* process integers */
<YYINITIAL> {Integer}  {   return getToken(sym.INT);     }


/* process string literal  */
<YYINITIAL> \"        { 
							
								this.resetString();
								yybegin(STRING);            // parse string
					  }
/* process comment (not inline) */
<YYINITIAL> "/*"                 { yybegin(COMMENT); }

/* process inline comment */
<YYINITIAL> {InlineComment}      { /* skip */}
					  
/* 

TODO: process operators (binary, unaray), square brackets, brackets, { } , boolean operators 
figure out what exactly to do with exceptions.. 

*/

<YYINITIAL> {WhiteSpace}         { /* skip */ }


<STRING> {
  \"                             { yybegin(YYINITIAL); 
                                   return getStringToken(sym.STRING, stringBuilder.toString());
                                 }
  {StringChar}+                  { stringBuilder.append(yytext()); }
  \\t                            { stringBuilder.append("\t"); }
  \\n                            { stringBuilder.append("\n"); }
  \\\"                           { stringBuilder.append("\""); }
  \\\\                           { stringBuilder.append("\\"); }
  <<EOF>>                        {
									// string was not closed
									throw new Error("String literal:" + stringBuilder.toString() 
											+ "was not closed\n");
								}
}

<COMMENT> {
	"*/"						{ 	/*  comment closed */
									yybegin(YYINITIAL);  } 
	[^]							{   /* ignore  */  }
	<<EOF>>                     {   /* comment was not closed */
									throw new Error("Comment was not closed!\n");
								}
}

[^]                             { throw new Error("Invalid char: "+ yytext() +" line:"+ yyline + " column: " + yycolumn); }
