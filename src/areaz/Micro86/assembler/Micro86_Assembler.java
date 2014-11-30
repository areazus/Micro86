/* Author: Ahmed Reaz
 * Assignment: 03-M86Asm-1.0
 * For: CISC 3160 FALL 2014
 * Achievement: The program turns simple assembly language code to java and Micro86 machine code
 * 
 * NOTICE AND DISCLAIMER: Please give credits if you are copying any portion of this software. I am NOT responsible
 * if this software turns your computer into Transformer or anything else, its not intended to do. 
 */

package areaz.Micro86.assembler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Vector;

public class Micro86_Assembler {
	public Scanner parser=new Scanner(System.in);
	private String[] output;
	private Vector<String> file;
	private List Variables;	//Will hold VAR variables
	private Vector<String> Opcode;		//Will hold First 16 bit of instruction(Opcode)
	private Vector<String> Operand;		//Will hold last 16 bit of instruction(Operand)
	private HashMap<String, Integer> Ins_to_Opcode;		//Holds the Opcode for String thrown at it
	private HashMap<String, String> Opcode_to_Ins;		//Holds the opposite of Ins_to_Opcode
	private Vector<String> Label;						//Will hold the jumps for assembly
	private HashMap<String, String> Variables_address;	//Will hold the variable names and addresses
	
	private boolean java;								//to check if to translate to java
	private boolean m86_machine_code;					//to check if to translate to machines language
	private boolean Debug;
	private Vector<String> javacode;					//will hold the translated java code
	private String filename;
	
	public static void main(String[] args){
		new Micro86_Assembler(args);					//initialize the object
	}
	
	public Micro86_Assembler(String[] args){
		
		if(args.length>1){
			for(String temp:args){
				if(temp.equalsIgnoreCase("-java"))
					java=true;
				else if(temp.equalsIgnoreCase("-m86"))
					m86_machine_code=true;
				else if(temp.equalsIgnoreCase("-Debug"))
					Debug=true;
			}	
		}else{
			System.out.println("Please send arguments to Assenbler");
		}
		try{
		filename=args[args.length-1];
		Loader();		//Loads the file to file Vector
		Parser();							//Parse the file to be translated into assigned language
		}catch(ArrayIndexOutOfBoundsException e){
			System.out.println("Exception 0x0001: Memory not initialized or not enough memory");
			//Exception 0x0001: Possible causes>> invalid file name(Should throw exception in loader), no file name, no argument
		}
	}
	private void init_HashMap() {
		//Initialize and add String and keys to Ins_to_Opcode 
		Ins_to_Opcode=new HashMap<String, Integer>();
		Opcode_to_Ins=new HashMap<String, String>();
		
		Ins_to_Opcode.put("HALT", 0x0100);
		Ins_to_Opcode.put("LOAD", 0x0202);
		Ins_to_Opcode.put("LOADI", 0x0201);
		Ins_to_Opcode.put("STORE", 0x0302);
		Ins_to_Opcode.put("ADD", 0x0402);
		Ins_to_Opcode.put("ADDI", 0x0401);
		Ins_to_Opcode.put("SUB", 0x0502);
		Ins_to_Opcode.put("SUBI", 0x0501);
		Ins_to_Opcode.put("MUL", 0x0602);
		Ins_to_Opcode.put("MULI", 0x0601);
		Ins_to_Opcode.put("DIV", 0x0702);
		Ins_to_Opcode.put("DIVI", 0x0701);
		Ins_to_Opcode.put("MOD", 0x0802);
		Ins_to_Opcode.put("MODI", 0x0801);
		Ins_to_Opcode.put("CMP", 0x0902);
		Ins_to_Opcode.put("CMPI", 0x0901);
		Ins_to_Opcode.put("JMPI", 0x0A01);
		Ins_to_Opcode.put("JEI", 0x0B01);
		Ins_to_Opcode.put("JNEI", 0x0C01);
		Ins_to_Opcode.put("JLI", 0x0D01);
		Ins_to_Opcode.put("JLEI", 0x0E01);
		Ins_to_Opcode.put("JGI", 0x0F01);
		Ins_to_Opcode.put("JGEI", 0x1001);
		Ins_to_Opcode.put("IN", 0x1100);
		Ins_to_Opcode.put("OUT", 0x1200);
		for (String reverse : Ins_to_Opcode.keySet()){ 
			Opcode_to_Ins.put(Integer.toHexString(Ins_to_Opcode.get(reverse)), reverse);
		}
	}

