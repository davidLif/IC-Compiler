
import java.io.FileReader;

import java_cup.runtime.Symbol;



public class Main {

	public static void main(String[] args) {
	    Symbol currToken;
	    try {
	        FileReader txtFile = new FileReader(args[0]);
	         Yylex scanner = new Yylex(txtFile);
	        do {
	            currToken = scanner.next_token();
	            if(currToken.sym != sym.EOF)
	            	System.out.print(currToken);
	        } while (currToken.sym != sym.EOF);
	        
	        
	    
	    } catch (Exception e) {
	        throw new RuntimeException("IO Error (brutal exit)");                               
	    }
	}

}
