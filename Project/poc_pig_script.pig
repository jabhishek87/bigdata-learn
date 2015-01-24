--pig -f poc_pig_script.pig -param book_data=/poc_project/dataset/BX-Books.csv -param book_rating_data=/poc_project/dataset/BX-Book-Ratings.csv -param out_path=/poc_project/pig_out

-- load Books data
BKS = load '$book_data' using PigStorage('\t') AS (ISBN:chararray, BookTitle:chararray, BookAuthor:chararray, YearOfPublication:int, Publisher:chararray);

--load book rating data
BXR = load '$book_rating_data' using PigStorage('\t') AS (UserID:chararray, ISBN:chararray, Rating:int);

-- Grouping all book by yearof publication
BooksPerYear = GROUP BKS by YearOfPublication;

-- counting no of book published each year
CountPerYear = FOREACH BooksPerYear GENERATE group AS YearOfPublication, COUNT($1) AS BooksCount;

-- Sorting It by Hishest books publised 
ORDERING = ORDER CountPerYear BY $1 DESC; --$1 is an alias for Book Title

-- storing ORDERING it in file
STORE ORDERING INTO '$out_path/CountPerYear';

-- storing Max book published year
--DUMP ORDERING;
YearMaxBooksPub = LIMIT ORDERING 1;

--DUMP YearMaxBooksPub;
-- storing YearMaxBooksPub it in file
STORE YearMaxBooksPub INTO '$out_path/MaxPublished';

-- Joining book_data and book_rating_data
BooksAndRating = JOIN BKS BY ISBN, BXR BY ISBN;

--filtering result only by year 2002
BKR_2002 = FILTER BooksAndRating BY YearOfPublication == 2002;

-- grouping it by book rating
GRPByRating = GROUP BKR_2002 BY Rating;

-- counting bo of books by rating
BooksByRating = FOREACH GRPByRating GENERATE group AS BookRating, COUNT($1);

-- storing BooksByRating it in file
STORE BooksByRating INTO '$out_path/BooksByRating';


