#to install an external packages, in powershell write 'py -m install PACKAGENAME',
#you only have to do it once per module

from colorama import init
from termcolor import colored, cprint

init()

texty=colored("Hi there", color="cyan")
print(texty)