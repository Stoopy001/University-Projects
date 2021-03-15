// -----------------------------------------------------------------------------
// Program: Generate steps to solve Tower of Hanoi problem
// -----------------------------------------------------------------------------


import java.util.Scanner;  // Import the Scanner class
import java.io.*; // Import the I/O library
public class TowerOfHanoi {
        public static int absSrc;
        public static int absDest;
        // ---------------------------------------------------------------------
        // Function: Empty Constructor
        // ---------------------------------------------------------------------
        public TowerOfHanoi ()
        {
        }

        // ---------------------------------------------------------------------
        // Function: Prints every move on s1creen and also writes it to a file
        // ---------------------------------------------------------------------
        public int print_move (int disc, int source_tower, int destination_tower, FileWriter writer)
        {
            try {
                System.out.printf("\nMove disk %d from T%d to T%d", disc, source_tower, destination_tower);
                writer.write("\n" + disc + "\t" +  source_tower + "\t" + destination_tower);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return 0;
        }

        // ---------------------------------------------------------------------
        // Function: The recursive function for moving n discs
        //           from s to d with t-2 buffer towers.
        //           It prints all the moves with disk numbers.
        // ---------------------------------------------------------------------
        public int move_t (int n, int t, int s, int d, FileWriter writer) {
            if (n==1){      //End condition when n becomes 1
                print_move(n, s, d, writer);
                return 0;
            }
            int helpPeg = s+1;  //start selecting help pegs from the source and go right
            boolean isInvalid = true;
            while (isInvalid){      //assume the helper peg is initially invalid
                isInvalid = false;
                if (helpPeg == absSrc || helpPeg == absDest){  //help peg cannot be equal to source or destination towers
                    helpPeg+=1;
                    isInvalid = true;
                }
                if (helpPeg > t){
                    helpPeg = 1;        //if help peg reaches the end, reset it back to 1
                    isInvalid = true;
                }

            }
                move_t(n - 1, t, s, helpPeg, writer);       //main recursive calls (s -> h -> d)
                print_move(n, s, d, writer);
                move_t(n - 1, t, helpPeg, d, writer);
            return 0;
        }
        // ---------------------------------------------------------------------
        // Function: main
        // ---------------------------------------------------------------------
        public static void main(String[] args) {
            Scanner myObj = new Scanner(System.in);  // Create a Scanner object
            int n, t, s, d;
            String my_ID = new String("cfl21");

            if (args.length < 4)
            {
                System.out.printf("Usage: java %s_task1 <n> <t> <s> <d>\n", my_ID);
                return;
            }

            n = Integer.parseInt(args[0]);  // Read user input n
            t = Integer.parseInt(args[1]);  // Read user input t
            s = Integer.parseInt(args[2]);  // Read user input s
            d = Integer.parseInt(args[3]);  // Read user input d

            // Check the inputs for sanity
            if (n<1 || t<3 || s<1 || s>t || d<1 || d>t)
            {
                System.out.printf("Please enter proper parameters. (n>=1; t>=3; 1<=s<=t; 1<=d<=t)\n");
                return;
            }

            // Create the output file name
            String filename;
            filename = new String(my_ID + "_ToH_n" + n + "_t" + t + "_s" + s + "_d" + d + ".txt");
            try {
                // Create the Writer object for writing to "filename"
                FileWriter writer = new FileWriter(filename, false);

                // Write the first line: n, t, s, d
                writer.write(n + "\t" + t + "\t" + s + "\t" + d);

                // Create the object of this class to solve the generalised ToH problem
                TowerOfHanoi ToH = new TowerOfHanoi();

                System.out.printf("\nFollowing is a set of dummy moves for n=6, t=5, s=1, d=2");
                System.out.printf("\nThe output is also written to the file %s", filename);

                // Call the recursive function that solves the ToH problem
                ToH.move_t(n, t, s, d, writer);
                absSrc = s;
                absDest = d;
                // Close the file
                writer.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.printf("\n");
            return;
        }
    }


