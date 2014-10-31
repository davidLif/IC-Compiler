
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
	
/*	public Operation setOp(String rep){
		switch(rep){
		case "*":
			return MUL;
		case "+":
			return ADD;
		case "-":
			return SUB;
		case "\\": //this is a great danger! must be careful;
			return DIV;
		case "<":
			return LESSTHAN;
		case ">":
			return BIGGERTHAN;
		case "<=":
			return LESSEQ;
		case ">=":
			return BIGEQ;
		case "==":
			return EQ;
		case "!=":
			return NOTEQ;	
		default:
			// Syntax error and out
			return MUL;//TODO: maybe a null op?
		}
	}*/
}
