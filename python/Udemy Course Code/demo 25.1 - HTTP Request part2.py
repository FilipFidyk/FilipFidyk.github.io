import requests

url2 = "https://icanhazdadjoke.com/search"

response2 = requests.get(url2, 
	headers={"Accept": "application/json"},
	params={"term": "cat", "limit": 1}
)

data = response2.json()
print(data["results"])