package IC;
import IC.Parser.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import java_cup.runtime.Symbol;


/**
 * @team pooyae <pooyae@mail.tau.ac.il>
 *  Team member 206107740
 *  Team member 320882988
 *  Team member 305686297
 *
 */

public class Compiler {

	static String errorMsg = null;

	public static void main(String[] args) {
	   
		
		try {
			FileReader txtFile = new FileReader(args[0]);
			Lexer scanner = new Lexer(txtFile);
			parser parser = new parser(scanner);
			
			try {
				Symbol parseSym = parser.parse();
				// logic
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}
	
	/* this method prints the tokens in a table */
//	private static void printTokens(Collection<Token> result) {
//		
//		// print title
//		PrintHeader();
//		
//		for(Token tk : result)
//		{
//			
//			String token_tag = null;
//			if(tk.getTag() == sym.INTEGER)
//			{
//				token_tag = Token.INTEGER_TAG;
//			}
//			else if(tk.getTag() == sym.STRING_LITERAL)
//			{
//				token_tag = Token.STRING_TAG;
//			}
//			else if(tk.getTag() == sym.CLASSID){
//				token_tag = Token.CLASS_TAG;
//			}
//			else if(tk.getTag() == sym.ID)
//			{
//				token_tag = Token.ID_TAG;
//			}
//			else
//				token_tag = tk.getValue();
//			
//			PrintToken(tk.getValue(), token_tag, tk.getLine(), tk.getColumn());
//		}
//		
//		if (errorMsg != null) {
//			PrintTokenError(errorMsg);
//		}
//	}

	
	/* 
	 * this method receives a file stream (FileReader) and returns
	 * an ordered collection of tokens.
	 * returns null on exception.
	 */
//	public static Collection<Token> generateTokens(FileReader txtFile)
//	{
//		
//		List<Token> tokenList = new ArrayList<Token>();
//		Token curr;
//		Lexer scanner = new Lexer(txtFile);
//		try {
//			// scan the tokens, until we reach EOF token
//			curr = scanner.next_token();
//		
//			while(curr.sym != sym.EOF){
//				tokenList.add(curr);
//				curr = scanner.next_token();
//			}
//		
//		} catch (LexicalError err) {
//			// our exception was thrown
//			
//			errorMsg=err.getMessage();
//			return tokenList;
//			
//		} catch (IOException e) {
//			
//			errorMsg=e.getMessage();
//			return tokenList;
//		}
//		
//		return tokenList;
//		
//	}
	
	public static void PrintToken(String token, String tag, int line, int column){
		System.out.println(token + "\t" + tag + "\t" + line + ":" + column);
	}
	
	public static void PrintTokenError(String errorMsg){
		System.err.println("Error!\t"+errorMsg);
	}
	public static void PrintHeader()
	{
		System.out.println("token\ttag\tline :column");
	
	}
	
	


}

