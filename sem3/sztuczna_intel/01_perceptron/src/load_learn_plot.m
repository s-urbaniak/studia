function load_learn_plot(prefix, learn, epochs)
    we = load([prefix '_i.txt'])';
    wy = load([prefix '_o.txt'])';
    
    net = newp(minmax(we), 1);

    if learn
        net = init(net);
        net.trainParam.epochs = epochs;
        net = train(net, we, wy);

        suffix = '_learned';
    else
        suffix = '_unlearned';
    end

    y = sim(net, we);
    
    figure(1)
    plot(abs(y-wy));
    print('-dpng', '-r75', [prefix '_error' suffix '.png'])
    
    figure(2)
    plotpv(we, wy);
    plotpc(net.iw{1,1}, net.b{1});
    print('-dpng', '-r75', [prefix '_data' suffix '.png'])
end
