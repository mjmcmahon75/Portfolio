package flowChartv2;

import java.awt.Color;
import java.util.*;
import csci348.drawings.SimpleDrawing;
import csci348.drawings.*;
import java.awt.event.*;

public class EventHandler extends SimpleDrawing {

	Scanner scan;
	Mode mode;

	DrawFunctions dF;

	QuitMode quitMode = new QuitMode();
	BGColorMode bgMode = new BGColorMode();
	FGColorMode fgMode = new FGColorMode();
	EraseMode eMode = new EraseMode();
	IndexMode iMode=new IndexMode();

	DrawLine lineMode = new DrawLine();
	DrawBox boxMode = new DrawBox();
	DrawInvTri invTriMode=new DrawInvTri();
	DrawVTri vTriMode=new DrawVTri();
	DrawDiamond diaMode = new DrawDiamond();
	DrawArrow arrowMode = new DrawArrow();
	DrawCircle circMode=new DrawCircle();

	int mouseCX=-1;
	int mouseCY=-1;
	int mouseRX=-1;
	int mouseRY=-1;

	public enum Mode {
		line, box, parallelogram, bgClr, fgClr, erase, quit, circle, arrow, invTri, vTri, diamond, index
	}

	public EventHandler() {
		super();
		dF = new DrawFunctions(this);
		scan = new Scanner(System.in);
		// runLoop();
		// scan.close();
	}

	public EventHandler(int x, int y) {
		super(x, y);
		dF = new DrawFunctions(this);
		scan = new Scanner(System.in);
		// runLoop();
		// scan.close();
	}

	public abstract class ModeRunBase {
		protected abstract void modeText();

		public abstract void modeRun();

	}

	public class QuitMode extends ModeRunBase {
		protected void modeText() {
			System.out.println("\nQuit Program? y/n");
		}

		public void modeRun() {
			modeText();
			String inString = scan.next();
			inString = inString.toLowerCase();
			if (inString.equals("y") || inString.equals("yes")) {
				System.exit(0);
			}

		}
	}

	public class BGColorMode extends ModeRunBase {
		protected void modeText() {
			System.out.println("Enter a color for the background in rgb");
			System.out.println("Enter three numerical values: ");
		}

		public void modeRun() {
			modeText();
			int rIn = scan.nextInt();
			int gIn = scan.nextInt();
			int bIn = scan.nextInt();
			Color clr = new Color(rIn, gIn, bIn);
			setBackgroundColor(clr);
		}
	}

	public class FGColorMode extends ModeRunBase {
		protected void modeText() {
			System.out.println("Enter a color for the foreground in rgb");
			System.out.println("Enter three numerical values: ");
		}

		public void modeRun() {
			modeText();
			int rIn = scan.nextInt();
			int gIn = scan.nextInt();
			int bIn = scan.nextInt();
			Color clr = new Color(rIn, gIn, bIn);
			setForegroundColor(clr);
		}
	}

	public class EraseMode extends ModeRunBase {
		protected void modeText() {
			System.out.println("Click to erase. \nSelect a new mode to stop erasing");
		}

		public void modeRun() {
			modeText();
		}
	}
	public class IndexMode extends ModeRunBase{
		protected void modeText()
		{
			System.out.println("Index commands");
			System.out.println("\nUtility");
			System.out.println("index: opens this help section");
			System.out.println("quit: quits program");
			System.out.println("fgclr: changes foreground color");
			System.out.println("bgclr: changes background color");
			System.out.println("erase: erases shapes");
			System.out.println("\nDrawing:");
			System.out.println("box: draws box");
			System.out.println("circ: draws circle");
			System.out.println("dia: draws diamond");
			System.out.println("line: draws line");
			System.out.println("arrow: draws arrow");
			System.out.println("vtri: draws vertical triangle");
			System.out.println("invtri: draws inverted triangle");
			
			
		}
		public void modeRun()
		{
			modeText();
		}
	}
	public abstract class DrawMode extends ModeRunBase {
		int x1;
		int x2;
		int y1;
		int y2;

