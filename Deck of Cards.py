import random

class Card:
	def __init__(self, suit, value):
		self.suit = suit
		self.value = value

	def __repr__():
		return f"{self.value} of {self.suit}"

for i in suits:
	for j in values:
		Card(i,j)

class Deck:
	def __init__(self):
	    suits = ["Hearts", "Diamonds", "Clubs", "Spades"]
		values = ["A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"]
		self.cards = [Card(i,j) for i in values for j in suits]

	def count(self):
		return len(self.cards)

	def __repr__(self):
		return f"Deck of {self.count()} cards"

	def _deal(self, amount):
		removedCards = list()
		for i in range(amount):
			if self.count() == 0:
				raise ValueError("All cards have been dealt")
			removedCards.append(self.cards.pop())
		return removedCards
			


	def shuffle(self):
		if self.count() != 52:
			raise ValueError("Only full decks can be shuffled")
		random.shuffle(self.cards)

	def deal_card(self)
		return self._deal(1)[0]

	def deal_hand(self, value):
		return self._deal(value)



	

deck = Deck()
print(deck)