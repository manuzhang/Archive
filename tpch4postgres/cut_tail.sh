# PostgreSQL required that the delimiter should not appear at the end of every
# line and this script helps to cut that "tail" out

DIR=.
for FILE in $( find $DIR -name '*.tbl')
do
   mv $FILE $FILE.old
   sed -e 's/[|]$//' $FILE.old > $FILE
done 
