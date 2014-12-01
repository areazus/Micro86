package areaz.Micro86.HL.parser;

import java.io.*;
import areaz.Micro86.HL.lexical.Lexer;
import areaz.Micro86.HL.lexical.Token;

class Parser {
	String name, value="";
	
	Parser(Lexer lexer) throws IOException, ParserException {
		this.lexer = lexer;
	}

	void parse()  throws IOException, ParserException {
		program();
		match(Token.EOI);
	}
		
	void program() throws IOException, ParserException {
		match(Token.PROGRAM);
		while (optMatch(Token.INT)) 
			declaration();
		while (!optMatch(Token.END)) 
			statement();
	}

	void declaration() throws IOException, ParserException {
		name=lexer.token().lexeme;
		match(Token.ID);
		MicroPL86.analyze.init(name, false, lexer);
		if(optMatch(Token.COMMA)){
			declaration();
		}else{
			match(Token.SEMICOLON);
		}
	}

	void statement() throws IOException, ParserException {
		name=lexer.token().lexeme;
		if (optMatch(Token.ID)) 
			assignmentStatement();
		else if (optMatch(Token.If))
			ifStatement();
		else if (optMatch(Token.While))
			whileStatement();
		else if (optMatch(Token.For))
			forStatement();
		else if (optMatch(Token.Print))
			printStatement();
		else if (optMatch(Token.read))
			readStatement();
		else if (optMatch(Token.LBRACE)){
			compoundStatement();
		}
		else throw new ParserException(lexer, "Expecting statement, found " + lexer.token());

	}
	
	void compoundStatement() throws IOException, ParserException {
		//Done
		while(!optMatch(Token.RBRACE))
			statement();
	}

	void readStatement() throws IOException, ParserException  {
		//Done
		match(Token.ID);
		match(Token.SEMICOLON);
	}

	void printStatement() throws IOException, ParserException  {
		//DONE
		expression();
		match(Token.SEMICOLON);
		
	}

	void forStatement() throws IOException, ParserException  {
		match(Token.LPAREN);
					name=lexer.token().lexeme;	//What happens in this case?
		match(Token.ID);
		match(Token.ASSIGN_SYMBOL);
		expression();
		match(Token.COMMA);
		expression();
		match(Token.RPAREN);
		statement();
	}

	void ifStatement() throws IOException, ParserException {
		//Done
		match(Token.LPAREN);
		expression();
		if (!lexer.token().isRelop()) throw new ParserException(lexer, "Expecting relational, found " + lexer.token());
		lexer.next();
		expression();
		match(Token.RPAREN);
		statement();
		if(optMatch(Token.Else)){
			statement();
		}
	}
	
	void whileStatement() throws IOException, ParserException{
		//Done
		match(Token.LPAREN);
		expression();
		if (!lexer.token().isRelop()) throw new ParserException(lexer, "Expecting relational, found " + lexer.token());
		lexer.next();
		expression();
		match(Token.RPAREN);
		statement();
		
		
	}

	void assignmentStatement() throws IOException, ParserException {
		//Done
		match(Token.ASSIGN_SYMBOL);
		expression();
		match(Token.SEMICOLON);
		MicroPL86.analyze.put(name, value, lexer);
		value="";
	}


	void expression() throws IOException, ParserException {
		value="";
		term();
		while (optMatch(Token.PLUS_OP) || optMatch(Token.MINUS_OP)){
			term();
		}
	}
	
	void term() throws IOException, ParserException{
		factor();
		while(optMatch(Token.MUL_OP)||optMatch(Token.DIV_OP)||optMatch(Token.MOD_OP))
			factor();
	}

	void factor() throws IOException, ParserException {
		
			value+=lexer.token().lexeme;
			
		if (optMatch(Token.ID)) 
			return;
		if(optMatch(Token.INT_LITERAL)) 
			return;
		if(optMatch(Token.LPAREN)){
			expression();
			match(Token.RPAREN);
			return;
		}else
			throw new ParserException(lexer, "Expecting factor, found " + lexer.token());
	}

	boolean optMatch(int tokenType) throws IOException {
		
		
		if (lexer.token().type == tokenType) {
			if(tokenType==Token.PLUS_OP){
				value+="+";
			}else if(tokenType==Token.MINUS_OP){
				value+="-";
			}else if(tokenType==Token.MOD_OP){
				value+="%";
			}else if(tokenType==Token.MUL_OP){
				value+="*";
			}else if(tokenType==Token.DIV_OP){
				value+="/";
			}
				
			
			lexer.next();
			return true;
		}
		return false;
	}

	void match(int tokenType) throws IOException, ParserException {
		if (!optMatch(tokenType)) throw new ParserException(lexer, "Expecting '" + new Token(tokenType) + "', found " + lexer.token());
	}

	Lexer lexer;
}
