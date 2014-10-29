
public class ComplexExpression implements IExpression{

	private char binaryOp; 
	private IExpression firstExp;
	private IExpression secExp;
	
	public ComplexExpression(char binaryOp)
	{
		this.binaryOp = binaryOp;
	}
	
	public void setFirstExp(IExpression exp)
	{
		this.firstExp = exp;
	}
	public void setSecondExp(IExpression exp)
	{
		this.secExp = exp;
	}
	
	@Override
	public Integer evaluate() {
		// TODO Auto-generated method stub
		// calc value and return
		return null;
	}

}
