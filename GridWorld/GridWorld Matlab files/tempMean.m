sim7Result_6(100) = 0;
tempValue = 0;

for i=1:100
    for z=1:5
        tempValue = tempValue + topNetsCopy5(i,z, 2);
        
    end
        
        sim7Result_6(i) = tempValue/5;
        tempValue = 0;
end
