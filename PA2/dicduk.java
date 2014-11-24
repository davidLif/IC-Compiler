
nonterminal String extends_opt


Program ::= class_list:lst
	{: RESULT = new Program(lst); :}
class_list ::= class:c
{: RESULT = new ArrayList<Icclass>();
   RESULT.add(c);
:}
|   class_list: lst class:c
{: lst.add(c);
   RESULT = lst; :}
}
| 
{: RESULT = null; :}



class ::= CLASS CLASSID:name extends_opt:extends_name CLP fields_methods_list:lst  CRP
{ :
	List<Field> fieldlst = new Arraylist<Field>();
	List<Method> methodlst = new Arraylist<Method>();
	for( item:lst)
	{
		if ( item instanceof(Field))
		{
			fieldlst.add(item);
		}
			else
			{
				methodlst.add(item);				
			}
		
		
	}
	RESULT = new ICClass(getLine(),name,field)
	
	
	
	
fields_methods_list ::= method:m
{: RESULT = new ArrayList<ASTNode>();
	RESULT.add(m); :}
| field : f
{: RESULT = new ArrayList<ASTNode>();
RESULT.add(f); :}
| fields_methods_list:lst method:m
{: lst.add(m);
	RESULT:lst; :}
| fields_methods_list:lst field:f
{: lst.add(f);
	RESULT:lst; :}
| 
{: RESULT = null; :}
	






extends_opt ::= EXTENDS CLASSID : id
	{: RESULT = id; :}
	| 
	{: RESULT = null; :} 
	
	
WHAT REMAINS:
	*field
	   *id list
	*type
	*method
	** static opt
	*type or void
	*formal list_opt
	*stmt_list(remember to derive epsilon)
	*formal list(no epsilon)
	*coma formal list(remember to derive epsilon)
	



