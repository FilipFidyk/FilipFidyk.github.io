#[start:end:step]
list1 = [1,2,3,4,5,6,7,8]
print(list1[3:]) # start from index 3, inclusive

print(list1[:5:]) #end at index 5, exclusive

print(list1[::2]) #every second one, starting at index 0

print(list1[::-1]) #count from the  end 
print(list1[6::-1])
print(list1[6:1:-1])

list2 = [1,2,3,4,5]
list2[1:3] = ['a','b','c']
print(list2)