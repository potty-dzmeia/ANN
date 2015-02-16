%Creating  2 layer perceptron with 3 input units, 7 unit hidden layer
% and 2 output units. Random weight and bias equals 0.



layer1 = newp([0 1; 0 1; 0 1], 7);
layer1.inputweights{1,1}.initFcn = 'rands';
layer1 = init(layer1);

layer2 = newp([0 1; 0 1; 0 1; 0 1; 0 1; 0 1; 0 1], 2);
layer2.inputweights{1,1}.initFcn = 'rands';
layer2 = init(layer2);
