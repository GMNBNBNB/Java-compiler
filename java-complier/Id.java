class Id {
	String identifier;
	
	void parse() {
		Parser.expectedToken(Core.ID);
		identifier = Parser.scanner.getId();
		Parser.scanner.nextToken();
	}
	
	void print() {
		System.out.print(identifier);
	}
	
	int execute(){
		Object result = Memory.getValue(identifier);
		if (result instanceof String) {
			return -1;
		} else{
			return (Integer)result;
		}
	}
}