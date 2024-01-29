def contains_purple(*args):
    if "purple" in args: 
    	return True
    return False

def combine_words(word,**kwargs):
    if 'prefix' in kwargs:
        return kwargs['prefix'] + word
    elif 'suffix' in kwargs:
        return word + kwargs['suffix']
    return word

def sg(**kwargs):
    if 'filip' in kwargs and kwargs['filip'] == 'him':
        return '*rolls out the red carpet*'
    elif 'filip' in kwargs and kwargs['filip'] != 'him':
        return 'shoot on sight!'
    return 'where is he?'
