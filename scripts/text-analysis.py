#!/usr/bin/env python3
# -*- coding: utf-8 -*-
#
# text-analysis.py
# by alemedeiros <alexandre.n.medeiros _at_ gmail.com>
#
# Script to plot the histogram for the text analysis

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
lab = []
count = []

for i in val:
    lab.append(int(i[0]))
    count.append(int(i[1]))

# configure LaTeX fonts for graph
rc('text', **{'usetex':True, 'latex.unicode':True})
rc('font', **{'family':'sans-serif','size': '14.0', 'sans-serif':['Computer Modern Sans serif'] } )

# opens pdf graph file
title = 'Text Analysis'
p = PdfPages('text-analysis.pdf')

# Plot data
pyplot.bar(lab, count, width=4)
pyplot.legend(loc=0)

# Set title
pyplot.title(title)
pyplot.xlabel('Size of Tweets')
pyplot.ylabel('Number of Tweets')

pyplot.savefig(p, format='pdf')
p.close()
#pyplot.show()
