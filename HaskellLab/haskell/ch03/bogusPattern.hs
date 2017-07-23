data Fruit = Apple | Orange
             deriving (Show)
apple = "apple"
orange = "orange"

-- whatever the argument it will always return Orange
notRealFruit :: String -> Fruit
notRealFruit f = case f of
                  orange -> Orange  -- orange is a local pattern variable
                  apple -> Apple    -- apple does not refer to the function 
                                    -- so it is the same as in
                                    -- case f of
                                    --      a -> Orange
                                    --      b -> Apple  
-- correct implementation
realFruit  :: String -> Fruit               
realFruit f = case f of 
                 "apple"  -> Apple
                 "orange" -> Orange
