module RecursiveContents (getRecursiveContents
                         , printContents) where

import Control.Monad (forM)
import System.Directory (doesDirectoryExist, getDirectoryContents)
import System.FilePath ((</>))

getRecursiveContents :: FilePath -> IO [FilePath]
getRecursiveContents topdir = do
  names <- getDirectoryContents topdir
  let properNames = filter (`notElem` [".", ".."]) names -- filter out the directory itself and its parent
  paths <- forM properNames $ \name -> do
    let path = topdir </> name
    isDirectory <- doesDirectoryExist path
    if isDirectory
      then getRecursiveContents path
      else return [path]
  return (concat paths)
  
printContents :: IO [FilePath] -> IO ()
printContents paths = (paths >>= \a -> return (concat (map (++ "\n") a))) >>= putStrLn

