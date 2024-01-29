#arrays of unordered data
s = set([1,2,3])
# s {1,2,3}

# add method
s.add(4)
# s {1,2,3,4}
s.add(4)
# s {1,2,3,4}

#remove method
s.remove(4)
# s {1,2,3}

#discard method, same as remove without a key error
s.dicard(4)

#copy method - creates a clone
another_s = s.copy()

#clear method - removes all contents



