import numpy as np
from openpyxl import load_workbook as lwb

def char_range(r1,r2):
	for c in range(ord(r1),ord(r2)+1):
		yield chr(c)


fname = input('input name file excel: ');
s = input('input row index: ');
n = input('input number of data: ');
sname = input('input generating data name: ');
cp = input('input column index range (like AZ,CP): ');
wb = lwb(fname+'.xlsx')
sheet = wb['Sheet1']
a = [[sheet[c + str(i)].value for c in char_range(cp[0],cp[1])] for i in range(int(s),int(n)+int(s))]
np.save(sname + '.npy',a)

