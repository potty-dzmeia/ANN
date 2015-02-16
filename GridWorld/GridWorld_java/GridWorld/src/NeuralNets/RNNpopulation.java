/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package NeuralNets;

import java.io.Serializable;
import java.util.*;
/**
 *
 * @author Levkov
 *
 * This class creates population (several) of Recurrent Neural Nets.
 * The nets is composed of input, hidden, context and output layer of neurons.
 * The activation function is hardlim().
 * The activity of the neurons could be 0 or 1. Floating point weights are represented using
 * decimal arithmetics, e.g: 0,576 equals 576
 *
 */
public class RNNpopulation implements Serializable{


    private int iNumberOfNets; // Number of networks that are to be created
    private int iInputLayer;   // Number of neurons in the input layer + context layer
    private int iHiddenLayer;  // Number of neurons in the hidden layer
    private int iOutputLayer;  // Number of neurons in the output layer


    /** Weights between the input and the hidden layer (including the
     *  weights between the context and the hidden layer). Where:
     *  IW[Number of the Network][hidden neurons][input+context neurons]
     *
     */
    int[][][] IW;

    /** Weights between the hidden and the output layer. Where:
     *  LW2[Number of the Network][output neurons][hidden neurons]
     *
     */
    int[][][] LW; // weights between the hidden and the output layer

    /** Activity of the neurons in the context layer. Where:
     *  Memory[Number of the Network][hidden neurons]
     */
    int[][] Memory; 

    /**
     *  Holds the action that each networks do in response to a given sensory input.
     *  Where netAction[number of networks][1]
     */
    int[] netAction;

    /**
     * Sets:
     *  the size of the popultaion
     *  number of neurons in the layers
     *  all weights to random values
     *  context layer activity to 0,5
     *
     * @param iNumberOfNets   Size of the population (number of networks)
     * @param iInputLayer     Number of neurons in the input layer
     * @param iHiddenLayer    Number of neurons in the hidden layer
     * @param iOutputLayer    Number of neurons in the output layer
     */
    public RNNpopulation(int iNumberOfNets,int iInputLayer,int iHiddenLayer,int iOutputLayer)
    {
        // Get the needed params
        this.iNumberOfNets = iNumberOfNets;
        this.iInputLayer   = iInputLayer;
        this.iHiddenLayer  = iHiddenLayer;
        this.iOutputLayer  = iOutputLayer;

        // Random number generator
        Random rd = new Random();

        // Set random weights to IW layer
        IW  = new int[iNumberOfNets][iHiddenLayer][iInputLayer+iHiddenLayer];
        for(int net=0; net<iNumberOfNets; net++)
            for(int a=0; a<iHiddenLayer; a++)
                for(int b=0; b<iInputLayer+iHiddenLayer; b++)
                    IW[net][a][b] = rd.nextInt(1001) - rd.nextInt(1001);


        // Set random weights to LW
        LW = new int[iNumberOfNets][iOutputLayer][iHiddenLayer];
        for(int net=0; net<iNumberOfNets; net++)
            for(int a=0; a<iOutputLayer; a++)
                 for(int b=0; b<iHiddenLayer; b++)
                    LW[net][a][b] = rd.nextInt(1001) - rd.nextInt(1001);


        // Set the activity of the Context Layer to 0
        Memory = new int[iNumberOfNets][iHiddenLayer];

        // Init the actions of each of the networks to do nothing
        netAction = new int[iNumberOfNets];

    }// Constructor




