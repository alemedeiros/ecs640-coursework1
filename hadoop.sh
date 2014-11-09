# Add a shell function to run the Hadoop jobs to the bash

function runhadoop {
  hadoop jar dist/TwitterAnalysis.jar bigdata.twitter.$1 /data/olympictweets twitter/$1
  hadoop fs -getmerge twitter/$1 output/${1}.out
}
