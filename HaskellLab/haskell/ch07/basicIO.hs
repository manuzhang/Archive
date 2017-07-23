main = do
       putStrLn "Greetings! What is your name?"
       inpStr <- getLine
       putStrLn $ "Welcome to Haskell, " ++ inpStr ++ "!"
       
nodo = 
  putStrLn "Greetings! What is your name?" >>
  getLine >>=
  (\inpStr -> putStrLn $ "Welcome to Haskell, " ++ inpStr ++ "!")