		public abstract void drawObject();

		protected abstract boolean boundBoxValidate();

	}

	public class DrawLine extends DrawMode {
		protected void modeText() {
			System.out.println("Create a line by clicking at one point, and releasing at another");
		}

		public void modeRun() {
			modeText();
		}

		public void drawObject() {
			boolean valid = false;
			valid = boundBoxValidate();
			if (valid == true) {
				dF.createLine(mouseCX, mouseCY, mouseRX, mouseRY);
			}
		}

		public boolean boundBoxValidate() {
			boolean valid = true;
			if (mouseCX == mouseRX || mouseCY == mouseRY)
				;
			valid = false;
			if (mouseCX < 0 || mouseCY < 0 || mouseRX < 0 || mouseRY < 0)
				valid = false;

			return valid;
		}

	}

	public class DrawArrow extends DrawMode {
		protected void modeText() {
			System.out.println("draw arrow by clicking at desired tail, and releasing at head");
		}

		public void modeRun() {
			modeText();
		}

		public void drawObject() {
			boolean valid = false;
			valid = boundBoxValidate();
			if (valid == true) {
				dF.createArrow(mouseCX, mouseCY, mouseRX, mouseRY);
			}
		}

		public boolean boundBoxValidate() {
			int OFFSETCONSTANT = 10;
			boolean valid = true;
			if (Math.abs(mouseRX - mouseCX) < OFFSETCONSTANT && Math.abs(mouseRY - mouseCY) < OFFSETCONSTANT)
				valid = false;

			// other conditions

			// within window
			if (mouseCX < 0 || mouseCY < 0 || mouseRX < 0 || mouseRY < 0)
				valid = false;

			return valid;
		}

	}

	
	public class DrawDiamond extends DrawMode {
		protected void modeText() {
			System.out.println("Create a diamond by clicking at one point, and releasing at another");
		}

		public void modeRun() {
			modeText();
		}

		public void drawObject() {
			boolean valid = false;
			valid = boundBoxValidate();
			if (valid == true) {
				dF.createDiamond(mouseCX, mouseCY, mouseRX, mouseRY);
			}
		}

		public boolean boundBoxValidate() {
			boolean valid = true;
			// other conditions

			// within window
			if (mouseCX < 0 || mouseCY < 0 || mouseRX < 0 || mouseRY < 0)
				valid = false;

			return valid;
		}

	}

	public class DrawCircle extends DrawMode {
		protected void modeText() {
			System.out.println("click and release to make a circle. circle radius is based on box width");
		}

		public void modeRun() {
			modeText();
		}

		public void drawObject() {
			boolean valid = false;
			valid = boundBoxValidate();
			if (valid == true) {
				 dF.createCircle(mouseCX, mouseCY, mouseRX, mouseRY);
			}
		}

		public boolean boundBoxValidate() {
			boolean valid = true;
			// other conditions
			if((Math.abs(mouseCX-mouseRX))==0 && (Math.abs(mouseCY-mouseRY))==0)
				valid=false;
			// within window
			if (mouseCX < 0 || mouseCY < 0 || mouseRX < 0 || mouseRY < 0)
				valid = false;

			return valid;
		}

	}

	public class DrawBox extends DrawMode {
		protected void modeText() {
			System.out.println("Create a box by clicking at one point, and releasing at another");
		}

		public void modeRun() {
			modeText();
		}

		public void drawObject() {
			boolean valid = false;
			valid = boundBoxValidate();
			if (valid == true) {
				dF.createBox(mouseCX, mouseCY, mouseRX, mouseRY);

			}
		}

		public boolean boundBoxValidate() {
			boolean valid = true;
			// other conditions

			// within window
			if (mouseCX < 0 || mouseCY < 0 || mouseRX < 0 || mouseRY < 0)
				valid = false;

			return valid;
		}

	}
	
