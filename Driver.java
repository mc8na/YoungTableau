// Filename: Driver.java
// Author: Miles Clikeman

import java.util.*;

// *****************************************************************************
// *****************************************************************************
// **** Drive
// *****************************************************************************
// *****************************************************************************

public class Driver {

  public static void main(String[] args) {
    
    Scanner console = new Scanner(System.in);
    
    // read in n
    System.out.print("Enter n (1-9): ");
    int n = console.nextInt();

    // create Standard Young Tableaux of size n
    TableauData tabData = new TableauData(n);
    tabData.printTableauData();
    //tabData.findAnomolies();
    //tabData.readFile7(console);

  } // main

} // end of Driver class
