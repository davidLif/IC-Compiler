

@SuppressWarnings("serial")
public class LexicalError extends Exception{
	
	private int line;     // line of error
	private int column;   // column of error
	private String value; // the text that raised the exception
	
	public LexicalError(String value, int line, int column)
	{
		this.line = line;
		this.column = column;
		this.value = value;
	}
	
	// default error constructor
	public LexicalError(String msg){
		super(msg);
		//this error was invoked by the lexer because he couldn't read some characther in the input.
		//this error is created automaticly by JFlex and doesn't resice line or column.
		line = -1;
		column = -1;
	}

	public int getLine() {
		return line;
	}

	public int getColumn() {
		return column;
	}

	public String getValue() {
		return value;
	}
	
}
