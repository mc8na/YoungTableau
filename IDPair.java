// Filename: IDPair.java
// Author: Miles Clikeman

import java.util.*;
import java.io.*;

// *****************************************************************************
// *****************************************************************************
// **** IDPair
// *****************************************************************************
// *****************************************************************************

public class IDPair {

  // two elements in a pair
  private int myFirst, mySecond;


  // IDPair constructor
  public IDPair(int first, int second) {
    this.myFirst = first;
    this.mySecond = second;
  } // end IDPair constructor


  public int getFirst() {
    return myFirst;
  } // end getFirst


  public int getSecond() {
    return mySecond;
  } // end getSecond


  public void setFirst(int newValue) {
    myFirst = newValue;
  } // end setFirst

  
  public void setSecond(int newValue) {
    mySecond = newValue;
  } // end setSecond
  
  
} // end of IDPair class
