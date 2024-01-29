from pyfiglet import figlet_format
import requests
import random

print(figlet_format("Dad Joke 3000"))

joke_topic = input("Let me tell you a joke! Give me a topic: ")

response_data = requests.get(
	"https://icanhazdadjoke.com/search",
	headers = {"Accept": "application/json"},
	params = {"term": joke_topic}
).json()

different_jokes = len(response_data["results"])

if different_jokes > 1:
	rnum = random.randrange(0, different_jokes)

	print(f"I've got {different_jokes} jokes about {joke_topic}. Here's one:")
	print(response_data["results"][rnum]["joke"])

elif different_jokes == 1:
	print(f"I've got only one joke about {joke_topic}. Here it is :")
	print(response_data["results"][0]["joke"])

else:
	print(f"Sorry I don't have any jokes about {joke_topic}!")




