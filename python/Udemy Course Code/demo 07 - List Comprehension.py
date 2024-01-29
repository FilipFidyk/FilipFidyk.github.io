# numbers = [1,2,3,4,5]
# doubled_numbers = []

# for num in numbers:
# 	doubled_number = num * 2
# 	doubled_numbers.append(doubled_number)

# print(numbers)
# print(doubled_numbers)

# or this way using list comprehension

numbers = [1,2,3,4,5]

[num * 2 for num in numbers] #[2,4,6,8,10] 

me = 'filip'
upper_name = [char.upper() for char in me]
print(upper_name)

friends = ['ashley', 'matt', 'fred']
print([friend.upper() for friend in friends])
