-- my solution to ConvexHull.hs
-- a more splendid solution can be found at 
-- <a href="typedef.me/?page=3">here</a>

import Data.List

data Direction = TurnLeft
               | TurnRight
               | Straight
               deriving (Show, Eq)

data Point = Point Double Double
             deriving (Show, Eq)


-- PolarAngle extends Point by adding width and height relative to an origin point
data PolarAngle = PolarAngle Double Double Double Double
                  deriving (Show, Eq)
                  

-- get direction from three points 
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
           
getDirection :: [Point] -> [Direction]
getDirection ps 
   | length ps >= 3 = (replicate 1 (getTurnFromList (take 3 ps))) ++ getDirection (drop 1 ps)
   | otherwise      = []
   
-- find the index of point with lowest y-coordinate
getX :: Point -> Double
getX (Point x y) = x

getY :: Point -> Double
getY (Point x y) = y
      
compareByY :: Point -> Point -> Ordering
compareByY (Point xa ya) (Point xb yb) 
           | ya <  yb  = LT
           | ya  > yb  = GT
           | ya == yb  = EQ

findLowestY :: [Point] -> Point
findLowestY ps = head (sortBy compareByY ps)


-- to get around the return type of elemIndex (Maybe Int)
elemIndex2 :: Int -> Point -> [Point] -> Int
elemIndex2 i (Point px py) ps 
           | null ps            = -1   
           | otherwise          = if (px == (getX (head ps)) && py == (getY (head ps)))
                                  then i
                                  else elemIndex2 (i + 1) (Point px py) (tail ps) 

findLowestYIndex :: [Point] -> Int
findLowestYIndex ps = elemIndex2 0 (findLowestY ps) ps

-- swap points indicated by two indices
swapPoints :: Int -> Int -> [Point] -> [Point]
swapPoints p1 p2 ps              
          | p1 == p2  = ps
          | p1  > p2  = swapPoints p2 p1 ps
          | otherwise = (take p1 ps) ++ (take 1 (drop p2 ps)) ++ (take (p2 - p1 - 1) (drop (p1 + 1) ps))
                                     ++ (take 1 (drop p1 ps)) ++ (drop (p2 + 1) ps)
                                     
-- conversion between Point and PolarAngle
pointToPolarAngle :: Point -> Point -> PolarAngle
pointToPolarAngle (Point oa ob) (Point pa pb) = (PolarAngle pa pb (pa - oa) (pb - ob)) 
                                     
polarAngleToPoint :: PolarAngle -> Point
polarAngleToPoint (PolarAngle pa pb w h) = (Point pa pb)

pointsToPolarAngles :: Point -> [Point] -> [PolarAngle]
pointsToPolarAngles p ps 
          | null ps     = []
          | otherwise   = (replicate 1 (pointToPolarAngle p (head ps))) ++ 
                          (pointsToPolarAngles p (tail ps))

polarAnglesToPoints :: [PolarAngle] -> [Point]
polarAnglesToPoints pas
          | null pas    = []
          | otherwise   = (replicate 1 (polarAngleToPoint (head pas))) ++ (polarAnglesToPoints (tail pas)) 
                                     
-- sort by polar angle provided with origin                                     
compareByPolarAngle :: PolarAngle -> PolarAngle -> Ordering 
compareByPolarAngle (PolarAngle xa ya wa ha) (PolarAngle xb yb wb hb)  
          | (hb * wa - ha * wb) > 0 = LT
          | (hb * wa - ha * wb) < 0 = GT
          | otherwise               = EQ
          
sortByPolarAngle :: [PolarAngle] -> [PolarAngle]
sortByPolarAngle pas = sortBy compareByPolarAngle pas 

-- ConvexHull using Graham Scan
getNext :: Int -> Int -> [Point] -> [Point]
getNext m i ps 
      | i == (length ps) = take (m + 1) ps
      | otherwise        = let d = getTurn ((!!) ps (m - 1)) ((!!) ps m) ((!!) ps i)
                            in if d == TurnLeft
                               then getNext (m + 1) (i + 1) (swapPoints (m + 1) i ps)
                               else if m == 2
                                    then getNext m (i + 1) (swapPoints m i ps)
                                    else getNext (m - 1) i ps

getSortedPoints :: [Point] -> [Point]
getSortedPoints ps = let ps1 = swapPoints 0 (findLowestYIndex ps) ps
                     in  polarAnglesToPoints (sortByPolarAngle (pointsToPolarAngles (head ps1) ps1))
                               
getConvexHull :: [Point] -> [Point]
getConvexHull ps =  let ps1 = swapPoints 0 (findLowestYIndex ps) ps
                    in let ps2 = polarAnglesToPoints (sortByPolarAngle (pointsToPolarAngles (head ps1) ps1))
                           in getNext 1 2 ps2
