
public class NFA2DFA {
    
    public static void main(String[] args) {
        int initNum;
        int finalNum;

        String fileName = "X.nfa";
        if (args.length > 0) {
            fileName = args[0];
        }
        System.out.println("NFA "+fileName+" to DFA X.dfa");
        NFA nfa = new NFA(fileName);
        DFA dfa = new DFA("X.dfa")
        ;
        nfa.printDFA("X.dfa");
        System.out.println(dfa);
        dfa.parsestrings(fileName);
        initNum = dfa.getQ();

        dfa.minimizeDfa();
        System.out.println("\nMinimized DFA from X.dfa");
        System.out.println(dfa);
        dfa.parsestrings(fileName);
        finalNum = dfa.getQ();

        System.out.println("\n|Q| "+initNum+" -> "+finalNum);

    }
}