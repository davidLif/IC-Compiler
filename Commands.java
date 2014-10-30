
public class Commands {

	/* the following classes represents commands IN - CODE, that can be interpreted by executing them */
	
	public static class ifCommand implements ICommand
	{
		private Variable leftVar;
		private Variable rightVar;
		private Operation booleanOp;
		private ICommand commandToTake;

		public ifCommand(Variable leftVar,Variable rightVar,Operation booleanOp,ICommand commandToTake){
			this.leftVar=leftVar;
			this.rightVar=rightVar;
			this.booleanOp=booleanOp;
			this.commandToTake=commandToTake;
		}

		@Override
		public void execute() {
			// TODO Auto-generated method stub
			
		}
		
		// add constructor
	}
	
	public static class gotoCommand implements ICommand
	{
		private Number labelNumber;

		@Override
		public void execute() {
			// TODO Auto-generated method stub
			// note: use Processor.gotoLabel to jump to a different label
		}
		
		// add constructor
	}
	public static class printCommand implements ICommand
	{
		private IExpression exp;

		@Override
		public void execute() {
			// TODO Auto-generated method stub
			
		}
		
		// add constructor
		
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
		
		// add constructor
		
	}
}
