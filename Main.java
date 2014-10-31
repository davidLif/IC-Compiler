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
		
				if(TokenGenerator.advanceToNextToken())
				{
					// err
					System.out.println("invalid token\n");
					return;
				}
				
			while(TokenGenerator.getToken().getTokenType() != TokenType.EOF){
				
				System.out.print(" [ " + TokenGenerator.getToken().getTokenType().toString()
						+ " " + TokenGenerator.getToken().getRep() + " ] ");
				if(TokenGenerator.advanceToNextToken())
				{
					// err
					System.out.println("invalid token\n");
					return;
				}
			}
			System.out.print(" [ " + TokenGenerator.getToken().getTokenType().toString()
					+ " " + TokenGenerator.getToken().getRep() + " ] ");
	}
	

	public static void Print(int val) {
		System.out.println(""+val);
	}
	
	public static void PrintError(int line , int code) {
		System.out.println("Error! Line:"+line+" Code:"+code);
	}
	


}
