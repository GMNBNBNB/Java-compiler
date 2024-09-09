import java.util.*;

class Procedure {
	String name;
	DeclSeq ds;
	StmtSeq ss;
	Function function;

	void parse() {
		Parser.expectedToken(Core.PROCEDURE);
		Parser.scanner.nextToken();
		Parser.expectedToken(Core.ID);
		name = Parser.scanner.getId();
		Parser.scanner.nextToken();
		Parser.expectedToken(Core.IS);
		Parser.scanner.nextToken();
		if (Parser.scanner.currentToken() == Core.INTEGER || Parser.scanner.currentToken() == Core.OBJECT) {
			ds = new DeclSeq();
			ds.parse();
		}
		while (Parser.scanner.currentToken() == Core.PROCEDURE) {
			function = new Function();
			function.parse();
			function.execute();
		}
		if (Parser.scanner.currentToken() == Core.INTEGER || Parser.scanner.currentToken() == Core.OBJECT) {
			ds = new DeclSeq();
			ds.parse();
		}
		Parser.expectedToken(Core.BEGIN);
		Parser.scanner.nextToken();
		ss = new StmtSeq();
		ss.parse();
		Parser.expectedToken(Core.END);
		Parser.scanner.nextToken();
		Parser.expectedToken(Core.EOS);
	}

	void print() {
		System.out.println("procedure " + name + " is");
		if (ds != null) {
			ds.print(1);
		}
		System.out.println("begin ");
		ss.print(1);
		System.out.println("end");
	}

	void execute() {
		// initialize
		Memory.initialize();

		//
		if (ds != null) {
			ds.execute();
		}
		//
		ss.execute();

		Memory.CleargarbageCollection();
	}
}