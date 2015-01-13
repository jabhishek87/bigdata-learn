#!/usr/bin/env python

##############################################################################
#
# Copyright (C) Abhishek Jaiswal
# Copyright (C) 2014 Abhishek jaiswal <ia.malhotra@gmail.com>
# All rights reserved.
##############################################################################

import sys

# input comes from STDIN (standard input)
# num = 1
for line in sys.stdin:
    # remove leading and trailing whitespace
    line = line.strip()
    # split the line
    rec = line.split('\t')
    # checking if it is int and greater than 0
    try:
        if int(rec[3].strip()) > 0:
            print '%s\t%s' % (rec[3].strip(), 1)

    except ValueError:
        continue
    # num = num+1
