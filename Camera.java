// Cassidy Lyons
// Ca264412
// COP3503 Spring 2018
// Due 3/02/2018
// A program that uses a greedy algorithm to decide fewest amount of pictures to capture all objects
// First line of input is two integers: n, d: # of objects and height of wall
// Second line is two integers. They are X - locations: Start of glass Partition and end of glass partition 
// Use these as points to form a line with the objects position when object is above wall 
// This determines the range of visibility 
// From line 3 to line n, two integers xi, yi, will represent the objects location that must be visible from the x - axis
// below wall - visible always, above wall only visible through glass partition

import java.util.*;
import java.text.NumberFormat;
import java.text.DecimalFormat;

public class Camera {

	public static void main(String[] args) {
		long strt = System.currentTimeMillis();
		Scanner scan = new Scanner(System.in);
		int numObj = scan.nextInt();
		Window[] r = new Window[numObj];
		
		int d = scan.nextInt();
		int start = scan.nextInt();
		int end = scan.nextInt();
		int[] points = {start, end};
		int[][] pts = new int[numObj][2];
			
        for (int i = 0; i < numObj; i++)
            	for (int j = 0; j < 2; j++)
            	{   // pts[i][0] = xi and pts[i][1] = yi
            		pts[i][j] = scan.nextInt();
            		// If yi is above the wall, compute range, otherwise set 0 for range to indicate always visible
            		if(pts[i][1] > d)
            			above(points, d, pts[i], numObj, r, i);
            		else
            			r[i] = new Window(0, 0);	
            	}
   scan.close();
   Arrays.sort(r);
// Always have to take at least one picture
int takePicture = 1;

double right = r[numObj - 1].x2, left = r[numObj - 1].x1;

for(int z = numObj - 1; z >= 0; z--)
{
	// Ignore this case because it was purposely set as a flag to say this object is below
	// wall and therefore always visible. All objects are guaranteed to be visible at some point so this is safe
	if(r[z].x1 == 0 && r[z].x2 == 0)
		continue;
	// initially set a right "pointer" (max value) to the very last x2, which is not technically guaranteed to be the 
	// max value but it's safe because the left "pointer is most important because thats what we sorted by 
	// Every time the x2 value is less than the right pointer, assign that value to be the new right pointer
	if(r[z].x2 < right)
		right = r[z].x2;
	// The left pointer does not change until the right pointer closes in on it.
	// When the left pointer is less than (or equal to) the right pointer, the left pointer is assigned 
	// to the x1 value of the perpetrator and the right pointer is assigned to the corresponding x2 value.
	// THIS is where we decide to take a picture because it is now or never
	if(left > right)
	{
		
		right = r[z].x2; 
		left = r[z].x1;
		System.out.println("TAKING PIC! NEW WINDOW BEGINS: left = " + left + " right = " + right);
		takePicture++;
	}
	
	System.out.print(z + " : ");
	print(r[z]);
	System.out.println();
	
	
 }
System.out.println(takePicture);
long en = System.currentTimeMillis();

NumberFormat formatter = new DecimalFormat("#0.00000");
System.out.print("Execution time is " + formatter.format((en - strt) / 1000d) + " seconds");
}
	public static void above(int[] pt,int y, int[]pts, int numObj, Window[] r, int i)
	{
				// pt[] holds start and end x location of glass partition
				// pts[] holds x and y location of object
				// d = height of glass partition, which is constant
		double x1 = 0.0, x2 = 0.0, m1, m2;
		// slope for beg of glass and object = y1 - y2 / x1 - x2
		// but if x position is equal to where partition starts, the opportunity begins directly below (x = point..vertical line)
		// ..don't divide by zero
		if(pts[0] == pt[0])
		 {
			x1 = (double)pts[0];
			m1 = 0;
		 }
		else
			m1 = ((pts[1] - y)/(double)(pts[0] - pt[0]));
		// slope for end of glass and object = y1 - y2 / x1 - x2
		// but if x position is equal to where partition ends, the opportunity begins directly below (x = point..vertical line)
		// ..don't divide by zero
		if(pts[0] == pt[1])
			 {
				x2 = (double)pt[1];
				m2 = 0;
			 }
		else
		 m2 = (double)((pts[1] - y)/(double)(pts[0] - pt[1]));
		
		double b1 = pts[1] - (m1 * pts[0]);
		double b2 = pts[1] - (m2 * pts[0]);
		// if b (y - intercept) == 0, then (0, 0) is the intercept for both
		// set x to 0 if b == 0
		// find x - intercept
		if(b1 != 0 && m1 != 0)
		x1 = ((0 - b1)/m1);
		else if(b1 == 0)
			x1 = 0;
		if(b2 != 0 && m2 != 0)
		x2 = ((0 - b2)/m2);
		else if(b2 == 0)
			x2 = 0;
		/*
		System.out.println(" slope1 = " + m1 + " y - intercept1 = " + b1 + " x intercept 1 = " + x1); 
		System.out.println(" slope2 = " + m2 + " y - intercept2 = " + b2+ " x intercept 2 = " + x2); 
	
		*/
	
		r[i] = new Window(x1, x2);	
}
	// DeBugging 
	public static void print(Window range)
	{
		System.out.println( " can be taken in the range of (" + range.x1 + " , " + range.x2 + ") " );
	}	
	
}
// Store window of opportunity 
class Window implements Comparable< Window >{
	double x1;
	double x2;
	// object is visible from (x1, 0) to (x2, 0) if yi > d
	// otherwise x1 == 0 && x2 == 0 and case will be ignored later
	public Window(double x, double xx) {
		x1 = x;
		x2 = xx;
	}
	// Sort by beginning of window of opportunity 
	public int compareTo(Window other) {
		
			return Double.compare(x1, other.x1);
	
	}
}
/* 
 2 1

3 5
2 2
6 2

4 1
9 10
1 2
20 2
9 100
9 2

9 2
3 5
2 3
6 3
2 4
6 4
1 1
1 4
20 3
9 100
9 3
*/