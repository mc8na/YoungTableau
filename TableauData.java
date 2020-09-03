// Filename: TableauData.java
// Author: Miles Clikeman

import java.util.*;
import java.io.*;
import java.lang.*;

// *****************************************************************************
// *****************************************************************************
// **** TableauData
// *****************************************************************************
// *****************************************************************************

public class TableauData {

  static final int NUM_ROWS = 3;
  static final int FIRST_ROW = 0;
  static final int SECOND_ROW = 1;
  static final int THIRD_ROW = 2;

  private ArrayList<StandardYoungTableau> syt; // stores all standard young tableau of size (n,n,n)
  private int n, numTableau, maxCrossings;
  private ArrayList<IDPair<Integer>> myAnomolies; // stores id pairs


  // TableauData constructor
  public TableauData(int n) {
    this.numTableau = Dimension.getDimension(n); // number of SYT of size (n,n,n)
    this.syt = new ArrayList<StandardYoungTableau>(numTableau);
    this.myAnomolies = new ArrayList<IDPair<Integer>>();
    this.n = n;
    maxCrossings = 0;
    generateTableauData();
  } // end TableauData constructor

  // generates relevant data (rank, shadow, crossing #, etc.) for all SYT of size (n,n,n)
  private void generateTableauData() {
    int[][] tableau = new int[NUM_ROWS][n];
    generateTableauData(0,0,0,tableau);
  } // end generateTableauData
  
  // recursively generates all SYT of size (n,n,n) and computes relevant data for each tableaux
  private void generateTableauData(int row1, int row2, int row3, int[][] tableau) {
    // row1 = # elements in first row, row2 = # elements in third row,
    // row3 = # elements in third row of tableau
    int numElements = row1 + row2 + row3;

    if (numElements == NUM_ROWS*n) {
      StandardYoungTableau tab = new StandardYoungTableau(tableau);
      syt.add(tab);
      if (tab.numCrossings() > maxCrossings) {
        maxCrossings = tab.numCrossings();
      }
    } else {
      if (row1 < n) { // try adding element to row 1
        tableau[FIRST_ROW][row1] = numElements + 1;
        generateTableauData(row1 + 1, row2, row3, tableau);
      } // end if
      if (row2 < row1) { // try adding element to row 2
        tableau[SECOND_ROW][row2] = numElements + 1;
        generateTableauData(row1, row2 + 1, row3, tableau);
      } // end if
      if (row3 < row2) { // try adding element to row 3
        tableau[THIRD_ROW][row3] = numElements + 1;
        generateTableauData(row1, row2, row3 + 1, tableau);
      } // end if
    } // end else
  } // end generateTableauData


  // defines the attributes two standard young tableau should exhibit to be
  // considered anomolous
  private boolean areAnomolous(StandardYoungTableau tab1, StandardYoungTableau tab2) {
    return ( tab1.rank() < tab2.rank() && tab1.shadow().contains(tab2.shadow()) );
  }

  
  // find all pairs (b,b') of SYT of size (n,n,n) that meet the conditions
  // defined in areAnomolous() and add them to myAnomolies
  public void findAnomolies() {
    int[] subcount = new int[2*maxCrossings];
    for (int i = 0; i < numTableau; ++i) {
      StandardYoungTableau tab1 = syt.get(i);
      for (int j = i+1; j < numTableau; ++j) {
        StandardYoungTableau tab2 = syt.get(j);

        if (areAnomolous(tab1, tab2)) {
          subcount[maxCrossings+(tab2.numCrossings()-tab1.numCrossings())]++;
          myAnomolies.add(new IDPair<>(i,j));
        } else if(areAnomolous(tab2, tab1)) {
          subcount[maxCrossings+(tab1.numCrossings()-tab2.numCrossings())]++;
          myAnomolies.add(new IDPair<>(j,i));
        } // end if

      } // end for
    } // end for

    printAnomolies();

    for (int i = 0; i < 2*maxCrossings; ++i) {
      if (subcount[i] > 0) {
        System.out.println("pairs with c(s')-c(s) = " + (i-maxCrossings) + ": " + subcount[i]);
      } // end if
    } // end for
  } // end findAnomolies


  // print rank, shadow, and tableau of each pair (b,b') of SYT of size (n,n,n)
  // that meet condition outlined in areAnomolous
  private void printAnomolies() {
    if (myAnomolies.size() > 0) {
      for (IDPair<Integer> pair : myAnomolies) { // for each pair of tableau
        printTableauData(pair.getFirst());
        printTableauData(pair.getSecond());
        printBuffer();
      } // end for
      System.out.print(myAnomolies.size() + " ");
    } else {
      System.out.print("No ");
    }
    System.out.println("anomolies found for Young Tableau");
    System.out.println("  of size (" + n + "," + n + "," + n + ")");
  } // end printAnomolies


  // print information for all tableau
  public void printTableauData() {
    for (int tabId = 0; tabId < numTableau; ++tabId) {
      System.out.println("id: " + tabId);
      syt.get(tabId).printData();
      printBuffer();
    } // end for
  } // end printTableauData


  // print basic information about the tableau with the given id number
  // (e.g. id number, rank, number of crossings, etc.)
  private void printTableauData(int tabId) {
    StandardYoungTableau tab = syt.get(tabId);
    System.out.println("id: " + tabId);
    System.out.println("rank: " + tab.rank());
    System.out.println("crossings: " + tab.numCrossings());
    System.out.println("shadow:");
    tab.shadow().print();
    System.out.println();
    tab.printYamanouchi();
    System.out.println();
    tab.printTableau();
  } // end printTableauData


  // print a buffer line to provide spacing
  private void printBuffer() {
    System.out.println();
    System.out.println("-------------------------");
    System.out.println();
  } // end printBuffer


  // read through the output7.txt file to find anomolies
  // rather than recomputing them
  public void readFile7(Scanner console) {
    int[] subcount = new int[2*maxCrossings];
    int numTabs = 0;

    for (int i = 0; i < 6; ++i) {
      console.nextLine();
    } // end for

    for (int i = 0; i < 62147; ++i) { 
      // read in tableaux from input file
      console.next();

      int id1 = console.nextInt();
      //System.out.println("id1 = " + id1);

      for (int j = 0; j < 15; ++j) {
        console.nextLine();
      } // end for

      console.next();
      int id2 = console.nextInt();
      //System.out.println("id2 = " + id2);

      for (int j = 0; j < 16; ++j) {
        console.nextLine();
      } // end for

      StandardYoungTableau tab1 = syt.get(id1);
      StandardYoungTableau tab2 = syt.get(id2);

      if ( (tab1.rank() < tab2.rank()/* && myCrossings[id2] <= myCrossings[id1]+1*/) ) {
        subcount[maxCrossings+(tab2.numCrossings()-tab1.numCrossings())]++;
        ++numTabs;
      } else if ( (tab2.rank() < tab1.rank()/* && myCrossings[id1] <= myCrossings[id2]+1*/) ) {
        subcount[1-(tab1.numCrossings()-tab2.numCrossings())]++;
        ++numTabs;
      } // end else

    } // end for

    System.out.print(numTabs + " anomolies found for Young Tableau");
    System.out.println(" of size (7,7,7)");
    
    for (int i = 0; i < 2*maxCrossings; ++i) {
      if (subcount[i] > 0) {
        System.out.println("pairs with c(s')-c(s) = " + (i-maxCrossings) + ": " + subcount[i]);
      } // end if
    } // end for
  } // end readFile7
  
  
} // end of TableauData class
