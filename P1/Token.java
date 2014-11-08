import java_cup.runtime.Symbol;


public class Token extends Symbol{
	
	public static final String ID_TAG ="ID";
	public static final String CLASS_TAG = "CLASS_ID";
	public static final String INTEGER_TAG = "INTEGER";
	public static final String STRING_TAG = "STRING";
	
	private int line;
	private int column;
	private String value;
	private String tag;
	
	
	public int getId() { return super.sym; }
	public Object getValue() { return this.value; }
	public int getLine() { return this.line; }
	public int getColumn() { return this.column; }
	
	public Token(int id, String tag, String value, int line, int column){
		
		super(id);
		this.value = value;
		this.line = line;
		this.column = column;
		this.tag = tag;
		
	}
	
	public String toString()
	{
		return String.format("%s\t%s\tline: %s\tcolumn: %s\t\n", value, tag, line, column);

	}
}
