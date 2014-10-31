
/* class represents a variable in program code */
public class Variable implements IExpression {

	private static final int numVars = 26;
	private static Variable[] vars = new Variable[Variable.numVars];
	private String rep;     // a to z
	private Integer value;  // integer value, may contain null
	
	/* 
	 * create a new variable node, rep should be a string [a-z]
	 * private constructor, to fetch instances from a pool, use GetVar
	 */
	private Variable(String rep)
	{
		this.rep = rep;
		this.value = null;
	}
	
	/*
	 * method returns a variable instance with representation rep
	 * rep - string representing the variable
	 * 
	 */
	public static Variable GetVar(String rep){
		
		int index = rep.charAt(0) - 'a';
		if(Variable.vars[index] == null){
			Variable.vars[index] = new Variable(rep);
		}
		return Variable.vars[index];
	}
	
	
	/* 
	 * method returns the variable's value
	 * returns null if variable was not initialized
	 */
	@Override 
	public Integer evaluate()
	{
		return this.value;
		
	}
	
	/* 
	 * method sets the variable's value
	 */
	public static void setVal(Variable var, int value)
	{
		var.value = value;
	}
	
	@Override
	public String toString()
	{
		return this.rep;
	}
	
	
	
	
}
