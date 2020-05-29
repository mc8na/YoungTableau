// Filename: FileDriver.java
// Author: Miles Clikeman

import java.util.*;

// *****************************************************************************
// *****************************************************************************
// **** Drive
// *****************************************************************************
// *****************************************************************************

public class FileDriver {

  public static void main(String[] args) {
    
    Scanner console = new Scanner(System.in);

    while (console.hasNext()) { 
      // read in tableaux from input file
      int[][] tableau = readArray(console);
      // print rank and shadow for each
      printRankAndShadow(tableau);
      System.out.println();
      System.out.println();
    } // end while

  } // main

  private static int[][] readArray(Scanner input) {
    // read in and return the next tableaux from the input file
    int n = input.nextInt();
    input.nextLine();
    int tableau[][] = new int[3][n];
    for (int i = 0; i < 3; ++i) {
      for (int j = 0; j < n; ++j) {
        tableau[i][j] = input.nextInt(); // read in elements
      } // end for
      input.nextLine();
    } // end for
    return tableau;
  } // end readArray

  private static void printRankAndShadow(int[][] tableau) {
    
    int n = tableau[0].length;

    Shadow shadow = new Shadow();
      
    int left, right;
    int rank = -n; // account for -1 part of depth-1 for each "0"
    boolean[] paired = new boolean[n]; // store whether "+" is paired
    for (int i = 0; i < n; ++i) { // for each "-"
      int j = n-1;
      // find closest unpaired "+" to the left
      while (paired[j] || tableau[2][i] < tableau[0][j]) {
        --j;
      } // end while
      paired[j] = true;

      left = tableau[0][j];
      right = tableau[2][i];

      // create arc between endpoints and add to shadow
      shadow.addArc(new Arc(left,right));

      // increment rank for each "0" contained in the new arc
      for (int k = 0; k < n; ++k) {
        if (tableau[1][k] > left && tableau[1][k] < right) {
          ++rank;
        } // end if
      } // end for

    } // end for
    
    // add sum of nesting to rank
    rank += shadow.sumOfNestingNumbers();

    // print rank and shadow
    System.out.println("rank: " + rank);
    System.out.println("shadow:");
    shadow.print();
  } // end printRankAndShadow

} // end of Driver class
