

public class Number implements IExpression{

	private int value;
	
	public Number(int value)
	{
		this.value = value;
		
	}
	@Override
	public Integer evaluate() {
		
		return this.value;
	}
	
	
	public int getValue()
	{
		return this.value;
	}
	
}
