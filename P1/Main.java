

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;



public class Main {

	public static void main(String[] args) {
	   
	    
	        try {
				FileReader txtFile = new FileReader(args[0]);
				Collection<Token> result = generateTokens(txtFile);
				if(result == null)
					return;
				// we've received a valid result, print it
				printTokens(result);
				
			} catch (FileNotFoundException e) {
				
				e.printStackTrace();
			}
	  
	}
	
	private static void printTokens(Collection<Token> result) {
		
		// print title
		System.out.println("token\ttag\tline : column");
		
		for(Token tk : result)
		{
			PrintToken(tk.getValue(), tk.getTag(), tk.getLine(), tk.getColumn());
		}
	}

	/* note to reader, this method is suppose to receive a String.. (?) */
	
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
			curr = scanner.next_token();
		
			while(curr.sym != sym.EOF){
				tokenList.add(curr);
				curr = scanner.next_token();
			}
		
		} catch (LexicalError e) {
			// TODO CHANGE THIS PART
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		return tokenList;
		
	}
	
	public static void PrintToken(String token, String tag, int line, int column){
		System.out.println(token + "\t" + tag + "\t" + line + ":" + column);
	}
	


}
