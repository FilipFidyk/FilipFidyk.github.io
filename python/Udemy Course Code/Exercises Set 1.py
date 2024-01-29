def Product(x,y):
	return x * y
#self-explanatory

def return_day(num):
	def return_day(num):
    days = ["Sunday","Monday", "Tuesday","Wednesday","Thursday","Friday","Saturday"]
    # Check to see if num valid
    if num > 0 and num <= len(days):
        # use num - 1 because lists start at 0 
        return days[num-1]
    return None
#returns a days depending on its index

def last_element(array):
	if len(array) == 0:
		return None
	return array[-1]
#returns the last element of an array

def number_compare(a,b):
	if a < b:
		return "Second is greater"
	elif a > b:
		return "First is greater"
	return "Numbers are equal"
#compares two numbers

def single_letter_count(string, character):
	return str.lower().count(character.lower())
#counts all of one letter in a string, isnt case sensitivve

def multiple_letter_count(string):
		return {i:string.count(i) for i in string}
