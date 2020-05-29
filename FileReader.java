// Filename: FileReader.java
// Author: Miles Clikeman

import java.util.*;

// *****************************************************************************
// *****************************************************************************
// **** Drive
// *****************************************************************************
// *****************************************************************************

public class FileReader {

  public static void main(String[] args) {
    
    int maxDiff = 0;
    int firstID = -1;
    int secondID = -1;
    Scanner console = new Scanner(System.in);

    console.nextLine();
    console.nextLine();
    console.nextLine();
    console.nextLine();

    for (int i = 0; i < 660; ++i) { 
      // read in tableaux from input file
      console.next();

      int id1 = console.nextInt();
      //System.out.println("id1 = " + id1);
      console.nextLine();
      console.next();

      int rank1 = console.nextInt();
      //System.out.println("rank1 = " + rank1);

      console.nextLine();
      console.nextLine();
      console.nextLine();
      console.nextLine();
      console.nextLine();
      console.nextLine();
      console.nextLine();
      console.nextLine();
      console.nextLine();
      console.nextLine();
      console.nextLine();
      console.nextLine();
      console.nextLine();

      console.next();
      int id2 = console.nextInt();
      //System.out.println("id2 = " + id2);

      console.nextLine();
      console.next();

      int rank2 = console.nextInt();
      //System.out.println("rank2 = " + rank2);

      console.nextLine();
      console.nextLine();
      console.nextLine();
      console.nextLine();
      console.nextLine();
      console.nextLine();
      console.nextLine();
      console.nextLine();
      console.nextLine();
      console.nextLine();
      console.nextLine();
      console.nextLine();
      console.nextLine();
      console.nextLine();

      if (Math.abs(rank2 - rank1) > maxDiff) {
        maxDiff = Math.abs(rank2 - rank1);
        firstID = id1;
        secondID = id2;
      }
    } // end for

    System.out.println("maxDiff = " + maxDiff);
    System.out.println("id1 = " + firstID);
    System.out.println("id2 = " + secondID);

  } // main

} // end of FileReader class
