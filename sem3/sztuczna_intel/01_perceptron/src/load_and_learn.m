load percep_i.txt;
load percep_o.txt;
we = percep_i’;
wy = percep_o’;

net = newp(minmax(we), 1);
net = init(net);
net.trainParam.epochs = 50;
net = train(net, we, wy);

y = sim(net, we);
figure(1)
plot(abs(y-wy));

figure(2)
plotpv(we, wy);
plotpc(net.iw{1,1}, net.b{1});
