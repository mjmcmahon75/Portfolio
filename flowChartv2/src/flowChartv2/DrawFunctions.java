package flowChartv2;

import java.util.*;
import java.io.*;
import csci348.drawings.*;
public class DrawFunctions {

	public LinkedList<Graphic> gList = new LinkedList<Graphic>();
	protected SimpleDrawing dR;
	public DrawFunctions(SimpleDrawing dR){
		this.dR=dR;
	}
	
 	public class Point {
		int x;
		int y;

		public Point(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}

	// used to arrange point parameters for nonlinear shapes
	protected void boundBoxArrange(Point p1, Point p2) {
		Point temp;
		if (p1.x > p2.x) {
			temp = p1;
			p1 = p2;
			p2 = temp;
		}
	}

	public abstract class Graphic {
		Point p1;
		Point p2;
		ArrayList<Point> pointSet = new ArrayList<Point>();

		public abstract void create(Point p1, Point p2);

		public void merge(Graphic gMerge) {
			for (int i = 0; i < gMerge.pointSet.size(); i++)
				{this.pointSet.add(gMerge.pointSet.get(i));
			//System.out.println(gMerge.pointSet.get(i).x+","+gMerge.pointSet.get(i).x);
				}
		}
		
		public boolean contained(Point test)
		{
			boolean found=false;
			if(p1.x>p2.x)
			{
				Point temp;
				temp=p2;
				p2=p1;
				p1=temp;
			}
			int lowerBoundY;
			int upperBoundY;
			
			if(p2.y<p1.y)
			{
				lowerBoundY=p2.y;
				upperBoundY=p1.y;
			}
			else
			{
				lowerBoundY=p1.y;
				upperBoundY=p2.y;
			}
			
			if(test.x>=p1.x&&test.x<=p2.x&&test.y>=lowerBoundY&&test.y<=upperBoundY)
			{
				//found=containedBody(test);
				found=true;
			}
			return found;
		}

		public void draw() {
			for (int i = 0; i < pointSet.size(); i++) {
				Point p = pointSet.get(i);
				if ((p.x <= (dR.getSize().getWidth())) && (p.y <= (dR.getSize().getWidth())))
					dR.showPoint(p.x, p.y);
			}
		}

		/*private boolean containedBody(Point test) {
			boolean found = false;
			for (int i = 0; i < pointSet.size(); i++) {
				Point pCompare=pointSet.get(i);
				if(pCompare.equals(test))
					found=true;
				
			}
			return found;
		}*/

		public void erase() {
			for (int i = 0; i < pointSet.size(); i++) {
				Point p = pointSet.get(i);
				dR.hidePoint(p.x, p.y);

			}
		}
	}
	
	
	public void erase(Point pTest)
	{
		boolean graphicFound=false;
		Graphic erasee;
		
		for(int x=0; x<gList.size(); x++)
		{
			erasee=gList.get(x);
			graphicFound=erasee.contained(pTest);
			if(graphicFound==true)
			{
				gList.get(x).erase();
				gList.remove(x);
				dR.hideAllPoints();
				drawAll();
			}
			
		}

	}
	
	public void erase(int x, int y)
	{
		Point p1=new Point(x, y);
		erase(p1);
	}

	public class Line extends Graphic {
		
		//Created using bresenham algorithm
		//modified from code obtained from outside source
		//see documentation for details
		public void create(Point p1, Point p2)
		{
			
			int x=p1.x;
			int y=p1.y;
			int x2=p2.x;
			int y2=p2.y;
			
			int w = x2 - x ;
		    int h = y2 - y ;
		    int dx1 = 0, dy1 = 0, dx2 = 0, dy2 = 0 ;
		    if (w<0) dx1 = -1 ; else if (w>0) dx1 = 1 ;
		    if (h<0) dy1 = -1 ; else if (h>0) dy1 = 1 ;
		    if (w<0) dx2 = -1 ; else if (w>0) dx2 = 1 ;
		    int longest = Math.abs(w) ;
		    int shortest = Math.abs(h) ;
		    if (!(longest>shortest)) {
		        longest = Math.abs(h) ;
		        shortest = Math.abs(w) ;
		        if (h<0) dy2 = -1 ; else if (h>0) dy2 = 1 ;
		        dx2 = 0 ;            
		    }
		    int numerator = longest >> 1 ;
		    for (int i=0;i<=longest;i++) {
		       pointSet.add(new Point(x,y));
		        numerator += shortest ;
		        if (!(numerator<longest)) {
		            numerator -= longest ;
		            x += dx1 ;
		            y += dy1 ;
		        } else {
		            x += dx2 ;
		            y += dy2 ;
		        }
		    }
		}
	}
	
