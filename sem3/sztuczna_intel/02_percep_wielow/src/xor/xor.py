import pyfann.libfann as fann
from matplotlib import pyplot, rc
import numpy

# init pyplot
rc('text', usetex=True)
rc('font', family='serif')

# create an empty simple network
def create_net(in_layers, hidden_layers, out_layers):
	net = fann.neural_net()
	net.create_sparse_array(1, (in_layers, hidden_layers, out_layers))
	net.set_activation_function_hidden(fann.SIGMOID_SYMMETRIC)
	net.set_activation_function_output(fann.LINEAR)

	return net

# run the network on some data
def run(net, data):
	out = numpy.zeros((len(data), 1))
	for i, y in enumerate(data):
		out[i] = net.run(y)

	return out

# plot error and data
def plot_output(y_learned, y, rowcol):
	pyplot.subplot(rowcol)
	pyplot.plot(y_learned, 'go')
	pyplot.plot(y, 'r.')
	pyplot.axis([-0.5, 3.5, -0.5, 1.5])
	pyplot.xlabel("Output data")

	error = abs(y_learned - y)

	pyplot.subplot(rowcol + 1)
	pyplot.plot(error, 'r')
	pyplot.axis([-0.5, 3.5, -0.5, 1.5])
	pyplot.xlabel("Error")

# create xor training data
data = fann.training_data()
inp = [[1,1],[0,1],[1,0],[0,0]]
out = [[0],[1],[1],[0]]
data.set_train_data(inp,out)

# create network
net = create_net(2, 2, 1)

# run on untrained network
out_learned = run(net, inp)

# save and plot
net.save("xor_untrained.net")
plot_output(out_learned, out, 211)
pyplot.savefig("xor_untrained.pdf", format = "pdf")

# train the network
net.train_on_data(data, 10000, 1000, 0.0000001)

# run on trained network
out_learned = run(net, inp)

# save and plot
net.save("xor_trained.net")
pyplot.clf()
plot_output(out_learned, out, 211)
pyplot.savefig("xor_trained.pdf", format = "pdf")

