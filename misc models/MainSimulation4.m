javaaddpath E:\ANN\GridWorld\GridWorld\dist\GridWorld.jar
import gridworld.*
import java.lang.Thread


tic

% INITIALIZATION
%--------------------------------------------------------------------------
% Create the 100 GridWorlds 
gridWorlds = javaArray('gridworld.GridWorld',100);
for i=1:100
    gridWorlds(i) = GridWorld(10,10,1); %10x10 world with 5 food elements
end

% Create 100 ANNs of the type Layer_Recurrent Network using hardlim transfer function with:
%3 input units
%7 hidden units
%2 ouput units
layers1 = cell(1,100);
for i=1:100    % The first three input units are the sensory input, 
               % the next 7 input units are the context layer of the Elman network
    layers1{i}= newp([0 1; 0 1; 0 1; 0 1; 0 1; 0 1; 0 1; 0 1; 0 1; 0 1],7); 
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


% Create the vector that will hold the results from the hidden layer
% and will be used as input on the next step (context layer)
contextLayers = cell(1,100);
for i=1:100
    contextLayers{i} = zeros(7,1);
end

% The matrix to hold the top networks: index od the network and number of
% food elements eaten
	


% The mean fitness of each generation's top 5 networks
fitness(150) = 0;
% END of initialization
%--------------------------------------------------------------------------




