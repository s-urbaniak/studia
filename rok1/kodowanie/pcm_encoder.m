clear;

in=[0:2047];

i=0;
out_reverse = zeros(0, 2);
for x = in
    i = i+1;

    % lower input index: 1, 32, 64, 128, 256, 512, 1024
    i_in1 = 1;

    % upper input index 32, 64, 128, 256, 512, 1024, 2048
    i_in2 = 32;

    % lower output index 1, 32, 48, 64, 80, 96, 112, 128
    i_out = 1;

    % find the current index i_in1 <= x+1 < in_2
    while (i_in2 < x+1)
        i_in1 = i_in2;
        i_in2 = i_in2*2;
        i_out = i_out+1;
    end

    if (i_in1 > 31)
        steps = i_in1/16;
        i_out = i_out*16;
    else
        steps = 1;
        i_in1 = 0;
        i_out = 0;
    end

    fraction = x-i_in1;
    resampled = floor(fraction/steps);
    out(i) = i_out + resampled;

    % build reverse lookup matrix
    irev = size(out_reverse)(1)+1;

    if (i_in1 > 31)
        if (mod(x, 2) == 0)
            if (out_reverse(irev-1,1) != out(i))
                out_reverse(irev,:) = [out(i) x];
            end
        end
    else
        out_reverse(irev,:) = [out(i) x];
    end
end

%plot(out_reverse(:,1),out_reverse(:,2))
plot(in, out);

in_dec=out;
in_dec = in_dec .+ 1;
out_dec = out_reverse(in_dec,2);
%plot(in_dec, out_dec);

