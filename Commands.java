
public class Commands {

	/* the following classes represents commands IN - CODE, that can be interpreted by executing them */
	
	public static class ifCommand implements ICommand
	{
		private Variable leftVar;
		private Variable rightVar;
		private Operation booleanOp;
		private ICommand commandToTake;

		@Override
		public void execute() {
			// TODO Auto-generated method stub
			
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
		private Number labelNumber;
		
		public gotoCommand(Number label) {
			labelNumber=label;
		}

		@Override
		public void execute() {
			// TODO Auto-generated method stub
			// note: use Processor.gotoLabel to jump to a different label
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
			// TODO Auto-generated method stub
			
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

		@Override
		public void execute() {
			// TODO Auto-generated method stub
			
			// note: use Variable.setVal method
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
