import java.util.function.BinaryOperator;


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
	public Integer evaluate() 
	{
		
		return  Operation.computeBinaryOperation(binaryOp, firstExp.evaluate() ,secExp.evaluate());
	}
	
	

	
	@Override
	public String toString()
	{
		return String.format("(%s %s %s)", firstExp.toString(), binaryOp.toString(), secExp.toString());
	}

}
