class Animal:
	
	def __init__(self, name, species):
		self.name = name
		self.species = species

	def __repr__(self):
		return f"{self.name} is a {self.species}"

	def make_sound(self, sound):
		print(f"this animal says {sound}")

	@property
	def age(self):
		return self._age #called with "name.age"

	@age.setter
	def age(self, value):
		self._age = max(value, 0)
	

class Cat(Animal):
	def __init__(self, name, breed, toy):
		#Animal.__init__(self, name, species)	- possible option
		super().__init__(name, species ="Cat")
		self.breed = breed
		self.toy = toy

	def play(self):
		print(f"{self.name} plays with {self.toy}")