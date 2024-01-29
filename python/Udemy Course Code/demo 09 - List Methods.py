#.append
list1 = [1,2,3,4,]
list1.append(5)
print(list1)

#extend
list2 = [1,2,3,4] 
#list2.append(5,6,7,8) doesn't work
#list2.append([5,6,7,8]) = [1,2,3,4,[5,6,7,8]]
list2.extend([5,6,7,8]) 
print(list2)

#insert
#inserts an item at a give position, inclusive counting
list3 = [1,2,3,4]
list3.insert(2,'Hi!') # adds 'Hi!' at index 2, 3rd thing
print(list3)

#clear
#removes everything
list4 = [1,2,3,4]
list4.clear()
print(list4)

#pop
#removes on item at an index
list5 = [1,2,3,4]
list5.pop() #removes last
list5.pop(1) # removes at index 1, second thing
print(list5)

#remove
#removes a certain value that you input
list6 = [1,2,3,4,4,4]
list6.remove(4)
print(list6)

#Swapping, not a method but grow up
list7 = ['apple','pear']
list7[0], list7[1] =list7[1], list7[0] 
print(list7)