	public class DrawVTri extends DrawMode {
		protected void modeText() {
			System.out.println("click and drag");
		}

		public void modeRun() {
			modeText();
		}

		public void drawObject() {
			boolean valid = false;
			valid = boundBoxValidate();
			if (valid == true) {
				dF.createVTri(mouseCX, mouseCY, mouseRX, mouseRY);
			}
		}

		public boolean boundBoxValidate() {
			boolean valid = true;
			// other conditions

			// within window
			if (mouseCX < 0 || mouseCY < 0 || mouseRX < 0 || mouseRY < 0)
				valid = false;

			return valid;
		}

	}

	public class DrawInvTri extends DrawMode {
		protected void modeText() {
			System.out.println("click and drag");
		}

		public void modeRun() {
			modeText();
		}

		public void drawObject() {
			boolean valid = false;
			valid = boundBoxValidate();
			if (valid == true) {
				dF.createInvTri(mouseCX, mouseCY, mouseRX, mouseRY);
			}
		}

		public boolean boundBoxValidate() {
			boolean valid = true;
			// other conditions

			// within window
			if (mouseCX < 0 || mouseCY < 0 || mouseRX < 0 || mouseRY < 0)
				valid = false;

			return valid;
		}

	}
	
	public void modeSelect(String modeString) {

		switch (modeString) {
		
		case "index":
			mode=Mode.index;
			iMode.modeRun();
			break;
			
		case "line":
			mode = Mode.line;
			lineMode.modeRun();
			break;

		case "quit":
			mode = Mode.quit;
			quitMode.modeRun();
			break;

		case "bgclr":
			mode = Mode.bgClr;
			bgMode.modeRun();
			break;

		case "fgclr":
			mode = Mode.fgClr;
			fgMode.modeRun();
			break;

		case "box":
			mode = Mode.box;
			boxMode.modeRun();
			break;
		case "dia":
			mode = Mode.diamond;
			diaMode.modeRun();
			break;

		case "erase":
			mode = Mode.erase;
			eMode.modeRun();
			break;
			
		case "arrow":
			mode = Mode.arrow;
			arrowMode.modeRun();
			break;
			
		case "circ":
			mode=Mode.circle;
			circMode.modeRun();
			break;
		case "vtri":
		mode=Mode.vTri;
		vTriMode.modeRun();
		break;
		
		case "invtri":
		mode=Mode.invTri;
		invTriMode.modeRun();
		break;
			

		default:
			System.out.println("Invalid input, type 'index' for help");
		}

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		this.mouseCX = arg0.getX();
		this.mouseCY = arg0.getY();
		System.out.println("awt position: " + mouseCX + "," + mouseCY);
		if (this.mode == Mode.erase)
			dF.erase(mouseCX, mouseCY);

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		this.mouseRX = arg0.getX();
		this.mouseRY = arg0.getY();
		drawSelect();
	}

	public void drawSelect() {
		switch (mode) {
		case line:
			lineMode.drawObject();
			break;

		case box:
			boxMode.drawObject();
			break;
		case diamond:
			diaMode.drawObject();
			break;

		case arrow:
			arrowMode.drawObject();
			break;
			
		case circle:
			circMode.drawObject();
			break;
			
		case vTri:
			vTriMode.drawObject();
			break;
			
		case invTri:
			invTriMode.drawObject();
			break;

		}
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		mouseCX = -1;
		mouseCY = -1;
		mouseRX = -1;
		mouseRY = -1;

	}

	@Override
	public void componentResized(ComponentEvent arg0) {
		if (dF != null)
			dF.drawAll();
	}

	public void runLoop() {
	
		mode = Mode.index;
		while (true) {
			System.out.println("enter mode");
			String modeTest = scan.next();
			modeSelect(modeTest.toLowerCase());

		}
	}
}
