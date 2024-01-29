person ={
	"name":"Filip",             #before colon = keys
	"age" :58,	                #after colon  = values
	"pets":False,
	"funny":"kinda",
	"fnumber":12,
}
#for val in person.values():   #shows all values
#        print(val)

for val in person.keys():     #shows all keys
        print(val)

for val in person.items():      #shows both keys and values
	print(val)

for key,value in person.items():
	# print("{} : {}".format(key,value))    #different possibilities of concatenating the strings
	print(f"{key} :  {value}")
	# print(str(key) + ":" + str(value))
	# print(key, ":", value)
