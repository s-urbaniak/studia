clear all;
x=[-360:0.1:360];

k=1;
for x1=x
    [ys1(k), ys2(k)] = system_swiatla(x1);
    k = k+1;
end

figure(1);
plot(x,ys1);xlabel "odchylenie kierownicy [°]"
ylabel "odchylenie swiatel [°]"
print -depsc char_swiatla.eps

figure(2);
plot(x,ys2);xlabel "odchylenie kierownicy [°]"
ylabel "intensywnosc [%]"
print -depsc char_swiatla_intensywnosc.eps

