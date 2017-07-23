import Data.Char (toUpper)

isGreen :: IO Bool
isGreen = 
  do putStrLn "Is green your favorite color?"
     inpStr <- getLine
     return ((toUpper . head $ inpStr) == 'Y')
     
returnTest :: IO ()
returnTest =
  do one <- return 1
     let two = 2
     putStrLn $ show (one + two)