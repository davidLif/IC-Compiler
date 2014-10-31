import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PushbackInputStream;
import java.io.Reader;
import java.util.Scanner;




public class TokenGenerator {

	private static Token currentToken;
	private static PushbackInputStream buffer;
	
	
	
	/* 
	 * method to advance to the next token (updates currentToken)
	 * returns true on syntax error, returns false on success
	 */
	public static boolean advanceToNextToken(){
		
	
			int nextChar = 0;
			
			try {

				while((nextChar = buffer.read()) != -1) /* read next char */
				{
					char c = (char)nextChar;
					
					// skip spaces
					if(c == ' ')
						continue;
					if(Character.isWhitespace(c))
					{
						// invalid white space
						return true;
					}
					if(c == 'i')
					{
						
						// possible if or variable
						nextChar = buffer.read();
						if(nextChar == -1)
						{
							// invalid termination of the program
							return true;
						}
						c = (char)nextChar;
						if(c == 'f')
						{
							// possible if
							nextChar = buffer.read();
							if(nextChar == -1)
							{
								// invalid termination of the program
								return true;
							}
							c = (char)nextChar;
							if(c != '(')
							{
								// invalid if structure
								return true;
							}
							else
							{
								// valid if token
								currentToken.update(TokenType.IF, "if"); 
								buffer.unread('(');
								return false;
								
							}
							
						}
						else if(c == ' ')
						{
							// i variable
							currentToken.update(TokenType.VAR, "i");
							return false;
						}
						else
						{
							// invalid token
							return true;
						}
					}
					else if(c == 'g')
					{
						// possible goto
						String gotoString = "oto ";
						for(int i = 0; i < gotoString.length(); ++i)
						{
							nextChar = buffer.read();
							if(nextChar == -1) // invalid EOF
								return true;
							c = (char)nextChar;
							if(i == 0 && c == ' ')
							{
								// g variable
								currentToken.update(TokenType.VAR, "g");
								return false;
							}
							else if (c != gotoString.charAt(i))
							{
								// doesn't fit goto string
								return true;
							}
						}
						// at this point, we determined that it was a goto token
						currentToken.update(TokenType.GOTO, "goto");
						return false;
					}
					else if (c == 'p')
					{
						// possible print
						String printString = "rint(";
						for(int i = 0; i < printString.length(); ++i)
						{
							nextChar = buffer.read();
							if(nextChar == -1) // invalid EOF
								return true;
							c = (char)nextChar;
							if(i == 0 && c == ' ')
							{
								// p variable
								currentToken.update(TokenType.VAR, "p");
								return false;
							}
							else if (c != printString.charAt(i))
							{
								// doesn't fit print string
								return true;
							}
						}
						// at this point, we determined that it was a print token
						
						currentToken.update(TokenType.PRINT, "print");
						buffer.unread('(');
						return false;
						
					}
					else if( 'a' <= c && c <= 'z')
					{
						// possible variable
						String possibleVar = Character.toString(c);
						nextChar = buffer.read();
						if(nextChar == -1) // invalid EOF
							return true;
						c = (char)nextChar;
						if(c == ' ' || c == ')' || c == ';')
						{
							currentToken.update(TokenType.VAR, possibleVar);
							
							buffer.unread(c);
							
							return false;
							
						}
						else
						{
							// invalid token, starts with a letter, but is not
							// any of the above commands, and not a valid variable
							return true;
						}
						
					}
					else if (c == '0')
					{
						currentToken.update(TokenType.NUM, "0");
						return false;
					}
					else if ( Character.isDigit(c))
					{
						// digit or number
						StringBuilder number = new StringBuilder(Character.toString(c));
						while( (nextChar = buffer.read()) != -1 )
						{
							c = (char)nextChar;
							if(Character.isDigit(c))
							{
								// might be 0 too
								number.append(c);
							}
							else
							{
								// push back the character
								buffer.unread(c);
								currentToken.update(TokenType.NUM, number.toString());
								return false;
								
							}
						}
						
						// reached here -> invalid EOF
						return true;
					}
					else if ( c == ':')
					{
						// possible : or :=
						nextChar = buffer.read();
						if(nextChar == -1) // invalid EOF
							return true;
						c = (char)nextChar;
						if(c == '=')
						{
							// assignment
							currentToken.update(TokenType.ASSIGN, ":=");
							return false;
						}
						else
						{
							buffer.unread(c);
							currentToken.update(TokenType.COLON, ":");
							return false;
						}
					}
					else if( c == ';')
					{
						// possible ;\n
						nextChar = buffer.read();
						if(nextChar == -1) // invalid EOF
							return true;
						c = (char)nextChar;
						if(c == '\n'){
							currentToken.update(TokenType.ENDLINE, ";\n");
							return false;
						}
						// ;something, invalid
						return true;
					}
					else if( c == '+' || c == '*' || c == '-' || c == '\\')
					{
						currentToken.update(TokenType.BINOP, Character.toString(c));
						return false;
					}
					else if (c == '=' || c == '!')
					{
						// possible == or !=
						nextChar = buffer.read();
						if(nextChar == -1) // invalid EOF
							return true;
						char nextC = (char)nextChar;
						if(c == '=' && nextC == '=')
						{
							currentToken.update(TokenType.BOOLOP, "==");
							return false;
						}
						else if (c == '!' && nextC == '=')
						{
							currentToken.update(TokenType.BOOLOP, "!=");
							return false;
						}
						// otherwise, invalid token
						return true;
						
					}
					else if(c == '<' || c == '>')
					{
						// possible <, >, <=, >=
						nextChar = buffer.read();
						if(nextChar == -1) // invalid EOF
							return true;
						char nextC = (char)nextChar;
						if(nextC == '=')
						{
							// <= or >=
							currentToken.update(TokenType.BOOLOP, 
									Character.toString(c) + "=");
							return false;
						}
						else
						{
							// < or >
							buffer.unread(nextC);
							currentToken.update(TokenType.BOOLOP, 
									Character.toString(c));
							return false;
						}
					}
					else if(c == '(')
					{
						currentToken.update(TokenType.LPAR, "(");
						return false;
					}
					else if(c == ')')
					{
						currentToken.update(TokenType.RPAR, ")");
						return false;
						
					}
					else
					{
						// read something else ?
						// invalid character
						return true;
					}
	
				}
				
				// reached EOF
				currentToken.update(TokenType.EOF, "EOF");
				freeResources();
				return false;
				
						
			} catch (IOException e) {

				System.out.println("Error: could not read next char\n");
				e.printStackTrace();
			}
			
			
			return false;
		
	}
	
	/* method to retrieve the next token
	 * (use this method to get current token) */ 
	public static Token getToken()
	{
		return TokenGenerator.currentToken;
	}
	
	/* method initiates TokenGenerator by giving the input file name
	 * method opens the file for reading */
	public static void initTokenGenerator(String fileName){
		
		try {
			buffer = new PushbackInputStream(new FileInputStream(fileName));
			/* create dummy token */
			TokenGenerator.currentToken = new Token(null, "#"); 
		} catch (FileNotFoundException e) {
			System.out.println("Error: could not open file\n");
			e.printStackTrace();
		}
	}
	
	/* method closes open file resources */
	
	private static void freeResources()
	{
		try {
			buffer.close();
		} catch (IOException e) {
			System.out.println("Error: could not close file\n");
			e.printStackTrace();
		}
	}
	
	/* remove this method */
	public static Token getAndAdvance(){
		advanceToNextToken();
		return getToken();
	}
}
