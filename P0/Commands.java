
public class Commands {

	/* the following classes represents commands in GIVEN INPUT CODE, that can be interpreted by executing them */
	
	public static class ifCommand implements ICommand
	{
		private Variable leftVar;
		private Variable rightVar;
		private Operation booleanOp;
		private ICommand commandToTake;

		/* compute the boolean expression, take command if result is true */
		@Override
		public void execute() {	
			/* first, check if both the variables were initialized */
			Integer leftVarVal = leftVar.evaluate();
			Integer rightValVal = rightVar.evaluate();
			if(leftVarVal == null || rightValVal == null) {
				/* error code 4 occurred */
				Processor.turnOnErrorFlag();
				return;
			}
			/* else, see if we need to take the branch */
			if(Operation.computeBooleanOperation(booleanOp, leftVarVal, rightValVal))
			{
				commandToTake.execute();
			}
		}
		
		public ifCommand(Variable leftVar,Variable rightVar,Operation booleanOp,ICommand commandToTake){
			this.leftVar=leftVar;
			this.rightVar=rightVar;
			this.booleanOp=booleanOp;
			this.commandToTake=commandToTake;
		}
		
		@Override
		public String toString()
		{
			return String.format("if (%s %s %s) then %s", leftVar.toString(), booleanOp.toString(), 
					rightVar.toString(), commandToTake.toString());
		}
		
	}
	
	public static class gotoCommand implements ICommand
	{
		private Number labelNumber; /* program label to jump to */
		
		public gotoCommand(Number label) {
			labelNumber=label;
		}

		/* method updates Processor's program counter index, by jumping to the label 
		 * note: since label is a number, it is always initialized
		 * */
		@Override
		public void execute() {
			Processor.gotoLabel(labelNumber.evaluate());
		}
		
		@Override
		public String toString()
		{
			return String.format("goto %s", labelNumber.toString());
		}
	}
	public static class printCommand implements ICommand
	{
		private IExpression exp;

		@Override
		public void execute() {
			Integer expVal = exp.evaluate();
			if(expVal == null)
			{
				// code 4 error, uninitialized variable was used
				Processor.turnOnErrorFlag();
				return;
			}
			Main.Print(exp.evaluate());
	
		}
		
		public printCommand(IExpression expToPrint){
			exp=expToPrint;
		}
		
		@Override
		public String toString()
		{
			return String.format("print(%s)", exp.toString());
		}
		
	}
	public static class assignCommand implements ICommand
	{
		private Variable var;
		private IExpression exp;

		/* method assigns exp's value to our variable */
		@Override
		public void execute() {
			Integer expVal = exp.evaluate();
			
			if(expVal == null) /* error code 4 , quit execution, set flag*/
			{
				Processor.turnOnErrorFlag();
				return;
			}	
			
			Variable.setVal(var, expVal);
		}
		
		public assignCommand(Variable var,IExpression exp){
			this.var=var;
			this.exp=exp;
		}
		
		@Override
		public String toString()
		{
			return String.format("%s = %s", var.toString(), exp.toString());
		}
		
	}
}
