from random import random

def coin_flip():
	#generate random number 0-1
	if random() > 0.5:
		return "Heads"
		#returns this output
	else:
		return "Tails"

print(coin_flip())

def uppercase_my_name(name):
	return name.upper()

def add(a,b):
	return a + b

#default parameters
#just assign a value to a parameter using '='

#you can also assign a function to the function using 'fn='

