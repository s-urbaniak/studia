while (true)
    load rtpstats.txt;

    num_datasets = 500;

    stop = length(rtpstats);
    if(stop < num_datasets)
        start = 1;
    else
        start = length(rtpstats)-num_datasets;
    endif
        

    figure(1);
    plot(rtpstats(start:stop,4));
    xlabel("Throughput kbit/sec");

    figure(2);
    plot(rtpstats(start:stop,2));
    xlabel("Jitter");

    figure(3);
    plot(rtpstats(start:stop,3));
    xlabel("Lost packets");

    sleep(1);
endwhile
