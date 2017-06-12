/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package minesweeper;

//processing import
import processing.core.*;
import java.util.*;


/**
*@author: Max McMahon
*@version: 9/12/14
*Project 1: MineSweeper
*MineSweeper class: creates mineSweeper class which runs minesweeper game.
*MineSweeper extends PApplet in order to use processing
*numMines, gridHeight, and gridWidth not constants (Planned user setup function in later version) 
*/
public class MineSweeper extends PApplet {

    private int numMines;
    private int gridHeight;
    private int gridWidth;
    private Cell[][] cells;
    boolean win;
    boolean lose;

    /**
    *MineSweeper Constructor<br>
    *Initializes MineSweeper, control variables <br>
    *Creates cells array<br>
    *Performs setMines and checkAdjacent functions
    */
    public MineSweeper()
    {
       numMines=10;
       gridHeight=10;
       gridWidth=10;
       
       cells=new Cell[gridHeight][gridWidth];
       for(int y=0; y<gridHeight; y++)
       {
           for(int x=0; x<gridWidth;x++)
           cells[y][x]=new Cell();
       }
       setMines();
       checkAdjacent();
       win=false;
       lose=false;
    }
    /*
    * setMines Function
    * Randomly places numMines mines
    * Nested while loop and for loops: make sure mines not placed in buffer
    *or on top of another mine
    * mineReady variable set to true with legal mine placement
    */
   private void setMines()
    {
        
      Random generator=new Random();
      
      for(int i=0; i<numMines; i++)
      {
       boolean mineCheck=false;
       boolean mineReady=false; 
       int mineX=0;
       int mineY=0;
      while(mineReady!=true)
        {
      mineX=generator.nextInt(gridWidth-1);
      mineY=generator.nextInt(gridHeight-1);
      //Makes Sure coords are mine are not in buffer zone
      if(mineX==0 || mineY==0)
          mineReady=false;
      else 
          mineReady=true;
      
      if(mineReady==true)
      {
      mineCheck=cells[mineY][mineX].getIsMine();
      //makes sure mine isn't placed on an existing mine
      if(mineCheck==true)
          mineReady=false;
      else
          mineReady=true;
      }
        }
      cells[mineY][mineX].setMine(mineReady);
      }
    }
   
   /*
   * checkAdjacent Function
   * Checks all grids and counts number of adjacent mines
   * adjCount iterator used as parameter to setMines function
   */
   private void checkAdjacent()
   {
       for(int y=1; y<gridHeight-1; y++)
       {
           int adjCount;
           for(int x=1; x<gridWidth-1; x++)
           {
               adjCount=0;
               for(int y2=y-1; y2<y+2; y2++)
               {
                   for(int x2=x-1; x2<x+2; x2++)
                       if(cells[y2][x2].getIsMine()==true)
                       {
                           adjCount++;
                       }
               }
               cells[y][x].setAdjMines(adjCount);
           }
       }
   }
    
   /*
   * checkWinLose() Function
   * Checks lose variable to see if game is lost
   * If lose is false, checks to see if all safe spaces are revealed
   * numSafeSpaces: total number of non-mine spaces in grid
   * currSpaceLeft (initialized to value of numSafeSpaces) decrements with each found mine
   * If currSpaceLeft=zero, all mines are found and game is won
   * Calls win/lose functions based on result
   */
   private void checkWinLose()
   {
       int numSafeSpaces=(((gridHeight-2)*(gridWidth-2))-numMines);
       int currSpaceLeft=numSafeSpaces;
       
       if(lose==true)
       {
          loser();
       }
       else
       {
            for(int y=1; y<(gridHeight-1); y++)
        {
            for(int x=1; x<(gridWidth-1); x++)
            {
              if(cells[y][x].getIsMine()==false && cells[y][x].getRevealed()==true)
              {
                  currSpaceLeft--;
              }
            }  
        }
            if(currSpaceLeft<=0)
            {
                win=true;
                winner();
            }
       }
   }
   
