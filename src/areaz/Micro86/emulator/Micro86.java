/* Author: Ahmed Reaz
 * Assignment:02-Micro86-1.0
 * For: CISC 3160 FALL 2014
 * Achievement: The program Emulates many of the features of IBM 8086
 * See Documentation section for details on features
 * NOTICE AND DISCLAIMER: Please give credits if you are copying any portion of this software. I am NOT responsible
 * if this software turns your computer into Transformer or anything else, its not intended to do. 
 */

 /* Documentation:
  * 	Runnable=(filename)||(-d filename)||(-t filename)||(-d -t filename)
  *			Emulator Specs:
  *				Micro86.class + *.class-Debug.class
  *					CPU():				//Has three resgisters
  *						Three Registers:
  *							acc;		//General Purpose Register or Accumulator
  *							ip;		//Instruction Pointer Register
  *							flags;	//Machine Status Register
  *							ir;		//Instruction register
  *
  *					Memory[];			//32bit*[size] word size memory.
  *			Debugging options;
  *					Debug.class
  *						trace (args=-t)
  *						decode (args=-d)
  *						MemoryDump(auto)
  */

package areaz.Micro86.emulator;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Scanner;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import areaz.Micro86.assembler.Micro86_Assembler;

@SuppressWarnings("serial")
public class Micro86 extends JComponent{	
	public static int[] Memory=new int[50];			//initial memory is 30
	public static OpCode[] op=new OpCode[0xFFFF];	//OpCode jump table
	public static Scanner input=new Scanner(System.in);	//to read input
	public static int acc=0x0;							//Accumulator
	public static int ins=0x0;							//Instruction Pointer holder
	public static int flag=0x0;							//Flag Register
	public static int ir=0x0;							//Instructions holder
	public static int counter=-1;						//Instruction counter for debugging
	public static String termination="DEATH";	//By default, the termination is DEATH				
	public static boolean trace, decode;				//Debugging properties
	
	public static void main(String[] args){
		
		//Filechooser based on http://www.java2s.com/Code/Java/Swing-JFC/FileChooserDemo.htm
		if(args.length==0){
			 JFrame frame = new JFrame("File Chooser");
			 frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			 Container contentPane = frame.getContentPane();
			 
			 final JLabel directoryLabel = new JLabel(" ");
			    directoryLabel.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 36));
			    contentPane.add(directoryLabel, BorderLayout.NORTH);

			    final JLabel filenameLabel = new JLabel(" ");
			    filenameLabel.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 36));
			    contentPane.add(filenameLabel, BorderLayout.SOUTH);

			    JFileChooser fileChooser = new JFileChooser(".");
			    fileChooser.setControlButtonsAreShown(false);
			    contentPane.add(fileChooser, BorderLayout.CENTER);

			    ActionListener actionListener = new ActionListener() {
			      public void actionPerformed(ActionEvent actionEvent) {
			        JFileChooser theFileChooser = (JFileChooser) actionEvent
			            .getSource();
			        String command = actionEvent.getActionCommand();
			        if (command.equals(JFileChooser.APPROVE_SELECTION)) {
			          File selectedFile = theFileChooser.getSelectedFile();
			          String[] toCompile=new String[1];
			          toCompile[0]=selectedFile.getParent()+"\\"+selectedFile.getName();
			          frame.setVisible(false);
			          if(toCompile[0].contains(".a86")){
			        	  String[] temp=new String[3];
			        	  temp[0]="-java";
			        	  temp[1]="-m86";
			        	  temp[2]=toCompile[0];
			        	  new Micro86_Assembler(temp);
			          }
			          else
			        	  new Micro86(toCompile);
			          
			        } 
			      }
			    };
			    fileChooser.addActionListener(actionListener);
			    frame.pack();
			    frame.setVisible(true);
		}else{
			if(args[args.length-1].contains(".a86"))
				new Micro86_Assembler(args);
	          else
	        	  new Micro86(args);
	
		
		
		}
	}
	public Micro86(String[] args){
		Loader(args);			//Loads program into Memory
		initializeOpCode();		//initializing opcodes
		while(true){
			counter++;
			ir=Memory[ins];	//Fetching instructions	
			if(trace)
				Debug.Trace();	//DEBUG
			op[ir>>>16].Execute(ir&0x0000FFFF);	//Execute
			ins+=0x00000001;					//Increment instruction register
		}
	}

	private static void initializeOpCode(){
		//Why array? That is a way of implementing jump table
		//Switch, at worse could be interepted as if-else-if, increasing the number of comparisons
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
	
	public static void Loader(String[] args){
		String filename="";
		for(int i=0; i<args.length; i++){
			if(i+1==args.length){
				filename=args[i];
			}
			else if(args[i].equalsIgnoreCase("-t"))
				trace=true;
			else if(args[i].equalsIgnoreCase("-d"))
				decode=true;
		}
		
		String temp;
		int i=0;
		Debug.print("================================\n"+
					"Micro86 Emulator version 1.0\n================================\n"+
						"Executable file: "+ filename+"\n");
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
		if(decode){
			Debug.DeCode();
		}
		
	}
}

///////////The Following classes and interfaces Implements the OpCode Functions
interface OpCode{
	public void Execute(int m);	//interface to implement "jump-table"
}

class Halt implements OpCode{
	//OpCode Halt stops the machine
	public void Execute(int m){
		Micro86.termination="Normal";
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
	static boolean activatetrace;
	
	static void print(String s){
		System.out.println(s);
	}
	static void printl(String s){
		System.out.print(s);
	}
	
	static void DeCode(){
		String toPrint, decoded;
		print("\n===== Disassembled Code =====");
		for(int i=0; i<Micro86.Memory.length; i++){
			decoded=mean((Micro86.Memory[i]>>>16), Micro86.Memory[i]&0x0000FFFF);
			toPrint=decoded.equalsIgnoreCase("Not Implemented Yet")?".":Integer.toHexString(i)+": "+decoded+"\n";
			printl(toPrint);
		}
	}
	
	static void Trace(){
		if(!activatetrace){
			print("\n===== Execution Trace =====\n");
			activatetrace=true;
		}
		if(activatetrace){
			print(Micro86.counter+":\t"+mean((Micro86.ir>>>16), Micro86.ir&0x0000FFFF)
							+"\tRegisters: acc: "+Micro86.acc +" ip: "+Micro86.ins +" flags: "+Micro86.flag +" (ir: "+Micro86.ir+")");
		}
	}
	static String mean(int oc, int instruction){
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
			case 0x0402:
				octoeng="Add";
				break;
			case 0x0401:
				octoeng="AddI";
				break;
			case 0x0502:
				octoeng="Sub";
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
				octoeng="Not Implemented Yet";
				break;
		}
		return octoeng;
	}
	
	static void Dump(){
		String toPrint;
		int counter=-1;
		print("\n\n===== Post-Mortem Dump ("+Micro86.termination+" termination) =====");
		print("--------------------");
		print("Registers: acc: "+Micro86.acc+" ip: "+Micro86.ins+" flags: "+Micro86.flag+" (ir: "+Micro86.ir+")");
		print("------------- Memory ---------------");
		for(int i:Micro86.Memory){
			counter++;
			toPrint=(i==0)?".":Integer.toHexString(counter)+": "+Integer.toHexString(i).toUpperCase()+"\n";
			printl(toPrint);
		}
		print("\n-------------End of Memory DUMP ---------------");
	}
}