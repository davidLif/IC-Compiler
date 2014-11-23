package IC.Parser;

import java_cup.runtime.Symbol;


public class Token extends Symbol{
	
	
	private int line;
	private int column;
	
	
	public int getId() { return super.sym; }
	public Object getValue() { return this.value; }
	public int getLine() { return this.line; }
	public int getColumn() { return this.column; }
	
	public Token(int id, Object value, int line, int column){
		
		super(id, value);
		this.line = line;
		this.column = column;

	}
	
	
}
