main = do
  putStrLn "please input two lines"
  inpStr1 <- getLine
  inpStr2 <- getLine
  putStrLn $ reverse inpStr2
  putStrLn $ reverse inpStr1