    /**
     * Simulates all the networks with the given SensoryInput input.
     *
     * @param SensoryInput - the sensory input for each of the nets. Where
     *                       SensoryInput[number of nets][number of sensors]
     *
     *
     */
    public void simNetworks(int[][] SensoryInput)
    {
        // Array to hold the activities of the hidden layer
        int[] tmpActivity = new int[this.iHiddenLayer];
        int[] tmpOuput = new int[this.iOutputLayer];

        // Sim each of the networks
        for(int netNumber=0; netNumber<this.iNumberOfNets; netNumber++)
        {

            // Calculate the activity for each of the neurons in the hidden layer
            for(int hiddenNeuron=0; hiddenNeuron<this.iHiddenLayer; hiddenNeuron++)
            {
                //Calculate the NetInput using the input neurons and the context layer
                    //Input neurons
                for(int inputNeuron=0; inputNeuron<this.iInputLayer; inputNeuron++)
                    tmpActivity[hiddenNeuron] += IW[netNumber][hiddenNeuron][inputNeuron]*SensoryInput[netNumber][inputNeuron];
                    //Context layer - the context layer weights are located in IW[][][iInputLayer+X contextLayerWeight]
                    //So the weight of the first context layer neuron with the first hidden neuron is IW[][0][iInputLayer+0]
                for(int inputNeuron=iInputLayer; inputNeuron < iHiddenLayer+iInputLayer; inputNeuron++)
                    tmpActivity[hiddenNeuron] += IW[netNumber][hiddenNeuron][inputNeuron]*Memory[netNumber][inputNeuron-iInputLayer];

                // Implement the hardlim() function;
                if(tmpActivity[hiddenNeuron] >= 0)
                    tmpActivity[hiddenNeuron] = 1;
                else
                    tmpActivity[hiddenNeuron] = 0;

            }

            // Save the activities of the hidden layer into the context layer
            for(int n=0; n<iHiddenLayer; n++)
            {
                Memory[netNumber][n] = tmpActivity[n];
            }

            // Calculate the output activity
            for(int outputNeuron=0; outputNeuron<iOutputLayer; outputNeuron++)
            {
                for(int hiddenNeuron=0; hiddenNeuron<iHiddenLayer; hiddenNeuron++)
                    tmpOuput[outputNeuron] += LW[netNumber][outputNeuron][hiddenNeuron] * tmpActivity[hiddenNeuron];

                // Implement the hardlim() function
                if(tmpOuput[outputNeuron] > 0)
                    tmpOuput[outputNeuron] = 1;
                else
                    tmpOuput[outputNeuron] = 0;
            }

            // Decode the ouput neurons activity into action
            if     (tmpOuput[0]==1 && tmpOuput[1]==1)
                netAction[netNumber] = 3; // move
            else if(tmpOuput[0]==0 && tmpOuput[1]==0)
                netAction[netNumber] = 0; // do nothing
            else if(tmpOuput[0]==1 && tmpOuput[1]==0)
                netAction[netNumber] = 1;     // turn right
            else if(tmpOuput[0]==0 && tmpOuput[1]==1)
                netAction[netNumber] = 2;     // turn left
         }

    }// simNetworks()


    public int simSingleNet(int iNet, int[] SensoryInput )
    {
          // Array to hold the activities of the hidden layer
         int[] tmpActivity = new int[this.iHiddenLayer];
         int[] tmpOuput = new int[this.iOutputLayer];

         // Calculate the activity for each of the neurons in the hidden layer
            for(int hiddenNeuron=0; hiddenNeuron<this.iHiddenLayer; hiddenNeuron++)
            {
                //Calculate the NetInput using the input neurons and the context layer
                    //Input neurons
                for(int inputNeuron=0; inputNeuron<this.iInputLayer; inputNeuron++)
                    tmpActivity[hiddenNeuron] += IW[iNet][hiddenNeuron][inputNeuron]*SensoryInput[inputNeuron];
                    //Context layer - the context layer weights are located in IW[][][iInputLayer+X contextLayerWeight]
                    //So the weight of the first context layer neuron with the first hidden neuron is IW[][0][iInputLayer+0]
                for(int inputNeuron=iInputLayer; inputNeuron < iHiddenLayer+iInputLayer; inputNeuron++)
                    tmpActivity[hiddenNeuron] += IW[iNet][hiddenNeuron][inputNeuron]*Memory[iNet][inputNeuron-iInputLayer];

                // Implement the hardlim() function;
                if(tmpActivity[hiddenNeuron] >= 0)
                    tmpActivity[hiddenNeuron] = 1;
                else
                    tmpActivity[hiddenNeuron] = 0;

            }

            // Save the activities of the hidden layer into the context layer
            for(int n=0; n<iHiddenLayer; n++)
            {
                Memory[iNet][n] = tmpActivity[n];
            }

            // Calculate the output activity
            for(int outputNeuron=0; outputNeuron<iOutputLayer; outputNeuron++)
            {
                for(int hiddenNeuron=0; hiddenNeuron<iHiddenLayer; hiddenNeuron++)
                    tmpOuput[outputNeuron] += LW[iNet][outputNeuron][hiddenNeuron] * tmpActivity[hiddenNeuron];

                // Implement the hardlim() function
                if(tmpOuput[outputNeuron] > 0)
                    tmpOuput[outputNeuron] = 1;
                else
                    tmpOuput[outputNeuron] = 0;
            }

            // Decode the ouput neurons activity into action
            if     (tmpOuput[0]==1 && tmpOuput[1]==1)
                netAction[iNet] = 3; // move
            else if(tmpOuput[0]==0 && tmpOuput[1]==0)
                netAction[iNet] = 0; // do nothing
            else if(tmpOuput[0]==1 && tmpOuput[1]==0)
                netAction[iNet] = 1;     // turn right
            else if(tmpOuput[0]==0 && tmpOuput[1]==1)
                netAction[iNet] = 2;     // turn left

        return netAction[iNet];
    }

