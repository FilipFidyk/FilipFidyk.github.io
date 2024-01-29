# numbers = [1,2,3,4,5,6]

# evens = [num for num in numbers if num % 2 == 0]
# odds = [num for num in numbers if num % 2 != 0]

# print(evens)
# print(odds)

this = [num*2 if num % 2 ==0 else num/2 for num in [1,2,3,4,5,6]]
print(this)
#times a number by 2 if it is even(check by using modulus 2 and answer = 0)
#and divide a number by 2 if it is odd(check by using modulus 2 and answer != 0)
answer = [num for num in [1,2,3,4] if num in [3,4,5,6]]
print(answer)
#cheks for values in both of the lists
answer2 = [person[::-1].lower() for person in ["Elie", "Tim", "Matt"]]
print(answer2)
#makes all items lowercase and slices them to be reversed