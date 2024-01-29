# #clear - removes everything
# d = dict(a=1, b=2, c=3)
# d.clear()
# print(d)


# #copy - creates a clone
# d1 = dict(apple = 1, pear = 2, car = 3, tree = 4, dog = 5)
# # c = d1.copy()
# # print(c)


# #fromkeys - used to generate default dictionaries
# new_user = {}.fromkeys(['name', 'score','profile','email'], 'unknown')
# print(new_user)


# #get - retrieves a value without an error if it aint present
# print(new_user.get('name'))
# print(new_user.get('apple'))


# #pop - removes a key-value pair, parameter is necessary
# d1.pop('pear')
# print(d1)
# #to remove a random item use .popitem
# d1.popitem()
# print(d1)


#update - updates 
first = dict(a=1,b=2,c=3,d=4,e=5)
second = {}
third = {'e' : 6}

second.update(first)
print(second)
second.update(third)
print(second)
second['a'] = "amazing"
print(second)
second.update(first)
print(second)