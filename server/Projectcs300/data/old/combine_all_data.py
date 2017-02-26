import numpy as np

a = np.load('data_tien.npy')
b = np.load('data_tu.npy')
c = np.load('data_an.npy')
d = np.load('data_khiem.npy')
e = []
e.append(x for x in a);
e.append(x for x in b);
e.append(x for x in c);
e.append(x for x in d);

np.save('data_all.npy',e)