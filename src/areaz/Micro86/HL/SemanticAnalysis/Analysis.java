package areaz.Micro86.HL.SemanticAnalysis;

import java.util.Map;
import java.util.TreeMap;

import areaz.Micro86.HL.lexical.Lexer;

public class Analysis {
	private Map<String ,String> variables = new TreeMap<String ,String>();
	
	public boolean init(String name, boolean Stackable, Lexer lexer){
		if(!variables.containsKey(name)){
			variables.put(name, "0");
			return true;
		}
		System.out.println(name+" already exists.");
		System.exit(1);
		return false;
	}
	
	public boolean put(String name, String value, Lexer lexer){
		if(variables.containsKey(name)){
			variables.replace(name, value);
			return true;
		}
		System.out.println(name+" doesn't exists.");
		System.exit(1);
		return false;
	}
	@Override
	public String toString(){
		String temp="Variables in the program:\n\n";
		for(String s:variables.keySet()){
			temp+=s+" = "+variables.get(s)+"\n";
		}
		return temp;
	}
	
}
