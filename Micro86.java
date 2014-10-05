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

public class Micro86{
	
	public static int[] Memory=new int[20];
	public static OpCode[] op=new OpCode[0xFFFF];
	public static int acc=0x00000000;
	public static int ins=0x00000000;
	public static int flag=0x0;
	public static void main(String[] args){
		Loader("C:\\Users\\ahmed\\SkyDrive\\Brooklyn College\\FALL 2014\\Programming languages 3160\\Jcreator\\Micro86Emulator\\areaz\\Micro86\\emulator\\test.m86");
		initializeOpCode();		//initializing oppcodes
		boolean run=true;
		int fetch, oc, instruction;
		while(run){
			fetch=Memory[ins];
			oc=fetch>>>16;
			instruction=fetch&0x000FFFF;
			Debug.print("about to execute OpCode:"+oc+" Instruction"+instruction);
			Debug.translate(oc, instruction);
			op[oc].Execute(instruction);
		
			ins+=0x00000001;
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
		//Dump m into Accumulator
	}
}

class Store implements OpCode{
	//OpCode Store copies accumulator into memory[m] 
	//It replaces anything there before
	public void Execute(int m){
		//Copy accumulator value to Memory[m]
		Micro86.Memory[m]=Micro86.acc;	
	}
}

class Add implements OpCode{
	//OpCode Add value of memory[m] to Accumulator
	public void Execute(int m){
		//acc+=Memory[m]
		Micro86.acc+=Micro86.Memory[m];
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
	//OpCode Cmp return 1 if memory[m]==acc
	//Otherwise it returns 0
	public void Execute(int m){
		/*
		 *if(memory[m]==acc)
		 *	flag 1
		 *	flag 0;
		 */
		 if(Micro86.acc==Micro86.Memory[m])
		 	Micro86.flag=0x00000001;
		 Micro86.flag=0x0;
	}
}

class CmpI implements OpCode{
	//OpCode CmpI return 1 if m==acc
	//Otherwise it returns 0
	public void Execute(int m){
		/*
		 *if(m==acc)
		 *	flag 1;
		 *	flag 0;
		 */
		 if(Micro86.acc==m)
		 	Micro86.flag=0x00000001;
		 Micro86.flag=0x0;
	}
}
//end of still to do
class JmpI implements OpCode{
	//OpCode JmpI jumps to m OpCode instructions
	public void Execute(int m){
		//OpCode[memory[m]];
		Micro86.ins=m-0x00000001;
	}
}

class Jei implements OpCode{
	//OpCode Jei checks if acc==m, if it is, jumps to OpCode[memory[m]]
	public void Execute(int m){
		//if (flag 1)
		//	OpCode[memory[m]];
		if(Micro86.flag==0x00000001)
			Micro86.ins=m-0x00000001;
	}
}

class Jnei implements OpCode{
	//OpCode Jnei checks if acc==m, if not, jumps to OpCode[memory[m]]
	public void Execute(int m){
		//if (acc!=m)
		//	OpCode[memory[m]];
		/*
		if(Micro86.Memory!=0x00000001)
			Micro86.ins=m-0x00000001;
	
		*/
	}
}
//Following classes not coded yet
class Jli implements OpCode{
	
	public void Execute(int m){
		
	}
}

class Jlei implements OpCode{
	
	public void Execute(int m){
		
	}
}

class Jgi implements OpCode{
	
	public void Execute(int m){
		
	}
}

class Jgei implements OpCode{
	
	public void Execute(int m){
		
	}
}

class In implements OpCode{
	
	public void Execute(int m){
		
	}
}

class Out implements OpCode{
	
	public void Execute(int m){
		
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
			case 0x0302:
				octoeng="Store";
				break;
			default:
				octoeng="Not Implemented Yet";
				break;
		}
		print(Micro86.ins+"\t"+octoeng+"\t"+instruction);
	}
	
	static void Dump(){
		print("Memory Dump");
		for(int i:Micro86.Memory)
			print(Integer.toHexString(i).toUpperCase()+"");
	}
}