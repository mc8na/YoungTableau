// Filename: StandardYoungTableau.java
// Author: Miles Clikeman

import java.util.*;
import java.io.*;
import java.lang.*;

// *****************************************************************************
// *****************************************************************************
// **** StandardYoungTableau
// *****************************************************************************
// *****************************************************************************

public class StandardYoungTableau {

  static final int NUM_ROWS = 3;
  static final int FIRST_ROW = 0;
  static final int SECOND_ROW = 1;
  static final int THIRD_ROW = 2;

  private int[] myRanks; // stores ranks
  private ArrayList<Shadow> myShadows; // stores shadows
  private int[] myCrossings; // stores crossing #'s
  private int myN, id, numTableau, maxCrossings;

  private ArrayList<IDPair> myAnomolies; // stores id pairs


  // StandardYoungTableau constructor
  public StandardYoungTableau(int n) {
    this.myN = n;
    this.numTableau = Dimension.getDimension(n); // number of SYT of size (n,n,n)
    this.myRanks = new int[numTableau];
    this.myCrossings = new int[numTableau];
    this.myShadows = new ArrayList<Shadow>(numTableau);
    this.myAnomolies = new ArrayList<IDPair>();
    id = 0;
    maxCrossings = 0;
    generateTableauData();
  } // end StandardYoungTableau constructor

  // generates relevant data (rank, shadow, crossing #, etc.) for all SYT of size (n,n,n)
  private void generateTableauData() {
    id = 0;
    int[][] tableau = new int[NUM_ROWS][myN];
    generateTableauData(0,0,0,tableau);
  } // end generateTableauData
  
