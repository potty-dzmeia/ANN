javaaddpath E:\ANN\GridWorld\GridWorld\dist\GridWorld.jar
import gridworld.*
import java.lang.Thread


% Create the ANN first
layer1 = newp([0 1; 0 1; 0 1], 7);
layer1.inputweights{1,1}.initFcn = 'rands';
layer1 = init(layer1);

layer2 = newp([0 1; 0 1; 0 1; 0 1; 0 1; 0 1; 0 1], 2);
layer2.inputweights{1,1}.initFcn = 'rands';
layer2 = init(layer2);

% Create the GridWorld simulation
gridWorld = GridWorld(10,10,10);
gridFrame = GridJFrame(10,10);


% Draw the current situation of the world
gridWorld.Draw(gridFrame);
Thread.sleep(1000);

%Simulate the network
%-------------------------------------------------------------------------

for i=1:20
    % Get the sensor information from the simulation 
    right = double(gridWorld.getRSensor());
    left  = double(gridWorld.getLSensor());
    front = double(gridWorld.getFSensor());

    % Using the sensor information simulate the perceptron
    actionToDo = sim(layer2, double(sim(layer1,[right;left;front])));

    % Update the position of the creature in the real world simulation
    gridWorld.action(actionToDo(1,1),actionToDo(2,1));
    % Draw the world
    gridWorld.Draw(gridFrame);
    Thread.sleep(500);

end






