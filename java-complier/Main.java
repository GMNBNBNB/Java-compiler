class Main {
	public static void main(String[] args) {
		// Initialize the scanner with the input file

		
		Scanner S = new Scanner(args[0]);
		Scanner inputData = new Scanner(args[1]);

		// Scanner S = new Scanner("src/main/java/Correct/8.code");
		// Scanner inputData = new Scanner("src/main/java/Correct/1.data");
		
		Parser.scanner = S;

		Parser.inputfile = inputData;
		
		
		Procedure p = new Procedure();
		
		p.parse();

		p.execute();
		
		//p.print();
	}
}