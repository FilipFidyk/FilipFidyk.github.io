#defining a class
class User: 
	#The __init__ method has to be defined and it runs by itself when an instance of the class is made
	#"self" parameter refers to the current class instance, always the first parameter
	#Attributes are defined like methods without ()
	#Parameters can be used to give individuals objects their own unique data
	def __init__(self, first, last, age):
		self.first = first
		self.last = last
		self.age = age

	# These are instance methods, different to class methods
	def full_name(self):
		return f"{self.first} {self.last}"

	def initials(self):
		return f"{self.first[0]} {self.last[0]}"

	def likes(self, thing):
		return f"{self.first} likes {thing}"

	def is_senior(self):
		return self.age >= 65

	def birthday(self):
		self.age += 1
		return f"Happy {self.age}th, {self.first}"

#instantiating an object
user1 = User("Joey", "White", 21)
print(user1.first, user1.last)
print(user1.initials())
print(user1.likes("Ice Cream"))
print(user1.is_senior())
print(user1.birthday())

#print(user1) # returns __main__.User object
#----------------------------------------------------------------------------------------

# Underscores

#_name - convention, private variable/method, python doesn't support true private variables/methods
#__name - Causes name mangling "__attr" -> "_CLASSNAME__attr", used with inheritance
#__name__ - Don't define Dunder methods, python searches for specific ones 