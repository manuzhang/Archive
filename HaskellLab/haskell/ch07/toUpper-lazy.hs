import Data.Char (toUpper)
main = do
       inpStr <- readFile "big.txt"
       writeFile "bigOut.txt" (map toUpper inpStr)