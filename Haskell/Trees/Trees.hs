module Trees where
-- Complete this according to the lab sheet.
-- Make sure to complete ALL the exercises.


-- Import UTree qualified.
import Data.UTree (UTree)
import qualified Data.UTree as UTree
import Data.Foldable

-- Define nubSort in terms of functions over the UTree data type. (You may need to import Data.Foldable.)
nubSort :: Ord a => [a] -> [a]
nubSort l = toList sL
    where 
        sL = UTree.fromList l


-- Define a type synonym for UTree called Set.

type Set = UTree