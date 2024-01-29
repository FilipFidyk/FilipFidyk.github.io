# for x in range(3):
# 	for num in range(1,11):
# 		print("\U0001f600" * num)

# times = 1
# while times < 11:
# 	print("\U0001f600" * times)
# 	times += 1

change = 8
for num in range(1,20,2):
	asterisks = "*" * num
	centring_spaces = " " * (num + change)
	print(centring_spaces+asterisks)
	change -= 3 
