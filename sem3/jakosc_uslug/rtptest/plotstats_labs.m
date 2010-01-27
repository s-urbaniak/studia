load rtpstats_labs.txt;

figure(1);
plot(rtpstats_labs(:,4));
xlabel("Throughput kbit/sec");

figure(2);
plot(rtpstats_labs(:,2));
xlabel("Jitter");

figure(3);
plot(rtpstats_labs(:,3));
xlabel("Lost packets");

sleep(1);
