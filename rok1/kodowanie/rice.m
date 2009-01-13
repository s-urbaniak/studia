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
    p = calc_prob_0(in);

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

function ret = dec2binvec (number, len)
	% number must be nonegative
	if( number > 2^len )
		error('Number %d is to big to fit length %d!', number, len);
	endif
	
	% get number as a string
	temp = dec2bin([number,2^len-1]);
	temp = temp(1,:);
	
	% build binary vector based on number
	ret = zeros(1,len);

    	% fill it!
	for i=1:len
		ret(i) = str2num(temp(i));
	endfor
endfunction

function out = get_unary_code (in, k)
    u = floor(in / (2^k));
    out = [zeros(1,u) 1];
endfunction

function out = get_binary_code (in, k)
    v = mod(in, 2^k);
    out = dec2binvec(v, k);
endfunction

function [out, k] = encode (in)
    k = calc_k(in);
    coding_words = get_coding_words(in)
    out = [];
    for word = coding_words
        unary = get_unary_code(word, k);
        binary = get_binary_code(word, k);
        out = [ out unary binary];
    endfor
endfunction

function out = decode (in, k)
    finished = false;
endfunction

in = open_data(in_file)
[out, k] = encode(in)
printf("input length = %d\n", length(in));
printf("output length = %d\n", length(out));
printf("ratio = %f%%\n", length(out)/length(in)*100);
decode(out, k)

