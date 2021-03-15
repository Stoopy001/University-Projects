// -----------------------------------------------------------------------------
// Program: Check the correctness of a solution for the Tower of Hanoi problem
// -----------------------------------------------------------------------------

import java.util.*; // Import the util library
import java.io.*; // Import the I/O library

public class ToHValidate {

    // ---------------------------------------------------------------------
    // Function: Empty Constructor
    // ---------------------------------------------------------------------
    public ToHValidate ()
    {
    }

    // ---------------------------------------------------------------------
    // Function: isBlank
    // ---------------------------------------------------------------------
    public static boolean isBlank (int character) {
        if (
                character == ' ' ||
                        character == '\t' ||
                        character == '\n' ||
                        character == '\r'
        )
            return true;
        return false;

    }

    // ---------------------------------------------------------------------
    // Function: getNextInteger
    // This function only works assuming that the file has positive integers
    // ---------------------------------------------------------------------
    public static int getNextInteger (FileInputStream input_file) {
        int character;
        int digit;
        int number = 0;
        try {
            while ((character = input_file.read()) != -1 && !isBlank(character))
            {
                number *= 10;
                digit = (int) character - '0';
                number += digit;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return number;
    }

    // ---------------------------------------------------------------------
    // Function: main
    // ---------------------------------------------------------------------
    public static void main(String[] args) {
        Scanner myObj = new Scanner(System.in);  // Create a Scanner object
        int n, t, s, d;
        String str_filename;
        String my_ID = new String("cfl21");

        // Check if the input filename has been provided as an argument
        if (args.length < 1)
        {
            System.out.printf("Usage: java %s_task2 <file_name>\n", my_ID);
            return;
        }

        try {
            // Get the filename
            str_filename = args[0];
            System.out.printf("Reading the file " + str_filename + "\n");

            // Create the object for reading the input file
            FileInputStream input_file = new FileInputStream(str_filename);

            // Read the four parameters in the first row of the input file
            n = getNextInteger (input_file);
            t = getNextInteger (input_file);
            s = getNextInteger (input_file);
            d = getNextInteger (input_file);
            System.out.printf("%d\t%d\t%d\t%d\n", n, t, s, d);

            // Your code for checking the correctness of the generalised ToH problem here
            ArrayList<Stack> stackList = new ArrayList<>();
            int finalDest = d;
            for (int i = 1 ; i<=t;i++){
                stackList.add(new Stack<Integer>());    //put all towers (stacks) in array list
            }
            for (int j = n ; j >=1;j--){
                stackList.get(s-1).push(j);     //initialise source stack
            }
            showStatus(stackList);
            boolean isError = false;
            boolean EOF = false;
            int numPop;
            while (!isError && !EOF){       //keep moving discs until an error is encountered or end of file is reached
                n = getNextInteger (input_file);
                s = getNextInteger (input_file);
                d = getNextInteger (input_file);
                if (n == 0 || s == 0 || d == 0){
                    EOF = true;
                    continue;
                }

                if ((int)stackList.get(s-1).peek() !=  n){      //error if disc being moved from source is not at the top
                    System.out.println("Move error: Number being moved is not at the top of source stack");
                    System.out.println("Source stack: " + stackList.get(s-1).toString());
                    isError = true;
                    showStatus(stackList);
                }
                if (!stackList.get(d-1).empty()&& (int)stackList.get(d-1).peek() < n){  //error if top disc at destination is smaller than disc being moved
                    System.out.println("Move Error: " + "Destination Tower: " + stackList.get(d-1).toString() + " has a smaller disc than " + n + " on top.");
                    isError = true;
                    showStatus(stackList);
                }else{
                    System.out.println("Move: disc" + n + " from tower " + s + " to tower " + d);   //move disc if no errors
                    sNdStatus(stackList, s, d, false);
                    numPop = (int) stackList.get(s-1).pop();
                    stackList.get(d-1).push(numPop);
                    sNdStatus(stackList, s, d, true);
                    System.out.println("");

                }
            }
            showStatus(stackList);
            boolean isCorrect = true;
            for (int i = 0 ; i < t; i++){
                if (stackList.get(i).empty() == false && i+1 != finalDest){     //check that all towers but the destination are empty
                    isCorrect = false;
                }
            }
            if (isCorrect){
                System.out.println("The sequence of moves is correct");
            }else{
                System.out.println("The sequence of moves is incorrect");
            }


            // Close the file
            input_file.close();


        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.printf("\n");
        return;
    }
    /**
     * Function: showStatus
     * It prints out the contents of each tower
     * @param inpStackList      The list of towers as an ArrayList
     * */
    public static void showStatus(ArrayList<Stack> inpStackList){
        System.out.println("The states of the towers are as follows:");
        for (int x = 0 ; x < inpStackList.size(); x++){
            System.out.println("Tower " + (x+1) + ": " + inpStackList.get(x).toString());
        }
    }
    /**
     * Function: sNdStatus (source and destination status)
     * Prints out the status of the source and destination towers (used before and after a move)
     * @param stacks    The list of towers
     * @param s     The source tower index + 1
     * @param d     The destination tower index + 1
     * @param isAfter   The boolean value to determine wether the status being printed is before or after a move (true -> after move)
     * */
    public static void sNdStatus(ArrayList<Stack> stacks, int s, int d, boolean isAfter){ //print stack status before and after move
        if (isAfter){
            System.out.println("After the move:");
        }else{
            System.out.println("Before the move:");
        }
        System.out.println("Source tower " + s + ": "+stacks.get(s-1).toString());
        System.out.println("Destination tower " + d + ": "+stacks.get(d-1).toString());
    }
}
