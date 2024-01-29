nested_list = [[1,2,3],[4,5,6],[7,8,9]]
# for l in nested_list:
# 	for num in l:
# 		print(num)

# coords = [[111,222],[333,444],[555,666]]
# for loc in coords:
# 	print(loc)

# for loc in coords:
# 	print(loc[1])

# print()

# for loc in coords:
# 	for coord in loc:
# 		print(coord)

# NESTED LIST COMPREHENSION
[[print(num) for num in l] for l in nested_list]

#create a 2d nested list
board = [[num for num in range(1,4)] for val in range(1,4)]
board_3D = [[[d1 for d1 in range(1,4)] for d2 in range(1,4)] for d3 in range(1,4)]
print(board_3D)

#create a nested list but with X an O instead of numbers
print([["X" if num1 % 2 != 0 else "O" for num1 in range(1,4)] for num2 in range(1,4)]) 
#X instead of an odd number
#O instead of an even number
#Looped through an 2D list
