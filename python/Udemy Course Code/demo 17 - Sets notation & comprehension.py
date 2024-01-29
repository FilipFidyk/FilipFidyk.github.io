group_1 = set(['Alfred','John','Anne','Zizi','Cameron', 'Nathan'])
group_2 = set(['Nathan', 'Rafie','Brian','Rose','Abigail','John'])

everyone = group_1 | group_2
print(everyone)
both = group_1 & group_2
print(both)

s = set({x**2 for x in range(10)})
print(s)