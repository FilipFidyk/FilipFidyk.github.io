def sum_all_values(*args):
	total = 0
	for num in args:
		total += num
	print(total)

#nums = [1,2,3,4,5,6]
#sum_all_values(*nums)

#unpacking uses the * before an argument that is a collection of data like a list, tuple or set

def display_names(first, second):
	print(f"{first} says hello to {second}")

names = {"first" : "Colt", "second" : "Sue"}
display_names(**names)

def add_and_multiply_numbers()