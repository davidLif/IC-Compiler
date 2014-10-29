
/* number expression class */
public class Number implements IExpression{

	/* simply stores the value of the number, and evaluates it as an expression */
	
	private int value;
	public Number(int value)
	{
		this.value = value;
		
	}
	@Override
	public Integer evaluate() {
		
		return this.value;
	}
	
	
}