   /**
    * revealAll Function<br>
    * Used by lose function, can be used by client code <br>
    * Reveals all grids<br>
    * If a mine is properly flagged, flag is left.<br>
    * Improperly flagged non-mines are de-flagged.
    */
   public void revealAll()
   {
       for(int y=1; y<gridHeight; y++)
       {
           for(int x=1; x<gridWidth-1; x++)
           {
               if(cells[x][y].getFlagged()!=true || cells[x][y].getIsMine()!=true)

                   cells[x][y].reveal();
           }
       }
   }
   
   /*
   * loser Function
   * When Player loses, calls revealAll() function
   * Places appropriately discouraging lose message
   * yPlace variable makes sure text does not overlap grid
   */
   private void loser()
   {
       revealAll();
       int yPlace=cells[gridHeight-2][gridWidth-2].getYTarget2();
       fill(153,0,0);
       textSize(40);
       text("You Lose", width/4, yPlace+40);
       fill(120,120,120);
       textSize(10);
   }
   
   /*
   * winner Function
   * When Player wins, calls revealAll() function to reveal any unflagged mines
   * Places appropriately congratulatory win message
   * yPlace variable makes sure text does not overlap grid
   */  
   private void winner()
   {
       revealAll();
       int yPlace=cells[gridHeight-2][gridWidth-2].getYTarget2();
       fill(0,204,0);
       textSize(40);
       text("You Win!!!", width/4, yPlace+40);
       fill(120,120,120);
       textSize(10);
       
   }
           
    /*
   * Cell class
   * Sub-class of MineSweeper, Cell class controls the contents of each square on grid
   * adjacentMines: number of mines adjacent to square
   * isMine: determines if cell is a mine
   * revealed: determines whether or not cell value (mine/number of adjacent mines) will be displayed
   * flagged: determines whether or not cell is a mine
   * firstMine: only set to true for the first mine a player clicks on. indicates which mine caused the player's untimely demise
   * (x/y)Target(Min/Max): boundaries of cell click area
   */
    private class Cell{
        private int adjacentMines;
        private boolean isMine;
        private boolean revealed;
        private boolean flagged;
        private boolean firstMine;
        
        //processing Target Variables
        private int xTargetMin;
        private int xTargetMax;
        private int yTargetMin;
        private int yTargetMax;
        
        
        /**
         * Cell Constructor<br>
         * Initializes cell values
         */
        public Cell()
        {
         adjacentMines=0;
         isMine=false;
         revealed=false;
         flagged=false;
         firstMine=false;
         
         xTargetMin=0;
         xTargetMax=0;
         yTargetMin=0;
         yTargetMax=0;
        }
        
        //Getters
        
        private boolean getRevealed()
        {
            return revealed;
        }
        
        private boolean getFlagged()
        {
            return flagged;
        }
        
        private boolean getIsMine()
        {
            return isMine;
        }
        
        private boolean getIsFirstMine()
        {
            return firstMine;
        }
        
        private int getAdjMines()
        {
            return adjacentMines;
        }
        
        private int getXTarget1()
        {
            return xTargetMin;
        }
        
         private int getXTarget2()
        {
            return xTargetMax;
        }
         
          private int getYTarget1()
        {
            return yTargetMin;
        }
          
         private int getYTarget2()
        {
            return yTargetMax;
        }
        
        
        //Setters
        
        private void setMine(boolean mineSet)
        {
            if(mineSet==true)
            {
                isMine=true;
            }
            else
                isMine=false;
        }
        
        //Reveals cell, only reveals square if square is not flagged or if game is over
        private void reveal()
        {
            if(flagged == false ||win==true || lose==true)
            {
            revealed=true;
            flagged=false;
            }
        }
        
        //Sets cell targets
        private void setTarget(int x1, int x2, int y1, int y2)
        {
         xTargetMin=x1;
         xTargetMax=x2;
         yTargetMin=y1;
         yTargetMax=y2;
        }
        
