module ListComprehensions where

import Data.List

--------------------------------------------------------------------------------

-- Returns True iff the first argument can be divided by the second with no remainder.
dividesBy :: Integer -> Integer -> Bool
dividesBy a b = mod a b == 0


-- Whether or not the argument is prime.
isPrime :: Integer -> Bool
isPrime 0 = False
isPrime 1 = False
isPrime n = not (or [dividesBy n x | x <- [2 .. floor (sqrt (fromIntegral n))]])


-- The infinite list of all prime numbers.
primes :: [Integer]
primes = filter isPrime [x | x <- [2 ..]]


-- All possible outcomes of rolling two die, with number of sides m and n, including duplicates.
rollOutcomes :: Int -> Int -> [Int]
rollOutcomes a b = [x + y | x <- [1 .. a], y <- [1 .. b]]


-- The Cartesian product of two lists.
cartProd :: [a] -> [b] -> [(a,b)]
cartProd a b = [(x,y) | x <- a, y <- b]


-- Given a function and two lists, apply f to every pair of elements from the two lists.
cartProdWith :: (a -> b -> c) -> [a] -> [b] -> [c]
cartProdWith f a b = map (uncurry f) (cartProd a b)


-- Implement rollOutcomes again, using the cartProdWith function defined above.
rollOutcomes' :: Int -> Int -> [Int]
rollOutcomes' a b = cartProdWith (+) [1 .. a] [1 .. b]  


-- The full lowercase alphabet.
letters :: [Char]
letters = ['a' .. 'z']


-- The five lowercase vowels.
vowels :: [Char]
vowels = "aeiou"


-- The twenty-one lowercase consonants.
consonants :: [Char]
consonants = letters \\ vowels


-- A function which gives back "FizzBuzz" if the number is a multiple of 3 and 5; "Fizz" if only a multiple of 3; "Buzz" if only a multiple of 5; and the number as a string otherwise.
fb :: Integer -> String
fb a 
    | a `mod` 3 == 0 && a `mod` 5 == 0 = "FizzBuzz"
    | a `mod` 3 == 0                   = "Fizz"
    | a `mod` 5 == 0                   = "Buzz"
    | otherwise                        = show a 

-- Using `map`, compute the full FizzBuzz from 1 up to the argument given.
fizzbuzzTo :: Integer -> [String]
fizzbuzzTo a = map fb [x | x <- [1 .. a]]


-- How many times is "Fizz" printed between 1 and 1000?
howManyFizzes :: Int
howManyFizzes = length [3,6 .. 1000]