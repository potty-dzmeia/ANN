import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
%Load population of networks%

%Read from disk using FileInputStream
f_in = FileInputStream('netPopulation.data');

%Read object using ObjectInputStream
obj_in = ObjectInputStream (f_in);

%Read an object
obj = (RNNpopulation) obj_in.readObject();


