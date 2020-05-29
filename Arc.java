// Filename: Arc.java
// Author: Miles Clikeman

import java.util.*;
import java.io.*;

// *****************************************************************************
// *****************************************************************************
// **** Arc
// *****************************************************************************
// *****************************************************************************

public class Arc {

  // left and right endpoints of arc, nesting number of arc
  private int myLeft, myRight, myNestingNumber;

  // construct Arc with left and right endpoints and nesting number
  public Arc(int left, int right, int nesting) {
    this.myLeft = left;
    this.myRight = right;
    this.myNestingNumber = nesting;
  } // end Arc constructor


  // construct Arc with left and right endpoints and default nesting number
  // of zero
  public Arc(int left, int right) {
    this(left, right, 0);
  } // end Arc constructor


  // return left endpoint of arc
  public int getLeft() { 
    return myLeft; 
  } // end of getLeft

  
  // return right endpoint of arc
  public int getRight() { 
    return myRight;
  } // end of getRight

  
  // return nesting number of arc
  public int getNestingNumber() { 
    return myNestingNumber; 
  } // end of getDepth


  // set the nesting number of the arc
  public void setNestingNumber(int num) {
    myNestingNumber = num;
  } // end of setNestingNumber

  
  // add one to nesting number of arc
  public void incrementNestingNumber() { 
    ++myNestingNumber; 
  } // end of incrementDepth


  // returns whether this arc contains the input arc
  public boolean contains(Arc other) {
    return (myLeft <= other.getLeft() && myRight >= other.getRight());
  } // end of contains


  // returns whether this arc crosses the input arc
  public boolean crosses(Arc other) {
    int otherLeft = other.getLeft();
    int otherRight = other.getRight();
    if (otherLeft < myLeft) {
      return (myLeft < otherRight && otherRight < myRight);
    } else if (otherLeft < myRight) {
      return (myRight < otherRight);
    } // end if
    return false;
  } // end of crosses
  
  
} // end of Arc class
