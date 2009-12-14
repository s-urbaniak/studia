function load_plot(in_file, out_file)
    we = load(in_file)';
    wy = load(out_file)';
    
    net = newp(minmax(we), 1);
    y = sim(net, we);
    
    figure(1)
    plot(abs(y-wy));
    
    figure(2)
    plotpv(we, wy);
    plotpc(net.iw{1,1}, net.b{1});
end
