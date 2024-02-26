module Data.UTree ( UTree(), empty, singleton, insert, fromList) where

-- Complete this according to the lab sheet.
-- Make sure to complete ALL the exercises.

-- NOTE: The error messages may not be too easy to read, due to the constructors not being exposed to the tests. In error messages, lists of values represent UTrees constructed by passing that list to the fromList function.

--------------------------------------------------------------------------------
-- Data Type
{-
No duplicates and tree will be in order
-}
-- A unique tree.
data UTree a = Empty | Node a (UTree a) (UTree a)
  deriving (Show, Eq)

-- Smart constructors

-- An empty tree.
empty :: Ord a => UTree a
empty = Empty 


-- A tree with a single value.
singleton :: Ord a => a -> UTree a
singleton x = Node x Empty Empty

--------------------------------------------------------------------------------
-- Useful functions

-- Insert a value into an existing tree.
insert :: Ord a => a -> UTree a -> UTree a
insert x Empty = Node x Empty Empty
insert x (Node v l r) 
  | x > v = Node v l (insert x r) 
  | x < v = Node v (insert x l) r
  |otherwise = Node v l r

-- Turn a list of values into a UTree.
fromList :: Ord a => [a] -> UTree a
fromList = foldr insert Empty


-- Define a Foldable instance for UTree.
instance Foldable UTree where
  foldr :: (a -> b -> b) -> b -> UTree a -> b
  foldr _ z Empty = z
  foldr fn z (Node x l r) = foldr fn z' l
    where
      z' = fn x z''
      z'' = foldr fn z r

