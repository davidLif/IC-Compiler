import java.util.List;



/**
 * 
 */

/**
 * @author Denis
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		// init token generator, give file name
		TokenGenerator.initTokenGenerator(args[0]);
		
				//if(TokenGenerator.advanceToNextToken())
				//{
					// err
				//	System.out.println("invalid token\n");
				//	return;
				//}
//		Token tok = TokenGenerator.currentToken;
//			while(tok.getTokenType() != TokenType.EOF){
//				
//				System.out.print(" [ " + tok.getTokenType().toString()
//						+ " " + tok.getRep() + " ] ");
//				if(TokenGenerator.advanceToNextToken())
//				{
//					// err
//					System.out.println("invalid token\n");
//					return;
//				}
//			}
//			System.out.print(" [ " + tok.getTokenType().toString()
//					+ " " + tok.getRep() + " ] ");
//			
			
			//TokenGenerator.initTokenGenerator(args[0]);
			List<Statement> st = Parser.parseProgram();
			if(st == null) System.out.println("error!!");
	}
	

	public static void Print(int val) {
		System.out.println(""+val);
	}
	
	public static void PrintError(int line , int code) {
		System.out.println("Error! Line:"+line+" Code:"+code);
	}
	


}
