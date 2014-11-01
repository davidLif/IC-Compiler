
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


import java.io.PushbackInputStream;


/* 
 * class generates tokens, one by one, for the parser 
 * currentToken is a singleton to contain current token, and will be updated in-place by the class,
 * each time advanceToNextToken is called
 */
public class TokenGenerator {

	public static Token currentToken;
	private static PushbackInputStream buffer;
	
	
	
	/* 
	 * method sets current token according to given string representation 
	 */
	private static void updateTokenByRep(String rep)
	{
		currentToken.update(TokenType.getTypeByString(rep), rep);
    }
	
	
	/* 
	 * method checks whether the buffer contains the given keyword
	 * (in the beginning of the stream )
	 * if so, returns true, otherwise, returns false
	 * (note, if the answer is no, the stream is restored to previous condition
	 *        if the answer is yes, the stream will no longer contain the keyword)
	 */
	private static boolean isKeywordToken(String keyword) throws IOException
	{
		

		int nextChar;
		char c;
		
		for(int i = 0; i < keyword.length(); ++i)
		{
			nextChar = buffer.read();
			if(nextChar == -1) {
				// invalid EOF, push string back
				for(int j = i-1; j >= 0; --j)
					buffer.unread(keyword.charAt(j));
				return false;
			}
			
			c = (char)nextChar;
			
			if (c != keyword.charAt(i))
			{
				// doesn't fit the string
				// push the string back on buffer
				buffer.unread(c);
				for(int j = i-1; j >= 0; --j)
				{
					buffer.unread(keyword.charAt(j));
				}
				return false;
			}
		}
		
		// string was indeed on the top of the buffer
		return true;
	}
	
	
	
	
	
	/* keywords in our program language */
	public static final String gotoStr = "goto";
	public static final String printStr = "print";
	public static final String ifStr = "if";
	/* special sequence to mark end of statement */
	public static final String lineSeperator = " ;\n";
	
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
					
					// search for keywords
					String[] keywords = {gotoStr, printStr, ifStr, lineSeperator };
					for(int i = 0; i < keywords.length; ++i)
					{
						if(c == keywords[i].charAt(0))
						{
							// check if rest of keyword is on the buffer
							if(isKeywordToken(keywords[i].substring(1)))
							{
								// it was on buffer (now popped out)
								TokenGenerator.updateTokenByRep(keywords[i]);
								return false;
							}
						}
					}
					
					/* 
					 *  take care of single character tokens (that are not prefixes to other valid tokens)
					 *  including variables
					 */
					if(c == ' ' || /* c == '\n' || c == ';' ||*/  c == '0' || c == '+' || c == '-' || c == '*' || c == '\\'
							|| c == '(' || c == ')' || Character.isAlphabetic(c))
					{
						TokenGenerator.updateTokenByRep(Character.toString(c));
						return false;
					}
					
					/* take care of numbers (zero was checked before) */

					if ( Character.isDigit(c))
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
								// update token
								TokenGenerator.updateTokenByRep(number.toString());
								return false;
								
							}
						}
						
						// reached here -> invalid EOF
						return true;
					}
					
					/* take care of  : or := */
					
					if ( c == ':')
					{
						// possible : or :=
						nextChar = buffer.read();
						if(nextChar == -1) // invalid EOF
							return true;
						c = (char)nextChar;
						if(c == '=')
						{
							// assignment
							updateTokenByRep(":=");
							return false;
						}
						else
						{
							// colon
							buffer.unread(c);
							updateTokenByRep(":");
							return false;
						}
					}
					
					
					/* take care of == and != */
					if (c == '=' || c == '!')
					{
						// possible == or !=
						nextChar = buffer.read();
						if(nextChar == -1) // invalid EOF
							return true;
						char nextC = (char)nextChar;
						if(c == '=' && nextC == '=')
						{
							updateTokenByRep("==");
							return false;
						}
						else if (c == '!' && nextC == '=')
						{
							updateTokenByRep("!=");
							return false;
						}
						
						// otherwise, invalid token
						return true;
						
					}
					
					/* take care of <, >, <=, >= */
					if(c == '<' || c == '>')
					{
						// possible <, >, <=, >=
						nextChar = buffer.read();
						if(nextChar == -1) // invalid EOF
							return true;
						char nextC = (char)nextChar;
						if(nextC == '=')
						{
							// <= or >=
							updateTokenByRep(Character.toString(c) + "=");
							return false;
						}
						else
						{
							// < or >
							buffer.unread(nextC);
							updateTokenByRep(Character.toString(c));
							return false;
						}
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
	

	private static final int maxBufferUnread = 10;
	/* method initiates TokenGenerator by giving the input file name
	 * method opens the file for reading */
	public static void initTokenGenerator(String fileName){
		
		try {
			buffer = new PushbackInputStream(new FileInputStream(fileName), maxBufferUnread);
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
	
	
}
