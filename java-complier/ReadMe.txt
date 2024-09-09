Meng Gao

Assign.java - Class for handling assignment statements.
Cmpr.java - Class for handling comparison operations.
Cond.java - Class for handling conditional expressions.
Core.java - Core functionality class for the compiler.
Decl.java - Class for handling declaration statements.
DeclInteger.java - Class for handling integer declarations.
DeclObj.java - Class for handling object declarations.
DeclSeq.java - Class for handling sequences of declarations.
Expr.java - Class for handling expressions.
Factor.java - Class for handling factors (basic components of expressions).
Id.java - Class for handling identifiers.
If.java - Class for handling if statements.
Loop.java - Class for handling loop statements.
Main.java - Main entry point class for the compiler.
Memory.java - Class for handling memory management.
Out.java - Class for handling output operations.
Parser.java - Class for handling syntax parsing.
Procedure.java - Class for handling procedures (functions, methods).
Scanner.java - Class for handling lexical analysis.
Stmt.java - Class for handling statements.
StmtSeq.java - Class for handling sequences of statements.
Term.java - Class for handling terms (elements in expressions).
Call.java - Class for call function.
Function.java - use to store function to memory.

Any special features or comments on your project : No

A description of the overall design of the interpreter, in particular how you are handling variables w.r.t. tracking their values and hiding variables when entering a new scope:
	I use Scanner.java, this scanner reads the source code and breaks it down into tokens. Tokens are the smallest units of meaning, such as keywords, identifiers, operators, and literals.And Parser.java,the parser takes the tokens produced by the scanner and arranges them into a syntax tree. This tree represents the grammatical structure of the source code, with nodes representing different constructs such as expressions, statements, and declarations. Then Core.java and other related files,The core of the interpreter performs semantic analysis and executes the code. This involves traversing the syntax tree, evaluating expressions, executing statements, and managing variables and scopes. I create a new file named Memory.java in this project, The interpreter maintains a symbol table to keep track of variables and their values. The symbol table is essentially a map that associates variable names with their current values. It also deal with local scope and global scope, in the global scope all variables are stored in the global scope. This scope is always accessible unless shadowed by a variable with the same name in a local scope. In the local scope, When the interpreter enters a new block of code that defines a local scope (such as a function or a conditional block), a new local scope is created. This local scope can hide variables from outer scopes. When scanner read a function, function.java will parse function and store function name whit function. Then when scanner read begin xxx, call.java will read map of memory to check if we have that function, if yes, we will run this function.For garbage collection, I add check garbage in the memory file and fix new object.

Any known bugs in your scanner : No