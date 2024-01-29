def list_manipulation(l, command, location, value):
	if command == "remove":
		if location == "beginning":
			return l.pop(0)
		elif location == "end":
			return l.pop()
	elif command == "add":
		if location == "beginning":
			l.insert(0, value)
			return l
		elif location == "end":
			l.append(value)
			return l

def is_palindrome(string):
	stripped = string.replace(" ", "")
	return stripped == stripped[::-1]:

def frequency(l, term):
	return l.count(term)

def multiply_even_numbers(collection):
	total = 1
	for i in collection:
		if i % 2 == 0:
			total = total * i
	return total

def capitalise(word)
	return word[:1].upper() + word[1:]

def compact(listy):
	return [i for i in listy if i]

def intersection(l1, l2):
    return [val for val in l1 if val in l2]

def partition(l, callback):
	return [[l.pop(l.index(i)) for i in l if callback(i)],l]