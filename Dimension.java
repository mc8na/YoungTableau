// Filename: Dimension.java
// Author: Miles Clikeman

import java.util.*;
import java.io.*;

// *****************************************************************************
// *****************************************************************************
// **** Dimension
// *****************************************************************************
// *****************************************************************************

public class Dimension {

  // stores the number of standard young tableaux of size (n,n,n)
  // e.g. 1 SYT of size (1,1,1), 5 of size (2,2,2), 42 of size (3,3,3), etc.
  private static int dimension[] = {0, 1, 5, 42, 462, 6006, 87516, 1385670, 23371634, 414315330 /*, 7646001090, 145862174640, 2861142656400, 57468093927120, 1178095925505960, 24584089974896430, 521086299271824330, 11198784501894470250, 243661974372798631650, 5360563436201569896300, 119115896614816702500900 */};


  // returns the number of standard young tableaux of size (n,n,n)
  public static int getDimension(int n) {
    if (n <= 0 || n > dimension.length) { // 
      System.out.println("n must be an integer between 1 and " + dimension.length);
      System.exit(1);
    } // end if
    return dimension[n];
  } // end getDimension
  
  
} // end of Dimension class
