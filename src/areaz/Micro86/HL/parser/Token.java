package areaz.Micro86.HL.parser;

import java.util.Map;
import java.util.TreeMap;

class Token {
	Token(int type, String lexeme) {
		this.type = type;
		this.lexeme = lexeme;
	}

	Token(int type) {this(type, null);}

	public String toString() {return tokMap.get(type) + (lexeme == null ? "" : " (" + lexeme + ")");}

	int type;
	String lexeme;

	static final int 
		EOI = 0,
		ID = 1,
		INT_LITERAL = 2,
		COMMA = 3,
		SEMICOLON = 4,
		INT = 5,
		ASSIGN_SYMBOL = 6,
		RPAREN = 7,
		LPAREN = 8,
		PROGRAM = 9,
		END = 10,
		LBRACE = 11,
		RBRACE = 12,
		GT = 13,
		GE = 14,
		read = 15,
		If = 16,
		While = 17,
		Else = 18,
		MOD_OP = 19,
		PLUS_OP = 20,
		MINUS_OP = 21,
		MUL_OP = 22,
		DIV_OP = 23,
		Print = 24,
		For = 25,
		EQ = 26,
		NE = 27,
		LT = 28,
		LE = 29;
		


	static final Map<Integer, String> tokMap = new TreeMap<Integer, String>();

	static {
		tokMap.put(EOI, "EOI");
		tokMap.put(ID, "ID");
		tokMap.put(INT_LITERAL, "INT_LITERAL");
		tokMap.put(COMMA, "COMMA");
		tokMap.put(SEMICOLON, "SEMICOLON");
		tokMap.put(INT, "int");
		tokMap.put(ASSIGN_SYMBOL, "ASSIGN_SYMBOL");
		tokMap.put(RPAREN, "RPAREN");
		tokMap.put(LPAREN, "LPAREN");
		tokMap.put(PROGRAM, "program");
		tokMap.put(END, "end");
		tokMap.put(LBRACE, "LBRACE");
		tokMap.put(RBRACE, "RBRACE");
		tokMap.put(GT, "Greater than");
		tokMap.put(GE, "Greater than or Equal to");
		tokMap.put(read, "read");
		tokMap.put(If, "if");
		tokMap.put(While, "while");
		tokMap.put(MOD_OP, "MOD_OP");
		tokMap.put(PLUS_OP, "PLUS_OP");
		tokMap.put(MINUS_OP, "MINUS_OP");
		tokMap.put(MUL_OP, "MUL_OP");
		tokMap.put(DIV_OP, "DIV_OP");
		tokMap.put(Print, "Print");
		tokMap.put(For, "for");
		tokMap.put(EQ, "Equal To");
		tokMap.put(NE, "Not Equal To");
		tokMap.put(LT, "Less than");
		tokMap.put(LE, "Less than or Equal to");
		tokMap.put(Else, "else");
	
	}

	static final Map<String, Token> keywordMap = new TreeMap<String, Token>();

	static {
		keywordMap.put("program", new Token(PROGRAM));
		keywordMap.put("int", new Token(INT));
		keywordMap.put("if", new Token(If) );
		keywordMap.put("else", new Token(Else) );
		keywordMap.put("while", new Token(While));
		keywordMap.put("for", new Token(For));
		keywordMap.put("print", new Token(Print));
		keywordMap.put("read", new Token(read));
		keywordMap.put("end", new Token(END));
	}
}
		