	//Assitance provided by Kevin
	public class Arrow extends Graphic{
		
		public void create(Point p1, Point p2)
		{
			this.p1=p1;
			this.p2=p2;
			
			int OFFSETCONSTANT=10;
			int OFFSET=4;
			Line lBody=new Line();
			lBody.create(p1, p2);
			int size=lBody.pointSet.size();
			System.out.println(size);
			Point pBase=lBody.pointSet.get(size-OFFSET);
			int nX=pBase.x;
			int nY=pBase.y;
			
			int xDif=(p2.x-nX);
			
			
			int yDif=(p2.x-nY);
			
			
			
			
			Point pTop=new Point((nX),(nY-xDif));
			Point pBot=new Point((nX),(nY+xDif));
			Line lTop=new Line();
			Line lBot=new Line();
			lTop.create(p2, pTop);
			lTop.create(p2, pBot);
			merge(lBody);
			merge((lTop));
			merge(lBot);
			
			
		}
	}

 	public class Box extends Graphic {



		public void create(Point p1, Point p2) {
			this.p1=p1;
			this.p2=p2;
			Line p1Horz = new Line();
			Line p1Vert = new Line();
			Line p2Horz = new Line();
			Line p2Vert = new Line();
			Point x1y2 = new Point(p1.x, p2.y);
			Point x2y1 = new Point(p2.x, p1.y);
			p1Horz.create(p1, x2y1);
			p2Horz.create(x1y2, p2);
			p1Vert.create(p1, x1y2);
			p2Vert.create(x2y1, p2);
			merge(p1Horz);
			merge(p1Vert);
			merge(p2Horz);
			merge(p2Vert);

		}
	}

	// needs work! help plz
	
	public class Diamond extends Graphic
	{
		public void create(Point p1, Point p2)
		{
			this.p1=p1;
			this.p2=p2;
			int xLength=p2.x-p1.x;
			int yLength=p2.y-p1.y;
			int midPointX=p1.x+(xLength/2);
			int midPointY=p1.y+(yLength/2);
					
			Line upLeft=new Line();
			Line upRight=new Line();
			Line botLeft=new Line();
			Line botRight=new Line();
			
			Point topMid=new Point(midPointX,p1.y);
			Point midLeft=new Point(p1.x,midPointY);
			Point midRight=new Point(p2.x, midPointY);
			Point botMid=new Point(midPointX, p2.y);
			dR.showPoint(topMid.x, topMid.y);
			dR.showPoint(midRight.x, midRight.y);
			dR.showPoint(midLeft.x, midLeft.y);
			dR.showPoint(botMid.x, botMid.y);
			upLeft.create(midLeft, topMid);
			System.out.println("line2");
			upRight.create(topMid,midRight);
			System.out.println("line3");
			botLeft.create(midLeft, botMid);
			System.out.println("line4");
			botRight.create(botMid, midRight);
			merge(upLeft);
			merge(upRight);
			merge(botLeft);
			merge(botRight);
			
			
		}
		
		protected void pointArrange(Point p1, Point p2)
		{
			this.p1=p1;
			this.p2=p2;
		}
		
		
	}
	
