we = [0 0 1 1; 0 1 0 1];
wy = [0 1 1 0];

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
