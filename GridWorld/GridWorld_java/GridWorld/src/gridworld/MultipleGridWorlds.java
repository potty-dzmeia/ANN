/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gridworld;
import java.io.Serializable;
import java.util.*;

/**
 *
 * @author Levkov
 *
 *  A class that realizes a SINGLE grid world with size of 10x10 squares.
 *  Where a creature is living eating food elements located across the
 *  grid. In any time there are 5 food elements and if an element is eaten
 *  new is added at random location.
 */
public class MultipleGridWorlds implements Serializable
{

    // Number of GridWorlds that are managed by the class
    int iNumberOfGridsWorlds;
     // Dimensions - number of horizontal and vertical cells in the GridWorld
    private int iSizeX, iSizeY;
    // Number of the food elements that will be present in the GridWorld
    private int iNumOfFoodEl;

    // Coordinates for each of the food element.
    private int[][] iFoodX;
    private int[][] iFoodY;
    // How much food the creature has eaten
    int[] iFoodEaten;

    // Coordinates of the creature
    private int[] iCreatureX; // from 0 to iSizeX-1
    private int[] iCreatureY; // from 0 to iSizeY-1

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
    private int[] iOrientation;

    // Creature's sensors status
    boolean[] bLeftSensor, bRightSensor, bFrontSensor;

    /**
     *
     * @param iSizeX Number of horizontal cells of the grid world
     * @param iSizeY Number of vertical cells of the grid world
     * @param iNumberOfFoodElements Number of food elements in the world
     */
    public MultipleGridWorlds(int iNumberOfWorlds, int iSizeX, int iSizeY, int iNumberOfFoodElements)
    {
       // Initialize the parameters
       this.iNumberOfGridsWorlds = iNumberOfWorlds;
       this.iSizeX         = iSizeX;
       this.iSizeY         = iSizeY;
       this.iNumOfFoodEl   = iNumberOfFoodElements;

       // Init the arrays
       this.iFoodEaten = new int[iNumberOfGridsWorlds];
       this.iCreatureX = new int[iNumberOfGridsWorlds];
       this.iCreatureY = new int[iNumberOfGridsWorlds];
       this.iOrientation = new int[iNumberOfGridsWorlds];
       this.bFrontSensor = new boolean[iNumberOfGridsWorlds];
       this.bLeftSensor = new boolean[iNumberOfGridsWorlds];
       this.bRightSensor = new boolean[iNumberOfGridsWorlds];

       this.iFoodX = new int[iNumberOfGridsWorlds][iNumOfFoodEl];
       this.iFoodY = new int[iNumberOfGridsWorlds][iNumOfFoodEl];


       // For each of the grids add the needed food elements
       for(int iNet=0; iNet<iNumberOfGridsWorlds; iNet++)
               for (int i = 0; i < iNumOfFoodEl; i++)
                  this.addNewFood(iNet,i);

       // Set the coordinates of the creature
       addCreature();

    }



     /** Clears the food eaten
      *
      */
     public void reset()
     {
         Arrays.fill(iFoodEaten,0);
     }

    /**Tells the creature what to do next.
     * @param iAction The action that the creature is to make
     *                0-do nothing;
     *                1-turn right;
     *                2-turn left;
     *                3-move forward
     */
    public void action(int[] iActions)
    {
        for(int iNet=0; iNet<iActions.length; iNet++)
        {
            switch(iActions[iNet])
            {
                case 0: // do Nothing
                    break;
                case 1: // turn Right
                    if(iOrientation[iNet] == 0)
                    {// From North to East
                        iOrientation[iNet] = 1;
                    }
                    else if(iOrientation[iNet] == 1)
                    {// From East to South
                        iOrientation[iNet] = 2;
                    }
                    else if(iOrientation[iNet] == 2)
                    {// From South to West
                        iOrientation[iNet] = 3;
                    }
                    else if(iOrientation[iNet] == 3)
                    {// From West to North
                        iOrientation[iNet] = 0;
                    }
                    break;

                case 2: // turn Left
                    if(iOrientation[iNet] == 0)
                    {// From North to West
                        iOrientation[iNet] = 3;
                    }
                    else if(iOrientation[iNet] == 3)
                    {// From West to South
                        iOrientation[iNet] = 2;
                    }
                    else if(iOrientation[iNet] == 2)
                    {// From South to East
                        iOrientation[iNet] = 1;
                    }
                    else if(iOrientation[iNet] == 1)
                    {// From East to North
                       iOrientation[iNet] = 0;
                    }
                    break;

                case 3: // move Forward
                    if(iOrientation[iNet] == 0)
                    {// North orientation
                        iCreatureY[iNet] -= 1;
                    }
                    else if(iOrientation[iNet] == 3)
                    {// West orientation
                        iCreatureX[iNet] -= 1;
                    }
                    else if(iOrientation[iNet] == 2)
                    {// South orientation
                        iCreatureY[iNet] += 1;
                    }
                    else if(iOrientation[iNet] == 1)
                    {// East orientation
                        iCreatureX[iNet] += 1;
                    }


                    // Make sure the creature stays in the grid
                    if( iCreatureX[iNet] > iSizeX-1 )
                        iCreatureX[iNet] = 0;
                    else if( iCreatureX[iNet] < 0)
                        iCreatureX[iNet] = iSizeX-1;
                    if( iCreatureY[iNet] > iSizeY-1 )
                        iCreatureY[iNet] = 0;
                    else if( iCreatureY[iNet] < 0 )
                        iCreatureY[iNet] = iSizeY-1;


                    // If the creature steps on food it eats it and new food element
                    // is added at random location
                    checkForEatenFood();

                    break;
                default:
                    //ERROR
                    break;
            }//switch(iAction)

        }// For each gridWorld

        updateSensors();
    }//public void action(int iAction)

    

