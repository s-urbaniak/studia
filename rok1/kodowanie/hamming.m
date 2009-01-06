clear;

expo = 2;
total_bits = 7;
total_errors = 2;

function out = generate_random_vector(expo)
    % generate random binary vector
    out_len = 2^expo;

    % set random values in vector
    out = rand(1, out_len);
    out = out .* 2;
    out = floor(out);
endfunction

% calculate parity bit positions
function parity_pos = parity_bit_positions (expo)
    r = expo+1;

    parity_pos = 1:r;
    for i = parity_pos
        parity_pos(i) = 2^(i-1);
    endfor
endfunction

% insert zeros at parity bit positions
function out = insert_empty_parities (in, expo)
    r = expo+1;
    r_pos = parity_bit_positions(expo);

    in_len = numel(in)+r;
    tf=false(1,in_len);
    in_new=double(tf);
    tf(r_pos)=true;
    in_new(tf)=zeros(1,r) .+ 0;
    in_new(~tf)=in;
    out = in_new;
endfunction

function parity_matrix = build_parity_matrix (expo)
    r = expo+1;
    in_len = 2^expo + r;

    for i = 1:in_len
        x = i;
        y = [];
        % Enter the main loop,
        while x > 1
            y = [y mod(x,2)]; % concatenating each binary digit,
            x = floor(x/2);   % updating the quotient.
        end
        y = [y x];            % Concatenate the last quotient.
        y = [y zeros(1, r-numel(y))];

        y = transpose(y);
        parity_matrix(:,i) = y;
    endfor
endfunction

function out = calculate_parities (in, expo)
    r = expo+1;
    parity_matrix = build_parity_matrix(expo);
    out = in;
    r_pos = parity_bit_positions(expo);

    % calculate parities
    for i = 1:r
        % parity bits at index i = Li
        parities = parity_matrix(i,:);
        paritiy_vals = and(parities, in);
        % reset parity bits of falsified vector
        paritiy_vals(r_pos(i)) = 0;

        p_old = 0;
        for p = paritiy_vals
            p_old = xor(p, p_old);
        endfor

        out(r_pos(i)) = p_old;
    endfor
endfunction

function pos = detect_error (in, expo)
    % calculate regular hamming parities of in vector
    out = calculate_parities(in, expo);
    parity_pos = parity_bit_positions (expo);

    % get the differences of the parities
    diff = xor(in, out);
    diff = diff(parity_pos);

    % calculate the position of the error
    i = 0;
    pos = 0;
    for bit = diff
        if (bit==1)
            pos = pos + 2^i;
        endif

        i = i+1;
    endfor
endfunction

function out = repair (in, expo)
    error_pos = detect_error(in, expo);
    out = in;
    if (error_pos > 0)
        out(error_pos) = not(out(error_pos));
    endif
endfunction

function out = hamming_random_vector (expo)
    out = generate_random_vector(expo);
    out = insert_empty_parities(out, expo);
    out = calculate_parities(out, expo);
endfunction

% generate long vector with different random hamming vectors
vector_length = 2^expo+expo+1;


% generate error positions
errors=randperm(floor(total_bits / vector_length));
errors=errors(1:total_errors)

hvec = [];
for vectors = 1:(total_bits / vector_length)
    hvec = [hvec hamming_random_vector(expo)];
endfor

decoding_errors = 0;
for i = 1:vectors
    % get error vector
    h_err = hvec_err(((i-1)*vector_length)+1:i*vector_length);

    % get correct vector
    h = hvec(((i-1)*vector_length)+1:i*vector_length);

    % try to repair the error vector
    repaired = repair(h_err, expo);
    repaired = calculate_parities(repaired, expo);

    % compare repaired vector with the original one
    if (sum(xor(repaired, h)) > 0)
        decoding_errors = decoding_errors + 1;
    endif
    decoding_errors;
endfor

