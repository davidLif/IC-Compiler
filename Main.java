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
	
		List<Statement> st = Parser.parseProgram();
		//System.out.println(st);
		Processor.Process(st);
	}
	

	public static void Print(int val) {
		System.out.println(""+val);
	}
	
	public static void PrintError(int line , int code) {
		System.out.println("Error! Line:"+line+" Code:"+code);
	}
	


}
