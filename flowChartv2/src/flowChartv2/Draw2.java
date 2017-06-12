package flowChartv2;
import csci348.drawings.*;
import java.util.*;
import java.io.*;
public class Draw2{
	
	static EventHandler eH; 


	
	public static void main(String args[])
	{
		
		
		
		Scanner scan=new Scanner(System.in);
		System.out.println("Flow Chart Gen 2: ");
		System.out.println("Please choose dimensions for the canvas (pixels), type -1 for default (600x600)");
		
		int canvasX=scan.nextInt();
		{
			if(canvasX>0)
			{
				int canvasY=scan.nextInt();
				eH=new EventHandler(canvasX, canvasY);
			}
			else
			{
				eH=new EventHandler();
			}
		}
		
		eH.runLoop();
		
		
		
	}
}
