

import java.util.ArrayList;
import java.util.Collections;
import java.io.*;
import java.util.StringTokenizer;

public class NFA {
    private int Q;
    private char[] Sigma;
    private ArrayList<ArrayList<ArrayList<Integer>>> structure;
    private int initialState;
    private ArrayList<Integer> acceptingStates;
    private ArrayList<String> inputStrings;

    /*
     * Create NFA from file
     */
    public NFA(String fileName) {
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

            //get {}
            this.structure = new ArrayList<ArrayList<ArrayList<Integer>>>();
            for (int i = 0; i < Q; i++) {
                line = reader.readLine();
                StringTokenizer st = new StringTokenizer(line);
                st.nextToken();
                ArrayList<ArrayList<Integer>> stage = new ArrayList<ArrayList<Integer>>();
                for (int k = 0; k < (Sigma.length + 1) && st.hasMoreTokens(); k++) {
                    ArrayList<Integer> sigma = new ArrayList<Integer>();
                    StringTokenizer temp = new StringTokenizer(st.nextToken().replace("{", "").replace("}", ""), ",");
                    for (int j = 0; j < Q && temp.hasMoreTokens(); j++) {
                        sigma.add(Integer.parseInt(temp.nextToken()));
                    }
                    stage.add(sigma);
                }
                structure.add(stage);
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
    
    public void printDFA(String fileName,DFA dfa) {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(fileName);
            // inherited method from java.io.OutputStreamWriter
            fileWriter.write(toDFA(dfa));
            fileWriter.write("\n-- Input strings for testing -----------\n");
            for (int i = 0; i < inputStrings.size(); i++) {
                fileWriter.write(inputStrings.get(i)+"\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileWriter != null) {
                    fileWriter.flush();
                    fileWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    
    // private String toDFA(DFA dfa) {

    //     ArrayList<ArrayList<Integer>> DFAstates = new ArrayList<ArrayList<Integer>>(); // list of each state and its states
    //     ArrayList<ArrayList<Integer>> DFAstructure = new ArrayList<ArrayList<Integer>>();
    //     ArrayList<Integer> state = new ArrayList<Integer>(); //currently working on state;
    //     ArrayList<Integer> DFAaccepting = new ArrayList<Integer>();

    //     state.add(initialState);
    //     DFAstates.add(state);

    //     dfa.Q = Q;
    //     dfa.Sigma = Sigma;
    //     dfa.structure = DFAstructure;
    //     dfa.initialState = 0;
    //     dfa.acceptingStates = DFAaccepting;
    //     dfa.inputStrings = inputStrings;


    //     for (int s = 0; s < DFAstates.size(); s++) {
    //         ArrayList<Integer> tempState = new ArrayList<Integer>();
    //         state = DFAstates.get(s);

    //         for (int k = 0; k < Sigma.length; k++) {// loop through each sigma character
    //             ArrayList<Integer> sigmaList = new ArrayList<Integer>();//sigma list is going to be empty initially sigma list = whatever states that can reach through that sigma character.

    //             for (int i = 0; i < state.size(); i++) {//loop through each state currently acceptable

    //                 int currentState = state.get(i); //access next state in the list
    //                 for (int z = 0; z < structure.get(currentState).get(k).size(); z++) { //loop states list for the current sigma letter
    //                     int node = structure.get(currentState).get(k).get(z);
    //                     //add the nodes that have not been added yet
    //                     sigmaList = addNode(sigmaList, node);
    //                 }
    //             }

    //             Collections.sort(sigmaList);
    //             boolean add = true;
    //             for (int c = 0; c < DFAstates.size(); c++) {
    //                 if (DFAstates.get(c).equals(sigmaList)) {
    //                     add = false;
    //                 }
    //             }
    //             if (add) {
    //                 DFAstates.add(sigmaList);
    //             }
    //             tempState.add(findState(DFAstates, sigmaList));

    //         }
    //         DFAstructure.add(tempState);
    //     }

       
    //     for (int i = 0; i < DFAstates.size(); i++) {
    //         boolean accept = false;
    //         for (int j = 0; j < acceptingStates.size(); j++) {
    //             if (DFAstates.get(i).contains(acceptingStates.get(j))) {
    //                 accept = true;
    //             }
    //         }
    //         if (accept) {
    //             DFAaccepting.add(i);
    //         }
    //     }

    //     String rep = "|Q|:\t" + DFAstates.size() + "\nSigma: ";
    //     for (int i = 0; i < Sigma.length; i++) {
    //         rep += "\t" + Sigma[i];
    //     }
    //     rep += "\n------------------------------";
    //     for (int i = 0; i < DFAstates.size(); i++) {
    //         rep += "\n\t" + i + ":";
    //         for (int j = 0; j < Sigma.length; j++) {
    //             rep += "\t" + DFAstructure.get(i).get(j);
    //         }
    //     }
    //     rep += "\n------------------------------\nInitial State: " + 0 + "\nAccepting Sate(s): ";
    //     rep += DFAaccepting.get(0);
    //     for (int i = 1; i < DFAaccepting.size(); i++) {
    //         rep += "," + DFAaccepting.get(i);
    //     }
    //     rep += "\n";
    //     return rep;
    // }
    
    private int findState(ArrayList<ArrayList<Integer>> states, ArrayList<Integer> sigmaList) {
        for (int i = 0; i < states.size(); i++) {
            if (states.get(i).equals(sigmaList)) {
                return i;
            }
        }
        return -1;
    }
    
    private ArrayList<Integer> addNode(ArrayList<Integer> sigmaList, int node) {
        if (!sigmaList.contains(node)) {
            sigmaList.add(node);
            for (int i = 0; i < structure.get(node).get(Sigma.length).size(); i++) {
                sigmaList = addNode(sigmaList, structure.get(node).get(Sigma.length).get(i));
            }
        }
        return sigmaList;
    }

    public String toString() {
        String rep = "|Q|:\t" + Q + "\nSigma: ";
        for (int i = 0; i < Sigma.length; i++) {
            rep += "\t" + Sigma[i];
        }
        rep += "\n------------------------------";
        for (int i = 0; i < Q; i++) {
            rep += "\n\t" + i + ":";
            for (int j = 0; j < Sigma.length + 1; j++) {
                rep += "\t{";
                for (int k = 0; k < structure.get(i).get(j).size(); k++) {
                    rep += "," + structure.get(i).get(j).get(k);
                }
                rep += "}";
            }
        }
        rep += "\n------------------------------\nInitial State: " + initialState + "\nAccepting Sate(s): ";
        rep += "," + acceptingStates.get(0);
        for (int i = 1; i < acceptingStates.size(); i++) {
            rep += "," + acceptingStates.get(i);
        }
        rep += "\n";
        return rep;
    }
}
