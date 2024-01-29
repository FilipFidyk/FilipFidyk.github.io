months = ('January','February','March', 'April', 'May')

for month in months:
	print(month)

print('')

i = len(months) - 1
while i >= 0:
	print(months[i])
	i -= 1

print('')

print(months.index('February'))
print(months[0:])
print(months[1:4])
print(months[::2])
