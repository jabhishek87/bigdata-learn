readme file to POC Project

Problem statement

steps
first run this command to clean dataset as it contain &amp;
`sed 's/\&amp;/\&/g' BX-Books.csv | sed -e '1d' | sed 's/\"\;\"/\t/g' | sed 's/\"//g' > BX-BooksCorrected.csv`

`sed 's/\"\;\"/\t/g' BX-Book-Ratings.csv | sed -e '1d' | sed 's/\"//g' > BX-BooksRatingCorrected.csv`

hdfs dfs -mkdir -p /poc_project/problem-1/dataset
hdfs dfs -put BX-BooksCorrected.csv  /poc_project/problem-1/dataset/BX-Books.csv

debug mapper.py
	cat dataset/BX-BooksCorrected.csv  | python books_each_year_mapper.py
	
hadoop jar /home/abhishekjaiswal/hadoop-2.2.0/hadoop-streaming-2.2.0.2.1.0.0-92.jar \
-file books_each_year_mapper.py -mapper books_each_year_mapper.py \
-file books_each_year_reducer.py -reducer books_each_year_reducer.py  \
-input /poc_project/dataset/BX-Books.csv \
-output /poc_project/map_reduce_out/CountPerYear

hadoop jar /home/abhishekjaiswal/hadoop-2.2.0/hadoop-streaming-2.2.0.2.1.0.0-92.jar \
-file join_books_mapper.py -mapper join_books_mapper.py \
-file join_books_reducer.py -reducer join_books_reducer.py  \
-input /poc_project/dataset/BX-Book-Ratings.csv \
-input /poc_project/dataset/BX-Books.csv \
-output /poc_project/map_reduce_out/BooksByRating

