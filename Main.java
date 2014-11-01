import java.util.List;


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
		if (st==null){//Found an error in the program.
			//Print all the error massages
			for(int i=0;i<Parser.errorMassages.size();i++){
				PrintError(Parser.errorLines.get(i),Parser.errorMassages.get(i));
			}
		}
		Processor.Process(st);
	}
	

	public static void Print(int val) {
		System.out.println(""+val);
	}
	
	public static void PrintError(int line , String code) {
		System.out.println("Error! Line:"+line+" Code:"+code);
	}
	


}
