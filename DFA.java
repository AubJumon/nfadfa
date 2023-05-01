
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.io.*;
import java.util.StringTokenizer;

public class DFA {
    private int Q;
    private char[] Sigma;
    private Integer[][] structure;
    private int initialState;
    private ArrayList<Integer> acceptingStates;
    private ArrayList<String> inputStrings;
    
    public DFA(String fileName) {
        File file = new File(fileName);

        try {
            FileInputStream fis = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            String line = null;

            //parse |Q| line
            line = reader.readLine();

            line = line.replace("|Q|:", "");
            line = line.replaceAll("\\s+", "");
            this.Q = Integer.parseInt(line);

            //parse sigma
            line = reader.readLine();
            line = line.replace("Sigma:", "");
            line = line.replaceAll("\\s+", "");
            this.Sigma = line.toCharArray();
            //skip -----
            line = reader.readLine();

            //get the matrix
            this.structure = new Integer[Q][Sigma.length];
            for (int i = 0; i < Q; i++) {
                line = reader.readLine();
                StringTokenizer st = new StringTokenizer(line);
                st.nextToken();
                /* aub code
                // ArrayList<ArrayList<Integer>> stage = new ArrayList<ArrayList<Integer>>();
                // for (int k = 0; k < (Sigma.length + 1) && st.hasMoreTokens(); k++) {
                //     ArrayList<Integer> sigma = new ArrayList<Integer>();
                //     StringTokenizer temp = new StringTokenizer(st.nextToken().replace("{", "").replace("}", ""), ",");
                //     for (int j = 0; j < Q && temp.hasMoreTokens(); j++) {
                //         sigma.add(Integer.parseInt(temp.nextToken()));
                //     }
                //     stage.add(sigma);
                // }
                // structure.add(stage);
                */
                //my code
                for (int k = 0; k < Sigma.length; k++) {
                    structure[i][k] = Integer.parseInt(st.nextToken());
                }
                //end my code
            }

            // skip -----
            line = reader.readLine();

            // parse initial state
            line = reader.readLine();
            line = line.replace("Initial State:", "");
            line = line.replaceAll("\\s+", "");
            this.initialState = Integer.parseInt(line);

            //parse accepting states
            line = reader.readLine();
            line = line.replace("Accepting Sate(s):", "");
            line = line.replaceAll("\\s+", "");
            this.acceptingStates = new ArrayList<Integer>();
            StringTokenizer st = new StringTokenizer(line, ",");
            for (int i = 0; st.hasMoreTokens(); i++) {
                this.acceptingStates.add(Integer.parseInt(st.nextToken()));
            }

            //skip -- Input strings for testing -----------
            line = reader.readLine();
            this.inputStrings = new ArrayList<String>();
            while ((line = reader.readLine()) != null) {
                inputStrings.add(line);
            }

            fis.close();
            reader.close();
        } catch (Exception e) {
            System.out.println("could not open file");
        }
    }

    //loops through the list of input strings, printing yes if the string is valid, no if it isn't. 
    //Keeps track of the number of yes and no to make sure exactly 15 are printed on each line and for the final count at the end.
    public void parsestrings(String fileName) {
        String curString;
        int curState;
        int curChar;
        int yes = 0;
        int no = 0;
        System.out.println("Printing results of strings attatched in "+fileName);
        for (int i = 1; i<inputStrings.size(); i++)
        {
            curState = initialState;
            curString = inputStrings.get(i);
            for (int j = 0; j<curString.length(); j++)
            {
                curChar = Arrays.binarySearch(Sigma,curString.charAt(j));
                curState = structure[curState][curChar];
            }
            //At the end of the input string, checks to see if the current state is an accepting state.
            if (acceptingStates.contains(curState))
            {
                System.out.print("Yes ");
                yes++;
            }
            else 
            {
                System.out.print("No ");
                no++;
            }
            if ((yes+no)==15)
            {
                System.out.println();
            }
        }
        System.out.println("\n\nYes:"+yes+" No:"+no);
    }

