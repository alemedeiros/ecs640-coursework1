#!/usr/bin/env python3
# -*- coding: utf-8 -*-
#
# time-analysis.py
# by alemedeiros <alexandre.n.medeiros _at_ gmail.com>
#
# Script to plot the graph for the time analysis

import csv
import datetime

from matplotlib import pyplot
from matplotlib import rc
from matplotlib.backends.backend_pdf import PdfPages

import sys

# Read a date in the format: YYYY-MM-DD
def read_date(d_str):
    d = d_str.split('-')
    return int(d[0]), int(d[1]), int(d[2])

if len(sys.argv) < 2:
    print('no input file', file=sys.stderr)
    exit(1)

# read input file
val = csv.reader(open(sys.argv[1], 'r'), delimiter='\t')

# initialize data arrays
lab = []
count = []

for i in val:
    y, m, d = read_date(i[0])
    # aparently there was a 31/06 date in the results...
    if m == 6 and d == 31:
        continue
    day = datetime.date(y, m, d)
    lab.append(day)
    count.append(int(i[1]))

#pyplot.xkcd()

# configure LaTeX fonts for graph
rc('text', **{'usetex':True, 'latex.unicode':True})
rc('font', **{'family':'sans-serif','size': '14.0', 'sans-serif':['Computer Modern Sans serif'] } )

# opens pdf graph file
title = 'Time Analysis'
p = PdfPages('time-analysis.pdf')

# Plot data
pyplot.plot(lab, count)
pyplot.legend(loc=0)

# Set title
pyplot.title(title)
pyplot.xlabel('Day of Tweet')
pyplot.ylabel('Number of Tweets')

pyplot.savefig(p, format='pdf')
p.close()
#pyplot.show()
