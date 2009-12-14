function load_delta(prefix, epochs)
    we = load([prefix '_i.txt'])';
    wy = load([prefix '_o.txt'])';

    [learned, weights, epochs_learned, success] = delta(we, wy, epochs);

    figure(1)
    plot(abs(learned - wy));
    weights
    epochs_learned

    if success
        printf("SUCCESS :-)\n");
    else
        printf("FAILURE :-(\n");
    end
endfunction
