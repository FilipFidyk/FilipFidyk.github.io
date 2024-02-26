module TuplesAndLists where

import Prelude hiding (curry, uncurry, null)

--------------------------------------------------------------------------------
-- PAIRS

pair :: a -> b -> (a, b)
pair a b = (a, b)

swap :: (a, b) -> (b, a)
swap (a, b) = (b, a) 

--------------------------------------------------------------------------------
-- TRIPLES

birthday :: (Int, Int, Int)
birthday = (23, 1, 2004)

today :: (Int,Int,Int)
today = (19, 1, 2024)

age :: (Int,Int,Int) -> (Int,Int,Int) -> Int
age (bd,bm,by) (td,tm,ty) 
    |tm > bm = ty - by 
    |tm == bm && td >= bd = ty - by
    |otherwise = ty - by - 1

--------------------------------------------------------------------------------
-- LISTS

oneTwoThree :: [Int]
oneTwoThree = 1 : (2 : (3 : []))

oneTwoThree' :: [Int]
oneTwoThree' = [1, 2, 3]

null :: [a] -> Bool
null [] = True
null (x: _) = False

isPalindrome :: Eq a => [a] -> Bool
isPalindrome a = a == reverse a

sayTimes :: String -> Int -> String
sayTimes a b 
    | b == 0 = ""
    | otherwise = concat[ a, sayTimes a (b-1)]