    public void minimizeDfa() {
        boolean initialStateChanged = false;
        int[][] m = new int[Q][Q];

        for (int p = 0; p < Q; p++) {
            for (int q = 0; q < Q; q++) {                
                // For every p, q isElementOf Q, set m[p, q] = 0
                m[p][q] = 0;
                if ((acceptingStates.contains(p) ^ acceptingStates.contains(q))) {
                    // For every p, q isElementOf Q, if p isElementOf F and q isElementOf F, set m[p, q]= m[q, p]=1
                    m[p][q] = 1;
                    m[q][p] = 1;
                }
            }
        }
        // Repeat the following process until no new m[p, q] is set to 1.
        boolean hasUpdated = true;
        while (hasUpdated) {
            hasUpdated = false;
            // For every p, q isElementOf Q with p notEqual q and m[p, q] = 0, 
            for (int p = 0; p < Q; p++) {
                for (int q = 0; q < Q; q++) {
                    if (p != q && m[p][q] == 0) {
                        // if there exists a isElementOf Sigma such that m[δ(p, a), δ(q, a)]=1,
                        for (int a = 0; a < Sigma.length; a++){
                            // char a = Sigma[i];
                            if ( m [ structure[p][a] ] [ structure[q][a] ] == 1) {
                                // then set m[p, q]=m[q, p]=1;
                                // if (m[p][q]==0 || m[q][p]==0){
                                    hasUpdated = true;
                                // }
                                m[p][q] = 1;
                                m[q][p] = 1;
                            }
                        }
                    }
                }
            }
        }

        // get all pairs that still have a 0 in m
        ArrayList<intPair> pairs = new ArrayList<intPair>();
        for (int p = 0; p < Q; p++) {
            for (int q = 0; q < Q; q++) {
                if (p < q && m[p][q] == 0) {
                    pairs.add(new intPair(p,q));
                }
            }
        }

        // combine all int pairs into int sets
        /*
         * while pairs remain
         *      place pair into new set
         *      while edits are made
         *          iterate pairs and add matches
         */
        ArrayList<HashSet<Integer>> intSets = new ArrayList<HashSet<Integer>>();
        ArrayList<Integer> intsInSets = new ArrayList<Integer>();
        while (pairs.size() > 0) {
            HashSet<Integer> newSet = new HashSet<Integer>();
            newSet.add(pairs.get(0).A());
            newSet.add(pairs.get(0).B());
            boolean changing = true;
            while (changing) {
                changing = false;
                for (int i = 0; i < pairs.size(); i++) {
                    if (newSet.contains(pairs.get(i).A()) || newSet.contains(pairs.get(i).B())) {
                        newSet.add(pairs.get(i).A());
                        newSet.add(pairs.get(i).B());
                        intsInSets.add(pairs.get(i).A());
                        intsInSets.add(pairs.get(i).B());
                        pairs.remove(i);
                        changing = true;
                    }
                }
            }
            intSets.add(newSet);
        }
        
        /*
         * Create a new matrix structure with less states
         * 
         * 
         */
        ArrayList<ArrayList<Integer>> newStruct = new ArrayList<ArrayList<Integer>>();
        // add unchanged states
        for (int i = 0; i < structure.length; i++) {
            if (!intsInSets.contains(i)){
                ArrayList<Integer> curState = new ArrayList<Integer>();
                // copy state into new structure
                for (int j = 0; j < structure[i].length; j++) {
                    if (intsInSets.contains(structure[i][j])) {
                        // find which set has it and use that index value - will add grouped states at those indexs
                        for (int k = 0; k < intSets.size(); k++) {
                            if (intSets.get(k).contains(structure[i][j])) {
                                curState.add(k);
                            }
                        }
                    } else {
                        // copy and update the index value
                        int stateNumber = structure[i][j];
                        stateNumber+=intSets.size();
                        for (int k = 0; k < intsInSets.size(); k++) {
                            if (intsInSets.get(k) < structure[i][j]) {
                                stateNumber--;
                            }
                        }
                        curState.add(stateNumber);
                    }
                }
                newStruct.add(curState);
            }
            
        }
        // add new grouped states at front of array
        for (int i = 0; i < intSets.size(); i++) {
            ArrayList<Integer> curState = new ArrayList<Integer>();
            int removedState = intSets.get(i).iterator().next();
            for (int j = 0; j < structure[removedState].length; j++) {
                if (intsInSets.contains(structure[removedState][j])) {
                    // find which set has it and use that index value - will add grouped states at those indexs
                    for (int k = 0; k < intSets.size(); k++) {
                        if (intSets.get(k).contains(structure[removedState][j])) {
                            curState.add(k);
                        }
                    }
                } else {
                    // if not, copy and update the index value from original structure
                    int tempState = structure[removedState][j];
                    tempState += intSets.size();
                    for (int l = 0; l < intsInSets.size(); l++) {
                        if (intsInSets.get(l) < structure[removedState][j]){
                            tempState--;
                        }
                    } 
                    curState.add(tempState);
                }
            }
            newStruct.add(i, curState);
        }

        // update initialState
        if (intsInSets.contains(initialState)) {
            for (int i = 0; i < intSets.size(); i++) {
                if (intSets.get(i).contains(initialState)) {
                    initialState = i;
                }
            }
        } else {
            int tempInitState = initialState;
            tempInitState += intSets.size();
            for (int k = 0; k < intsInSets.size(); k++) {
                if (intsInSets.get(k) < initialState) {
                    tempInitState--;
                }
            }    
            initialState = tempInitState;
        }

        // update accepting states
        ArrayList<Integer> newAcceptingStates = new ArrayList<Integer>();
        for (int i = 0; i < acceptingStates.size(); i++){
            if (intsInSets.contains(acceptingStates.get(i))) {
                for (int k = 0; k < intSets.size(); k++) {
                    if (intSets.get(k).contains(acceptingStates.get(i)) && !newAcceptingStates.contains(k)) {
                        newAcceptingStates.add(k);
                    }
                }
            } else {
                int tempState = acceptingStates.get(i);
                tempState += intSets.size();
                for (int k = 0; k < intsInSets.size(); k++) {
                    if (intsInSets.get(k) < acceptingStates.get(i)) {
                        tempState--;
                    }
                }  
                if (!newAcceptingStates.contains(tempState)){
                    newAcceptingStates.add(tempState);
                }  
            }
        }
        Collections.sort(newAcceptingStates);
        acceptingStates = newAcceptingStates;

        // copy new Struct into struct
        Q = newStruct.size();
        Integer[][] tempStruct = new Integer[Q][Sigma.length];
        for (int i = 0; i < Q; i++) {
            tempStruct[i] = newStruct.get(i).toArray(structure[i]);
        }
        structure = tempStruct;
    }

    public String toString() {
        String rep = "Sigma: ";
        for (int i = 0; i < Sigma.length; i++) {
            rep += "\t" + Sigma[i];
        }
        rep += "\n----------------------------------";
        for (int i = 0; i < Q; i++) {
            rep += "\n" + i + ":";
            for (int j = 0; j < Sigma.length; j++) {
                rep += "\t" + structure[i][j];
            }
        }
        rep += "\n----------------------------------\nInitial State: " + 0 + "\nAccepting State(s): ";
        rep += acceptingStates.get(0);
        for (int i = 1; i < acceptingStates.size(); i++) {
            rep += "," + acceptingStates.get(i);
        }
        rep += "\n";
        return rep;
    }
    

    // simple helper data structure
    private class intPair {
        private int a;
        private int b;

        public intPair(int a, int b) {
            this.a = a;
            this.b = b;
        }

        public int A() { return a; }
        public int B() { return b; }
        public void setA(int a) { this.a = a; }
        public void setB(int b) { this.b = b; }
    }

    public int getQ()
    {
        return Q;
    }
}
