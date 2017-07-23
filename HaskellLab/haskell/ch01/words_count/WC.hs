-- count number of lines, number of words, number of words per line

numOfLines = interact wordCount
  where wordCount input = show (length (lines input)) ++ "\n" 
numOfWords = interact wordCount
  where wordCount input = show (length (words input)) ++ "\n" 
numOfChars = interact wordCount
  where wordCount input = show (length (      input)) ++ "\n"



