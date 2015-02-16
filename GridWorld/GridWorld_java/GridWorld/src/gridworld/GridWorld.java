/*
 * A class that realizes a SINGLE grid world with size of 10x10 squares.
 * Where a creature is living eating food elements located across the
 * grid. In any time there are 5 food elements and if an element is eaten
 * new is added at random location.
 */

package gridworld;

import java.util.*;

/**
 *
 * @author hoho
 */
public class GridWorld {

    // Dimensions - number of horizontal and vertical cells
    private int iSizeX, iSizeY;
    // Number of the food elements
    private int iNumOfFoodEl;

    // Coordinates for each of the food element.
    private int[] iFoodX;
    private int[] iFoodY;
    // How much food the creature has eaten
    int iFoodEaten;

    // Coordinates of the creature
    private int iCreatureX; // from 0 to iSizeX-1
    private int iCreatureY; // from 0 to iSizeY-1
    
    /* Creature orientation is defined the following way:
     *                    | North|East|South|West
     * ------------------------------------------
     * iOrientation[0][0] |  0   |  1 |  0  | -1
     * ------------------------------------------
     * iOrientation[0][1] |  -1  |  0 |  1  |  0
     * -----------------------------------------
     * iOrientation[0][2] |   0  |  1 |  2  |  3
     * ------------------------------------------
     */
    //private int[][] iOrientation = new int[1][3]; // Start facing North
    private int iOrientation;

    // Creature's sensors status
    boolean bLeftSensor, bRightSensor, bFrontSensor;

    /**
     *
     * @param iSizeX Number of horizontal cells of the grid world
     * @param iSizeY Number of vertical cells of the grid world
     * @param iNumberOfFoodElements Number of food elements in the world
     */
    public GridWorld(int iSizeX, int iSizeY, int iNumberOfFoodElements)
    {
       this.iSizeX  = iSizeX;
       this.iSizeY  = iSizeY;
       iNumOfFoodEl = iNumberOfFoodElements;

       iFoodX = new int[iNumOfFoodEl];
       iFoodY = new int[iNumOfFoodEl];

       // Add the needed number of food elements at random locations
       for (int i = 0; i < iNumOfFoodEl; i++)
            this.addNewFood(i);

       // Set the coordinates of the creature
       addCreature();


    }


    
     /** Clears the food eaten
      *
      */
     public void reset()
     {
         iFoodEaten = 0;
     }

    /**Tells the creature what to do next.
     * @param iAction The action that the creature is to make
     *                0-do nothing;
     *                1-turn right;
     *                2-turn left;
     *                3-move forward
     */
    public void action(int iAction)
    {
        switch(iAction)
        {
            case 0: // do Nothing
                break;
            case 1: // turn Right
                if(iOrientation == 0)
                {// From North to East
                    iOrientation = 1;
                }
                else if(iOrientation == 1)
                {// From East to South
                    iOrientation = 2;
                }
                else if(iOrientation == 2)
                {// From South to West
                    iOrientation = 3;
                }
                else if(iOrientation == 3)
                {// From West to North
                    iOrientation = 0;
                }
                break;

            case 2: // turn Left
                if(iOrientation == 0)
                {// From North to West
                    iOrientation = 3;
                }
                else if(iOrientation == 3)
                {// From West to South
                    iOrientation = 2;
                }
                else if(iOrientation == 2)
                {// From South to East
                    iOrientation = 1;
                }
                else if(iOrientation == 1)
                {// From East to North
                   iOrientation = 0;
                }
                break;

            case 3: // move Forward
                if(iOrientation == 0)
                {// North orientation   
                    iCreatureY -= 1;
                }
                else if(iOrientation == 3)
                {// West orientation
                    iCreatureX -= 1;
                }
                else if(iOrientation == 2)
                {// South orientation
                    iCreatureY += 1;
                }
                else if(iOrientation == 1)
                {// East orientation
                    iCreatureX += 1;
                }
               

                // Make sure the creature stays in the grid
                if( iCreatureX > iSizeX-1 )
                    iCreatureX = 0;
                else if( iCreatureX < 0)
                    iCreatureX = iSizeX-1;
                if( iCreatureY > iSizeY-1 )
                    iCreatureY = 0;
                else if( iCreatureY < 0 )
                    iCreatureY = iSizeY-1;


                // If the creature steps on food it eats it and new food element
                // is added at random location
                checkForEatenFood();

                break;
            default:
                //ERROR
                break;   
        }//switch(iAction)
        
        updateSensors();
    }//public void action(int iAction)

 
    
    /** Same as Action(int) but the action is coded using 2bits
     *
     * @param bit0
     * @param bit1
     */
    public void action(boolean bit0, boolean bit1)
    {
        if(bit1 && bit0)
            action(3); //Move
        else if(bit1==false && bit0==false)
            action(0); //Do nothing
        else if(bit1==true && bit0==false)
            action(2); // Turn left
        else if(bit1==false && bit0==true)
            action(1); // Turn right

    }
    
    
     /**
     * Draws the current GridWorld on a supplied JFrame
     */
    public void Draw(GridJFrame gridFrame)
    {
        gridFrame.setVisible(true);
        
        gridFrame.doDrawing(iCreatureX, iCreatureY, 
                            iOrientation,
                            iFoodX, iFoodY);
    }

    
    /** Reports if the left sensor detects food
     * 
     * @return true - food detected on the left side of the creature
     */
    public boolean getLSensor()
    {
        return bLeftSensor;
    }
    public boolean getRSensor()
    {
        return bRightSensor;
    }
    public boolean getFSensor()
    {
        return bFrontSensor;
    }


    public int getEatenFood()
    {
        return iFoodEaten;
    }

