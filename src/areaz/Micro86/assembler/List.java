package areaz.Micro86.assembler;

//Modified from Areasus Library

public class List {
	
	private String Variable, Value;
	private int length;
	private boolean isMain;
	
	private List next=null;
	
	public List(){
		isMain=true;
	}
	
	public List(String a, String b){
		Variable=a;
		Value=b;
	}
	
	public void add(String a, String b){
		if(isMain)
			length++;
		
		if(next==null){
			next=new List(a,b);
		}else{
			next.add(a, b);
		}
	}

	public String getVariableName(int now, int index){
		if(now==index)
			return Variable;
		else
			return next.getVariableName(now+1, index);
	}
	
	public String getVariableValue(String a){
		if(isMain)
			return next.getVariableValue(a);
		else if(Variable.equalsIgnoreCase(a))
			return Value;
		
		else
			return next.getVariableValue(a);
	}
	
	public int length(){
		return length;
	}

}
