-- convert char to corresponding ascii 
import Data.Char
char2num :: Char -> Int
char2num c = if val >= 0 && val <= 9
             then val
             else 0
             where val = ord c - ord '0'
