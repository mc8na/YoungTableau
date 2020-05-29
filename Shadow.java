// Filename: Shadow.java
// Author: Miles Clikeman

import java.util.*;
import java.io.*;

// *****************************************************************************
// *****************************************************************************
// **** Shadow
// *****************************************************************************
// *****************************************************************************

public class Shadow {

  private ArrayList<Arc> myArcs; // stores the arcs in the shadow


  // Shadow constructor
  public Shadow() {
    myArcs = new ArrayList<Arc>();
  } // end Shadow constructor


  // return iterator over arcs in shadow
  public Iterable<Arc> arcs() {
    return myArcs;
  } // end arcs


  // add an arc to the shadow
  public void addArc(Arc newArc) { 
    newArc.setNestingNumber(0);
    for (Arc arc : myArcs) {
      if (arc.contains(newArc)) {
        newArc.incrementNestingNumber();
      } else if (newArc.contains(arc)) {
        arc.incrementNestingNumber();
      } // end if
    } // end for
    myArcs.add(newArc); 
  } // end of addArc


  // computes the nesting numbers of each arc in the shadow
  private void computeNestingNumbers() {
    wipeNestingNumbers();
    int size = myArcs.size();
    for (int i = 0; i < size; ++i) {
      for (int j = 0; j < size; ++j) {
        if (j != i && myArcs.get(j).contains(myArcs.get(i))) {
          myArcs.get(i).incrementNestingNumber();
        } // end if
      } // end for
    } // end for
  } // end computeDepths


  // resets the nesting numbers of each arc in the shadow to zero
  private void wipeNestingNumbers() {
    for (Arc arc : myArcs) {
      arc.setNestingNumber(0);
    } // end for
  } // end wipeNestingNumbers


  // returns the number of crossings between the arcs in the two shadows
  public int numCrossings(Shadow other) {
    int crossings = 0;
    for (Arc arc1 : other.arcs()) {
      for (Arc arc2 : this.myArcs) {
        if (arc1.crosses(arc2)) {
          ++crossings;
        } // end if
      } // end for
    } // end for
    return crossings;
  } // end of numCrossings


  // returns the sum of the nesting numbers of the arcs in the shadow
  public int sumOfNestingNumbers() {
    return myArcs.stream().map(arc -> arc.getNestingNumber()).reduce(0,Integer::sum);
    /*
    int sum = 0;
    for (Arc arc : myArcs) {
      sum += arc.getNestingNumber();
    } // end for
    return sum;
    */
  } // end sumOfDepths

  
  // return whether this shadow contains the input shadow
  public boolean contains(Shadow other) { 
    for (Arc arc1 : other.arcs()) {
      boolean contains = false;
      for (Arc arc2 : this.myArcs) {
        if (arc1.getNestingNumber() == arc2.getNestingNumber() && arc2.contains(arc1)) {
          contains = true;
        } // end if
      } // end for
      if (contains == false) {
        return false;
      } // end if

    } // end for
    return true;
  } // end of contains


  // prints the nested sequence of intervals at each nesting number in the shadow
  public void print() {
    int size = myArcs.size();
    for (int i = 0; i < size; ++i) { // for each nesting number
      System.out.print("s" + (i+1) + " = {");
      boolean prior = false;
      for (Arc arc : myArcs) { // for each arc
        if (arc.getNestingNumber() == i) { // if arc has right nesting number
          if (prior) {
            System.out.print(",");
          } else {
            prior = true;
          } // end else
          System.out.print("(" + arc.getLeft() + "," + arc.getRight() + ")");
        } // end if
      } // end for
      System.out.println("}");
    } // end for
  } // end print
  
  
} // end of Shadow class
