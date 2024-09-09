class Factor {
	Id id;
	Id index;
	int constant;
	Expr expr;

	void parse() {
		if (Parser.scanner.currentToken() == Core.ID) {
			id = new Id();
			id.parse();
			if (Parser.scanner.currentToken() == Core.LBRACE) {
				Parser.scanner.nextToken();
				index = new Id();
				index.parse();
				Parser.expectedToken(Core.RBRACE);
				Parser.scanner.nextToken();
			}
		} else if (Parser.scanner.currentToken() == Core.CONST) {
			constant = Parser.scanner.getConst();
			Parser.scanner.nextToken();
		} else if (Parser.scanner.currentToken() == Core.LPAREN) {
			Parser.scanner.nextToken();
			expr = new Expr();
			expr.parse();
			Parser.expectedToken(Core.RPAREN);
			Parser.scanner.nextToken();
		} else if (Parser.scanner.currentToken() == Core.IN) {
			// Set constant to -1 to remember is input
			constant = -1;
			Parser.expectedToken(Core.IN);
			Parser.scanner.nextToken();
			Parser.expectedToken(Core.LPAREN);
			Parser.scanner.nextToken();
			Parser.expectedToken(Core.RPAREN);
			Parser.scanner.nextToken();
		} else {
			System.out.println("ERROR: Expected ID, CONST, LPAREN, or IN, recieved " + Parser.scanner.currentToken());
			System.exit(0);
		}
	}

	void print() {
		if (id != null) {
			id.print();
			if (index != null) {
				System.out.print("[");
				index.print();
				System.out.print("]");
			}
		} else if (expr != null) {
			System.out.print("(");
			expr.print();
			System.out.print(")");
		} else if (constant == -1) {
			System.out.print("in()");
		} else {
			System.out.print(constant);
		}
	}

	int execute() {
		int value = 0;
		if (id != null) {
			value = id.execute();
			if (index != null) {
				value = Memory.GetValueFromArray(id.identifier,index.identifier);
			}
		} else if (expr != null) {
			value = expr.execute();
		} else if (constant == -1) {
			if(Parser.inputfile.currentToken() == Core.EOS){
				System.out.print(".data file not having enough values");
				System.exit(0);
			}
			value = Parser.inputfile.getConst();
			Parser.inputfile.nextToken();
		} else {
			value = constant;
		}
		return value;
	}
}