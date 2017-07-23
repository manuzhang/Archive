getInt :: IO Int
getInt = do
  input <- getLine
  return (read input :: Int)

mySum :: Int -> IO ()
mySum x = do
  int <- getInt
  if int /= 0 
   then mySum (x + int)
   else print x
        
mySum2 :: Int -> Int -> IO ()
mySum2 x num = do
  if num > 0
     then do 
          int <- getInt
          mySum2 (int + x) (num - 1)
     else print x

main :: IO ()
main = do 
  putStrLn "how many Integers do you want to input?"
  num <- getInt
  mySum2 0 num