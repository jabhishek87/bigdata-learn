-- Hive Script for Poc
-- hive –f /path_to/file/hive_script.sql

-- Creating Table 
CREATE TABLE IF NOT EXISTS tbl_books (isbn STRING, book_title STRING, book_author STRING, year_of_publication STRING, publisher STRING, image_url STRING, image_urlm STRING, image_url_l STRING) COMMENT 'BX-Books Table' ROW FORMAT DELIMITED FIELDS TERMINATED BY "\t" MAP KEYS TERMINATED BY '\t';

LOAD DATA LOCAL INPATH '/home/abhishekjaiswal/bigdata-learn/poc_project/dataset/BX-BooksCorrected.csv' OVERWRITE INTO TABLE tbl_books;

CREATE TABLE IF NOT EXISTS tbl_books_rating(user_id int, isbn string, book_rating int) row format delimited fields terminated by '\t' MAP KEYS TERMINATED BY '\t';

LOAD DATA LOCAL INPATH '/home/abhishekjaiswal/bigdata-learn/poc_project/dataset/BX-BooksRatingCorrected.csv' OVERWRITE INTO TABLE tbl_books_rating;

INSERT OVERWRITE LOCAL DIRECTORY 'hive_out/CountPerYear' ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t' SELECT year_of_publication, COUNT(book_title) AS CNT FROM tbl_books GROUP BY year_of_publication SORT BY CNT DESC;

INSERT OVERWRITE LOCAL DIRECTORY 'hive_out/MaxPublished' ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t' SELECT year_of_publication, COUNT(book_title) AS CNT FROM tbl_books GROUP BY year_of_publication SORT BY CNT DESC LIMIT 1;

INSERT OVERWRITE LOCAL DIRECTORY 'hive_out/BooksByRating' ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t' SELECT a.book_rating, COUNT(book_title) FROM tbl_books b JOIN tbl_books_rating a on (b.isbn = a.isbn) WHERE b.year_of_publication == '2002' GROUP BY a.book_rating;

