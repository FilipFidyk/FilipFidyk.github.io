module Recursion where
  
import Prelude hiding (or, zip, zipWith)

--------------------------------------------------------------------------------

-- | Takes a list of boolean values and returns True if any of them is True.
or :: [Bool] -> Bool
or = foldr (||) False
 
-- | or implemented using implicit recursion (the `any` function)
or' :: [Bool] -> Bool
or'  = any (\x -> x == True)  


--------------------------------------------------------------------------------

-- | Takes a list of Maybe values and collects together all the Just ones.
catMaybes :: [Maybe a] -> [a]
catMaybes [] = []
catMaybes (x:xs) = case x of
                     Nothing -> catMaybes xs
                     Just n -> n : catMaybes xs

-- | catMaybes implemented using a fold.
catMaybes' :: [Maybe a] -> [a]
catMaybes' xs= foldr f [] xs
    where
        f :: (Maybe a) -> [a] -> [a]
        f Nothing xs = xs
        f (Just x) xs = x:xs 


--------------------------------------------------------------------------------

-- | Given a list of pairs, returns the lower element for each pair.
minima :: Ord a => [(a,a)] -> [a]
minima = map (uncurry min)


--------------------------------------------------------------------------------

-- | Keeps only the pairs of the input whose values sum to 7.
sumToSeven :: (Num a, Eq a) => [(a,a)] -> [(a,a)]
sumToSeven = filter (\(x, y) -> x + y == 7)


--------------------------------------------------------------------------------

-- | Pairs up the elements of two lists elementwise, until either list runs out.
zip :: [a] -> [b] -> [(a,b)]
zip _ [] = []
zip [] _ = []
zip (x:xs) (y:ys) = (x, y): zip xs ys


-- | Pairs up the elements of two lists elementwise, until either list runs out.
zipWith :: (a -> b -> c) -> [a] -> [b] -> [c]
zipWith _ _ [] = []
zipWith _ [] _ = []
zipWith f (x:xs) (y:ys) = f x y: zipWith f xs ys