    /**
     * Gets the grid world/s in which the Agent has eaten most food.
     * @param iNetCount - for example 5 best networks or 3 best nets
     * @return - First dimension of the array specifies the number of the networks
     * returned it ranges from 0 to iNetCount-1. The first value of the second
     * dimension specifies the index of the network, the second specifies the
     * number of food elements eaten.
     */
    public int[][] getBestNetworks(int iNetCount)
    {

        int[][] bestNets = new int[iNetCount][2];

        // Check each net how much food has eaten and if needed place it in the
        // array with best nets
        for(int iNet=0; iNet<iNumberOfGridsWorlds; iNet++)
        {
            // Check the current net with each position of the bestNets array
            for(int i=iNetCount-1; i>-1; i--)
            {   // If the net has eaten more food than the net in the current position
                // replace it
                if( iFoodEaten[iNet] >= bestNets[i][1])
                {
                    //Move all the nets to the lower position
                    for(int zz = 0; zz<i;zz++)
                    {
                        bestNets[zz][0] = bestNets[zz+1][0];
                        bestNets[zz][1] = bestNets[zz+1][1];
                    }

                    // Now save the current net
                    bestNets[i][0] = iNet;
                    bestNets[i][1] = iFoodEaten[iNet];

                    break;
                }

            }// for each position of the bestArray
        }// for each net

        return bestNets;
    }

     /**
     * Draws the needed GridWorld on a supplied JFrame
     */
    public void Draw(GridJFrame gridFrame, int gridNumber)
    {
        gridFrame.setVisible(true);

        gridFrame.doDrawing(iCreatureX[gridNumber], iCreatureY[gridNumber],
                            iOrientation[gridNumber],
                            iFoodX[gridNumber], iFoodY[gridNumber]);
    }


    /**
    * Sensory input for the agent for each of the GridWorlds.
    * @return - The first dimension of the array specifies the number of the
    * network, the second specifies one of the three sensors. 
    */
    public int[][] getSensoryInput()
    {
        int[][] iSensorsActivity = new int[iNumberOfGridsWorlds][3];

        for(int iNet=0; iNet<iNumberOfGridsWorlds; iNet++)
        {  //bLeftSensor, bRightSensor, bFrontSensor
            if(bRightSensor[iNet])
                iSensorsActivity[iNet][0] = 1;
            if(bLeftSensor[iNet])
                iSensorsActivity[iNet][1] = 1;
            if(bFrontSensor[iNet])
                iSensorsActivity[iNet][2] = 1;
        }

        return iSensorsActivity;
    }
    

    public int getAverageEatenFood()
    {
        int iTemp = 0;

        for(int i=0; i<iNumberOfGridsWorlds; i++)
        {
            iTemp += iFoodEaten[i];
        }

        return iTemp/iNumberOfGridsWorlds;
    }

    /**
     *  Checks if the creature stands on food and if so eats it. Than a new
     *  food element is added
     */
    private void checkForEatenFood()
    {
        for(int iNet=0; iNet<this.iNumberOfGridsWorlds; iNet++)
        {
            // Check if the creature is standing on food?
            for(int i=0; i<iNumOfFoodEl; i++)
            {
                if(iCreatureX[iNet] == iFoodX[iNet][i])
                    if(iCreatureY[iNet] == iFoodY[iNet][i])
                    {
                        iFoodEaten[iNet]++;
                        // A food el. was eaten - add new food
                        addNewFood(iNet, i);
                        break;
                    }
            }
        }// For each gridWorld
    }//checkForEatenFood()

