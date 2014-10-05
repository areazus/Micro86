/* Author: Ahmed Reaz
 * Assignment:02-Micro86-1.0
 * For: CISC 3160 FALL 2014
 * Achievement: The program Emulates many of the features of IBM 8086
 * See Documentation section for details on features
 * NOTICE AND DISCLAIMER: Please give credits if you are copying any portion of this software. I am NOT responsible
 * if this software turns your computer into Transformer or anything else, its not intended to do. 
 */


 /* Documentation:
  *			Emulator Specs:
  *					CPU():				//Has three resgisters
  *						Three Registers:
  *							acc();		//General Purpose Register or Accumulator
  *							ip();		//Instruction Pointer Register
  *							flags();	//Machine Status Register
  *
  *					Memory();			//32bit*1000 word size memory.
  *
  *
  *
  *
  *
  *
  *
  */




package areaz.Micro86.emulator;
import java.io.*;
import java.util.Scanner;

public class Micro86{
	
	public static int[] Memory=new int[50];			//initial memory is 20
	public static OpCode[] op=new OpCode[0xFFFF];	//OpCode jump table
	public static Scanner input=new Scanner(System.in);	//to read input
	public static int acc=0x00000000;
	public static int ins=0x00000000;
	public static int flag=0x0;
	public static void main(String[] args){
		Loader("C:\\Users\\ahmed\\SkyDrive\\Eclipse\\Micro86\\assets\\11_int_io.m86");
		initializeOpCode();		//initializing oppcodes
		boolean run=true;
		int fetch, oc, instruction;
		while(run){
			fetch=Memory[ins];
			oc=fetch>>>16;
			instruction=fetch&0x0000FFFF;
			Debug.translate(oc, instruction);
			op[oc].Execute(instruction);
			ins+=0x00000001;
			Debug.print(Integer.toHexString(acc)+"\t"+Integer.toHexString(flag));
		}
		
		
		
		
		int x=0x01000000;
		int y=x>>>16;
		int z=x&0x000FFFF;
		Debug.print(z+"");
		op[y].Execute(x);	
	}
	
	public static void initializeOpCode(){
		op[0x0100]=new Halt();
		op[0x0202]=new Load();
		op[0x0201]=new LoadI();
		op[0x0302]=new Store();
		op[0x0402]=new Add();
		op[0x0401]=new AddI();
		op[0x0502]=new Sub();
		op[0x0501]=new SubI();
		op[0x0602]=new Mul();
		op[0x0601]=new MulI();
		op[0x0702]=new Div();
		op[0x0701]=new DivI();
		op[0x0802]=new Mod();
		op[0x0801]=new ModI();
		op[0x0902]=new Cmp();
		op[0x0901]=new CmpI();
		op[0x0A01]=new JmpI();
		op[0x0B01]=new Jei();
		op[0x0C01]=new Jnei();
		op[0x0D01]=new Jli();
		op[0x0E01]=new Jlei();
		op[0x0F01]=new Jgi();		
		op[0x1001]=new Jgei();
		op[0x1100]=new In();
		op[0x1200]=new Out();
		
	}
	
	public static void Loader(String filename){
		String temp;
		int i=0;
		try (BufferedReader br = new BufferedReader(new FileReader(filename)))
		{
			while ((temp = br.readLine()) != null) {
				//Debug.print(temp);
				Memory[i]=Integer.parseInt(temp,16);
				Debug.print(temp);
				i++;
				}
		} catch (Exception e) {
			System.out.println(e.getMessage()+"\n"+e.toString());
		} 	
	}
}

///////////The Following classes and interfaces Implements the OpCode Functiona
interface OpCode{
	public void Execute(int m);	//interface to implement "jump-table"
}

class Halt implements OpCode{
	//OpCode Halt stops the machine
	public void Execute(int m){
		Debug.print("Halt");
		Debug.Dump();
		System.exit(0);
	}
}

class Load implements OpCode{
	//OpCode Load copies memory[m] into accumulator
	//It replaces anything there before
	public void Execute(int m){
		//Dump memory[m] into Accumulator
		Micro86.acc=Micro86.Memory[m];		
	}
}

