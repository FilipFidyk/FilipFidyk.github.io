# the correct order of different types of parameters
#	1 - parameters
#	2 - *args
#	3 - default parameters
#	4 - **kwargs

def display_info(a, b, *args, instructor="Filip", **kwargs):
	return [a,b, args, instructor, kwargs]

print(display_info(1, 2, 3, last_name="Fidyk", job="Binman"))

#In order to change the default parameter, in this case Filip, you have to specify the parameter,
#in this case 'instructor'


