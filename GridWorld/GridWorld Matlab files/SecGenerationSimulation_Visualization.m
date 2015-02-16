% This file should be run after SecGenerationSimulation
% It visualizes the best network behaviour.

% Configurations:
%-------------------------------
NumberOfFoodElements  = 20;
NetworkToSimulate = 99;

SimulateForNumberOfSteps = 160;

GridWorldSizeX = 10;
GridWorldSizeY = 10;


% Initialization
%-------------------------------
% Create the virtual grid world
gridWorld = GridWorld(GridWorldSizeX,GridWorldSizeY,NumberOfFoodElements); 
% Create the object responsible for drawing
gridFrame = GridJFrame(GridWorldSizeX,GridWorldSizeY);

tempArray1(3) = 0;
  
for a=1:SimulateForNumberOfSteps
    
    tempArray1(1) = double(gridWorld.getRSensor());
    tempArray1(2) = double(gridWorld.getLSensor());
    tempArray1(3) = double(gridWorld.getFSensor());
        
       
    actionToDo = netPopulation.simSingleNet(NetworkToSimulate,tempArray1); 

    % Update the position of the creature in the real world simulation
    gridWorld.action(actionToDo);
    % Draw the world
    gridWorld.Draw(gridFrame);
    Thread.sleep(200);
    
end
    

% Clear the memory
%-------------------------------
gridFrame.dispose();
