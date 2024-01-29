import string
import random

#Declare most if not all simple typable characters
numbers = [x for x in range(10)]

lowerLetters = string.ascii_lowercase
lowerLetters[0::1]

upperLetters = string.ascii_uppercase
upperLetters[0::1]

symbols = ['!','"','£','$','$','%','^','^','&','*','(',')','-',
           '_','=','+','[',']','{','}',';',':','#','~',',','<',
           '.','>','/','?',"'",'@']

#Choose a character based on random
def choose_character():
	type = random.randrange(1,5)
	if type == 1:
		character = random.randrange(len(numbers))
		return numbers[character]
	elif type == 2:
		character = random.randrange(len(lowerLetters))
		return lowerLetters[character]
	elif type == 3:
		character = random.randrange(len(upperLetters))
		return upperLetters[character]
	else:
		character = random.randrange(len(symbols))
		return symbols[character]

#Create the string
def generate_password():
	passwordLength = input("How long should your password to be? ")
	
	password = ''
	passwordvalues = []
	
	for x in range(int(passwordLength)):
		passwordvalues.append(str(choose_character()))
		password = ''.join(passwordvalues)

	print(password)


def main():
	print('Hello! What would you like?')
	print('1 : Generate a Password')
	print('2 : Exit')
	print('')

	choice = input()
	while choice != '2':
		if choice == '1':
			generate_password()
			choice2 = input('Would you like another password? Answer yes or no. ')
			if choice2 == 'no':
				exit()
		else:
			exit()

main()