

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class Main {

	public static void main(String[] args) {
	   
		if(args.length == 0)
		{
			System.err.println("Error: no input was given");
			return;
		}
	    
	        try {
				FileReader txtFile = new FileReader(args[0]);
				Collection<Token> result = generateTokens(txtFile);
				if(result == null)
					return;
				
				// we've received a valid result, print it
				printTokens(result);
				
			} catch (FileNotFoundException e) {
				
				System.err.println(e.getMessage());
			}
	  
	}
	
	/* this method prints the tokens in a table */
	private static void printTokens(Collection<Token> result) {
		
		// print title
		//System.out.println("token\ttag\tline : column");
		PrintHeader();
		
		for(Token tk : result)
		{
			
			String token_tag = null;
			if(tk.getTag() == sym.INTEGER)
			{
				token_tag = Token.INTEGER_TAG;
			}
			else if(tk.getTag() == sym.STRING_LITERAL)
			{
				token_tag = Token.STRING_TAG;
			}
			else if(tk.getTag() == sym.CLASSID){
				token_tag = Token.CLASS_TAG;
			}
			else if(tk.getTag() == sym.ID)
			{
				token_tag = Token.ID_TAG;
			}
			else
				token_tag = tk.getValue();
			
			PrintToken(tk.getValue(), token_tag, tk.getLine(), tk.getColumn());
		}
	}

	
	/* 
	 * this method receives a file stream (FileReader) and returns
	 * an ordered collection of tokens.
	 * returns null on exception.
	 */
	public static Collection<Token> generateTokens(FileReader txtFile)
	{
		
		List<Token> tokenList = new ArrayList<Token>();
		Token curr;
		Lexer scanner = new Lexer(txtFile);
		try {
			// scan the tokens, until we reach EOF token
			curr = scanner.next_token();
		
			while(curr.sym != sym.EOF){
				tokenList.add(curr);
				curr = scanner.next_token();
			}
		
		} catch (LexicalError err) {
			// our exception was thrown
			
			PrintTokenError(err.getValue(), err.getLine(), err.getColumn());
			return null;
			
		} catch (IOException e) {
			
			System.err.println(e.getMessage());
			return null;
		}
		
		return tokenList;
		
	}
	
	public static void PrintToken(String token, String tag, int line, int column){
		System.out.println(token + "\t" + tag + "\t" + line + ":" + column);
	}
	
	public static void PrintTokenError(String token, int line, int column){
		if (line == -1 && column == -1){
			//this error was invoked by the lexer because he couldn't read some characther in the input.
			//this error is created automaticly by JFlex and doesn't resice line or column.
			System.err.println("Reading error!\t"+token);
		}
		else{
			System.err.println("Error!\t"+token+"\t"+"\t"+line+":"+column);
		}
	}
	public static void PrintHeader()
	{
		System.out.println("token\ttag\tline:column");
	}
	
	


}
