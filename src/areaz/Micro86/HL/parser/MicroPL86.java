package areaz.Micro86.HL.parser;

import java.io.File;

class MicroPL86 {
	public static void main(String [] args) throws Exception {
		if(args.length==0){
			args=new String[1];
			args[0]="C:\\Users\\ahmed\\SkyDrive\\Eclipse\\Micro86\\assets\\sample.pl86";
		}
		processCommandLine(args);
		if (filename == null) {
			System.out.println("Usage: MicroPL86 <filename>");
			System.exit(2);
		}
		System.out.println(filename);
		Lexer lexer = new Lexer(new File(filename));
		
		while (lexer.token().type !=  Token.EOI) {
			System.out.println(lexer.token());
			lexer.next();
		}
	}

	static void processCommandLine(String [] args) {
		for (int i = 0; i < args.length; i++)
			if (args[i].startsWith("-")) {
			}
			else
				filename = args[i];
	}

	static String filename = null;
}