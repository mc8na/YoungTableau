# YoungTableau
Generate data for standard young tableau of size (n,n,n)

To compile and run program (command line arguments):

javac Driver.java
javac FileDriver.java

java Driver
java FileDriver < input_filename.txt

The command java Driver will prompt
Enter n:
and will find and print out all pairs (b,b') of Young Tableau of size (n,n,n) for the input n with rank(b) < rank(b') and shadow(b) > shadow(b').

The command java FileDriver < input_filename.txt will read the input file and print out the rank and shadow of each Young Tableau in that file. The input file should be formatted

n
tableau

n
tableau

n
tableau

where n is the number of rows and tableau is the ordered matrix of numbers. For example:

6
1 2 3 4 5 14
6 7 8 9 10 15
11 12 13 16 17 18

2
1 2
3 4
5 6

