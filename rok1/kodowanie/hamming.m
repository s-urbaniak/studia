expo = 2;

% generate random binary vector
in_len = 2^expo;
in = rand(1, in_len);
in = in.*2;
in = floor(in);

% calculate amount of parity bits
r = expo+1;

% calculate parity bit positions
r_pos = 1:r;
for i = r_pos
    r_pos(i) = 2^(i-1);
endfor

in

% insert zeros at parity bit positions
in_len = numel(in)+numel(r_pos);
tf=false(1,in_len);
in_new=double(tf);
tf(r_pos)=true;
in_new(tf)=zeros(1,r) .+ 0;
in_new(~tf)=in;
in = in_new;
in

% construct parity matrix
parity_matrix = zeros(r, in_len);
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
parity_matrix

% calculate parities
for i = 1:r
    row = parity_matrix(i,:);
    paritiy_vals = and(row,in);

    p_old = 0;
    for p = paritiy_vals
        p_old = xor(p, p_old);
    endfor

    in(r_pos(i)) = p_old;
endfor
in

% falsify one bit
false_index = 3
in_false = in;
in_false(false_index) = not(in(false_index))

