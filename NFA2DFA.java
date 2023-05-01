
public class NFA2DFA {
    
    public static void main(String[] args) {
        String fileName = "temp.txt";
        if (args.length > 0) {
            fileName = args[0];
        }
        NFA nfa = new NFA(fileName);
        DFA dfa = new DFA("test.txt");
        nfa.printDFA("test.txt",dfa);
        dfa.parsestrings(fileName);

    }
}