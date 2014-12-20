#!/usr/bin/env python
##############################################################################
#
# Copyright (C) Abhishek Jaiswal
# Copyright (C) 2014 Abhishek jaiswal <ia.malhotra@gmail.com>
# All rights reserved.
##############################################################################

from operator import itemgetter
import sys

prev_year = None
prev_temp = 0
year = None
temp = 0

# input comes from STDIN
for line in sys.stdin:
    # remove leading and trailing whitespace
    line = line.strip()

    # parse the input we got from mapper.py (year , temperature)
    year, temp = line.split('\t')

    # convert temp (currently a string) to int for comparision
    try:
        temp = int(temp)
    except ValueError:
        # temp was not a number, so silently
        # ignore/discard this line
        continue
    
    # this IF-switch only works because Hadoop shuffle process sorts map output
    # by key (here: year) before it is passed to the reducer
    # checking rec by previous record to current record
    if prev_year == year:
        if temp > prev_temp:
            prev_temp = temp
    
    else:
        
        # if it is not matched with pre record and if year the write
        if prev_year:
            # write result to STDOUT
            print '%s\t%s' % (prev_year, prev_temp)
    
    #assign swap the value to prev value
        prev_year = year
        prev_temp = temp
 
# output of the last month
if prev_year == year:
    print '%s\t%s' % (prev_year, prev_temp)