    /**
     *  Checks if the creature stands on food and if so eats it. Than a new
     *  food element is added
     */
    private void checkForEatenFood()
    {
        // Check if the creature is standing on food?
        for(int i=0; i<iNumOfFoodEl; i++)
            if(iCreatureX == iFoodX[i])
                if(iCreatureY == iFoodY[i])
                {
                    iFoodEaten++;
                    // A food el. was eaten - add new food
                    addNewFood(i);
                    break;
                }
    }//checkForEatenFood()

    private void addCreature()
    {
        boolean bFlag; //Flag set to FALSE when an empty location is found
        int     randomX, randomY;

        Random rd = new Random();

        //Place the creature on a random location (must be empty from other
        //food elements).
        do
        {
            bFlag = false;

            randomX = rd.nextInt(iSizeX);
            randomY = rd.nextInt(iSizeY);

            //check if the generated location is not occupied by a food element
            for(int i=0; i<iNumOfFoodEl; i++)
            {
                if(iFoodX[i] == randomX && iFoodY[i]==randomY )
                   bFlag = true;
            }
        }while(bFlag);

        // A suitable place was found - store it
        iCreatureX = randomX;
        iCreatureY = randomY;

    }


    /**
     *  Adds new food element near another food element
     * 
     * @param iLocationInTheArray Add new food element in the specified
     *                            location in the array
     */
    private void addNewFood(int iLoc)
    {
        boolean bFlag; //Flag set to FALSE when an empty location is found
        int     randomX, randomY;
       
        Random rd = new Random();
         
        //Place the food element on a random location (must be empty from other
        //food elements).
        do
        {
            bFlag = false;

            randomX = rd.nextInt(iSizeX);
            randomY = rd.nextInt(iSizeY);

            //check if the generated location is not occupied by a food element
            for(int i=0; i<iNumOfFoodEl; i++)
            {
                if(iFoodX[i] == randomX && iFoodY[i]==randomY )
                   bFlag = true;
            }
        }while(bFlag);

        // A suitable place was found - store it
        iFoodX[iLoc] = randomX;
        iFoodY[iLoc] = randomY;

    }// addNewFood()

    
    /** Checks for food near the creature
     * 
     */
    private void updateSensors()
    {
        // Set the sensors to 0
        bLeftSensor  = false;
        bRightSensor = false;
        bFrontSensor = false;


        // Coordinates of the cells in front, on the left and on the right
        // sides of the creature
        int iLeftSideX = 0, iLeftSideY = 0,
            iRightSideX = 0, iRightSideY = 0,
            iFrontSideX = 0, iFrontSideY = 0;
        
        // Get coordinates relative to the creatures orientation
        if (iOrientation == 0) {// North orientation   
            iLeftSideX = iCreatureX-1;
            iLeftSideY = iCreatureY;
            iRightSideX = iCreatureX+1;
            iRightSideY = iCreatureY;
            iFrontSideX = iCreatureX;
            iFrontSideY = iCreatureY-1;
        } else if (iOrientation == 3) {// West orientation
            iLeftSideX = iCreatureX;
            iLeftSideY = iCreatureY+1;
            iRightSideX = iCreatureX;
            iRightSideY = iCreatureY-1;
            iFrontSideX = iCreatureX-1;
            iFrontSideY = iCreatureY;
        } else if (iOrientation == 2) {// South orientation
            iLeftSideX = iCreatureX+1;
            iLeftSideY = iCreatureY;
            iRightSideX = iCreatureX-1;
            iRightSideY = iCreatureY;
            iFrontSideX = iCreatureX;
            iFrontSideY = iCreatureY+1;
        } else if (iOrientation == 1) {// East orientation
            iLeftSideX = iCreatureX;
            iLeftSideY = iCreatureY-1;
            iRightSideX = iCreatureX;
            iRightSideY = iCreatureY+1;
            iFrontSideX = iCreatureX+1;
            iFrontSideY = iCreatureY;
        }


        // Make sure there are no coordinates out of bounds
        if (iLeftSideX > iSizeX - 1) {
            iLeftSideX = 0;
        } else if (iLeftSideX < 0) {
            iLeftSideX = iSizeX - 1;
        }
        if (iLeftSideY > iSizeY - 1) {
            iLeftSideY = 0;
        } else if (iLeftSideY < 0) {
            iLeftSideY = iSizeY - 1;
        }

        if (iRightSideX > iSizeX - 1) {
            iRightSideX = 0;
        } else if (iRightSideX < 0) {
            iRightSideX = iSizeX - 1;
        }
        if (iRightSideY > iSizeY - 1) {
            iRightSideY = 0;
        } else if (iRightSideY < 0) {
            iRightSideY = iSizeY - 1;
        }

        if (iFrontSideX > iSizeX - 1) {
            iFrontSideX = 0;
        } else if (iFrontSideX < 0) {
            iFrontSideX = iSizeX - 1;
        }
        if (iFrontSideY > iSizeY - 1) {
            iFrontSideY = 0;
        } else if (iFrontSideY < 0) {
            iFrontSideY = iSizeY - 1;
        }


        // Now for each food element check if it's on the right left or
        // in front of the creature
        for(int i=0; i<iNumOfFoodEl; i++)
        {
            // check for food on the left
            if(iFoodX[i] == iLeftSideX && iFoodY[i] == iLeftSideY)
                bLeftSensor = true;
            // check for food on the right
            else if(iFoodX[i] == iRightSideX && iFoodY[i] == iRightSideY)
                bRightSensor = true;
            // check for food in front
            else if(iFoodX[i] == iFrontSideX && iFoodY[i] == iFrontSideY)
                bFrontSensor = true;
        }
    }//private void updateSensors()

   

   
    
}
