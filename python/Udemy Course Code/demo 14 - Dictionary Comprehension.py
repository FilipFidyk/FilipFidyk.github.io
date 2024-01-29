# syntax {___:___ for __ in __}

#using a pre-existing dictionary
numbers = {
	'first' : 1,
	'second' : 2,
	'third' : 3
}

squared_numbers = {key:value ** 2 for key,value in numbers.items()}
for keys, values in squared_numbers.items():
	print(f"{keys} : {values}")

#creating a dictionary
print({num: num**2 for num in [1,2,3,4,5]})

#linking two strings
str1 = 'ABC'
str2 = '123'
combo = {str1[i]: str2[i] for i in range(0,len(str1))}
print(combo)

print()

#conditional logic
num_list = [1,2,3,4] # can use range instead for a list of items

dictionary = {num: ('even' if num % 2 == 0 else 'odd') for num in num_list}
for keys, values in dictionary.items():
	print(f"{keys} : {values}")