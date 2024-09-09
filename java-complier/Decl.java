class Decl implements Stmt {
	DeclInteger declInt;
	DeclObj declObj;
	boolean isInteger;
	
	public void parse() {
		if (Parser.scanner.currentToken() == Core.INTEGER) {
			declInt = new DeclInteger();
			declInt.parse();
			isInteger = true;
		} else {
			isInteger = false;
			declObj = new DeclObj();
			declObj.parse();
		}
	}
	
	public void print(int indent) {
		if (declInt != null) {
			declInt.print(indent);
		} else {
			declObj.print(indent);
		}
	}

	public void execute() {
			if (isInteger) {
				declInt.execute();
			} else {
				declObj.execute();
			}
	}
}