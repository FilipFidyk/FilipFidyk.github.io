module Monoids where

import Data.Map (Map, (!))
import qualified Data.Map as Map
import Data.Semigroup(stimes)

newtype Wheel = Wheel (Map Char Char)
    deriving Show

makeWheel :: [(Char, Char)] -> Wheel
makeWheel xs = Wheel (Map.fromList xs)

rot1 :: Wheel
rot1 = makeWheel (zip letters lettersMoved1)
    where 
        letters = ['A' .. 'Z']
        lettersMoved1 = ['B' .. 'Z'] ++ ['A']

w :: Wheel
w = makeWheel (zip "ABCDEFGHIJKLMNOPQRSTUVWXYZ" "KFBOLRUETVXYGJAPIHDMZQSCWN")

traceWire :: Wheel -> Char -> Char
traceWire (Wheel wheelMap) key = wheelMap ! key

decode :: Wheel -> String -> String
decode wheel = map (traceWire wheel)

instance Semigroup Wheel where
    (<>) (Wheel x) (Wheel y) = Wheel (Map.compose x y)

instance Monoid Wheel where
    mempty = makeWheel [(x, x)| x <- ['A'.. 'Z']]

listOfWheels :: [Wheel]
listOfWheels = [w, rot1, w, rot1, w]

newDecode :: String -> String
newDecode = decode (mconcat listOfWheels)

decodeW :: Wheel-> String-> String
decodeW wheel text = if take 20 text == "PUBLICSTATICVOIDMAIN" then text
    else
        decodeW wheel (decode wheel text)

googolW :: Wheel -> String -> String
googolW wheel = decode (stimes (10^100) wheel)  