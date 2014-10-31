
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
}

