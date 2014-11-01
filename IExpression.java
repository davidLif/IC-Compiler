
public interface IExpression {
	
	/* this interface represents an expression that can be evaluated 
	 * returns null when expression cannot be evaluated (variable was not initialized)*/
	public Integer evaluate() throws NullPointerException;
}
