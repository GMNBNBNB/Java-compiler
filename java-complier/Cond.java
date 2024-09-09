class Cond {
	Cmpr cmpr;
	Cond cond;
	int option;
	
	void parse() {
		option = 0;
		if (Parser.scanner.currentToken() == Core.NOT){
			option = 1;
			Parser.scanner.nextToken();
			cond = new Cond();
			cond.parse();
		} else if (Parser.scanner.currentToken() == Core.LBRACE) {
			option = 2;
			Parser.scanner.nextToken();
			cond = new Cond();
			cond.parse();
			Parser.expectedToken(Core.RBRACE);
			Parser.scanner.nextToken();
		} else {
			cmpr = new Cmpr();
			cmpr.parse();
			if (Parser.scanner.currentToken() == Core.OR) {
				option = 3;
				Parser.scanner.nextToken();
				cond = new Cond();
				cond.parse();
			} else if (Parser.scanner.currentToken() == Core.AND) {
				option = 4;
				Parser.scanner.nextToken();
				cond = new Cond();
				cond.parse();
			}
		}
	}
	
	void print() {
		if (option == 1) {
			System.out.print("not ");
			cond.print();
		} else if (option == 2) {
			System.out.print("[");
			cond.print();
			System.out.print("]");
		}else {
			cmpr.print();
			if (cond != null) {
				if (option == 3) System.out.print(" or ");
				if (option == 4) System.out.print(" and ");
				cond.print();
			}
		}
	}
	// do executes
	boolean execute(){
		boolean check;
		if (option == 1) {
			if(!cond.execute()){
				check = true;
			}else{
				check = false;
			}
		}else if(option == 2){
			check = cond.execute();
		}else {
			check = cmpr.execute();
			if (cond != null) {
				if (option == 3){
					check = check || cond.execute();
				}
				if (option == 4){
					check = check && cond.execute();
				}
			}
		}
		return check;
	}

	
}