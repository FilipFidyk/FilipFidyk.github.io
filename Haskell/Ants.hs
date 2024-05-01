module Ants where

--------------------------------------------------------------------------------
-- This file should contain your complete work for the first coursework of 
-- CS141 Functional Programming.
-- 
-- USER ID: 
--
-- Before starting on this file, ensure that you have read the specification IN
-- ITS ENTIRETY and that you understand everything that is required from a good
-- solution.
--------------------------------------------------------------------------------

import Ants.Types
import Hatch
import Data.List (elemIndex)
import Data.Maybe (fromJust)

-- | Uncomment these two lines once you get to Exercise 6.
import Data.Set (Set)
import qualified Data.Set as Set



{-| 
  Ex. 1: Implement a Show instance for LineState.

  (The pipe character and full block character are | and █)

  [JUSTIFY]
  A LineState is a list of CellStates and i need to convert that into a list of characters
  which tends itself well to a map function that converts ons into '█'s and off into ' 's. Then
  concatenate vertical bars either side of it and we have a acceptable representation of the
  LineState
-}

instance Show LineState where
  show :: LineState -> String
  show (LS xs) = "|" ++ map (\x -> if x == On then '█' else ' ') xs ++ "|"

{-|
  Ex. 2: Implement ruleX, which turns a cell on if it has exactly one neighbour which was on in the previous step. The three arguments are the states of the left neighbour, current cell, and right neighbour respectively.

  [JUSTIFY]
  This rule only depends on of the cells neighbours therefore, we can ignore the cells state
  and just pattern match on the neighbours and output a correct result. That follows how xor works 
-}

ruleX :: CellState -> CellState -> CellState -> CellState
ruleX Off _ Off = Off
ruleX On _ Off  = On
ruleX Off _ On  = On
ruleX On _ On   = Off

{-|
  Ex. 3: Implement applyRule, which, given a rule and a line state, applies the rule to each cell in the state and returns the updated state.

  [JUSTIFY]
  applyRule takes a function that takes three inputs and a good way to do so is to map over 
  triples as opposed to trying to pattern match using (:). However for this to work we will 
  have to uncurry the function so that it can take triples. An uncurry function for triples 
  had to be created

  To create triples we use the input linestate, which is deconstructed into the constructor
  and the list of cellstates. We add extra offs either end of the list and then zip3 it with
  itself but without the first variable and again itself but without the first two elements

  zip3 was found on hoogle 
-}
uncurry3 :: (a -> b -> c -> d) -> ((a, b, c) -> d)
uncurry3 f (x,y,z) = f x y z 

applyRule :: (CellState -> CellState -> CellState -> CellState)
          -> LineState
          -> LineState
