#!/usr/bin/env python

##############################################################################
#
# Copyright (C) Abhishek Jaiswal
# Copyright (C) 2014 Abhishek jaiswal <ia.malhotra@gmail.com>
# All rights reserved.
##############################################################################

import sys

# input comes from STDIN (standard input)
for line in sys.stdin:
    # remove leading and trailing whitespace
    line = line.strip()
    # split the line into words
    words = line.split()
    # outputting value year temp
    print '%s\t%s' % (words[0].strip(), words[1])

