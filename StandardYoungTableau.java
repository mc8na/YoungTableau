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

  private int myN, myRank, myCrossingNumber;
  private Shadow myShadow;

  private static final int NUM_ROWS = TableauData.NUM_ROWS;
  private static final int FIRST_ROW = TableauData.FIRST_ROW;
  private static final int SECOND_ROW = TableauData.SECOND_ROW;
  private static final int THIRD_ROW = TableauData.THIRD_ROW;

  public StandardYoungTableau(int[][] tableau) {
    myN = tableau[0].length;
    computeMyRankAndShadow(tableau);
    computeMyCrossingNumber(tableau);
  }

  public int numCrossings() { return myCrossingNumber; }

  public Shadow shadow() { return myShadow; }

  public int rank() { return myRank; }

  public void printRankAndShadow() {
    System.out.println("rank: " + myRank);
    System.out.println("shadow:");
    myShadow.print();
    System.out.println();
  }

  // compute rank and shadow of input tableau
  private void computeMyRankAndShadow(int[][] tableau) {
    myShadow = new Shadow();

    int left, right;
    myRank = -myN; // account for -1 part of depth-1 for each "0"
    boolean[] paired = new boolean[myN]; // store whether "+" is paired
    for (int i = 0; i < myN; ++i) { // for each "-"
      int j = myN-1;
      // find closest unpaired "+" to the left
      while (paired[j] || tableau[THIRD_ROW][i] < tableau[FIRST_ROW][j]) {
        --j;
      } // end while
      paired[j] = true;

      left = tableau[FIRST_ROW][j];
      right = tableau[THIRD_ROW][i];

      // create arc between endpoints and add to shadow
      myShadow.addArc(new Arc(left,right));

      // increment rank for each "0" contained in the new arc
      for (int k = 0; k < myN; ++k) {
        if (tableau[SECOND_ROW][k] > left && tableau[SECOND_ROW][k] < right) {
          ++myRank;
        } // end if
      } // end for

    } // end for

    // add sum of nesting to rank
    myRank += myShadow.sumOfNestingNumbers();

  }

  // computes the # of crossings in the M-diagram of the input tableau
  private void computeMyCrossingNumber(int[][] tableau) {
    Shadow greenShadow = new Shadow(); // arcs from '-' to '0'
    Shadow blueShadow = new Shadow();  // arcs from '0' to '+'
    int left, right;

    // fill greenShadow with arc from '-' to '0', i.e. from third to second row
    boolean[] paired = new boolean[myN];
    for (int i = 0; i < myN; ++i) {
      int j = myN-1;

      while (paired[j] || tableau[THIRD_ROW][i] < tableau[SECOND_ROW][j]) {
        --j;
      } // end while
      paired[j] = true;

      left = tableau[SECOND_ROW][j];
      right = tableau[THIRD_ROW][i];

      greenShadow.addArc(new Arc(left,right));
    } // end for

    for (int i = 0; i < myN; ++i) {
      paired[i] = false;
    } // end for

    // fill blueShadow with arc from '0' to '+', i.e. from second to first row
    for (int i = 0; i < myN; ++i) {
      int j = myN-1;

      while (paired[j] || tableau[SECOND_ROW][i] < tableau[FIRST_ROW][j]) {
        --j;
      } // end while
      paired[j] = true;

      left = tableau[FIRST_ROW][j];
      right = tableau[SECOND_ROW][i];

      blueShadow.addArc(new Arc(left,right));
    } // end for
    
    // compute number of crossings between blue and green arcs
    myCrossingNumber = greenShadow.numCrossings(blueShadow);
  } // end of computeMyCrossingNumber

  // returns 2-D array representing the tableau
  private int[][] tableau() {
    // use the shadow to reconstruct the tableau

    int[][] tableau = new int[NUM_ROWS][myN];

    // store which numbers 1,2,...,3n have been accounted for
    boolean[] used = new boolean[NUM_ROWS*myN];
    int idx = 0;
    int temp;
    for (Arc arc : myShadow.arcs()) { // for each arc
      temp = arc.getLeft();
      tableau[FIRST_ROW][idx] = temp; // add left endpoint to first row
      used[temp-1] = true;

      temp = arc.getRight(); // add right endpoint to third row
      tableau[THIRD_ROW][idx] = temp;
      used[temp-1] = true;
      ++idx;
    } // end for

    // sort first and third rows in increasing order
    Arrays.sort(tableau[FIRST_ROW]);
    Arrays.sort(tableau[THIRD_ROW]);

    idx = 0;
    // fill second row with numbers not in first and third rows
    for (int i = 0; i < NUM_ROWS*myN; ++i) {
      if (!used[i]) {
        tableau[SECOND_ROW][idx] = i+1;
        ++idx;
      } // end if
    } // end for

    return tableau;
  } // end tableau

  // print basic information about this tableau
  // (e.g. id number, rank, number of crossings, etc.)
  public void printData() {
    System.out.println("rank: " + myRank);
    System.out.println("crossings: " + myCrossingNumber);
    System.out.println("shadow:");
    myShadow.print();
    System.out.println();
    printYamanouchi();
    System.out.println();
    printTableau();
  } // end printData

  // print the tableau
  public void printTableau() {
    
    int[][] tableau = this.tableau();

    int length = String.valueOf(NUM_ROWS*myN).length();

    for (int row = 0; row < NUM_ROWS; ++row) {
      for (int col = 0; col < myN; ++col) {
        System.out.printf(String.format("%" + (length + 1) + "s", String.valueOf(tableau[row][col])));
      }
      System.out.println();
    }
    System.out.println();
    
  } // end printTableau

  // print the Yamanouchi String for this tableau
  public void printYamanouchi() {
    int[] plus = new int[myN];  // first row of tableau
    int[] minus = new int[myN]; // third row

    int idx = 0;
    int temp;
    for (Arc arc : myShadow.arcs()) { // for each arc
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


} // end of StandardYoungTableau class
