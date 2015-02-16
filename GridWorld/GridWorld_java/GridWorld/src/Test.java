/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.*;
import NeuralNets.*;
/**
 *
 * @author hoho
 */
public class Test {
     public static void main(String args[])
     {
         int iBestNetsNumber = 5;
         int iNumberOfGrids = 100;
         int[] iFoodEaten = new int[100];

         Random rand = new Random();

         for(int i=0; i<100; i++)
         {
             iFoodEaten[i] = rand.nextInt(1001);
         }


         int[][] bestNets = new int[iBestNetsNumber][2];

        // Check each net how much food has eaten and if needed place it in the
        // array with best nets
        for(int iNet=0; iNet<iNumberOfGrids; iNet++)
        {
            // Check the current net with each position of the bestNets array
            for(int i=iBestNetsNumber-1; i>-1; i--)
            {   // If the net has eaten more food than the net in the current position
                // replace it
                if( iFoodEaten[iNet] > bestNets[i][1])
                {
                    //Move all the nets to the lower position
                    for(int zz = 0; zz<i-1;zz++)
                    {
                        bestNets[zz][0] = bestNets[zz+1][0];
                        bestNets[zz][1] = bestNets[zz+1][1];
                    }

                    // Now save the current net
                    bestNets[i][0] = iNet;
                    bestNets[i][1] = iFoodEaten[iNet];

                    break;
                }
            }

        }


     }
}