hadoop jar /home/abhishekjaiswal/hadoop-2.2.0/hadoop-streaming-2.2.0.2.1.0.0-92.jar \
-file 2002_mapper.py -mapper 2002_mapper.py \
-file 2002_reducer.py -reducer 2002_reducer.py  \
-input /poc_project/map_reduce_out/BooksByRating/* \
-output /poc_project/map_reduce_out/BooksByRating_1

hadoop jar /home/abhishekjaiswal/hadoop-2.2.0/hadoop-streaming-2.2.0.2.1.0.0-92.jar \
-file join_mapper.py -mapper join_mapper.py \
-file join_reducer.py -reducer join_reducer.py  \
-input /poc_project/dataset/BX-Book-Ratings.csv \
-input /poc_project/dataset/BX-Books.csv \
-output /poc_project/map_reduce_out/BooksByRating -partitioner org.apache.hadoop.mapred.lib.KeyFieldBasedPartitioner \
-jobconf num.key.fields.for.partition=1


Pig ANALYTICS
==============
	BKS = load '/poc_project/problem-1/dataset/BX-Books.csv' using PigStorage('\t') AS (ISBN:chararray, BookTitle:chararray, BookAuthor:chararray, YearOfPublication:int, Publisher:chararray);
	BooksPerYear = GROUP BKS by YearOfPublication;
	CountPerYear = FOREACH BooksPerYear GENERATE group AS YearOfPublication, COUNT( $1) AS BooksCount;
	ORDERING = ORDER CountPerYear BY $1 DESC; //$1 is an alias for Book Title
	STORE ORDERING INTO '/poc_project/problem-1/pig_out/CountPerYear'
	//DUMP ORDERING;
	YearMaxBooksPub = LIMIT ORDERING 1;
	//DUMP YearMaxBooksPub;
	STORE CountPerYear INTO '/poc_project/problem-1/pig_out/MaxPublished'

	How many books were published based on ranking for a given year?
	================================================================
	BXR = load '/poc_project/problem-1/dataset/BX-Book-Ratings.csv' using PigStorage('\t') AS (UserID:chararray, ISBN:chararray, Rating:int);
	BooksAndRating = JOIN BKS BY ISBN, BXR BY ISBN;
	BKR_2002 = FILTER BooksAndRating BY YearOfPublication == 2002;
	GRPByRating = GROUP BKR_2002 BY Rating;
	BooksByRating = FOREACH GRPByRating GENERATE group AS BookRating, COUNT($1);
	STORE BooksByRating INTO '/poc_project/problem-1/pig_out/BooksByRating';
	

HIVE solution to problem -1
============================
	1. CREATE TABLE IF NOT EXISTS books (isbn STRING, book_title STRING, book_author STRING, year_of_publication STRING, publisher STRING, image_url STRING, image_urlm STRING, image_url_l STRING) COMMENT 'BX-Books Table' ROW FORMAT DELIMITED FIELDS TERMINATED BY "\t" MAP KEYS TERMINATED BY '\t' ;

	-- location '/my/location/in/hdfs';
	2. LOAD DATA LOCAL INPATH '/home/abhishekjaiswal/bigdata-learn/poc_project/dataset/BX-BooksCorrected.csv' OVERWRITE INTO TABLE books;
	-- SELECT book_title, year_of_publication FROM books LIMIT 10;
	3. SELECT year_of_publication, COUNT(book_title) AS CNT FROM BOOKS GROUP BY year_of_publication SORT BY CNT DESC
	4. SELECT year_of_publication, COUNT(book_title) AS CNT FROM BOOKS GROUP BY year_of_publication SORT BY CNT DESC LIMIT 1
	
	5. CREATE TABLE books_rating(user_id int, isbn string, book_rating int) row format delimited fields terminated by '\t' MAP KEYS TERMINATED BY '\t';
	6. LOAD DATA LOCAL INPATH  '/home/abhishekjaiswal/bigdata-learn/poc_project/dataset/BX-BooksRatingCorrected.csv' OVERWRITE INTO TABLE books_rating;
	-- SELECT isbn, book_rating from books_rating LIMIT 10;

	
	7. SELECT a.book_rating, COUNT(book_title) FROM books b JOIN books_rating a on (b.isbn = a.isbn) WHERE b.year_of_publication == '2002' GROUP BY a.book_rating;
	
	
	INSERT OVERWRITE DIRECTORY '/poc_project/problem-1/hive_out/MaxPublished' ROW FORMAT DELIMITED FIELDS TERMINATED BY ',' SELECT year_of_publication, COUNT(book_title) AS CNT FROM tbl_books GROUP BY year_of_publication SORT BY CNT DESC LIMIT 1;
	
	INSERT OVERWRITE DIRECTORY '/poc_project/problem-1/hive_out/MaxPublished' ROW FORMAT DELIMITED FIELDS TERMINATED BY ',' SELECT book_title, year_of_publication FROM tbl_books LIMIT 10;
	
	INSERT OVERWRITE LOCAL DIRECTORY '/home/abhishekjaiswal/bigdata-learn/poc_project/MaxPublished' ROW FORMAT DELIMITED FIELDS TERMINATED BY ',' SELECT book_title, year_of_publication FROM tbl_books LIMIT 10;
	
	INSERT OVERWRITE DIRECTORY '/user/hadoop/output'
ROW FORMAT DELIMITED
FIELDS TERMINATED BY
SELECT * FROM graph_edges;

	
	
	
	
	
	
	
	
	
	
	
================================================================
read pdf :- in simple way calculate maximum temp for every year
     
Mr_Assignments.jar is java solution

command to run 
hadoop jar Mr_Assignments.jar /assign-3 /assign-3-output

where /assign-3  is data set on hdfs
and /assign-3-out is output in hdfs

python run

debug mapper.py
	cat dataset/reducer_debug | python max_temp_mapper.py

debug reducer.py
	cat dataset/reducer_debug | python max_temp_reducer.py


hadoop jar $HOME/hadoop-2.2.0/hadoop-streaming-2.2.0.2.1.0.0-92.jar \
-file max_temp_mapper.py -mapper max_temp_mapper.py \
-file max_temp_reducer.py -reducer max_temp_reducer.py \
-input /assign_4_dataset/* -output /assign_4_output_python
