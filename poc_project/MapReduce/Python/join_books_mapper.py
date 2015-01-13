#!/usr/bin/env python
 
import sys

# input comes from STDIN (standard input)
for line in sys.stdin:
    try: #sometimes bad data can cause errors use this how you like to deal with lint and bad data

        personName = "-1" #default sorted as first
        personType = "-1" #default sorted as first
        countryName = "-1" #default sorted as first
        country2digit = "-1" #default sorted as first
        
        isbn = "-1" #default sorted as first
        book_name = "-1" #default sorted as first
        author_name = "-1" #default sorted as first
        year_pub = "-1" #default sorted as first

        isbn = "-1"
        rating = "-1"
         
        # remove leading and trailing whitespace
        line = line.strip()

        splits = line.split("\t")
        #print splits
         
        if len(splits) == 3: #country data
            isbn = splits[1]
            rating = splits[2]
        else: #people data
            isbn = splits[0]
            book_name = splits[1]
            author_name = splits[2]
            year_pub = splits[3]

         
        #print '%s^%s^%s^%s' % (country2digit,personType,personName,countryName)
        print "{}\t{}\t{}\t{}\t{}".format(rating, book_name, author_name, year_pub, isbn)
    except: #errors are going to make your job fail which you may or may not want
        pass
