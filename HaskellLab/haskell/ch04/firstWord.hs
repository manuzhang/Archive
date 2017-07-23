import System.Environment (getArgs)

interactWith function inputFile outputFile = do
   input <- readFile inputFile
   writeFile outputFile (function input)
   
main = mainWith myFunction
     where mainWith function = do
              args <- getArgs
              case args of
                [input, output] -> interactWith function input output
                _ -> putStrLn "error: exactly two arguments needed"
           myFunction = getFirstWords2

-- split lines at '\r', '\n' or '\r\n'
splitLines :: String -> [String]           
splitLines [] = []
splitLines cs =
    let (pre, suf) = break isLineTerminator cs
    in pre : case suf of
              ('\r':'\n':rest) -> splitLines rest
              ('\r':rest)      -> splitLines rest
              ('\n':rest)      -> splitLines rest
              _                -> []

isLineTerminator c = c == '\r' || c == '\n'

-- find first word of each line
firstWord :: String -> String
firstWord xs 
      | null xs   = ""
      | otherwise = head (words xs)

firstWords :: [String] -> String
firstWords xss
      | null xss  = ""
      | otherwise = firstWord (head xss) ++ "\n" ++ firstWords (tail xss)

getFirstWords :: String -> String
getFirstWords cs = firstWords (splitLines cs)

-- another implementation
firstWord2 :: String -> String
firstWord2 []    = []
firstWord2 input = let ws = words input
                   in case ws of 
                        [] -> []
                        xs -> head xs
getFirstWords2 :: String -> String
getFirstWords2 cs = unlines (map firstWord2 (splitLines cs))