	private void Loader(){
		//Reads file and store it in file Vector
		String temp;
		file=new Vector<String>(6, 2);
		try (BufferedReader br = new BufferedReader(new FileReader(filename))){
			while ((temp = br.readLine()) != null) {
				file.addElement(temp);
				}
		}catch (Exception e) {
			System.out.println(e.getMessage()+"\n"+e.toString());
		}
	}
	
	private void Parser(){
		init_HashMap();	//Initialize hashmap that keeps Opcode_Keys and Opcode
		
		Variables=new List();
		Variables_address=new HashMap<String, String>();
		Opcode=new Vector<String>(6,2);		
		Operand=new Vector<String>(6,2);
		Label=new Vector<String>();
		
		String toParse;			//To Hold each Word read in
		int counter=-1;			//Keeps hold of line number
		
		boolean haslabel=false;
		
		Validate_file_start();	//This method is important for creation of java file//Will add one extra instruction for CPU
		
		for(String temp:file){
			if(temp.trim().length()>0&&!temp.startsWith(";"))
				counter++;					//For Variable number and Label Placement
			parser=new Scanner(temp);	//Adds the line to Scanner
			
		lineparser:	while(parser.hasNext()){	//Loop to parse each line
						toParse=parser.next();
						if(isOpcode(toParse)){
							if(haslabel)
								haslabel=false;
							//If the letter read in is opcode, assign it to OpCode vector
							Opcode.addElement(Integer.toHexString(Ins_to_Opcode.get(toParse))+"0000");
							
							try{
								toParse=parser.next();
								if(!isComment(toParse)){
									Operand.addElement(""+toParse);
								}
								else
									Operand.addElement("0000");
							}catch(NoSuchElementException e){
								Operand.addElement("0000");
							}
							
						}//End of Opcode and Operand
						else if(isLabel(toParse)){
							toParse=toParse.replace(":", "");
							Label.addElement(toParse);
							Variables_address.put(toParse, counter+"");
							if(!parser.hasNext()){
								Opcode.addElement("04010000");
								Operand.addElement("0000");
							}else{
								haslabel=true;
							}
						}else if(toParse.equalsIgnoreCase("VAR")){
							String t=parser.next();
							String s=parser.next();
							Variables.add(t, s);
							Variables_address.put(t, counter+"");
						}else if(isComment(toParse)){
							if(haslabel){
								Opcode.addElement("04010000");
								Operand.addElement("0000");
								haslabel=false;
							}
							break lineparser;	//it breaks the line parsing if it is comment
						}
			}
		}
		
		Make();
	}
	
	private void Validate_file_start() {
		// TODO Auto-generated method stub
		if(file.get(0).startsWith(":")){
			file.add(0, "LOADI 0");
		}
			
	}

