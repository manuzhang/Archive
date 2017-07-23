import Control.Monad (filterM)
import System.Directory (Permissions(..), getModificationTime, getPermissions)
import System.Time (ClockTime(..))
import System.FilePath (takeExtension)
import Control.Exception (bracket, handle, SomeException(..))
import System.IO (IOMode(..), hClose, hFileSize, openFile)

import RecursiveContents (getRecursiveContents, printContents)

type Predicate = FilePath          -- path to directory entry
               -> Permissions      -- permissions
               -> Maybe Integer    -- file size (Nothing if not file)
               -> ClockTime        -- last modified
               -> Bool             

type InfoP a = FilePath
               -> Permissions 
               -> Maybe Integer
               -> ClockTime
               -> a
               
simpleFileSize :: FilePath -> IO Integer
simpleFileSize path = do
  h <- openFile path ReadMode
  size <- hFileSize h
  hClose h
  return size

saferFileSize :: FilePath -> IO (Maybe Integer)
saferFileSize path = handle (\(SomeException _) -> return Nothing) $ do
  h <- openFile path ReadMode
  size <- hFileSize h -- if an exception occurs the handle cannot be released
                      -- which will crash the process if there are sufficient
                      -- files to process
  hClose h
  return (Just size)

getFileSize :: FilePath -> IO (Maybe Integer)
getFileSize path = handle (\(SomeException _) -> return Nothing) $
  bracket (openFile path ReadMode) hClose $ \h -> do
    size <- hFileSize h
    return (Just size)

betterFind :: Predicate -> FilePath -> IO [FilePath]
betterFind p path = getRecursiveContents path >>= filterM check
    where check name = do
            perms    <- getPermissions name
            size     <- getFileSize name
            modified <- getModificationTime name
            return (p name perms size modified)
            
myTest path _ (Just size) _ =
  takeExtension path == ".c" && size > 131072
myTest _    _ _           _ = 
  False
  
pathP :: InfoP FilePath
pathP path _ _ _ = path

sizeP :: InfoP Integer
sizeP _ _ (Just size) _ = size
sizeP _ _ Nothing     _ = -1

equalP :: (Eq a) => InfoP a -> a -> InfoP Bool
equalP f k = \w x y z -> f w x y z == k

{-
EqualP' :: (Eq a) => 
           (t -> t1 -> t2 -> t3 -> a) -> a -> t -> t1 -> t2 -> t3 -> Bool
-}
equalP' :: (Eq a) => InfoP a -> a -> InfoP Bool
equalP' f k w x y z = f w x y z == k

liftP :: (a -> b -> c) -> InfoP a -> b -> InfoP c 
liftP q f k = \w x y z -> f w x y z `q` k

greaterP :: (Ord a) => InfoP a -> a -> InfoP Bool
greaterP = liftP (>)

lesserP :: (Ord a) => InfoP a -> a -> InfoP Bool
lesserP  = liftP (<)

liftP2 :: (a -> b -> c) -> InfoP a -> InfoP b -> InfoP c
liftP2 q f g w x y z = f w x y z `q` g w x y z
andP = liftP2 (&&)
orP  = liftP2 (||)

-- Combinators
constP :: a -> InfoP a
constP k _ _ _ _ = k
liftP' q f k w x y z = f w x y z `q` constP k w x y z

liftPath :: (FilePath -> a) -> InfoP a 
liftPath f w _ _ _ = f w

myTest2 = (liftPath takeExtension `equalP` ".c") `andP`
          (sizeP `greaterP` 131072)

(==?) = equalP
(&&?) = andP
(>?)  = greaterP

myTest3 = (liftPath takeExtension ==? ".c") &&? (sizeP >? 131072)

infix  4 ==?
infixr 3 &&?
infix  4 >?
myTest4 =  liftPath takeExtension ==? ".c" &&? sizeP >? 131072