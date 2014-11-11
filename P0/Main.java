import java.util.List;



/**
 * @team pooyae <pooyae@mail.tau.ac.il>
 *  Team member 206107740
 *  Team member 320882988
 *  Team member 305686297
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
				for(int i=0 ; i < Parser.codeOneErrors.size() ; i++){
					PrintError(Parser.codeOneErrors.get(i),1);
				}
				for(int i=0 ; i < Parser.codeTwoErrors.size() ; i++){
					PrintError(Parser.codeTwoErrors.get(i),2);
				}
				for(int i=0 ; i < Parser.codeThreeErrors.size() ; i++){
					PrintError(Parser.codeThreeErrors.get(i),3);
				}
			}
			else{
				Processor.Process(st);
			}
		}
	
	

	public static void Print(int val) {
		System.out.println(""+val);
	}
	
	public static void PrintError(int line , int code) {
		System.out.println("Error! Line:"+line+" Code:"+code);
	}
	
	public static void PrintError(int line , String msg) {
			System.out.println("Error! Line:"+line+" Code:"+msg);
	}

}
