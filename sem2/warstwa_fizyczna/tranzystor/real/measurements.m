Ib = [20, 50, 70, 100];
Uce = [14, 12.5, 11, 9.5];

Ic = 15 .- Uce;
Ic = Ic ./ 220;

plot(Ib, Uce);
xlabel("Ib");
ylabel("Uce");
print -dpng measurement.png;
keyboard;