	private void Initialize_java_code() {
		javacode=new Vector<String>(20, 2);
		javacode.addElement("//Filename must be changed to Output.java to compile and run");
		javacode.addElement("import java.util.*;");
		javacode.addElement("public class Output{");
		javacode.addElement("\tprivate static int acc;");
		javacode.addElement("\tprivate static int comp;");
		javacode.addElement("\tprivate static Scanner input=new Scanner(System.in);");
		boolean Is_if=false;
		String variables;
		for(int i=1; i<=Variables.length(); i++){
			variables=Variables.getVariableName(0, i);
			javacode.addElement("\tprivate static int "+variables+" = "+Variables.getVariableValue(variables)+";");
			
		}
		
		int instruction_counter=0;
		String oc, op;
		
		javacode.addElement("\tpublic Output(){");
		String nextLabel=null;
		try{
			nextLabel=Label.get(0);
		}catch(Exception e){
			
		}
		while(instruction_counter<Opcode.size()){
			
			oc=(Integer.parseInt(Opcode.get(instruction_counter),16)>>>16)+"";
			oc=Opcode_to_Ins.get(Integer.toHexString(Integer.parseInt(oc)));
			op=Operand.get(instruction_counter);
			//JUMPS
			if(oc.equalsIgnoreCase("JMPI")){
				javacode.addElement("\t\t"+op+"();");
			}else if(oc.equalsIgnoreCase("JEI")){
				if(Is_if){
					javacode.addElement("\t\t}");
					Is_if=false;
				}
				javacode.addElement("\t\tif(acc==comp)\n\t\t\t"+op+"();\n\t\telse{");
					Is_if=true;
			}else if(oc.equalsIgnoreCase("JNEI")){
				if(Is_if){
					javacode.addElement("\t\t}");
					Is_if=false;
				}
				javacode.addElement("\t\tif(acc!=comp)\n\t\t\t"+op+"();\n\t\telse{");
				Is_if=true;
			}else if(oc.equalsIgnoreCase("JLI")){
				if(Is_if){
					javacode.addElement("\t\t}");
					Is_if=false;
				}
				javacode.addElement("\t\tif(acc<comp)\n\t\t\t"+op+"();\n\t\telse{");
				Is_if=true;
			}else if(oc.equalsIgnoreCase("JLEI")){
				if(Is_if){
					javacode.addElement("\t\t}");
					Is_if=false;
				}
				javacode.addElement("\t\tif(acc<=comp)\n\t\t\t"+op+"();\n\t\telse{");
				Is_if=true;
			}else if(oc.equalsIgnoreCase("JGI")){
				if(Is_if){
					javacode.addElement("}");
					Is_if=false;
				}
				javacode.addElement("\t\tif(acc>comp)\n\t\t\t"+op+"();\n\t\telse{");
				Is_if=true;
			}else if(oc.equalsIgnoreCase("JGEI")){
				if(Is_if){
					javacode.addElement("\t\t}");
					Is_if=false;
				}
				javacode.addElement("\t\tif(acc>=comp)\n\t\t\t"+op+"();\n\t\telse{");
				Is_if=true;
			}//END OF JUMPS
			//Comparisons
			 else if(oc.equalsIgnoreCase("CMP")||oc.equalsIgnoreCase("CMPI")){
				javacode.addElement("\t\tcomp="+op+";");
			}//End of Comparisons
			//Halt
			 else if(oc.equalsIgnoreCase("HALT")){
					javacode.addElement("\t\tSystem.exit(0);");
				}
			//End of Halt
			
			//Load
			  else if(oc.equalsIgnoreCase("LOAD")||oc.equalsIgnoreCase("LOADI")){
					javacode.addElement("\t\tacc="+op+";");
				}
			//End of Load
			
			//Store
			  else if(oc.equalsIgnoreCase("STORE")){
					javacode.addElement("\t\t"+op+"=acc;");
				}
			//End of Store
			
			//Arithmetic
			   else if(oc.equalsIgnoreCase("ADD")||oc.equalsIgnoreCase("ADDI")){
					javacode.addElement("\t\tacc+="+op+";");
			   }else if(oc.equalsIgnoreCase("SUB")||oc.equalsIgnoreCase("SUBI")){
					javacode.addElement("\t\tacc=-"+op+";");
			   }else if(oc.equalsIgnoreCase("MUL")||oc.equalsIgnoreCase("MULI")){
					javacode.addElement("\t\tacc*="+op+";");
			   }else if(oc.equalsIgnoreCase("DIV")||oc.equalsIgnoreCase("DIVI")){
					javacode.addElement("\t\tacc/="+op+";");
			   }else if(oc.equalsIgnoreCase("MOD")||oc.equalsIgnoreCase("MODI")){
					javacode.addElement("\t\tacc%="+op+";");
			   }
			//End of Arithmetic
			
			//Input/output
			    else if(oc.equalsIgnoreCase("IN")){
					javacode.addElement("\t\tacc=input.nextLine().charAt(0);");
			   }else if(oc.equalsIgnoreCase("OUT")){
					javacode.addElement("\t\tSystem.out.println(acc);");
			   }
			//End of Input/output
			
			try{
				if(Integer.parseInt(Variables_address.get(nextLabel))==(instruction_counter+1)){
					javacode.addElement("\t\t"+nextLabel+"();\n\t}");
					if(Is_if){
						javacode.addElement("}");
						Is_if=false;
					}
					javacode.addElement("\tprivate void "+Label.remove(0)+"(){");
					nextLabel=Label.get(0);
					}
				}catch(Exception e){
					//NO NEED
				}			

			instruction_counter++;
		}
		if(Is_if){
			javacode.addElement("\t\t}");
			Is_if=false;
		}
		javacode.addElement("\t}\n\tpublic static void main(String[] args){new Output();}\n}");
		
		
		for(String temp:javacode)
			System.out.println(temp);
		System.out.println("\n\n");
	}

