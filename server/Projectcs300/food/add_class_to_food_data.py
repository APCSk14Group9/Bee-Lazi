import numpy as np

num_class = 8

a = np.load('..\\data\\foodlist.npy')

y = np.random.random_integers(1,num_class,a.shape[0])

np.save('foodclasslist.npy', y)
