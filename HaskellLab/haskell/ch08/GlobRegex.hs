module GlobRegex
       (
         globToRegex,
         matchesGlob,
         matchesGlobCase
       ) where

import Text.Regex.Posix ((=~))
import Data.Char(toUpper, toLower, isAlpha)

globToRegex :: String -> String
globToRegex cs = '^' : globToRegex' cs ++ "$"

globToRegex' :: String -> String
globToRegex' ""             = ""
globToRegex' ('*':cs)       = ".*" ++ globToRegex' cs
globToRegex' ('?':cs)       = '.' : globToRegex' cs
globToRegex' ('[':'!':c:cs) = "[^" ++ c : charClass cs
globToRegex' ('[':c:cs)     = '['  :  c : charClass cs
globToRegex' ('[':_)        = error "unterminated character class"
globToRegex' (c:cs)         = escape c ++ globToRegex' cs

escape :: Char -> String
escape c | c `elem` regexChars = '\\' : [c]
         | otherwise           = [c]
              where regexChars = "\\+()^$.{}]|"
                    
charClass :: String -> String
charClass (']':cs) = ']' : globToRegex' cs
charClass (c:cs)   = c : charClass cs
charClass []       = error "unterminated character class"

-- I'd better use the map function
toUpperString :: String -> String
toUpperString (x:xs) = (toUpper x) : (toUpperString xs)
toUpperString _      = []

matchesGlob :: FilePath -> String -> Bool
name `matchesGlob` pat = name =~ globToRegex pat

-- whether case sensitive
matchesGlobCase :: FilePath -> String -> Bool -> Bool
matchesGlobCase name pat tof 
  | tof == False = (toUpperString name) `matchesGlob` (toUpperString pat)
  | otherwise    = name `matchesGlob` pat

-- a splendid way to have control over case sensitivity
-- @http://book.realworldhaskell.org/read/efficient-file-processing-regular-expressions-and-file-name-matching.html
escape2 :: Char -> String
escape2 c
  | c `elem` regexChars = '\\' : [c]
  | isAlpha c           = '[' : [toUpper c] ++ "|" ++ [toLower c] ++ "]"
  | otherwise           = [c]
  where regexChars = "\\+()^$.{}]|"

