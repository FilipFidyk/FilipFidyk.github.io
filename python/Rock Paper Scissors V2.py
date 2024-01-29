#Another file where young me didn't care about comments and spacing
import random

computerWins = 0
playerWins = 0
game_length = input("To how many do you want to play? : ")
game_length = int(game_length)
print("if you want to leave early then write 'quit' or 'q'")

while computerWins < game_length and playerWins < game_length:
	print("")
	print(f"Computer Score : {computerWins}-{playerWins} : Player Score")
	print("Rock...")
	print("Paper...")
	print("Scissors...")

	player = input("Player, please make your move : ").lower()
	if player == "quit" or player == "q":
		print("")
		break
	print("")
	print(f"Player picks {player}")

	rand_num = random.randint(0,2)
	if rand_num == 0:
		computer = "rock"
	elif rand_num == 1:
		computer = "paper"
	else:
		computer = "scissors"
	print(f"Computer picks {computer}")
	print("")

	if player == computer:
		print("it is a tie!")
	elif player == "rock":
		if computer == "scissors":
			print("Player wins!")
			playerWins += 1
		elif computer == "paper":
			print("Computer wins!")
			computerWins += 1
	elif player == "paper":
		if computer == "rock":
			print("Player wins!")
			playerWins += 1
		elif computer == "scissors":
			print("Computer wins!")
			computerWins += 1
	elif player == "scissors":
		if computer == "paper":
			print("Player wins!")
			playerWins += 1
		elif computer == "rock":
			print("Computer wins!")
			computerWins += 1
	else:
		print("something went wrong!")

	print("")

print(f"FINAL SCORE : Computer Score : {computerWins}-{playerWins} : Player Score")
if playerWins > computerWins:
	print("Well done, You won!")
else:
	print("Unlucky, you lost!")



	