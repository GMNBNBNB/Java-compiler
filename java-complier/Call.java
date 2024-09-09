class Call {
  String[] parameter = new String[9999];
  String functionName;
  int i;

  void parse() {
    i = 0;
    Parser.scanner.nextToken();
    Parser.expectedToken(Core.ID);
    functionName = Parser.scanner.getId();
    Parser.scanner.nextToken();

    Parser.expectedToken(Core.LPAREN);
    Parser.scanner.nextToken();
    parameter[i] = Parser.scanner.getId();
    i++;
    Parser.scanner.nextToken();
    while (Parser.scanner.currentToken() == Core.COMMA) {
      Parser.scanner.nextToken();
      parameter[i] = Parser.scanner.getId();
      i++;
      Parser.scanner.nextToken();
    }
    Parser.expectedToken(Core.RPAREN);
    Parser.scanner.nextToken();
    Parser.expectedToken(Core.SEMICOLON);

    Parser.scanner.nextToken();
  }

  void execute() {
    Function function = Memory.GetFunctionByName(functionName);
    Memory.FunctionGc(true);
    
    for (int j = 0; j < i; j++) {
      Memory.SetFunctionParameterValue(function.parameter[j], parameter[j], Memory.getValue(parameter[j]));
    }

    if (function.ds != null) {
      function.ds.execute();
    }
    
    function.ss.execute();
    for (int j = 0; j < i; j++) {
      if (!Memory.declareNew(function.parameter[j], 0)) {
        Memory.assignValue(parameter[j], (Integer) Memory.GetFunctionParameterValue(function.parameter[j]));
      }
    }


    
    Memory.FunctionGc(true);
    Memory.CleargarbageCollection();
    Memory.FunctionGc(false);
  }

}