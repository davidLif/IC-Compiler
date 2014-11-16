

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
