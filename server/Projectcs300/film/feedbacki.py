import numpy as np

def feedback(y):

	B = np.load('./Projectcs300/film/rank_list.npy')
	Y = np.load('./Projectcs300/film/filmclasslist.npy')

	for x,k in enumerate(y):
		print(B[x][0])
		tmp = B[x][1]
		if k==1:
			Y[tmp] += 1
			Y[tmp] = min(Y[tmp], 8)
		elif k==0:
			Y[tmp] -= 1
			Y[tmp] = max(Y[tmp], 1)

	np.save('filmclasslist.npy',Y)
