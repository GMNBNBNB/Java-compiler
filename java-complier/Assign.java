class Assign implements Stmt {
	// type is
	// 0 if id := <expr> assignment
	// 1 if id[id] := <expr> assignment
	// 2 if "new" assignment
	// 3 if "alias" assignment
	int type;
	// assignTo is the id on the LHS of assignment
	Id assignTo;
	// index is the id in the "new" assignment or id[id] assignment
	Id index;
	// init is the expression in a "new" assignment
	Expr init;
	// assignFrom is the id on RHS of "alias" assignment
	Id assignFrom;
	// RHS expression in an assignment
	Expr expr;

	public void parse() {
		type = 0;
		assignTo = new Id();
		assignTo.parse();
		if (Parser.scanner.currentToken() == Core.LBRACE) {
			type = 1;
			Parser.scanner.nextToken();
			index = new Id();
			index.parse();
			Parser.expectedToken(Core.RBRACE);
			Parser.scanner.nextToken();
		}
		if (Parser.scanner.currentToken() == Core.ASSIGN) {
			Parser.scanner.nextToken();
			if (Parser.scanner.currentToken() == Core.NEW) {
				// new object assign
				type = 2;
				Parser.scanner.nextToken();
				Parser.expectedToken(Core.OBJECT);
				Parser.scanner.nextToken();
				Parser.expectedToken(Core.LPAREN);
				Parser.scanner.nextToken();
				index = new Id();
				index.parse();
				Parser.expectedToken(Core.COMMA);
				Parser.scanner.nextToken();
				init = new Expr();
				init.parse();
				Parser.expectedToken(Core.RPAREN);
				Parser.scanner.nextToken();
			} else {
				// Expression assign, type will be either 0 or 1
				expr = new Expr();
				expr.parse();
			}
		} else {
			// Aliasing assign
			Parser.expectedToken(Core.COLON);
			Parser.scanner.nextToken();
			type = 3;
			assignFrom = new Id();
			assignFrom.parse();
		}
		Parser.expectedToken(Core.SEMICOLON);
		Parser.scanner.nextToken();
	}

	public void print(int indent) {
		for (int i = 0; i < indent; i++) {
			System.out.print("\t");
		}
		assignTo.print();
		if (type == 1) {
			System.out.print("[");
			index.print();
			System.out.print("]");
		}
		if (type == 0 || type == 1) {
			System.out.print("=");
			expr.print();
		} else if (type == 2) {
			System.out.print("=new object(");
			index.print();
			System.out.print(",");
			init.print();
			System.out.print(")");
		} else if (type == 3) {
			System.out.print(":");
			assignFrom.print();
		}
		System.out.println(";");
	}

	// do executes
	public void execute() {
		int value;
		if (type == 0 || type == 1) {
			value = expr.execute();
			if (type == 1) {
				Memory.AssginArrayValue(assignTo.identifier, index.identifier, value);	
				Memory.assignValue(assignTo.identifier, value);
			} else {
				Memory.assignValue(assignTo.identifier, value);
			}
		} else if (type == 2) {
			Memory.declareNew(assignTo.identifier,1);
			Memory.declareObject(assignTo.identifier);
			Memory.declareObject(index.identifier);
			value = init.execute();
			if(index.identifier.equals("default")){
				Memory.assignValue(assignTo.identifier, value);
				Memory.assignValue(index.identifier, value);
			}else{
				Memory.assignValue(index.identifier, value);
			}
			Memory.AssginArrayValue(assignTo.identifier, index.identifier, value);	
			Memory.GarbageCollection();
		} else if (type == 3) {
			Memory.assignReference(assignFrom.identifier, assignTo.identifier);
		}
	}
}