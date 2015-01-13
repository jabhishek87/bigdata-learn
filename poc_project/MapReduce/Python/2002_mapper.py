#!/usr/bin/env python
 
import sys
 
# input comes from STDIN (standard input)
for line in sys.stdin:
    try: #sometimes bad data can cause errors use this how you like to deal with lint and bad data
		rating, year_pub, count = line.split('\t')
		
		# check if it is 2002 then print
		if year_pub == int(year_pub):
			print '%s\t%s' % (rating, count)
    except: #errors are going to make your job fail which you may or may not want
        pass
