clear;

expo = 4;
vectors = 100;
errors_prob = 1/100;

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

function out = hamming_vector_length (expo)
    out = 2^expo+expo+1;
endfunction

% generate long vector with different random hamming vectors
vector_length = hamming_vector_length(expo);
all_vectors_length = vector_length * vectors;

errors = all_vectors_length * errors_prob;

printf("length of one hamming vector: %d bits\n", vector_length);
printf("length of one normal vector: %d bits\n", 2^expo);
printf("amount of hamming vectors: %d (= %d bits)\n", vectors, all_vectors_length);
printf("amount of error bits: %d\n", errors);

% generate error positions
error_pos=randperm(all_vectors_length);
error_pos=error_pos(1:errors);

false_error_detections = 0;

parity_pos = parity_bit_positions(expo);
for i = 1:vectors
    current_vector = hamming_random_vector(expo);

    vstart = ((i-1)*vector_length)+1;
    vend = (i*vector_length);
    errors_vstart = error_pos >= vstart;
    errors_vend = error_pos <= vend;
    error_indices = errors_vstart & errors_vend;
    error_indices = find(error_indices);

    % calculate current positions of errors
    current_error_pos = error_pos(error_indices) .- (vstart - 1);

    % falsify bits at error positions
    false_vector = current_vector;
    false_vector(current_error_pos) = !false_vector(current_error_pos);

    % detect error position
    error_detected = detect_error(false_vector, expo);

    % correct current vector with found position
    corrected_vector = false_vector;
    if ((error_detected > 0) && (error_detected <= vector_length))
        corrected_vector(error_detected) = !false_vector(error_detected);
    endif

    % if the corrected vector is not equal to our current input vector,
    % then the error detection didn't work out properly :(

    current_vector(parity_pos) = 0;
    corrected_vector(parity_pos) = 0;

    false_bits_vector = xor(current_vector, corrected_vector);
    false_error_detections += sum(false_bits_vector);
endfor

printf("False bits: %d\n", false_error_detections);
percent = (false_error_detections / ((2^expo)*vectors))*100;
printf("Wrong error detection ratio: %f%%\n", percent);

