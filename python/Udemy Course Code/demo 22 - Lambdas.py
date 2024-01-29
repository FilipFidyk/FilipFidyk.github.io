#A lambda is a one line function that contain an identifier
lambdasPrint = lambda : print("Hello")
# it can be stored in a variable and called like a function
#lambdasPrint()

L1 = [1,2,3,4,5]
doubleL1 = list(map(lambda x : 2*x, L1))
print(doubleL1)

evens = list(filter(lambda x : x % 2, L1))
print(evens)


people = ["don", "jonn", "dannn", "onnnn"]
peeps = list(map(lambda name : name.upper(), people))
for y in peeps:
	print(y)

d_names = list(filter(lambda x : x[0]=='d', people))

