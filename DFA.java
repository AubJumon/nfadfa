
import java.util.ArrayList;
import java.util.Arrays;
import java.io.*;
import java.util.StringTokenizer;

public class DFA {
    public int Q;
    public char[] Sigma;
    public ArrayList<ArrayList<Integer>> structure;
    public int initialState;
    public ArrayList<Integer> acceptingStates;
    public ArrayList<String> inputStrings;
    
    public DFA(String fileName) {
        File file = new File(fileName);
    }

    public void parsestrings(String fileName) {
        String curString;
        int curState;
        int curChar;
        int yes = 0;
        int no = 0;
        System.out.println("Printing results of strings attatched in "+fileName);
        for (int i = 0; i<inputStrings.size(); i++)
        {
            curState = initialState;
            curString = inputStrings.get(i);
            for (int j = 0; j<curString.length(); j++)
            {
                curChar = Arrays.binarySearch(Sigma,curString.charAt(j));
                curState = structure.get(curState).get(curChar);
            }
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
        }
        System.out.println("\n\nYes:"+yes+" No:"+no);
    }

    
   
}
