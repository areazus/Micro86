package areaz.Micro86.HL.parser;

import areaz.Micro86.HL.lexical.*;

class ParserException extends Exception {
	private static final long serialVersionUID = -6827221677497338346L;

	ParserException(Lexer lexer, String message) {
		super("At line " + lexer.line + " col " + lexer.col + ": " + message);
		System.out.println("At line " + lexer.line + " col " + lexer.col + ": " + message);
		}
}
