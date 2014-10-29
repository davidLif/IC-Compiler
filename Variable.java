import java.util.HashMap;
import java.util.Map;


public class Variable implements IExpression {

	private static Map<String, Integer> varToVal = new HashMap<String, Integer>();
	private String variable; // a to z
	
	/* 
	 * create a new variable node, rep should be a string [a-z]
	 */
	public Variable(String rep)
	{
		this.variable = rep;
	}
	
	@Override 
	public Integer evaluate()
	{
	
		// return variable value
		// MAY RETURN NULL
		return varToVal.get(this.variable);
	}
	
	// update variable value
	public static void setVal(String var, int value)
	{
		varToVal.put(var, value);
	}
	
	
	
	
}
