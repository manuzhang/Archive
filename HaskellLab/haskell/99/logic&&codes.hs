import Control.Monad(replicateM)
import Data.List(sort, sortBy, insertBy)
import Data.Ord(comparing)

not' :: Bool -> Bool
not' True  = False
not' False = True

and', or', nor', nand', xor', impl', equ' :: Bool -> Bool -> Bool

and'  = (&&)
or'   = (||)
equ'  = (==)
nor'  a b = not' $ or' a b
nand' a b = not' $ and' a b
xor'  a b = and' (nand' a b) (or' a b)
impl' a b = or' (not' a) b

space :: Bool -> String
space True = "  "
space False = " "

-- truth tables for logical expressions
table :: (Bool -> Bool -> Bool) -> IO ()
table f = mapM_ putStrLn [show a ++ space a ++ show b ++ space b ++ show (f a b) 
           | a <- [True, False], b <- [True, False]]

                
table2 :: (Bool -> Bool -> Bool) -> String
table2 f = printBinary f [True, False]

printBinary :: (Show a, Show b) => (a -> a -> b) -> [a] -> String
printBinary f domain = concatMap (++ "\n") [printBinaryInstance f x y | x <- domain, y <- domain]

printBinaryInstance :: (Show a, Show b) => (a -> a -> b) -> a -> a -> String
printBinaryInstance f x y = show x ++ " " ++ show y ++ " " ++ show (f x y)

infixl 7 `equ'`
infixl 4 `and'`
infixl 3 `or'` 

-- infixl 6 `or'`
-- try False `and'` True `or'` True respectively 
-- for two fixities of `or'`


 
sublists :: Int -> [[Bool]]
sublists 0 = [[]]
sublists n = [y:sublist | y <- [True, False], sublist <- sublists (n-1)] 

tablen :: Int -> ([Bool] -> Bool) -> IO ()
tablen n f = putStrLn $ concatMap (\x -> concatMap (\y -> show y ++ space y) x ++ show (f x) ++ "\n") $ sublists n


tablen2 :: Int -> ([Bool] -> Bool) -> IO ()
tablen2 n f = mapM_ putStrLn [toStr a ++ " => " ++ show (f a) | a <- args n]
      where args n = replicateM n [True, False]
            toStr  = unwords . map (\x -> show x ++ space x)
            space True = "  "
            space False = " "
            
-- gary codes 
-- n = 1: C(1) = ['0','1']
-- n = 2: C(2) = ['00','01','11','10']
-- n = 3: C(3) = ['000','001',011','010','110','111','101','100']
gray :: Int -> [String]
gray n = helper n "0" ++ helper n "1"
      where
             helper 1 c  = [c] 
             helper n c  = map (\x -> c ++ x) $ last_gray c
             last_gray "0" = gray (n-1) 
             last_gray "1" = reverse . gray $ (n-1)

gray2 0 = [""]
gray2 n = let xs = gray2 (n-1) in map ('0':) xs ++ map ('1':) (reverse xs)

gray3 0 = [""]
gray3 n = ['0' : x | x <- prev ] ++ ['1' : x | x <- reverse prev]
    where prev = gray3 (n-1)
          
-- huffman codes          
huffman :: [(String, Int)] -> [(String, String)]
huffman xs = sortBy (comparing fst) $ down $ up $ map (\x -> ("", x)) xs
         
up :: [(String, (String, Int))] -> [[(String, (String, Int))]]
up xs 
  | length xs == 1 = []
  | otherwise      = ((cm,(sm,frm)):(cbm,(sbm,frbm)):others) : up new
         where new              = ("", (sm ++ sbm, frm + frbm)):others
               (cm,(sm,frm))    = ("0", snd $ head $ sorted)
               (cbm,(sbm,frbm)) = ("1", snd $ head $ tail $ sorted)
               others           = tail $ tail $ sorted 
               sorted           = sortBy (comparing $ snd . snd) xs
               
down :: [[(String, (String, Int))]] -> [(String, String)]
down xs = map (\(code,(s,fr)) -> (s,code)) $ foldl f [] xs
        where f []  x = x
              f acc x = map ff acc
                      where ff (code,(s,fr)) = (((foldl fff "" x)++code),(s,fr))
                               where fff acc' (code',(s',_)) = acc' ++ ffff s'                    
                                       where ffff [] = ""
                                             ffff (y:ys) = if [y] == s
                                                           then code'
                                                           else ffff ys     
                                                                
data HTree a = Leaf a | Branch (HTree a) (HTree a)
               deriving Show
huffman2 :: (Ord a, Ord w, Num w) => [(a,w)] -> [(a,[Char])]
huffman2 freq = sortBy (comparing fst) $ serialize 
               $ htree $ sortBy (comparing fst) $ [(w,Leaf x) | (x,w) <-freq]
          where  htree [(_,t)] = t
                 htree ((w1,t1):(w2,t2):wts) =
                   htree $ insertBy (comparing fst) (w1+w2, Branch t1 t2) wts
                 serialize (Branch l r) =
                   [(x,'0':code) | (x,code) <- serialize l] ++
                   [(x,'1':code) | (x,code) <- serialize r]
                 serialize (Leaf x) = [(x,"")]
