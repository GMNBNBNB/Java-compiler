import java.util.List;

class DeclSeq {
	Decl decl;
	DeclSeq ds;

	void parse() {
		decl = new Decl();
		decl.parse();
		if (Parser.scanner.currentToken() == Core.INTEGER || Parser.scanner.currentToken() == Core.OBJECT) {
			ds = new DeclSeq();
			ds.parse();
		}
	}

	void print(int indent) {
		decl.print(indent);
		if (ds != null) {
			ds.print(indent);
		}
	}

	void execute() {
		decl.execute();
		if (ds != null) {
			ds.execute();
		}
	}
}