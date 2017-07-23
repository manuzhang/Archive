{-# LANGUAGE NoMonomorphismRestriction, TypeFamilies #-}

module Test where

asExprTyp :: Expr γ =>
    γ α
    -> α
    -> γ α
asExprTyp x _ = x

int = undefined :: Integer

class Expr γ where
    a :: γ α

-- this works fine
b = a `asExprTyp` int

--this fails
mcode = do
    return ()
    where b = a `asExprTyp` int
