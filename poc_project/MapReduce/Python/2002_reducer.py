#!/usr/bin/env python

from operator import itemgetter
import sys

current_rating = None
current_count = 0
rating = None

# input comes from STDIN
for line in sys.stdin:
    # remove leading and trailing whitespace
    line = line.strip()
    if line:
        # parse the input we got from mapper.py
        rating, count = line.split('\t')

        # convert count (currently a string) to int
        try:
            count = int(count)
        except ValueError:
            # count was not a number, so silently
            # ignore/discard this line
            continue

        # this IF-switch only works because Hadoop sorts map output
        # by key (here: word) before it is passed to the reducer
        if current_rating == rating:
            current_count += count
        else:
            if current_rating:
                # write result to STDOUT
                print '%s\t%s' % (current_rating, current_count)
            current_count = count
            current_rating = rating

# do not forget to output the last word if needed!
if current_rating == rating:
    print '%s\t%s' % (current_rating, current_count)
