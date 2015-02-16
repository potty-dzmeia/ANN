/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import NeuralNets.*;
import gridworld.*;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author hoho
 */
public class Simulation1 {
     public static void main(String args[])
     {
         int[][] iBestNets       = new int[5][2];
         int[]   iBestNetsInput  = new int[5];
         int[]   iHistory        = new int[10000];

         System.out.println("Start");


         // Create the neural nets
         RNNpopulation netPopulation = new RNNpopulation(100,3,7,2);

         // Create the environment for each of the net
         MultipleGridWorlds gridWorlds = new MultipleGridWorlds(100, 10,10, 1);


       


         for(int gen=0; gen<100; gen++)
         {
             
             for(int iStep=0; iStep<5000; iStep++)
             {
                netPopulation.simNetworks(gridWorlds.getSensoryInput());
                gridWorlds.action(netPopulation.getActions());
             }

             //Get the best nets
             iBestNets = gridWorlds.getBestNetworks(5);
             // Format the result and send it to netPopultaion
             for(int i=0; i<5; i++)
                 iBestNetsInput[i] = iBestNets[i][0];


             netPopulation.doNaturalSelection(iBestNetsInput);
             gridWorlds.reset();
             iHistory[gen] = iBestNets[4][1];
         }


         System.out.println("End");


         GridJFrame gFrame = new GridJFrame(10,10);
         for(int iStep=0; iStep<400; iStep++)
         {
             netPopulation.simNetworks(gridWorlds.getSensoryInput());
             gridWorlds.action(netPopulation.getActions());
            try {
                Thread.sleep(200);
            } catch (InterruptedException ex) {
                Logger.getLogger(Simulation1.class.getName()).log(Level.SEVERE, null, ex);
            }
             gridWorlds.Draw(gFrame, iBestNets[4][0]);
         }





     }// main

}
