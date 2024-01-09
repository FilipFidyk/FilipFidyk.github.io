import random

class Card:
        #constructor
	def __init__(self, suit, value):
		self.suit = suit
		self.value = value

        #print what card it is
	def __repr__():
		return f"{self.value} of {self.suit}"


class Deck:
        #constructor
	def __init__(self):
	    suits = ["Hearts", "Diamonds", "Clubs", "Spades"]
		values = ["A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"]
		self.cards = [Card(i,j) for i in values for j in suits]

        #returns how many cards there in the deck
	def count(self):
		return len(self.cards)

        #returns a string statement as to ahow many cards there are in the deck
	def __repr__(self):
		return f"Deck of {self.count()} cards"
        #removes a number of cards from the deck and returns them
	def _deal(self, amount):
		removedCards = list()
		for i in range(amount):
			if self.count() == 0:
				raise ValueError("All cards have been dealt")
			removedCards.append(self.cards.pop())
		return removedCards
			
        #shuffles the order of cards in the deck
	def shuffle(self):
		if self.count() != 52:
			raise ValueError("Only full decks can be shuffled")
		random.shuffle(self.cards)

        #deals one card
	def deal_card(self)
		return self._deal(1)[0]
	
        #deals a given number of cards
	def deal_hand(self, value):
		return self._deal(value)



	

deck = Deck()
print(deck)