applyRule rule (LS xs) = LS $ map (uncurry3 rule) xs3
  where
    xs' = [Off] ++ xs ++ [Off]
    xs3 = zip3 xs' (tail xs') (tail $ tail xs')



{-|
  Ex. 4: Implement the loopedAt function, which takes a rule and a starting configuration, and returns the number of the iteration at which the automaton first revisits a state.

  [JUSTIFY]
  The idea here is to create all the stages of repeatedly applying a rule and put them in a
  list so that element 0 has a list only with the initial state, element 1 has a list with the 
  initial state and first application of the rule, element 2 has the initial state, first 
  application of the rule and second application of the rule and so on. This in theory is an
  infinite set but because Haskell is lazy we only generate all the stages up to the necessary 
  one. Then we find the first index where a linestate repeats

  This is achieved using the iterate function to repeatedly apply a rule to a linestate and 
  this generates a list of all the applications but we take the first x amount. That is put in
  a list comprehension, where x is a value from 1 onwards

  We map the repeated function over the list containing the rule applications from 0 to x in 
  order to create a list of bools and then find the index of the first true which is exactly 
  the number of applications that we need

  the repeated function was adjusted after being taken from https://stackoverflow.com/questions/26217871/haskell-function-that-tests-if-a-list-has-repeated-duplicate-elements
  iterate on line 117 was found https://stackoverflow.com/questions/7423123/how-to-call-the-same-function-n-times
  elemIndex and fromJust were found on Hoogle

-}

repeated :: Eq a => [a] -> Bool
repeated [] = False
repeated [_] = False
repeated (x:xs) = elem x xs || repeated xs 

loopedAt :: (CellState -> CellState -> CellState -> CellState)
  -> LineState
  -> Int
loopedAt rule line =
  let
    ls = [ take x $ iterate (applyRule rule) line | x <- [1 ..] ] 
  in
    fromJust $ elemIndex True (map repeated ls)

      


{-|
  Ex. 5: Implement allRules, which returns all 256 possible rules.

  [JUSTIFY]
  The idea behind this is that every rule has 8 outputs on or off and this results in there
  being 256 functions. All of these functions can be created by pattern matching the inputs to 
  an element in a list that contains a combination of the outputs. All of the combinations are
  generated in the complete which contains all 256 combinations which are created using list 
  comprehension with 8 "<- [Off, On]" generators. Specific values can be accessed using (!!)
  and this has to be done twice since we have a list of lists

  The final state of this code, the one that is below, was achieved with a bit of educated 
  guessing as originally the x next to func in the list and funcs in the where clause
  weren't there. The compiler didn't like that as the error "variable out of scope" was 
  underlined under the x's on the right hand side of the where clause. 

  *AFTER AN EMAIL FROM ALEX about "func" in the where clause*

  After the email this works because the function, used in the list and the where clause, has 
  the type:

      func :: Int -> CellState -> CellState -> CellState -> CellState

  This new int parameter allows for distinction between the rules and accessing elements. Also,
  it works because of partial application of variables. In the code, the int parameter is applied
  leaving a function with the necessary type to be output
-}

allRules :: [ CellState -> CellState -> CellState -> CellState ]
allRules = [ func x | x <- [0..255]]
  where
    complete = [[a,b,c,d,e,f,g,h] | a <- [Off, On], b <- [Off, On], c <- [Off, On], d <- [Off, On], e <- [Off, On], f <- [Off, On], g <- [Off, On], h <- [Off, On]]
    func x Off Off Off = (!!) (complete!!x) 0
    func x Off Off On  = (!!) (complete!!x) 1
    func x Off On Off  = (!!) (complete!!x) 2
    func x Off On On   = (!!) (complete!!x) 3
    func x On Off Off  = (!!) (complete!!x) 4
    func x On Off On   = (!!) (complete!!x) 5
    func x On On Off   = (!!) (complete!!x) 6
    func x On On On    = (!!) (complete!!x) 7


{-|
  Ex. 6: Implement initialState, which returns the initial configuration of Langton's Ant.
-}
initialState :: AntState
initialState = AS West (0,0) Set.empty

{-|
  Ex. 7: Define the functions leftOf and rightOf, which return the direction to the left and right of the given direction, respectively. Follow the additional constraints given in the specification, and answer the written question here.

  [JUSTIFY]
  leftOf is written using top level pattern matching, whilst rightOf is written using guards. I prefer
  the top level pattern matching for this since there are only 4 options. So writing all of them
  out is quick and simple. Also guards has extra syntax and otherwise which is unnecessary as
  there aren't other possible inputs
-}
leftOf :: Direction -> Direction
leftOf North = West
leftOf West = South
leftOf South = East
leftOf East = North

rightOf :: Direction -> Direction
rightOf x
  | x == North = East
  | x == East = South
  | x == South = West
  | otherwise = North


{-|
  Ex. 8: Implement the step function, which takes the ant state and applies the logic given in the specification to return the new ant state.

  [JUSTIFY]
  Here we are simply changing the ants state and we just have to cover all the possible options.
  We have to consider, which direction the ant will turn, what will be the new coordinates and 
  if the previous tile has to be added to removed from the set of black tiles.

  We start by deconstructing the input antstate, then checking if the tile the ant is standing
  on is black or white with an if statement. If the ant is on a black tile we output an antstate
  where the ant is facing to the right, has moved in that direction and removed the tile from
  the set. Otherwise the output antstate is with the ant facing left, moved in that direction
  and the previous tile is added to the set. The new direction uses case ... of and the functions
  in Ex. 7 to have the correct direction in the new antstate.

  These pattern matching methods were used because in this situation they seem the neatest, for
  that same reason a let .. in clause is used in order to have short identifiers for what needs
  to be output.
-}
step :: AntState -> AntState
step (AS direc (x,y) blackSet) = 
  let
    newSetAdd = Set.insert (x,y) blackSet
    newSetDel = Set.delete (x,y) blackSet
    l = leftOf direc
    r = rightOf direc
  in
    if Set.member (x,y) blackSet then
      case direc of
        North -> AS l (x-1,y) newSetDel
        West  -> AS l (x,y-1) newSetDel
        South -> AS l (x+1,y) newSetDel
        East  -> AS l (x,y+1) newSetDel
    else
      case direc of
        North -> AS r (x+1,y) newSetAdd
        East  -> AS r (x,y-1) newSetAdd
        South -> AS r (x-1,y) newSetAdd
        West  -> AS r (x,y+1) newSetAdd


{-|
  Ex. 9: Visualise the behaviour of Langton's Ant by implementing the "animation" function. It takes a number (the step) and you must return a picture that represents the state of the automaton at that step.

  There are no tests for this. You can use `stack run` to test your implementation.

  [JUSTIFY]
  A good place to start is that every function call of animation creates a new image as opposed
  to painting on top of the previous image, which helps with understanding of how to create the
  animation. Secondly the animation is split in to 2, the background of black and white tiles
  and the ant.

  Firstly the ant is created by printing its image at a coordinate (newPosX, newPosy) and 
  rotating it so that it faces the correct direction. An "antstate" is used to do so and it is
  selected from a list by "slowerT", which is t integer divided by some number, so that the 
  animation doesn't run to quickly. Also it is integer divided so that the code doesn't try 
  to access a fractional index. The list of states is generated using "iterate" where "step" is
  applied to the initial state n times. When we have an "antstate" picked, the orientation of 
  the ant image can be chosen using "rotate" and "direcRotation". DirecRotation converts a 
  direction of the ant to an integer, that is used as the degrees the image should be
  rotated. Finally the coordinates of where the ant should be on the screen is calculated
  using the "size" function written in "hatch.Layout.hs", that outputs the pair (width, height).
  The width and height are multiplied by the x and y coordinates respectively and then both 
  values are integer divided by 10 since the images are rendered at a scale of 0.1.  

  Secondly the black tiles are generated by creating a image for every tile in the set of 
  black tiles and the superimposing them on top of each other to create one image that can be
  rendered. This is done by changing the set to a list using toList, since sets are foldable, 
  and then mapping the "offset" function over every element. The position of every black tile
  is calculated using the same method that was used for the ant, under the function names 
  "rectanglePosX" and "rectanglePosY". Meanwhile the final parameter of "offset" is the function
  "rect" which draw a rectangle with a given width and a height. Those are calculated by
  taking the size of the ant image and again integer dividing by 10. But the vertical height
  is increased by 1 because in the rendering there occasionally is a break line. After that, 
  "superimposeAll" is used on the list to create one image that can be
  used.

  Finally the animation imposes the ant on to the black tiles using <@> and that creates our 
  final rendering
-}
animation :: Int -> Image
animation t =     offset 0 0 (drawRectangles (size biggerAnt) blackSet)
              <@> offset newPosX newPosY (rotate (direcRotation direc) biggerAnt)
  where
    slowerT factor = t `div` factor
    (AS direc (x,y) blackSet) = iterate step initialState !! slowerT 5
    biggerAnt  = scale 2 ant
    newPosX = (x * fst (size biggerAnt)) `div` 10
    newPosY = (y * snd (size biggerAnt)) `div` 10
    
    
direcRotation :: Direction -> Int
direcRotation direction = case direction of
  North -> 0
  East -> 90
  South -> 180
  West -> 270 


drawRectangles :: Size -> Set (Int, Int) -> Image
drawRectangles antSize blackSet
  = superimposeAll $ map (\z -> offset (rectanglePosX z) (rectanglePosY z) (rect sizeX (sizeY+1))) (Set.toList blackSet)
    where 
      rectanglePosX z = (fst z * fst antSize) `div` 10
      rectanglePosY z = (snd z * snd antSize) `div` 10
      sizeX = fst antSize `div` 10
      sizeY = snd antSize `div` 10