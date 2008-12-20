clear;

in_file = ("rice1.txt")

function vector = open_data (fname)
    vector = [];
    fd = fopen(fname);
    vector = fread(fd, "uint8");

    % remove line feeds
    remove = find(vector.-10);
    vector = vector(remove);

    % remove CRs
    remove = find(vector.-13);
    vector = vector(remove);

    % calculate 0's and 1's
    vector = vector .- 48;

    vector = transpose(vector);
    fclose(fd);
endfunction

function p = calc_prob_0 (in)
    total_ones = sum(in);
    p = 1 - (total_ones / length(in));
endfunction

function k = calc_k (in)
    p = calc_prob_0(in)

    z = log2((sqrt(5)-1)/2);
    n = log2(p);

    k = log2(z/n);
    k = ceil(k);
endfunction

function words = get_coding_words (in)
    words = [];
    words(1) = 0;
    word_index = 1;

    for i = in
        if (i == 0)
            words(word_index)++;
        else
            word_index++;
            words(word_index) = 0;
        endif
    endfor

    % delete last obsolete value
    words(word_index) = [];
endfunction

in = open_data(in_file)
k = calc_k(in)
get_coding_words(in)

