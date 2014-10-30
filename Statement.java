
public class Statement {

	/* statement label number */
	private int labelNumber;
	
	/* command root */
	private ICommand commandRoot;
	
	public Statement(int labelNum,ICommand statCommand){
		labelNumber=labelNum;
		commandRoot=statCommand;
	}
	
	//get functions
	public int getLabel(){
		return labelNumber;
	}
	public ICommand getCommand(){
		return commandRoot;
	}
	
}
