package areaz.Micro86.HL.lexical;

import java.io.*;

class Lexer {
	Lexer(File file) throws IOException {
		is = new FileInputStream(file);
		advance();
		next();
	}

	@SuppressWarnings("deprecation")
	Lexer(String s) throws IOException {
		is = new StringBufferInputStream(s);
		
		advance(); 
	}

	public Token next() throws IOException {
		currentToken = null;
		String lexeme = null;
		int state = START;
		while (currentToken == null) {
			boolean shouldAdvance = true;
			switch (state) {
				case START:
					lexeme = "";
					if (Character.isWhitespace(currChar)) state = START;
					else if (currChar == '#') {
						state = COMMENT;
					}
					else if (Character.isDigit(currChar))  state = INT_LITERAL;
					else if (Character.isLetter(currChar)) state = ID;
					else if (currChar == '+') currentToken = new Token(Token.PLUS_OP);
					else if (currChar == '-') currentToken = new Token(Token.MINUS_OP);
					else if (currChar == '*') currentToken = new Token(Token.MUL_OP);
					else if (currChar == '/') currentToken = new Token(Token.DIV_OP);
					else if (currChar == '%') currentToken = new Token(Token.MOD_OP);
					else if (currChar == '=') state = AssignmentOp_OR_Equal;
					else if (currChar == '!') state = Not_Equal;
					else if (currChar == '>') state = GT_OR_GE;
					else if (currChar == '<') state = GT_OR_GE;
					else if (currChar == ')') currentToken = new Token(Token.RPAREN);
					else if (currChar == '(') currentToken = new Token(Token.LPAREN);
					else if (currChar == '{') currentToken = new Token(Token.LBRACE);
					else if (currChar == '}') currentToken = new Token(Token.RBRACE);
					else if (currChar == ';') currentToken = new Token(Token.SEMICOLON);
					else if (currChar == ',') currentToken = new Token(Token.COMMA);
					else if (currChar == '\0') state = EOI;
					else state = ERROR;
					break;
				case COMMENT:
					if((int)currChar == 10){
						state=START;
						shouldAdvance = true;
					}
					break;

				case INT_LITERAL:
					if(Character.isDigit(currChar))
						state=INT_LITERAL;
					else {
						currentToken=new Token(Token.INT_LITERAL, lexeme);
						state = START;
						shouldAdvance=false;
					}
					break;
				case ID:
					if (Character.isLetterOrDigit(currChar))
						state = ID;
					else {
						if(lexeme.equals("program")){
							currentToken=new Token(Token.PROGRAM);
							shouldAdvance=true;
							state = START;
						}else if(lexeme.equals("int")){
							currentToken=new Token(Token.INT);
							state = START;
							}else if(lexeme.equals("if")){
							currentToken=new Token(Token.If);
							state = START;
						}else if(lexeme.equals("while")){
							currentToken=new Token(Token.While);
							state = START;
						}else if(lexeme.equals("for")){
							currentToken=new Token(Token.For);
							state = START;
						}else if(lexeme.equals("print")){
							currentToken=new Token(Token.Print);
							state = START;
						}else if(lexeme.equals("read")){
							currentToken=new Token(Token.read);
							state = START;
						}else if(lexeme.equals("end")){
							currentToken=new Token(Token.END);
							state = START;
						}else if(lexeme.equals("else")){
							currentToken=new Token(Token.Else);
							state = START;
						}else{
							currentToken=new Token(Token.ID, lexeme);
							shouldAdvance=false;
							state=START;
						}
					}
					break;
				case GT_OR_GE:  
					if (currChar == '=') 
						currentToken = new Token(Token.GE); 
					else {
						currentToken = new Token(Token.GT);
						shouldAdvance = false;
					}
					break;
				case LT_OR_LE:  
					if (currChar == '=') 
						currentToken = new Token(Token.LE); 
					else {
						currentToken = new Token(Token.LT);
						shouldAdvance = false;
					}
					break;
				case AssignmentOp_OR_Equal:
					if (currChar == '=') 
						currentToken = new Token(Token.EQ); 
					else {
						currentToken = new Token(Token.ASSIGN_SYMBOL);
						shouldAdvance = false;
					}
					break;
					
				case ERROR:
					System.err.println("Unexpected character " + currChar + " on line " + line + " at column " + col);
					state = START;
					break;
					
				case EOI:  currentToken = new Token(Token.EOI); break;

				default:
					System.err.println("Unknown state " + state);
					System.exit(0);
					
			}

			lexeme += currChar;
			
			if (shouldAdvance && state != ERROR) advance();

		}
		return currentToken;
	}

	public Token token() {return currentToken;}
		
	void advance() throws IOException {
		int i = is.read();
		currChar = (i < 0) ? '\0' : (char)i;
		if (currChar == '\n') line ++; col = 0;
		col++;
	}

	InputStream is;
	char currChar;
	Token currentToken = null;
	int line = 1, col = 1;

	static final int 
		EOI = -2,
		ERROR = -1,
		START = 0,
		ID = 1,
		GT_OR_GE = 2,
		LT_OR_LE = 3,
		AssignmentOp_OR_Equal=4,
		Not_Equal=5,
		Maybe_program=6,
		Maybe_int=7,
		Maybe_if=8,
		Maybe_while=9,
		Maybe_for=10,
		Maybe_print=11,
		Maybe_read=12,
		Maybe_end=13,
		INT_LITERAL=14,
		COMMENT=30;
	
}