	private void Make() {
		//Method joins all the Program and make Machine Code Array
		if(m86_machine_code)
			initialize_machineCode();
		if(java)
			Initialize_java_code();
		if(Debug)
			Debug();
		
		
		
	}

	private void initialize_machineCode() {
		//Makes machine code
		output=new String[Opcode.size()+Variables.length()];
		int a, b, Opcodesize=Opcode.size();
		String temp_Operand;
		
		for(int i=0; i<output.length; i++){
			if(i<Opcodesize){
				a=Integer.parseInt(Opcode.get(i), 16);
				temp_Operand=Operand.get(i);
				try{
				b=Integer.parseInt(temp_Operand);
				}catch(NumberFormatException e){
					b=Integer.parseInt(Variables_address.get(temp_Operand));
				}
				output[i]=Integer.toHexString(a+b);
			}else{
				String variables;
				for(int j=1; j<=Variables.length(); j++){
					variables=Variables.getVariableName(0, j);
					output[i]=Integer.toHexString(Integer.parseInt(Variables.getVariableValue(variables)));
					i++;
				}
				break;
			}
		}
		Make_output_preety();
		
		for(String temp:output)
			System.out.println(temp);
		
		System.out.println("\n\n");
		
	}

	private void Debug(){
		String temp;
		System.out.println("Instructions set is going to be "+output.length);
		System.out.println("Opcode set is going to be "+Opcode.size());
		System.out.println("Operand set is going to be "+Operand.size());
		System.out.println("\nOpCode:\t\t\tOperand:\t\tAddress:");
		int x=Opcode.size();
		for(int i=0; i<x; i++){
			System.out.println(Integer.parseInt(Opcode.get(i), 16)+"\t\t\t"+(temp=Operand.get(i))+"\t\t\t"+(temp=Variables_address.get(temp)));
		}
		
		System.out.println("\n\nVariables");
		String variables;
		for(int j=1; j<=Variables.length(); j++){
			variables=Variables.getVariableName(0, j);
			System.out.println(variables+"\t"+Variables.getVariableValue(variables));
		}
		
		
	}

	//Helper Methods below
	private boolean isLabel(String toParse) {
		if(toParse.startsWith(":")&&toParse.endsWith(":"))
			return true;
		return false;
	}

	private boolean isComment(String check) {
		if(check.contains(";"))
			return true; 
		return false;
	}

	private boolean isOpcode(String check){
		if(Ins_to_Opcode.get(check)!=null)
			return true;
		return false;
	}
	
	private void Make_output_preety(){
		for(int i=0; i<output.length; i++){
			while(output[i].length()<8)
				output[i]="0"+output[i];
			output[i]=output[i].toUpperCase();
		}
	}
	
	

}
