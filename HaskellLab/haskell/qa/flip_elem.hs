flip_elem = all (`elem` listx) input 
             where listx = "abcde"
                   input = "ab"
     
flip_elem2 = all (flip elem listx) input
              where listx = "abcde"
                    input = "ab"
                    
                   
