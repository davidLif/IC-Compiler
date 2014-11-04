
public class Statement {

	/* statement label number */
	private int labelNumber;
	
	/* command root */
	private ICommand commandRoot;
	
	/* basic constructor */
	public Statement(int labelNum, ICommand statCommand){
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
	
	@Override
	public String toString(){
		return String.format("[Statement %s: %s ]\n", Integer.toString(labelNumber), commandRoot.toString() );
	}
}
