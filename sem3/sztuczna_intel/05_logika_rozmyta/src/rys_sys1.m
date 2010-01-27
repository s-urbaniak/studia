clear all;
x=[0:0.1:100];

k=1;
for x1=x
    ys(k) = system_wiarygodnosc(x1);
    k = k+1;
end

plot(x,ys);xlabel "wiek [lata]"
ylabel "wiarygodnosc [%]"
