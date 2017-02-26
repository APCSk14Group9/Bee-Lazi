import numpy as np

B = np.load('rank_list.npy')
Y = np.load('food_class_data_all.npy')

for x in B:
	print(x[0])
	y = input("Like or Dislike or Exit (1|0|x):")
	if y=='1':
		Y[x[1]] += 1
		Y[x[1]] = min(Y[x[1]], 8)
	elif y=='0':
		Y[x[1]] -= 1
		Y[x[1]] = max(Y[x[1]], 1)
	else:
		break

np.save('food_class_data_all.npy',Y)
	