class LoadI implements OpCode{
	//OpCode LoadI copies m into accumulator
	//It replaces anything there before
	public void Execute(int m){
		Micro86.acc=m;
	}
}

class Store implements OpCode{
	//OpCode Store copies accumulator into memory[m] 
	//It replaces anything there before
	public void Execute(int m){
		//Copy accumulator value to Memory[m]
		Micro86.Memory[m]&=0xFFFF0000;
		Micro86.Memory[m]=Micro86.acc;	
	}
}

class Add implements OpCode{
	//OpCode Add value of memory[m] to Accumulator
	public void Execute(int m){
		//acc+=Memory[m]
		Micro86.acc=Micro86.Memory[m];
	}
}

class AddI implements OpCode{
	//OpCode AddI value of m to Accumulator
	public void Execute(int m){
		//acc+=m
		Micro86.acc+=m;
	}
}

class Sub implements OpCode{
	//OpCode Sub subtracts value of memory[m] from Accumulator
	public void Execute(int m){
		//acc=acc-memory[m];
		Micro86.acc-=Micro86.Memory[m];
	}
}

class SubI implements OpCode{
	//OpCode SubI subtracts value of m from Accumulator
	public void Execute(int m){
		//acc=acc-m;
		Micro86.acc-=m;
	}
}

class Mul implements OpCode{
	//OpCode Mul multiples value of memory[m] ito Accumulator
	public void Execute(int m){
		//acc*=memory[m];
		Micro86.acc*=Micro86.Memory[m];
	}
}

class MulI implements OpCode{
	//OpCode MulI multiples value of m into Accumulator
	public void Execute(int m){
		//acc*=m;
		Micro86.acc*=m;
	}
}

class Div implements OpCode{
	//OpCode Div divides value of memory[m] by Accumulator
	public void Execute(int m){
		//acc=acc/memory[m];
		Micro86.acc/=Micro86.Memory[m];
	}
}

class DivI implements OpCode{
	//OpCode DivI divides value of m by Accumulator
	public void Execute(int m){
		//acc=acc/m;
		Micro86.acc/=m;
	}
}

class Mod implements OpCode{
	//OpCode Mod takes remainder of Accumulator divided by memory[m]
	public void Execute(int m){
		//acc=acc%memory[m];
		Micro86.acc%=Micro86.Memory[m];
	}
}

class ModI implements OpCode{
	//OpCode ModI takes remainder of Accumulator divided by m
	public void Execute(int m){
		//acc=acc%m;
		Micro86.acc%=m;
	}
}

class Cmp implements OpCode{
	public void Execute(int m){
		if(Micro86.acc==Micro86.Memory[m])
		 	Micro86.flag=0x00000001;	//For JEI, 0X0B01
		else if(Micro86.acc!=Micro86.Memory[m])
			Micro86.flag=0x00000110;	//For JNEI, 0X0C01
		
		 if(Micro86.acc>Micro86.Memory[m])
			 Micro86.flag=0x00000010;	//For JGI, 0X0F01
		 else if(Micro86.acc<Micro86.Memory[m])
			 Micro86.flag=0x00000100;	//For JLI, 0X0D01
		 //How does JGEI and JLEI work?
		 //Flags are set and || is used to check if any of >= or <= flags are turned on
	}
}

class CmpI implements OpCode{
	public void Execute(int m){
		if(Micro86.acc==m)
		 	Micro86.flag=0x00000001;	//For JEI, 0X0B01
		else if(Micro86.acc!=m)
			Micro86.flag=0x00000110;	//For JNEI, 0X0C01
		
		 if(Micro86.acc>m)
			 Micro86.flag=0x00000010;	//For JGI, 0X0F01
		 else if(Micro86.acc<m)
			 Micro86.flag=0x00000100;	//For JLI, 0X0D01
		 //How does JGEI and JLEI work?
		 //Flags are set and || is used to check if any of >= or <= flags are turned on

	}
}


