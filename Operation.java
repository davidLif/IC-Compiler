
/* 
 * simple enum to represent binary and boolean operations
 * MUL- *
 * ADD- +
 * SUB- -
 * DIV- \
 * LESSTHAN- <
 * BIGGERTHAN- >
 * LESSEQ- <=
 * BIGEQ- >=
 * EQ- ==
 * NOTEQ- !=
 */
public enum Operation {

	MUL, ADD, SUB, DIV, LESSTHAN, BIGGERTHAN, LESSEQ, BIGEQ, EQ, NOTEQ;
	
	public static Operation getOpByString(String rep){
		if(rep.equals("*"))
			return MUL;
		if(rep.equals("+"))
			return ADD;
		if(rep.equals("-"))
			return SUB;
		if(rep.equals("\\"))
			return DIV;
		if(rep.equals("<"))
			return LESSTHAN;
		if(rep.equals(">"))
			return BIGGERTHAN;
		if(rep.equals("<="))
			return LESSEQ;
		if(rep.equals(">="))
			return BIGEQ;
		if(rep.equals("=="))
			return EQ;
		if(rep.equals("!="))
			return NOTEQ;	
		return null;
	}
	
	/* method computes binary operation op on two given inputs, x and y [ *, +, -, \ ]
	 * if a boolean operation is given by mistake, method returns 0
	 */
	public static int computeBinaryOperation(Operation op, int x, int y)
	{
		
		switch(op)
		{
		case ADD:
			return x+y;
		case SUB:
			return x-y;
		case MUL:
			return x*y;
		case DIV:
			return x/y;
		default: /* denis - returning 0 is wrong. it can be the real output. we might need to add execptions*/
			System.out.println("Error evaluating the op");
			return -1; /* change that!*/
			
		}
	}
	
	/* method computes boolean operation op on two given inputs, x and y [ <, >, <=, >=, ==, != ]
	 * if a binary operation is given by mistake, method returns false
	 */
	public static boolean computeBooleanOperation(Operation op, int x, int y)
	{
		
		switch(op)
		{
		case LESSTHAN:
			return x < y;
		case BIGGERTHAN:
			return x > y;
		case LESSEQ:
			return x <= y;
		case BIGEQ:
			return x >= y;
		case EQ:
			return x == y;
		case NOTEQ:
			return x != y;
		default:
			return false; /* denis - we might ned to add expection - false is a legitimc result */
			
		}
	}
	
	
	
}

