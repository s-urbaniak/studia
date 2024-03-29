function y = nadzor(in, clusters, epochs)
    eta = 0.1

    assert(epochs > 0);
    assert(clusters > 0);

    neurons = clusters;

    mx = min_max(in);
    range = (-mx(:,1) + mx(:,2))';
    for i=1:neurons
        initial_w = mx(:,1)' + (rand(1, size(in, 1)) .* range);
        % initial_w = zeros(1, size(in, 1));
        neuron(i).w = initial_w;
        neuron(i).y = initial_w';
    endfor

    for epoch=1:epochs
        for n=1:neurons
            for x=1:size(in,2)
                X = in(:,x)';

                % calculate distanc for each neuron
                for i=1:neurons
                    d = X - neuron(i).w;
                    d = d .^ 2;
                    d = sqrt(sum(d));
                    distance(i) = d;
                endfor

                % get neuron with smalles distance
                [distance, winner] = min(distance);

                % if the current neuron of interest (n) is the winner,
                % then correct its weights. if not, try another input
                if winner == n
                    delta_w = eta * (X - neuron(winner).w);
                    w_new = neuron(winner).w + delta_w;

                    neuron(winner).w = w_new;
                    neuron(winner).y = w_new';
                    break;
                endif
            endfor
        endfor
    endfor

    y = [ neuron.y ];
endfunction