class JmpI implements OpCode{
	//OpCode JmpI jumps to m OpCode instructions
	public void Execute(int m){
		Micro86.ins=m-0x00000001;	//Why-...01? Because that is added by the loop in main 
	}
}

class Jei implements OpCode{
	public void Execute(int m){
		if((Micro86.flag&0x0000000F)==0x00000001){
			Micro86.ins=m-0x00000001;	//Why-...01? Because that is added by the loop in main
		}
	}
}

class Jnei implements OpCode{
	public void Execute(int m){
		if((Micro86.flag&0x0000000F)==0x00000000){
			Micro86.ins=m-0x00000001;	//Why-...01? Because that is added by the loop in main
			//Micro86.flag&=0xFFFFF00F;	//Resets flag for the this //May not be needed
		}
	}
}
class Jli implements OpCode{	
	public void Execute(int m){
		if((Micro86.flag&0x00000F00)==0x00000100){
			Micro86.ins=m-0x00000001;	//Why-...01? Because that is added by the loop in main
		}
	}
}

class Jlei implements OpCode{
	public void Execute(int m){
		if(((Micro86.flag&0x00000F00)==0x00000100)||((Micro86.flag&0x0000000F)==0x00000001)){
			Micro86.ins=m-0x00000001;	//Why-...01? Because that is added by the loop in main
		}
	}
}

class Jgi implements OpCode{
	public void Execute(int m){
		if((Micro86.flag&0x000000F0)==0x00000010){
			Micro86.ins=m-0x00000001;	//Why-...01? Because that is added by the loop in main
		}
	}
}

class Jgei implements OpCode{
	public void Execute(int m){
		if(((Micro86.flag&0x000000F0)==0x00000010)||((Micro86.flag&0x0000000F)==0x00000001)){
			Micro86.ins=m-0x00000001;	//Why-...01? Because that is added by the loop in main
			//Micro86.flag&=0xFFFFFF00;	//Resets flag for the this //May not be needed
		}
	}
}

class In implements OpCode{
	public void Execute(int m){
		Micro86.acc=Micro86.input.nextLine().charAt(0);	//reads the char from the console
	}
}

class Out implements OpCode{
	public void Execute(int m){
		System.out.print("out>>>"+(char)Micro86.acc);
	}
}
/////////////End of OpCode Functions


//

//Debugger
class Debug{
	static void print(String s){
		System.out.println(s);
	}
	
	static void translate(int oc, int instruction){
		String octoeng="";
		switch(oc){
			case 0x0100:
				octoeng="HALT";
				break;
			case 0x0202:
				octoeng="LOAD";
				break;
			case 0x0201:
				octoeng="LOADI";
				break;
			case 0x0302:
				octoeng="Store";
				break;
			case 0x0501:
				octoeng="SUBI";
				break;
			case 0x0602:
				octoeng="MUL";
				break;
			case 0x0702:
				octoeng="DIV";
				break;
			case 0x0701:
				octoeng="DIVI";
				break;
			case 0x0802:
				octoeng="MOD";
				break;
			case 0x0801:
				octoeng="MODI";
				break;
			case 0x0902:
				octoeng="CMP";
				break;
			case 0x0901:
				octoeng="CMPI";
				break;
			case 0x0A01:
				octoeng="JMP1";
				break;
			case 0x0B01:
				octoeng="JEI";
				break;
			case 0x0C01:
				octoeng="JNEI";
				break;
			case 0x0D01:
				octoeng="JLI";
				break;
			case 0x0E01:
				octoeng="JLEI";
				break;
			case 0x0F01:
				octoeng="JGI";
				break;
			case 0x1001:
				octoeng="JGEI";
				break;
			case 0x1101:
				octoeng="IN";
				break;
			case 0x1201:
				octoeng="OUT";
				break;
			//All other cases
			default:
				octoeng="Not Implemented Yet"+Integer.toHexString(oc);
				break;
		}
		print(Micro86.ins+"\t"+octoeng+"\t"+Integer.toHexString(instruction));
	}
	
	static void Dump(){
		print("Memory Dump");
		for(int i:Micro86.Memory){
			print(Integer.toHexString(i).toUpperCase()+"");
		}
	}
}