    /**
     * 
     * @param selectedNetworks - Array that hold the most capable networks that 
     *                           are to be reproduced.
     */
    public void doNaturalSelection(int[] selectedNetworks)
    {
        int offset = 0;

        int[][][] tempIW = new int[selectedNetworks.length][iHiddenLayer][iInputLayer+iHiddenLayer];
        int[][][] tempLW = new int[selectedNetworks.length][iOutputLayer][iHiddenLayer];

        Random rand = new Random();


        //For each of the successful networks make new copies
        for(int i=0; i<selectedNetworks.length; i++)
        {
            for(int xx=0; xx<iHiddenLayer; xx++)
                System.arraycopy(IW[selectedNetworks[i]][xx], 0, tempIW[i][xx], 0, IW[selectedNetworks[i]][xx].length);
            for(int xx=0; xx<iOutputLayer; xx++)
                System.arraycopy(LW[selectedNetworks[i]][xx], 0, tempLW[i][xx], 0, LW[selectedNetworks[i]][xx].length);
        }

        //From each network make 20 new networks
        for(int i=0; i<selectedNetworks.length; i++)
        {
            for(int zz=0; zz<20; zz++)
            {
                 //Copy the network
                 for(int xx=0; xx<iHiddenLayer; xx++)
                     System.arraycopy(tempIW[i][xx], 0, IW[zz+offset][xx], 0, tempIW[i][xx].length);
                for(int xx=0; xx<iOutputLayer; xx++)
                    System.arraycopy(tempLW[i][xx], 0, LW[zz+offset][xx], 0, tempLW[i][xx].length);
                 
                 //Modify some of the weights (mutation)
                 int iTemp = rand.nextInt(iHiddenLayer);
                 int iTemp2= rand.nextInt(iInputLayer+iHiddenLayer);
                 IW[zz+offset][iTemp][iTemp2]
                         = rand.nextInt(1001) - rand.nextInt(1001);
                 iTemp = rand.nextInt(iHiddenLayer);
                 iTemp2= rand.nextInt(iInputLayer+iHiddenLayer);
                 IW[zz+offset][iTemp][iTemp2]
                         = rand.nextInt(1001) - rand.nextInt(1001);
                 LW[zz+offset][rand.nextInt(iOutputLayer)][rand.nextInt(iHiddenLayer)] 
                         = rand.nextInt(1001) - rand.nextInt(1001);                    
            }
            
            offset += 20;
        }

        // Zero the Context layer
        for(int i=0;i<this.iNumberOfNets; i++)
            Arrays.fill(Memory[i], 0);
    }


    public int[] getActions()
    {
        return netAction;
    }
}//class RNNpopulation
