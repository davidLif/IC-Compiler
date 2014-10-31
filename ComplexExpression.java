
public class ComplexExpression implements IExpression{

	private Operation binaryOp; 
	private IExpression firstExp;
	private IExpression secExp;
	
	public ComplexExpression(Operation binaryOp)
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
	
	@Override
	public String toString()
	{
		return String.format("(%s %s %s)", firstExp.toString(), binaryOp.toString(), secExp.toString());
	}

}