%--------------------------------------------------------------------------
for generations=1:5 %50 generations
    
    for i=1:100 % for 100 networks

        for a=1:10 % Each network will live for 'a' number of steps

            % Get the sensor information from the simulation 
            right = double(gridWorlds(i).getRSensor());
            left  = double(gridWorlds(i).getLSensor());
            front = double(gridWorlds(i).getFSensor());

            % Create the new input=old result from the hidden units + new sensory input
            tempRes = [[right;left;front];contextLayers{i}];
            % Now simulate the network
            contextLayers{i} = sim(layers1{i},tempRes);
            actionToDo = sim(layers2{i}, double(contextLayers{i}) );

            % Update the position of the creature in the real world simulation
            gridWorlds(i).action(actionToDo(1,1),actionToDo(2,1));
            % Draw the world
            %gridWorlds(i).Draw(gridFrame);
            %Thread.sleep(500);
        end

    end


    % Genetic algorithm  START
    %--------------------------------------------------------------------------

    % Find the best 5 networks considering the eaten food and store their index
    % and number of food elements that were eaten
    topNets(5,2) = 0;

    for i=1:100 % check food eaten for every network
        for aa=1:5 % compare with the current best 5   
            if gridWorlds(i).getEatenFood()>=topNets(aa,2) 
               % Save the network if it has eaten more food than a previous
               % best 5

               for zz=5:-1:(aa+1) % But first make room for the new network
                   topNets(zz,1) = topNets(zz-1,1);
                   topNets(zz,2) = topNets(zz-1,2);
               end

               topNets(aa,1) = i;
               topNets(aa,2) = gridWorlds(i).getEatenFood();
               break;
            end
        end
    end

    % store the fitness of the current generations  
    fitness(generations) = mean(topNets(1:5,2));

    % Store temporary the parent networks so that the 2 cell arrays
    % are free for the offspring networks
    tempArray1 = cell(1,5); %IW + LW{1,1}
    tempArray2 = cell(1,5); %LW{2,1}

    for i=1:5
        tempArray1{i} = layers1{topNets(i,1)};
        tempArray2{i} = layers2{topNets(i,1)};
    end
    

    % Create the offspring from the top 5 networks adding some mutation to the weights 
    % The new cell array of networks will be composed of 19 offspring networks
    % and one parent for each of the top 5 networks, resulting in 100 networks.

    % Offpsring for top network 1
    % ---------------------------

    % first copy the network without change in the weights
    layers1{1}.IW{1,1} = tempArray1{1}.IW{1,1};
    layers2{1}.IW{1,1} = tempArray2{1}.IW{1,1};
    % then copy the offspring networks with 2 random changes in their weights
    for i=2:20
        layers1{i}.IW{1,1} = tempArray1{1}.IW{1,1};
        layers1{i}.IW{1,1}(int32(mod(rand()*1000,48)+1)) = rand()-rand();
        layers1{i}.IW{1,1} = tempArray1{1}.IW{1,1};
        layers1{i}.IW{1,1}(int32(mod(rand()*1000,48)+1)) = rand()-rand();

        layers2{i}.IW{1,1} = tempArray2{1}.IW{1,1};
        layers2{i}.IW{1,1}(int32(mod(rand()*1000,13)+1)) = rand()-rand();
    end

    % Offpsring for top network 2
    % ---------------------------

    % first copy the network without change in the weights
    layers1{21}.IW{1,1} = tempArray1{2}.IW{1,1};
    layers2{21}.IW{1,1} = tempArray2{2}.IW{1,1};
    % then copy the offspring networks with 2 random changes in their weights
    for i=22:40
        layers1{i}.IW{1,1} = tempArray1{2}.IW{1,1};
        layers1{i}.IW{1,1}(int32(mod(rand()*1000,48)+1)) = rand()-rand();
        layers1{i}.IW{1,1} = tempArray1{2}.IW{1,1};
        layers1{i}.IW{1,1}(int32(mod(rand()*1000,48)+1)) = rand()-rand();

        layers2{i}.IW{1,1} = tempArray2{2}.IW{1,1};
        layers2{i}.IW{1,1}(int32(mod(rand()*1000,13)+1)) = rand()-rand();
    end


    % Offpsring for top network 3
    % ---------------------------

    % first copy the network without change in the weights
    layers1{41}.IW{1,1} = tempArray1{3}.IW{1,1};
    layers2{41}.IW{1,1} = tempArray2{3}.IW{1,1};
    % then copy the offspring networks with 2 random changes in their weights
    for i=42:20
        layers1{i}.IW{1,1} = tempArray1{3}.IW{1,1};
        layers1{i}.IW{1,1}(int32(mod(rand()*1000,48)+1)) = rand()-rand();
        layers1{i}.IW{1,1} = tempArray1{3}.IW{1,1};
        layers1{i}.IW{1,1}(int32(mod(rand()*1000,48)+1)) = rand()-rand();

        layers2{i}.IW{1,1} = tempArray2{3}.IW{1,1};
        layers2{i}.IW{1,1}(int32(mod(rand()*1000,13)+1)) = rand()-rand();
    end



    % Offpsring for top network 4
    % ---------------------------

    % first copy the network without change in the weights
    layers1{61}.IW{1,1} = tempArray1{4}.IW{1,1};
    layers2{61}.IW{1,1} = tempArray2{4}.IW{1,1};
    % then copy the offspring networks with 2 random changes in their weights
    for i=62:80
        layers1{i}.IW{1,1} = tempArray1{4}.IW{1,1};
        layers1{i}.IW{1,1}(int32(mod(rand()*1000,48)+1)) = rand()-rand();
        layers1{i}.IW{1,1} = tempArray1{4}.IW{1,1};
        layers1{i}.IW{1,1}(int32(mod(rand()*1000,48)+1)) = rand()-rand();

        layers2{i}.IW{1,1} = tempArray2{4}.IW{1,1};
        layers2{i}.IW{1,1}(int32(mod(rand()*1000,13)+1)) = rand()-rand();
    end



    % Offpsring for top network 5
    % ---------------------------

    % first copy the network without change in the weights
    layers1{81}.IW{1,1} = tempArray1{5}.IW{1,1};
    layers2{81}.IW{1,1} = tempArray2{5}.IW{1,1};
    % then copy the offspring networks with 2 random changes in their weights
    for i=82:20
        layers1{i}.IW{1,1} = tempArray1{5}.IW{1,1};
        layers1{i}.IW{1,1}(int32(mod(rand()*1000,48)+1)) = rand()-rand();
        layers1{i}.IW{1,1} = tempArray1{5}.IW{1,1};
        layers1{i}.IW{1,1}(int32(mod(rand()*1000,48)+1)) = rand()-rand();

        layers2{i}.IW{1,1} = tempArray2{5}.IW{1,1};
        layers2{i}.IW{1,1}(int32(mod(rand()*1000,13)+1)) = rand()-rand();
    end


    % Genetic algorithm  END
    %--------------------------------------------------------------------------



    % Zero the eaten food for each of the gridWorlds
    for i=1:100
        gridWorlds(i).reset();
    end

    
end % generations

toc
%{
%Simulate each of the networks 50 times
%--------------------------------------------------------------------------


for i=1:100 % for 100 networks
    
    for a=1:100 % 100 times each
        
        % Get the sensor information from the simulation 
        right = double(gridWorlds(i).getRSensor());
        left  = double(gridWorlds(i).getLSensor());
        front = double(gridWorlds(i).getFSensor());

        % Create the new input=old result from the hidden units + new sensory input
        tempRes = [[right;left;front];contextLayers{i}];
        % Now simulate the network
        contextLayers{i} = sim(layers1{i},tempRes);
        actionToDo = sim(layers2{i}, double(contextLayers{i}) );

        % Update the position of the creature in the real world simulation
        gridWorlds(i).action(actionToDo(1,1),actionToDo(2,1));
        % Draw the world
        %gridWorlds(i).Draw(gridFrame);
        %Thread.sleep(500);
    end

end

%}






