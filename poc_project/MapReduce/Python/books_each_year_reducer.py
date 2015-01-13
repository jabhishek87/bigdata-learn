#!/usr/bin/env python
##############################################################################
#
# Copyright (C) Abhishek Jaiswal
# Copyright (C) 2014 Abhishek jaiswal <ia.malhotra@gmail.com>
# All rights reserved.
##############################################################################

from operator import itemgetter
import sys

current_year = None
current_count = 0
year = None

# input comes from STDIN
for line in sys.stdin:
    # remove leading and trailing whitespace
    line = line.strip()

    # parse the input we got from mapper.py
    year, count = line.split('\t')

    # convert count (currently a string) to int
    try:
        count = int(count)
    except ValueError:
        # count was not a number, so silently
        # ignore/discard this line
        continue

    # this IF-switch only works because Hadoop sorts map output
    # by key (here: year) before it is passed to the reducer
    if current_year == year:
        current_count += count
    else:
        if current_year:
            # write result to STDOUT
            print '%s\t%s' % (current_year, current_count)
        current_count = count
        current_year = year

# do not forget to output the last year if needed!
if current_year == year:
    print '%s\t%s' % (current_year, current_count)