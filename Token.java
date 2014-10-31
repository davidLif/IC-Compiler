
public class Token {
	
	/*
	 * token type (class) see definition
	 */
	private TokenType tokenType;
	
	/* representation, could be a variable, number, operation
	 * stored as a string
	 */
	/* please specify the possibilities of the rep*/
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
		
	/* basic constructor */
	public Token(TokenType type, String rep)
	{
		this.tokenType = type;
		this.rep = rep;
	}
	
	/* update token method */
	public  void update(TokenType type, String rep)
	{
		this.tokenType = type;
		this.rep = rep;
	}
	
	

}
