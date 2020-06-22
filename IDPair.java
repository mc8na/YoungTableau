// Filename: IDPair.java
// Author: Miles Clikeman

import java.util.*;
import java.io.*;

// *****************************************************************************
// *****************************************************************************
// **** IDPair
// *****************************************************************************
// *****************************************************************************

public class IDPair<ID> {

  // two elements in a pair
  private ID myFirst, mySecond;


  // IDPair constructor
  public IDPair(ID first, ID second) {
    myFirst = first;
    mySecond = second;
  } // end IDPair constructor


  // returns first ID in the pair
  public ID getFirst() {
    return myFirst;
  } // end getFirst


  // returns second ID in the pair
  public ID getSecond() {
    return mySecond;
  } // end getSecond


  // sets first ID in the pair to newValue
  public void setFirst(ID newValue) {
    myFirst = newValue;
  } // end setFirst

  
  // sets second ID in the pair to newValue
  public void setSecond(ID newValue) {
    mySecond = newValue;
  } // end setSecond
  
  
} // end of IDPair class
