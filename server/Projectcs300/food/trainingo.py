import numpy as np

from sklearn.datasets import make_gaussian_quantiles
from sklearn.ensemble import AdaBoostClassifier
from sklearn.metrics import accuracy_score
from sklearn.tree import DecisionTreeClassifier

def training(User):

	X = np.load('./Projectcs300/data/foodlist.npy')
	Y = np.load('./Projectcs300/food/foodclasslist.npy')
	A = X.copy()
	X = X[:,4:].astype(object)
	b = [X[i,1].split('-') for i in range(X.shape[0])]
	p = np.hstack((X[:,0].reshape(X.shape[0],1),X[:,2:])).astype(int) - np.array(User)
	X = np.hstack((p,np.array(b)))

	bdt = AdaBoostClassifier(
    		DecisionTreeClassifier(max_depth=1),
    		n_estimators=200,
    		learning_rate=1.5,
    		algorithm="SAMME")

	bdt.fit(X,Y)

	y = bdt.predict(X)

	y = y.astype(object)

	for i in range(len(y)):
		y[i] = (y[i],i)

	y.sort()
	y = y[::-1]

	nitem = 20

	for i in range(nitem):
		tmp = y[i][1]
	print(A[tmp,0])


	B = np.array([(A[y[i][1],0],y[i][1]) for i in range(len(y))], dtype=object)


	np.save('rank_list.npy',B)
