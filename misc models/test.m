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


%Simulate the network - 
actionToDo = layer1.sim(layer2, double(sim(layer1,[gridWorld.getRSensor();gridWorld.getLSensor();gridWorld.getFSensor()])));

gridWorld.action(actionToDo(1,1),actionToDo(2,1));







