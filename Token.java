
public class Token {
	
	/*
	 * token type (class) see definition
	 */
	private TokenType tokenType;
	
	/* representation, could be a variable, number, operation
	 * stored as a string
	 */
	private String rep; 
	
	/* 
	 * get methods
	 */
	
	public String getRep()
	{
		return this.rep;
	}
	
	public TokenType getTokenType()
	{
		return this.tokenType;
	}
	//this function makes a Variable object according to the token;
	public Variable tokenToVar(){
		//TODO
		return null;
	}
		
	

}