    private void addCreature()
    {
        boolean bFlag; //Flag set to FALSE when an empty location is found
        int     randomX, randomY;

        Random rd = new Random();

        //Place the creature on a random location (must be empty from other
        //food elements).
        for(int iNet=0; iNet<this.iNumberOfGridsWorlds; iNet++)
        {
            do
            {
                bFlag = false;

                randomX = rd.nextInt(iSizeX);
                randomY = rd.nextInt(iSizeY);

                //check if the generated location is not occupied by a food element
                for(int i=0; i<iNumOfFoodEl; i++)
                {
                    if(iFoodX[iNet][i] == randomX && iFoodY[iNet][i]==randomY )
                       bFlag = true;
                }
            }while(bFlag);

            // A suitable place was found - store it
            iCreatureX[iNet] = randomX;
            iCreatureY[iNet] = randomY;
        }

    }


    /**
     *  Adds new food element near another food element
     *
     * @param iLocationInTheArray Add new food element in the specified
     *                            location in the array
     */
    private void addNewFood(int iNet, int iLoc)
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
                    if(iFoodX[iNet][i]==randomX && iFoodY[iNet][i]==randomY )
                       bFlag = true;
                }
        }while(bFlag);

       // A suitable place was found - store it
       iFoodX[iNet][iLoc] = randomX;
       iFoodY[iNet][iLoc] = randomY;
        
       

    }// addNewFood()


    /** Checks for food near the creature
     *
     */
    private void updateSensors()
    {
        for(int iNet=0; iNet<this.iNumberOfGridsWorlds; iNet++)
        {
            // Set the sensors to 0
            bLeftSensor[iNet]  = false;
            bRightSensor[iNet] = false;
            bFrontSensor[iNet] = false;


            // Coordinates of the cells in front, on the left and on the right
            // sides of the creature
            int iLeftSideX = 0, iLeftSideY = 0,
                iRightSideX = 0, iRightSideY = 0,
                iFrontSideX = 0, iFrontSideY = 0;

            // Get coordinates relative to the creatures orientation
            if (iOrientation[iNet] == 0) {// North orientation
                iLeftSideX = iCreatureX[iNet]-1;
                iLeftSideY = iCreatureY[iNet];
                iRightSideX = iCreatureX[iNet]+1;
                iRightSideY = iCreatureY[iNet];
                iFrontSideX = iCreatureX[iNet];
                iFrontSideY = iCreatureY[iNet]-1;
            } else if (iOrientation[iNet] == 3) {// West orientation
                iLeftSideX = iCreatureX[iNet];
                iLeftSideY = iCreatureY[iNet]+1;
                iRightSideX = iCreatureX[iNet];
                iRightSideY = iCreatureY[iNet]-1;
                iFrontSideX = iCreatureX[iNet]-1;
                iFrontSideY = iCreatureY[iNet];
            } else if (iOrientation[iNet] == 2) {// South orientation
                iLeftSideX = iCreatureX[iNet]+1;
                iLeftSideY = iCreatureY[iNet];
                iRightSideX = iCreatureX[iNet]-1;
                iRightSideY = iCreatureY[iNet];
                iFrontSideX = iCreatureX[iNet];
                iFrontSideY = iCreatureY[iNet]+1;
            } else if (iOrientation[iNet] == 1) {// East orientation
                iLeftSideX = iCreatureX[iNet];
                iLeftSideY = iCreatureY[iNet]-1;
                iRightSideX = iCreatureX[iNet];
                iRightSideY = iCreatureY[iNet]+1;
                iFrontSideX = iCreatureX[iNet]+1;
                iFrontSideY = iCreatureY[iNet];
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
                if(iFoodX[iNet][i] == iLeftSideX && iFoodY[iNet][i] == iLeftSideY)
                    bLeftSensor[iNet] = true;
                // check for food on the right
                else if(iFoodX[iNet][i] == iRightSideX && iFoodY[iNet][i] == iRightSideY)
                    bRightSensor[iNet] = true;
                // check for food in front
                else if(iFoodX[iNet][i] == iFrontSideX && iFoodY[iNet][i] == iFrontSideY)
                    bFrontSensor[iNet] = true;
            }
        }// For each GridWorld
    }//private void updateSensors()





}