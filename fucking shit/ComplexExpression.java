
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
	
	/* method evaluates expression's value, returns null if some variable that was
	 * used in the expression was not initialized when used (code 4)
	 */
	@Override
	public Integer evaluate() {
		Integer firstExpVal, secExpVal;
		firstExpVal = firstExp.evaluate();
		secExpVal =   secExp.evaluate();

		if(firstExpVal == null || secExpVal == null ) return null;
		return  Operation.computeBinaryOperation(binaryOp, firstExpVal , secExpVal);
	}
	
	@Override
	public String toString()
	{
		return String.format("(%s %s %s)", firstExp.toString(), binaryOp.toString(), secExp.toString());
	}

}
