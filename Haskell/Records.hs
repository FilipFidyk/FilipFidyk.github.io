module Records where

--------------------------------------------------------------------------------
-- For this lab, the imports are defined for you.
-- Notice the use of epxlicit import lists.

import Data.Time.Calendar (Day, fromGregorian)
import Data.List (intercalate, intersperse)

import Data.Set (Set)
import qualified Data.Set as Set

import Data.Function (on)

--------------------------------------------------------------------------------
-- Define your data types and functions here.

data Book = Book {
        title          :: String
    ,   authorInitials :: [Char]
    ,   authorSurname  :: String
    ,   numPages       :: Int
    ,   tags           :: Set String
    ,   releaseDate    :: Day
    }   deriving Show

programmingInHaskell :: Book
programmingInHaskell = Book 
    {
        title           = "Programming in Haskell"
    ,   authorInitials  = ['G']
    ,   authorSurname   = "Hutton"
    ,   numPages        = 304
    ,   tags            = Set.fromList ["Haskell","Learning"]
    ,   releaseDate     = fromGregorian 2016 1 1
    }

authorFullName :: Book -> String
authorFullName b = concat initials ++ authorSurname b
    where
        initials = map (: ". ") (authorInitials b)

prettyPrintBook :: Book -> String
prettyPrintBook b = "\"" ++ title b ++ "\" by " ++ authorFullName b ++ ", which has " ++ show (numPages b) ++ " pages and was released on " ++ show (releaseDate b) 

hasTag :: String -> Book -> Bool
hasTag tag b = Set.member tag (tags b)

addTag :: String -> Book -> Book
addTag tag b = b { tags = Set.insert tag (tags b) }

sameAuthor :: Book -> Book -> Bool
sameAuthor = (==) `on` authorFullName