        //sets flag on cell if not revealed. removes flag if flag is present.
        private void setFlag()
        {
            if(revealed==false)
            {
                if(flagged==true)
                    flagged=false;
                else
                    flagged=true;
            }
        }
        
        private void setAdjMines(int adjMines)
        {
            adjacentMines=adjMines;
        }
        
        //only activated when the first mine is clicked, decided which mine decided to sell a farm, which the player subsequently bought.
        private void setFirstMine()
        {
            firstMine=true;
        }
        
    }
    
 /**
 * Processing setup function<br>
 * sets canvas size/color
 */
    public void setup() {
        size(400, 400);
        strokeWeight(1);
    }
   /**
    * Processing draw function<br>
    * Draws grids<br>
    * Determines buffer value, spacing out table<br>
    * Sets targets of each cell<br>
    * Calls resolveGrid function to determine what occupies cell<br>
    * At end of function calls checkWinLose
    */      
    public void draw() {
        textSize(10);
        strokeWeight(1);
        final int squareSize=20;
        int buffer =((width/2)-(((gridWidth-2)*squareSize)/2));
        int yCoordSum=90;
        stroke(0,0,0);
        fill(120,120,120);
        
        drawFace();
        
//Grid Draw
        for(int y=1; y<(gridHeight-1); y++)
        {
            int xCoordSum=buffer;
            for(int x=1; x<(gridWidth-1); x++)
            {
              rect(xCoordSum,yCoordSum,squareSize,squareSize);
              //sets targets of cells to coordinates of square corners
              cells[y][x].setTarget(xCoordSum, (xCoordSum+squareSize), yCoordSum, (yCoordSum+squareSize));
              xCoordSum+=squareSize;
              resolveGridDraw(y,x);
            }
            yCoordSum+=squareSize;
        }
      checkWinLose();
      
    }
    
    /**
     * Processing mousePressed function<br>
     * Triggered when user clicks within canvas<br>
     * Determines whether user-click is within a cell<br>
     * If user clicks within a cell, sets status based on button pressed<br>
     * Left-Click: reveals cell<br>
     * Right-Click: flags/de-flags a cell
     */
        public void mousePressed()
        {
          int clickTargetX=mouseX;
          int clickTargetY=mouseY;
          int targetCellIndexX=0;
          int targetCellIndexY=0;
          boolean cellFound=false;
          int xMin;
          int xMax;
          int yMin;
          int yMax;
          
          for(int y=1; y<gridHeight-1; y++)
          {
              for(int x=1; x<gridWidth; x++)
              {
                  //Gets boundaries of cell
                  xMin=cells[y][x].getXTarget1();
                  xMax=cells[y][x].getXTarget2();
                  yMin=cells[y][x].getYTarget1();
                  yMax=cells[y][x].getYTarget2();
                  
                  //determines if clicked cell is current cell
                  if(clickTargetX>=xMin && clickTargetX<=xMax && clickTargetY>=yMin && clickTargetY<=yMax)
                  {
                      targetCellIndexX=x;
                      targetCellIndexY=y;
                      cellFound=true;
                  }
                  if(cellFound==true)
                      break;
              }
              if(cellFound==true)
                      break;
          }
          //sets reveal/flag if game is still going and cell is found;
          if(cellFound==true && win==false && lose==false)
          {
              if(mouseButton==LEFT)
              {
                  cells[targetCellIndexY][targetCellIndexX].reveal();
              }
              else if(mouseButton==RIGHT)
                  cells[targetCellIndexY][targetCellIndexX].setFlag();
          }
          
          
        }
    
    private void resolveGridDraw(int yCoord, int xCoord)
    {
        boolean cellRevealed=cells[yCoord][xCoord].getRevealed();
        boolean cellFlagged=cells[yCoord][xCoord].getFlagged();
        if(cellRevealed==true)
            drawReveal(yCoord, xCoord);
        if(cellFlagged==true)
            drawFlag(yCoord, xCoord);
            
    }
    
