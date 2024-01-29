# Function 'all()' checks if all values within an iterable are truthy
# Function 'any()' checks if a single value within an iterable is truthy
nums = [1,2,3,4,5,6]

print(all(num % 2 == 0 for num in nums))
print(all(num for num in nums))

print(any(num % 2 == 0 for num in nums))
print(any(num % 7 == 0 for num in nums))

#Function 'sorted(iterable_object, key, reverse)' sorts all values within an iterable
more_nums = (3,6,9,3,5,2,6)
sorted_more_nums = sorted(more_nums)
reversed_more_nums = sorted(more_nums, reversed=True) #Using 'reversed=True' can sort values from highest to lowest
print(sorted_more_nums)
print(reversed_more_nums)

users = [
	{"username": "samuel", "tweets": ["I love cake", "I love pie", "hello world!"]},
	{"username": "katie", "tweets": ["I love my cat"]},
	{"username": "jeff", "tweets": [], "color": "purple"},
	{"username": "bob123", "tweets": [], "num": 10, "color": "teal"},
	{"username": "doggo_luvr", "tweets": ["dogs are the best", "I'm hungry"]},
	{"username": "guitar_gal", "tweets": []}
]

# To sort users by their username
users_sorted = sorted(users,key=lambda user: len(user['username']))
users_filtered = list(map(lambda user: user["username"], users_sorted))
# Finding our most active users...
# Sort users by number of tweets, descending
sorted(users,key=lambda user: len(user["tweets"]), reverse=True)