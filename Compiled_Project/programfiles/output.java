import java.util.*;
public class output{
	private static int acc;
	private static int comp;
	private static Scanner input=new Scanner(System.in);
	private static int C = 0;
	private static int Num = 0;
	private static int Divisor = 0;
	public output(){
		acc=0;
		Num=acc;
		acc=input.nextLine().charAt(0);
		Loop1();
	}
	private void Loop1(){
		comp=32;
		if(acc==comp)
			Next1();
		else{
		comp=13;
}
		if(acc==comp)
			Next1();
		else{
		acc=-48;
		C=acc;
		acc=Num;
		acc*=10;
		acc+=C;
		Num=acc;
		acc=input.nextLine().charAt(0);
		Loop1();
		Next1();
	}
}
	private void Next1(){
		acc=Num;
		acc*=2;
		Num=acc;
		acc=1;
		Divisor=acc;
		Loop2();
	}
	private void Loop2(){
		acc=Divisor;
		acc*=10;
		comp=Num;
		Divisor=acc;
		Loop2();
		Loop3();
	}
	private void Loop3(){
		acc=Num;
		acc/=Divisor;
		acc+=48;
		System.out.println(acc);
		acc=Num;
		acc%=Divisor;
		Num=acc;
		acc=Divisor;
		acc/=10;
		Divisor=acc;
		acc=Num;
		comp=0;
		if(acc!=comp)
			Loop3();
		else{
		System.exit(0);
}
	}
	public static void main(String[] args){new output();}
}