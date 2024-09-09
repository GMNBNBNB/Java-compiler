import java.util.List;

class StmtSeq {
	Stmt stmt;
	StmtSeq ss;
	Call call;

	void parse() {
		if (Parser.scanner.currentToken() == Core.ID) {
			stmt = new Assign();
		} else if (Parser.scanner.currentToken() == Core.OUT) {
			stmt = new Output();
		} else if (Parser.scanner.currentToken() == Core.IF) {
			stmt = new If();
		} else if (Parser.scanner.currentToken() == Core.WHILE) {
			stmt = new Loop();
		} else if (Parser.scanner.currentToken() == Core.INTEGER || Parser.scanner.currentToken() == Core.OBJECT) {
			stmt = new Decl();
		} else if(Parser.scanner.currentToken() == Core.BEGIN){
			call = new Call();
			call.parse();
		}else {
			System.out.println("ERROR: Bad start to statement: " + Parser.scanner.currentToken());
			System.exit(0);
		}
		if(stmt != null){
			stmt.parse();
		}
		if (Parser.scanner.currentToken() != Core.END && Parser.scanner.currentToken() != Core.ELSE) {
			ss = new StmtSeq();
			ss.parse();
		}
	}

	void print(int indent) {
		stmt.print(indent);
		if (ss != null) {
			ss.print(indent);
		}
	}

	void execute() {
		if(stmt != null){
			stmt.execute();
		}
		if(call != null){
			call.execute();
		}
		if (ss != null) {
			ss.execute();
		}	
	}
}