    /*
    * drawReveal function
    * Draws result of revealed cells
    * Prints number of adjacent mines if cell is safe
    * Draws mine if cell is a mine
    * If the mine is the first mine clicked, mine is colored red to signify it as the instrument of the player's doom
    * Sets lose condition to true if the mine drawn is the first mine
    */
    private void drawReveal(int yCoord, int xCoord)
    {
      int xMin=cells[yCoord][xCoord].getXTarget1();
      int xMax=cells[yCoord][xCoord].getXTarget2();
      int xDiff=(xMax-xMin);
      int yMin=cells[yCoord][xCoord].getYTarget1();
      int yMax=cells[yCoord][xCoord].getYTarget2();
      int yDiff=(yMax-yMin);
      if(cells[yCoord][xCoord].getIsMine()==false)
      {
          int adjMines=cells[yCoord][xCoord].getAdjMines();
          stroke(0,0,0);
          fill(0,0,0);
          textSize(10);
          text(adjMines, (xMin+(xDiff/2)-2),(yMin+(yDiff/2)+5));
          fill(120,120,120);
          
      }
      else
      {
         if(lose==false && win==false)
         {
            cells[yCoord][xCoord].setFirstMine();
            lose=true;
         }
         if(cells[yCoord][xCoord].getIsFirstMine()==true)
         {
             fill(153,0,0);
             stroke (153,0,0);
         }
         else
             fill(0,0,0);
         
            ellipse((xMin+(xDiff/2)+1), (yMin+(yDiff/2)+1),(xDiff/2),(yDiff/2));
            fill(120,120,120);
            stroke(0,0,0);
      }
    }
    
    //draws a flag in the target cell
    private void drawFlag(int yCoord, int xCoord)
    {
      int xMin=cells[yCoord][xCoord].getXTarget1();
      int xMax=cells[yCoord][xCoord].getXTarget2();
      int xDiff=(xMax-xMin);
      //int yMin=cells[yCoord][xCoord].getYTarget1();
      int yMax=cells[yCoord][xCoord].getYTarget2();
      
      fill(0,0,0);
      rect((xMin+(xDiff/2)-2),(yMax-5),2,1);
      rect((xMin+(xDiff/2)-2), (yMax-14),1,9);
      fill(153,0,0);
      triangle((xMin+(xDiff/2)-2),(yMax-14),(xMin+(xDiff/2)+4),(yMax-10),(xMin+(xDiff/2)-2),(yMax-7));
      fill(120,120,120); 
    }
    
    /*
    * drawFace function
    * draws face based on status of game
    * Win: smiley face
    * Lose: frowny face
    * Game-run: ambivalent face
    */
    private void drawFace()
    {
        int widthMod=width/2;
        
        if(win==true)
        {
        fill(255,255,0);
        ellipse(widthMod,50,30,30);
        fill(0,0,0);
        ellipse(widthMod-5,50,3,3);
        ellipse(widthMod+5,50,3,3);
        noFill();
        bezier(widthMod-7, 57, widthMod-1, 60, widthMod+1, 60, widthMod+7, 57);
        
        stroke(0,0,0);
        fill(120,120,120);   
        }
        else if(lose==true)
        {
          fill(255,255,0);
        ellipse(widthMod,50,30,30);
        fill(0,0,0);
        ellipse(widthMod-5,50,3,3);
        ellipse(widthMod+5,50,3,3);
        noFill();
        bezier(widthMod-7, 60, widthMod-1, 57, widthMod+1, 57, widthMod+7, 60);  
        }
        else
        {
        fill(255,255,0);
        ellipse(widthMod,50,30,30);
        fill(0,0,0);
        ellipse(widthMod-5,50,3,3);
        ellipse(widthMod+5,50,3,3);
        line(widthMod-5,60,widthMod+5,60);
 
        stroke(0,0,0);
        fill(120,120,120);   
        }
    }

    /**
     * 
     * @param args 
     * 
     * Main method, utilized only by processing
     */
    static public void main(String args[]) {
       PApplet.main(new String[] { "minesweeper.MineSweeper" });
    }

    }
    