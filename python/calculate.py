#Simple practise to learn about python key word arguments as parameters
def calculate(message="The result is ", **kwargs):
	answer = 0
	if kwargs["operation"] == 'add':
		answer = kwargs["first"] + kwargs["second"]
	if kwargs["operation"] == 'subtract':
		answer = kwargs["first"] - kwargs["second"]
	if kwargs["operation"] == 'mutliply':
		answer = kwargs["first"] * kwargs["second"]
	if kwargs["operation"] == 'divide':
		answer = kwargs["first"] / kwargs["second"]

	if kwargs["make_float"] == False:
		answer // 1

	print(message + str(answer))

calculate(message="You added ", make_float=False, operation='add', first=2, second=4)
calculate(make_float=True, operation='divide', first=3.5, second=5)