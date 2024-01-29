import requests

# url = "https://www.google.com"
# response = requests.get(url)

#print(f"Your request to {url} came back with status code {response.status_code}")

###################################################################

url2 = "https://icanhazdadjoke.com/"

#"text/html" = normal, "text/plain" = normal text, not for every webpage,
#application/json = json version of the webpage
response2 = requests.get(url2, headers={"Accept": "application/json"})

print(type(response2.text)) #returns a str
print(response2.text)		

print(type(response2.json())) #returns a python dictionary
print(response2.json())
print("")		

data = response2.json()

print(type(data))
print(data["joke"])