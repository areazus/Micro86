package areaz.Micro86.HL.parser;

import java.io.File;
import java.io.IOException;

import areaz.Micro86.HL.SemanticAnalysis.Analysis;
import areaz.Micro86.HL.lexical.Lexer;

public class MicroPL86 {
	public static Analysis analyze;
	
	public static void main(String [] args) throws Exception {
		analyze=new Analysis();
		
		
		if(args.length==0){
			args=new String[1];
			args[0]="C:\\Users\\ahmed\\SkyDrive\\Eclipse\\Micro86\\assets\\error1.1.pl86";
		}
		processCommandLine(args);

		if (filename == null) {
			System.out.println("Usage: MicroPL86 <filename>");
			System.exit(2);
		}

		try {
			new Parser(new Lexer(new File(filename))).parse();
			System.out.println("*** Success ***\n"+analyze.toString());
			
		} catch (IOException e) {
			System.err.println(e.getMessage());
		} catch (ParserException e) {
			System.err.println(e.getMessage());
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