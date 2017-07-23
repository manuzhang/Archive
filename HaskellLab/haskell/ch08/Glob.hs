module Glob (namesMatching) where

import System.Directory (doesDirectoryExist, doesFileExist,
                         getCurrentDirectory, getDirectoryContents)
import System.FilePath (dropTrailingPathSeparator, splitFileName, (</>))
import Control.Exception (handle)
import Control.Monad (forM)
import GlobRegex (matchesGlob)

isPattern :: String -> Bool
isPattern = any (`elem` "[*?")

-- splitFileName "foo/bar"
-- ("foo/", "bar")
-- splitFileName "foo/"
-- ("foo/", "")
-- forM :: (Monad m) => [a] -> (a -> m b) -> m [b]
namesMatching pat
 | not (isPattern pat) = do
   exists <- doesNameExist pat
   return (if exists then [pat] else [])
 | otherwise           = do
   case splitFileName pat of
     ("", baseName)      -> do
       curDir <- getCurrentDirectory
       listMatches curDir baseName
     (dirName, baseName) -> do
       dirs <- if isPattern dirName
               then namesMatching (dropTrailingPathSeparator dirName)
               else return [dirName]
       let listDir = if isPattern baseName
                     then listMatches
                     else listPlain
       pathNames <- forM dirs $ \dir -> do
                        baseNames <- listDir dir baseName
                        return (map (dir </>) baseNames)
       return (concat pathNames)
       
doesNameExist :: FilePath -> IO Bool
doesNameExist name = do
  fileExists <- doesFileExist name
  if fileExists
  then return True
  else doesDirectoryExist name

-- handle :: (Exception e) => (e -> IO a) -> IO a -> IO a
-- const :: a -> b -> a
-- return [] :: (Monad m) => m [a]
-- const (return []) :: (Monad m) => b -> m [a]
-- we use const to write an exception handler that ignores the exception 
-- it is passed. 
-- instead, it cause our code to return an empty list if we catch an exception
listMatches :: FilePath -> String -> IO [String]
listMatches dirName pat = do
  dirName' <- if null dirName
              then getCurrentDirectory   
              else return dirName
  handle (const (return [])) $ do                  
    names  <- getDirectoryContents dirName'
    let names' = if isHidden pat
                 then filter isHidden names
                 else filter (not . isHidden) names     
    return (filter (`matchesGlob` pat) names')

listPlain :: FilePath -> String -> IO [String]
listPlain dirName baseName = do
  exists <- if null baseName
            then dpesDirectoryExist dirName
            else doesNameExist (dirName </> baseName)
  return (if exists then [baseName] else [])
isHidden ('.':_) = True
isHidden _       = False