	public class Circle extends Graphic
	{
		public void create(Point p1, Point p2)
		{
		int midPointY=p2.y-p1.y;	
		if(p1.x<p2.x)
			{
				Point temp=p2;
				p2=p1;
				p1=temp;
			}
		if(p2.y<p1.y)
		{
			midPointY=(p1.y-p2.y)/2+p2.y;
		}
		
		int midPointX=p1.x+((p2.x-p1.x)/2);
		int radius=midPointX-p1.x;
		radius=Math.abs(radius);
		this.p1=new Point(midPointX-radius, midPointY-radius);
		this.p2=new Point(midPointX+radius, midPointY+radius);
		
		for(int theta=0; theta<90; theta++)
		{
			Point q1P;
			Point q2P;
			Point q3P;
			Point q4P;
			double rad=Math.toRadians(theta);
			int cosine= (int) (radius*(Math.cos(rad)));
			int sine= (int) (radius*(Math.sin(rad)));
			q1P=new Point(midPointX+cosine,midPointY+sine);
			q2P=new Point(midPointX-cosine,midPointY+sine);
			q3P=new Point(midPointX+cosine, midPointY-sine);
			q4P=new Point(midPointX-cosine, midPointY-sine);
			this.pointSet.add(q1P);
			this.pointSet.add(q2P);
			this.pointSet.add(q3P);
			this.pointSet.add(q4P);
		}
		}
		
	}
	
	public class VTri extends Graphic
	{
		public void create(Point p1, Point p2)
		{
			this.p1=p1;
			this.p2=p2;
			int length=p2.x-p1.x;
			int midPoint=p1.x+(length/2);
			Point basePoint=new Point(p1.x,p2.y);
			Point topMid=new Point(midPoint, p1.y);
			Line l1=new Line();
			Line l2=new Line();
			Line l3=new Line();
			l1.create(p2, basePoint);
			l2.create(p2, topMid);
			l3.create(basePoint, topMid);
			merge(l1);
			merge(l2);
			merge(l3);
		}
	}
	
	public class InvTri extends Graphic
	{
		public void create(Point p1, Point p2)
		{	this.p1=p1;
			this.p2=p2;
			int length=p2.x-p1.x;
			int midPoint=p1.x+(length/2);
			Point basePoint=new Point(p1.x,p1.y);
			Point botMid=new Point(midPoint, p2.y);
			Line l1=new Line();
			Line l2=new Line();
			Line l3=new Line();
			l1.create(p2, basePoint);
			l2.create(p2, botMid);
			l3.create(basePoint, botMid);
			merge(l1);
			merge(l2);
			merge(l3);
		}
	}
	
	public void createLine(int x1, int y1, int x2, int y2)
	{
		Point p1 = new Point(x1, y1);
		Point p2 = new Point(x2, y2);
		Graphic l1;
		
		l1=new Line();
		
		l1.create(p1, p2);
		gList.add(l1);
		l1.draw();
	}
	
	public void createBox(int x1, int y1, int x2, int y2)
	{
		Point p1 = new Point(x1, y1);
		Point p2 = new Point(x2, y2);
		Box b1=new Box();
		b1.create(p1, p2);
		gList.add(b1);
		b1.draw();
	}
	
	public void createArrow(int x1, int y1, int x2, int y2)
	{
		Point p1 = new Point(x1, y1);
		Point p2 = new Point(x2, y2);
		Arrow a1=new Arrow();
		a1.create(p1, p2);
		gList.add(a1);
		a1.draw();
	}
	

	public void createDiamond(int x1, int y1, int x2, int y2)
	{
		Point p1 = new Point(x1, y1);
		Point p2 = new Point(x2, y2);
		Graphic d1;
		d1=new Diamond();
		d1.create(p1, p2);
		gList.add(d1);
		d1.draw();
	}
	
	public void createCircle(int x1, int y1, int x2, int y2)
	{
		Point p1 = new Point(x1, y1);
		Point p2 = new Point(x2, y2);
		Graphic c1;
		c1=new Circle();
		c1.create(p1, p2);
		gList.add(c1);
		c1.draw();
	}
	
	public void createVTri(int x1, int y1, int x2, int y2)
	{
		Point p1 = new Point(x1, y1);
		Point p2 = new Point(x2, y2);
		Graphic vt;
		vt=new VTri();
		vt.create(p1, p2);
		gList.add(vt);
		vt.draw();
	}

	public void createInvTri(int x1, int y1, int x2, int y2)
	{
		Point p1 = new Point(x1, y1);
		Point p2 = new Point(x2, y2);
		Graphic vt;
		vt=new InvTri();
		vt.create(p1, p2);
		gList.add(vt);
		vt.draw();
	}

	public void drawAll() {
		for (int i = 0; i < gList.size(); i++) {
			gList.get(i).draw();
		}
	}
}
