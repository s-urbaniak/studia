function [learned,weights]=delta(in,out,epochs)
    % debug input parameters
    % in
    % out
    % epochs

    % specify learning parameter
    eta = 0.2

    % specify the bias. for our case, only 1 and 0 makes sense
    bias = 1
 
    % specify the threshold (theta)
    % in our case 0 makes since since we have boolean functions
    threshold = 0

    % initialize empty weight vector
    % we have as many weights as rows in "in" + 1
    % the additional entry is the weight for the bias
    weights = rand(1, size(in, 1)+1);

    % the learned variable has the same dimensions as the expected out variable
    learned = zeros(size(out,1), size(out,2));

    % loop through all epochs and try to learn
    for current_epoch=1:epochs
        % iterate through all input values in the current epoch
        for i=1:size(in,2)
            % transpose the current values
            cur_in_transposed = in(:,i)';

            % add the bias
            cur_in_and_bias = [cur_in_transposed, bias];

            % calculate the weighted inputs (including the bias)
            weighted_ins = weights .* cur_in_and_bias;

            % sum the weighted inputs
            % this sum is the current output value
            sum_weighted_ins = sum(weighted_ins);

            if (sum_weighted_ins >= threshold)
                learned(i) = 1;
            else
                learned(i) = 0;
            end

            % error in the current epoch
            err = out(i) - learned(i);

            % the delta of the weights
            delta_weight = eta * err * cur_in_and_bias;

            % updates the current weight values with the calculated delta
            weights = weights + delta_weight;
        end

        % if the learned vector equals the expected vector, then we are finished with learning
        if (learned == out)
            break;
        end
    end

    if (learned == out)
        printf("---\nlearning SUCCESS after %d epochs\n", current_epoch)
    else
        printf("---\nlearning FAILURE !\n");
    end
end
