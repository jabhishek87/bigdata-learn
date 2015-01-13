#!/usr/bin/env python
 
import sys
 
# maps words to their counts
foundKey = ""
foundValue = ""
isFirst = 1
currentCount = 0
current_isbn = "-1"
current_rating = "-1"
IsbnMappingLine = False

# input comes from STDIN
for line in sys.stdin:
        # remove leading and trailing whitespace
        line = line.strip()

        try:
                # parse the input we got from mapper.py
                # print "{}\t{}\t{}\t{}\t{}".format(rating, book_name, author_name, year_pub, isbn)
                # country2digit,personType,personName,countryName = line.split('\t')
                rating, book_name, author_name, year_pub, isbn = line.split('\t')
                # print line
                # if int(year_pub) == 2002:

                # the first line should be a mapping line, otherwise we need to set the currentCountryName to not known
                # the first line mapping line , otherwise we need to set rating to not known
                # if personName == "-1": # this is a new country which may or may not have people in it
                if book_name == "-1": # this is a new country which may or may not have people in it
                        current_rating = rating
                        current_isbn = isbn
                        IsbnMappingLine = True
                else:
                        IsbnMappingLine = False # this is a person we want to count

                if not IsbnMappingLine: #  we only want to count people but use the country line to get the right name

                        # first check to see if the 2digit country info matches up, might be unknown country
                        if current_isbn != isbn:
                                current_isbn = isbn
                                current_rating = '%s - Unkown Country' % current_isbn

                        currentKey = '%s\t%s' % (rating,year_pub)

                        if foundKey != currentKey: # new combo of keys to count
                                if isFirst == 0:
                                        print '%s\t%s' % (foundKey,currentCount)
                                        currentCount = 0 # reset the count
                                else:
                                        isFirst = 0

                                foundKey = currentKey # make the found key what we see so when we loop again can see if we increment or print out

                        currentCount += 1 # we increment anything not in the map list
        except:
                pass

try:
        print '%s\t%s' % (foundKey,currentCount)
except:
        pass
