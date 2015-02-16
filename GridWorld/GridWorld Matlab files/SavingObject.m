import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
%save population of networks%

%Write to disk with FileOutputStream
f_out = FileOutputStream('netPopulation.data');
%Write object with ObjectOutputStream
obj_out = ObjectOutputStream (f_out);
%Write object out to disk
obj_out.writeObject ( netPopulation );