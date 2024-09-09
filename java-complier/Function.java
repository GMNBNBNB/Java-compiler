class Function {
  String name;
  String[] parameter = new String[9999];
  DeclSeq ds;
  StmtSeq ss;
  int i;

  void parse() {
    i = 0;
    Parser.expectedToken(Core.PROCEDURE);
    Parser.scanner.nextToken();
    Parser.expectedToken(Core.ID);
    name = Parser.scanner.getId();

    Parser.scanner.nextToken();
    Parser.expectedToken(Core.LPAREN);
    Parser.scanner.nextToken();
    parameter[i] = Parser.scanner.getId();
    i++;
    
    Parser.scanner.nextToken();
    while (Parser.scanner.currentToken() == Core.COMMA) {
      Parser.scanner.nextToken();
      parameter[i] = Parser.scanner.getId();
      for(int j = 0; j < i; j++){
        if(parameter[j].equals(parameter[i])){
          System.out.println("ERROR: duplicate formal parameters");
          System.exit(0);
        }
      }
      i++;
      Parser.scanner.nextToken();
    }
    Parser.expectedToken(Core.RPAREN);

    Parser.scanner.nextToken();
    Parser.expectedToken(Core.IS);
    Parser.scanner.nextToken();
    if (Parser.scanner.currentToken() == Core.INTEGER || Parser.scanner.currentToken() == Core.OBJECT) {
      ds = new DeclSeq();
      ds.parse();
    }
    ss = new StmtSeq();
    ss.parse();
    Parser.expectedToken(Core.END);

    Parser.scanner.nextToken();
  }

  void print() {
    String temp = " ( ";
    for(int j = 0; j < i; j++) {
      temp += parameter[j];
      if (j != parameter.length - 1) {
        temp += ",";
      }
    }
    temp += " ) ";
    System.out.println("procedure " + name + temp + " is");
    if (ds != null) {
      ds.print(1);
    }
    System.out.println("begin ");
    ss.print(1);
    System.out.println("end");
  }

  void execute() {
    Memory.SetInFunctionMap(name, this);
  }
}