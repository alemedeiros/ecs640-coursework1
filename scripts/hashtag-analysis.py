#!/usr/bin/env python3
# -*- coding: utf-8 -*-
#
# hashtag-analysis.py
# by alemedeiros <alexandre.n.medeiros _at_ gmail.com>
#
# Script to plot a horizontal bar graph for the hashtag analysis

import csv
import sys

from matplotlib import pyplot
from matplotlib import rc
from matplotlib.backends.backend_pdf import PdfPages

if len(sys.argv) < 2:
    print('no input file', file=sys.stderr)
    exit(1)

# read input file
val = csv.reader(open(sys.argv[1], 'r'), delimiter='\t')

# initialize data arrays
data = [ (int(x[1]), x[0]) for x in val ]
data.sort(reverse=True)
lab = []
count = []

for i in data[:6]:
    lab.append(i[1])
    count.append(i[0])

# configure LaTeX fonts for graph
rc('text', **{'usetex':True, 'latex.unicode':True})
rc('font', **{'family':'sans-serif','size': '14.0', 'sans-serif':['Computer Modern Sans serif'] } )

# opens pdf graph file
title = 'Hashtag Analysis'
p = PdfPages('hastag-analysis.pdf')

# Plot data
col = [ 'b', 'g', 'r', 'c', 'm', 'y' ]
for i in range(len(count)):
    pyplot.barh((6-i), count[i], label=lab[i], color=col[i])

pyplot.legend(loc=0)

# Set title
pyplot.title(title)
pyplot.xlabel('Number of Tweets')
pyplot.ylabel('Supported countries')

frame = pyplot.gca()
frame.axes.get_yaxis().set_visible(False)
frame.axes.get_yaxis().set_ticks([])

pyplot.savefig(p, format='pdf')
p.close()
#pyplot.show()
