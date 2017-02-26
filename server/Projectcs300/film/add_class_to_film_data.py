import numpy as np

num_class = 8

a = np.load('..\\data\\filmlist.npy')

y = np.random.random_integers(1,num_class,a.shape[0])

np.save('filmclasslist.npy', y)
