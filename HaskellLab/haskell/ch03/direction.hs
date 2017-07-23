-- return the direction of a path
-- foundation of the convellHull problem
import Data.List
data Direction = TurnLeft
               | TurnRight
               | Straight
               deriving (Show)

data Point = Point Double Double
             deriving (Show)
 
goLeft :: (Point) -> (Point) -> (Point) -> Bool
goLeft (Point xa ya) (Point xb yb) (Point xc yc)
                    = (yb - ya) * xc + (ya * xb - xa * yb) < (xb - xa) * yc
       
goRight :: (Point) -> (Point) -> (Point) -> Bool
goRight (Point xa ya) (Point xb yb) (Point xc yc)
                    = (yb - ya) * xc + (ya * xb - xa * yb) > (xb - xa) * yc
       
goStraight :: (Point) -> (Point) -> (Point) -> Bool
goStraight (Point xa ya) (Point xb yb) (Point xc yc) 
                    = ((yb - ya) * xc + (ya * xb - xa * yb) == (xb - xa) * yc)
      
getTurn :: (Point) -> (Point) -> (Point) -> Direction
getTurn (Point xa ya) (Point xb yb) (Point xc yc) 
       | goLeft (Point xa ya) (Point xb yb) (Point xc yc)  = TurnLeft
       | goRight (Point xa ya) (Point xb yb) (Point xc yc) = TurnRight
       | otherwise                                         = Straight
       
getTurnFromList :: [Point] -> Direction
getTurnFromList ps = getTurn ((!!) ps 0) ((!!) ps 1) ((!!) ps 2) 
       
getDirection :: [Point] -> [Direction]
getDirection ps 
   | length ps >= 3 = (replicate 1 (getTurnFromList (take 3 ps))) ++ getDirection (drop 1 ps)
   | otherwise      = []
   
