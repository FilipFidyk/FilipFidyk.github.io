import re

def main():
	choice = '0'
	while choice != '4': 	
		print("Please choose one of the following using numbers")
		print("1 : Length count with spaces")
		print("2 : Length count without spaces")
		print("3 : Word count")
		print("4 : Exit")

		choice = input()

		if choice == '1':
			sen1 = input("Please enter a sentence : ")
			print(f" There are {length_count_wspaces(sen1)} characters in that sentence including spaces")
		
		elif choice == '2':
			sen2 = input("Please enter a sentence : ")
			print(f" There are {length_count_wospaces(sen2)} characters in that sentence including spaces")
		
		elif choice == '3':
			sen3 = input("Please enter a sentence : ")
			if word_count(sen3) == 1:
				print(f"There is {word_count(sen3)} word in that sentence")
			else:
				print(f"There are {word_count(sen3)} words in that sentence")
		
		else:
			exit()

def length_count_wspaces(var1):
	return len(var1)

def length_count_wospaces(var2):
	return len(var2) - var2.count(' ')

def word_count(var3):
	var3.strip()
	var3 = re.sub(' {2,}',' ', var3)
	words = var3.count(' ')
	return words + 1

main()