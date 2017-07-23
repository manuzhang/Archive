import Control.Exception (SomeException(..), handle, bracket)
import Control.Monad (mapM, forM, liftM)
import System.Directory (Permissions(..), getPermissions, getDirectoryContents, getModificationTime)
import System.FilePath ((</>))
import System.IO (IOMode(..), hClose, hFileSize, openFile)
import System.Time (ClockTime(..))

data Info = Info 
    { infoPath    :: FilePath
    , infoPerms   :: Maybe Permissions
    , infoSize    :: Maybe Integer
    , infoModTime :: Maybe ClockTime
    } deriving (Eq, Ord, Show)

maybeIO :: IO a -> IO (Maybe a)
maybeIO act = handle (\(SomeException _) -> return Nothing) (Just `liftM` act)

getInfo :: FilePath -> IO Info
getInfo path = do
  perms <- maybeIO (getPermissions path)
  size <- maybeIO (bracket (openFile path ReadMode) hClose hFileSize)
  modified <- maybeIO (getModificationTime path)
  return (Info path perms size modified)
  
traverse :: ([Info] -> [Info]) -> FilePath -> IO [Info]
traverse order path = do
  names <- getUsefulContents path
  contents <- mapM getInfo (path : map (path </>) names)
  liftM concat $ forM (order contents) $ \info -> do
    if isDirectory info && infoPath info /= path
       then traverse order (infoPath info)
       else return [info]
            
getUsefulContents :: FilePath -> IO [String]
getUsefulContents path = do
  names <- getDirectoryContents path
  return (filter (`notElem` [".", ".."]) names)
          
isDirectory :: Info -> Bool
isDirectory = maybe False searchable . infoPerms


  