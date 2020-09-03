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
      StandardYoungTableau tab = new StandardYoungTableau(tableau);
      // print rank and shadow for each
      tab.printData();

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

} // end of FileDriver class
