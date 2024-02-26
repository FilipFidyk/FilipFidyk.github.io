module Definitions where

{-
NOTE FROM ALEX:

The comments in lab sheets are just helpers to remind you what you have done.

You should still download and read the lab sheet - it has other things
that you need to know, and more exercises to complete!

Have fun :)
-}

--------------------------------------------------------------------------------

-- Implement `not` using the four different syntaxes we have seen.

notIf, notCase, notTLP, notGuard :: Bool -> Bool

-- if...then...else...
notIf x = if x == True 
    then False
    else True

-- case...of...
notCase x = case x of 
    True -> False
    False -> True 

-- top level pattern matching
notTLP True = False
notTLP False = True

-- guards
notGuard x 
    | x == True = False
    | otherwise = True

{-I think the most appropriate way to implement the not function is TLP
and the least appropriate is either if.then.else or guards as they have
functionality with else or otherwise that can handle multiple different
inputs in one statement, which is unnecessary for flipping a bool -}

--------------------------------------------------------------------------------
-- Implement the factorial function.

fac :: Integer -> Integer
fac 0 = 1
fac x = x * fac (x - 1)

--------------------------------------------------------------------------------

-- Here is a weird function called interesting:
interesting :: (p -> q) -> p -> q
interesting a b = a b

-- Write interesting as a lambda function.
interestingLambda :: (p -> q) -> p -> q
interestingLambda = \a -> \b -> a b

-- Write interesting as an operator called £.
infixr 0 £
(£) :: (p -> q) -> p -> q
(£) a b = a b


--------------------------------------------------------------------------------

-- Here is a function called foo:
foo :: Int -> Int -> Int
foo x y = if x > y then x - y else y - x

-- Write foo as a lambda function.
fooLambda :: Int -> Int -> Int
fooLambda = \x -> \y -> if x > y 
    then x - y 
    else y - x

-- Golf the definition of foo.
fooGolfed :: Int -> Int -> Int
fooGolfed = error "Not implemented"