  // recursively generates all SYT of size (n,n,n) and computes relevant data for each tableaux
  private void generateTableauData(int row1, int row2, int row3, int[][] tableau) {
    // row1 = # elements in first row, row2 = # elements in third row,
    // row3 = # elements in third row of tableau
    int numElements = row1 + row2 + row3;
    int n = tableau[FIRST_ROW].length;

    if (numElements == NUM_ROWS*n) {
      computeMyRankAndShadow(tableau);
      computeMyCrossingNumber(tableau);
      ++id; // increment id for next tableau
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


  // compute rank and shadow of input tableau and store in myRanks and myShadows
  private void computeMyRankAndShadow(int[][] tableau) {
    int n = tableau[FIRST_ROW].length;

    Shadow shadow = new Shadow();
      
    int left, right;
    myRanks[id] = -n; // account for -1 part of depth-1 for each "0"
    boolean[] paired = new boolean[n]; // store whether "+" is paired
    for (int i = 0; i < n; ++i) { // for each "-"
      int j = n-1;
      // find closest unpaired "+" to the left
      while (paired[j] || tableau[THIRD_ROW][i] < tableau[FIRST_ROW][j]) {
        --j;
      } // end while
      paired[j] = true;

      left = tableau[FIRST_ROW][j];
      right = tableau[THIRD_ROW][i];

      // create arc between endpoints and add to shadow
      shadow.addArc(new Arc(left,right));

      // increment rank for each "0" contained in the new arc
      for (int k = 0; k < n; ++k) {
        if (tableau[SECOND_ROW][k] > left && tableau[SECOND_ROW][k] < right) {
          myRanks[id] = myRanks[id] + 1;
        } // end if
      } // end for

    } // end for

    // add sum of nesting to rank
    myRanks[id] = myRanks[id] + shadow.sumOfNestingNumbers();
      
    // add shadow to myShadows
    myShadows.add(id,shadow);

  } // end computeMyRankAndShadow


  // computes the # of crossings in the M-diagram of the input tableau
  // and stores it in myCrossings
  private void computeMyCrossingNumber(int[][] tableau) {
    int n = tableau[FIRST_ROW].length;

    Shadow greenShadow = new Shadow(); // arcs from '-' to '0'
    Shadow blueShadow = new Shadow();  // arcs from '0' to '+'
    int left, right;

    // fill greenShadow with arc from '-' to '0', i.e. from third to second row
    boolean[] paired = new boolean[n];
    for (int i = 0; i < n; ++i) {
      int j = n-1;

      while (paired[j] || tableau[THIRD_ROW][i] < tableau[SECOND_ROW][j]) {
        --j;
      } // end while
      paired[j] = true;

      left = tableau[SECOND_ROW][j];
      right = tableau[THIRD_ROW][i];

      greenShadow.addArc(new Arc(left,right));
    } // end for

    for (int i = 0; i < n; ++i) {
      paired[i] = false;
    } // end for

    // fill blueShadow with arc from '0' to '+', i.e. from second to first row
    for (int i = 0; i < n; ++i) {
      int j = n-1;

      while (paired[j] || tableau[SECOND_ROW][i] < tableau[FIRST_ROW][j]) {
        --j;
      } // end while
      paired[j] = true;

      left = tableau[FIRST_ROW][j];
      right = tableau[SECOND_ROW][i];

      blueShadow.addArc(new Arc(left,right));
    } // end for
    
    // compute number of crossings between blue and green arcs
    myCrossings[id] = greenShadow.numCrossings(blueShadow);
    if (myCrossings[id] > maxCrossings) {
      maxCrossings = myCrossings[id];
    } // end if
  } // end of computeMyCrossingNumber

  
  // find all pairs (b,b') of SYT of size (n,n,n) such that rank(b) < rank(b')
  // and shadow(b) contains shadow(b') and add them to myAnomolies
  public void findAnomolies() {
    int[] subcount = new int[2*maxCrossings];
    for (int i = 0; i < numTableau; ++i) {
      for (int j = i+1; j < numTableau; ++j) {
        if ( (myRanks[i] < myRanks[j] && myShadows.get(i).contains(myShadows.get(j))/* && myCrossings[j] <= myCrossings[i]+1*/) ) {
          subcount[maxCrossings+(myCrossings[j]-myCrossings[i])]++;
          IDPair pair = new IDPair(i,j);
          myAnomolies.add(pair);
          /*
          if (myCrossings[j]-myCrossings[i] == 1) {
            printYamanouchi(i);
            printYamanouchi(j);
            System.out.println();
          }
          */
        } else if( (myRanks[j] < myRanks[i] && myShadows.get(j).contains(myShadows.get(i))/* && myCrossings[i] <= myCrossings[j]+1*/) ){
          subcount[maxCrossings+(myCrossings[i]-myCrossings[j])]++;
          IDPair pair = new IDPair(j,i);
          myAnomolies.add(pair);
          /*
          if (myCrossings[i]-myCrossings[j] == 1) {
            printYamanouchi(j);
            printYamanouchi(i);
            System.out.println();
          }
          */
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
  // such that rank(b) < rank(b') and shadow(b) contains shadow(b')
  private void printAnomolies() {
    if (myAnomolies.size() == 0) {
      System.out.println("No anomolies found for Young Tableau");
      System.out.println("  of size (" + myN + "," + myN + "," + myN + ")");
    } else {
      for (IDPair pair : myAnomolies) { // for each pair of tableau
        printTableauData(pair.getFirst());
        printTableauData(pair.getSecond());
        printBuffer();
      } // end for
      
      System.out.print(myAnomolies.size() + " anomolies found for Young Tableau");
      System.out.println(" of size (" + myN + "," + myN + "," + myN + ")");
    } // end for
  } // end printAnomolies


  // print information for all tableau
  public void printTableauData() {
    for (int tabId = 0; tabId < numTableau; ++tabId) {
      printTableauData(tabId);
      printBuffer();
    } // end for
  } // end printTableauData


  // print basic information about the tableau with the given id number
  // (e.g. id number, rank, number of crossings, etc.)
  private void printTableauData(int tabId) {
    System.out.println("id: " + tabId);
    System.out.println("rank: " + myRanks[tabId]);
    System.out.println("crossings: " + myCrossings[tabId]);
    System.out.println("shadow:");
    myShadows.get(tabId).print();
    printYamanouchi(tabId);
    printTableau(tabId);
  } // end printTableauData


  // print a buffer line to provide spacing
  private void printBuffer() {
    System.out.println();
    System.out.println("-------------------------");
    System.out.println();
  } // end printBuffer

  
  // print the tableau with the given id number
  private void printTableau(int tabId) {
    
    // use the shadow to reconstruct the tableau
    Shadow shadow = myShadows.get(tabId);

    int[] plus = new int[myN]; // first row of tableau
    int[] zero = new int[myN]; // second row
    int[] minus = new int[myN]; // third row
    // store which numbers 1,2,...,3n have been accounted for
    boolean[] used = new boolean[NUM_ROWS*myN];
    int idx = 0;
    int temp;
    for (Arc arc : shadow.arcs()) { // for each arc
      temp = arc.getLeft();
      plus[idx] = temp; // add left endpoint to first row
      used[temp-1] = true;

      temp = arc.getRight(); // add right endpoint to third row
      minus[idx] = temp;
      used[temp-1] = true;
      ++idx;
    } // end for

    // sort first and third rows in increasing order
    Arrays.sort(plus);
    Arrays.sort(minus);

    idx = 0;
    // fill second row with numbers not in first and third rows
    for (int i = 0; i < NUM_ROWS*myN; ++i) {
      if (!used[i]) {
        zero[idx] = i+1;
        ++idx;
      } // end if
    } // end for

    // print first row
    for (int i = 0; i < myN; ++i) {
      System.out.printf("%3d ", plus[i]);
    } // end for
    System.out.println();

    // print second row
    for (int i = 0; i < myN; ++i) {
      System.out.printf("%3d ", zero[i]);
    } // end for
    System.out.println();

    // print third row
    for (int i = 0; i < myN; ++i) {
      System.out.printf("%3d ", minus[i]);
    } // end for
    System.out.println();
    

  } // end printTableau


  private void printYamanouchi(int tabId) {
    Shadow shadow = myShadows.get(tabId);
    int[] plus = new int[myN];  // first row of tableau
    int[] minus = new int[myN]; // third row

    int idx = 0;
    int temp;
    for (Arc arc : shadow.arcs()) { // for each arc
      temp = arc.getLeft();
      plus[idx] = temp; // add left endpoint to first row

      temp = arc.getRight(); // add right endpoint to third row
      minus[idx] = temp;
      ++idx;
    } // end for

    // sort first and third rows in increasing order
    Arrays.sort(plus);
    Arrays.sort(minus);

    int idx1 = 0;
    int idx2 = 0;
    for (int i = 0; i < NUM_ROWS*myN; ++i) {
      if (idx1 < myN && (i+1) == plus[idx1]) {
        System.out.print("+");
        ++idx1;
      } else if (idx2 < myN && (i+1) == minus[idx2]) {
        System.out.print("-");
        ++idx2;
      } else {
        System.out.print("0");
      } // end else
    } // end for
    System.out.println();
  } // end printYamanouchi


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

      if ( (myRanks[id1] < myRanks[id2]/* && myCrossings[id2] <= myCrossings[id1]+1*/) ) {
        subcount[maxCrossings+(myCrossings[id2]-myCrossings[id1])]++;
        /*
        if (myCrossings[id2]-myCrossings[id1] == 1) {
          printYamanouchi(id1);
          printYamanouchi(id2);
          System.out.println();
          System.out.println("" + id1);
          System.out.println("" + id2);
          System.out.println();
        }
        printTableau(id1);
        System.out.println();
        System.out.println();
        printTableau(id2);
        printBuffer();
        */
        ++numTabs;
      } else if ( (myRanks[id2] < myRanks[id1]/* && myCrossings[id1] <= myCrossings[id2]+1*/) ) {
        subcount[1-(myCrossings[id1]-myCrossings[id2])]++;
        /*
        if (myCrossings[id1]-myCrossings[id2] == 1) {
          printYamanouchi(id2);
          printYamanouchi(id1);
          System.out.println();
          System.out.println("" + id2);
          System.out.println("" + id1);
          System.out.println();
        }
        printTableau(id2);
        System.out.println();
        System.out.println();
        printTableau(id1);
        printBuffer();
        */
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
  
  
} // end of StandardYoungTableau class
