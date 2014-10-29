
public class Commands {

	public static class ifCommand implements ICommand
	{
		private Variable leftVar;
		private Variable rightVar;
		private String booleanOp;

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
			
		}
		
		// add constructor
		
	}
}
