javaaddpath D:\Programming_Projects\ANN\GridWorld\GridWorld_java\GridWorld\dist\GridWorld.jar
import gridworld.*
import NeuralNets.*
import java.lang.Thread


tic

%This is a simulation of 100 neural nets for given number of steps.
%After the steps are over using the fitness of each function the 5 best 
%networks are selected and from them new 100 networks are created -
%a kind of genetic algorithm is implemented.

%---------------------------------
% Parameters
%---------------------------------
iNumberOfGen = 100; % For how many generations the simulation will be running
iEpochs      = 300; % Epochs(actions) in one generation
iFood        = 7; % Number of food elements into the grid world



%---------------------------------
% Initialization
%---------------------------------

% Create 100 RNNs, with 3 input, 7 hidden and 2 output neurons
netPopulation = RNNpopulation(100,3,7,2);
% Create the 100 virtual worlds for each of the nets. Each gridworld is
% of size 10x10 cells and 1 food element
gridWorlds = MultipleGridWorlds(100, 10,10, iFood);

% Array to hold the fitness of each generation top 5 nets
% 200 generations, 5 nets, 2
topNets(iNumberOfGen, 5, 2) = 0;
% The average fitness of each of the generations
averageFitness(iNumberOfGen) = 0;

%TempArray
iTempArray(5) = 0;

%---------------------------------
% Simulation starts here
%---------------------------------

for gen=1:iNumberOfGen % The algorithm will last iNumberOfGen generations
    for steps=1:iEpochs % Each generation will live for iEpochs steps
        % simulate the nets, the sensory input is determined in the virtual
        % world
        netPopulation.simNetworks(gridWorlds.getSensoryInput());
        % Eeach network has done some action - update the virtual world
        gridWorlds.action(netPopulation.getActions());
    end
   
    
    % At the end of each generation get the fitness for the top 5 nets
    % and using them make the new offspring
    BestNets = gridWorlds.getBestNetworks(5); 
    iTempArray(1:5)= BestNets(1:5, 1);
   
    % Get the average fitness of the generation
    averageFitness(gen) = gridWorlds.getAverageEatenFood();
    
    if(gen<iNumberOfGen) % If this is the last generation skip the following actions
        % Make natural selection
        netPopulation.doNaturalSelection(iTempArray);
        % Reset the eaten food for each virtual grid world
        gridWorlds.reset();
    end
   
    
    % Copy the fitness of the best 5 networks for each generation into an array
    for inet=1:5
     topNets(gen,inet, 1:2) = BestNets(inet,1:2);
    end
    
end % for gen


%---------------------------------
% Delete unneeded data
%---------------------------------
clear iNumberOfGen;
clear iEpochs;
clear iFood;

clear iTempArray;
clear gen;
clear steps;
clear inet;


toc


