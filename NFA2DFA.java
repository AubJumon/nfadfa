
public class NFA2DFA {
    
    public static void main(String[] args) {
        String fileName = "temp.txt";
        if (args.length > 0) {
            fileName = args[0];
        }
        NFA nfa = new NFA(fileName);
        nfa.printDFA("test.txt");
        DFA dfa = new DFA("test.txt");
        System.out.println(dfa);
        dfa.minimizeDfa();
        System.out.println(dfa);
    }
}