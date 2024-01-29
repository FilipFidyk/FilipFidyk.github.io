# max() tells us the largest numerical or alphanumerical value, in the brackets or in an iterable
# min() tells us the smallest numerical or alphanumerical value, in the brackets or in an iterable
max(3,75,8,95,4)
min(3,75,8,95,4)

names = ['Arya','Samson',"Dora","Tim"]
#print(min(len(name) for name in names))

max(names, key=lambda n: len(n))

songs = [
	{"title": "happy birthday", "playcount": 1},
	{"title": "Survive", "playcount": 6},
	{"title": "YMCA", "playcount": 99},
	{"title": "Toxic", "playcount": 31}
]

# Finds the song with the lowerest playcount
min(songs, key=lambda s: s['playcount']) #{"title": "happy birthday", "playcount": 1}

# Finds the title of the most played song
max(songs, key=lambda s: s['playcount'])['title'] #YMCA


#Reversed() returns a reversed iterableb
for x in reversed(range(0,10)):
	print(x)

#abs() returns the positive corresponding value of a number, aka absolute value
print(abs(20))
print(abs(-17.3436))

#sum(iterable, start = 0) returns the sum of all values in an iterable to the 'start' which 
#defaults to 0

#round() returns a rounded number to ndigits or if ndigits = None it rounds to an integer

