javaaddpath E:\ANN\GridWorld\GridWorld\dist\GridWorld.jar
import gridworld.*
import java.lang.Thread



% INITIALIZATION
%--------------------------------------------------------------------------
% Create the 100 GridWorlds 
gridWorlds = javaArray('gridworld.GridWorld',100);
for i=1:100
    gridWorlds(i) = GridWorld(10,10,10); %10x10 world with 10 food elements
end

% Create 100 ANNs
layers1 = cell(1,100);
for i=1:100
    layers1{i}= newp([0 1; 0 1; 0 1],7); % 3 inputs and 7 units in the hidden layer
    layers1{i}.inputweights{1,1}.initFcn = 'rands';
    %layers1{i}.biases{1}.initFcn = 'rands';
    layers1{i} = init(layers1{i});
end

layers2 = cell(1,100);
for i=1:100
    layers2{i}= newp([0 1; 0 1; 0 1; 0 1; 0 1; 0 1; 0 1], 2); % 2 outputs
    layers2{i}.inputweights{1,1}.initFcn = 'rands';
    %layers2{i}.biases{1}.initFcn = 'rands';
    layers2{i} = init(layers2{i});
end




% Draw the current situation of the world
%gridWorld.Draw(gridFrame);
%Thread.sleep(1000);

%Simulate each of the networks 50 times
%--------------------------------------------------------------------------

for i=1:100 % 100 networks
    
    for a=1:50 % 50 times each
        
        % Get the sensor information from the simulation 
        right = double(gridWorlds(i).getRSensor());
        left  = double(gridWorlds(i).getLSensor());
        front = double(gridWorlds(i).getFSensor());

        % Using the sensor information simulate the perceptron
        actionToDo = sim(layers2{i}, double(sim(layers1{i},[right;left;front])));

        % Update the position of the creature in the real world simulation
        gridWorlds(i).action(actionToDo(1,1),actionToDo(2,1));
        % Draw the world
        %gridWorld{.Draw(gridFrame);
        %Thread.sleep(500);
    end

end


% Genetic algorithm  here
%--------------------------------------------------------------------------

% Find the best 5 networks considering the eaten food
topNets(5) = 0;
best = 0;
for i=1:100
    
   if gridWorlds(i).getEatenFood()>=best 
        topNets(5) = topNets(4);
        topNets(4) = topNets(3);
        topNets(3) = topNets(2);
        topNets(2) = topNets(1);
        topNets(1) = i;
        best = gridWorlds(i).getEatenFood();
   end

end

      





