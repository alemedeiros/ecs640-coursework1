#Coursework 1 - Twitter Analysis
by Alexandre Novais de Medeiros (alemedeiros)

Student ID 140667280

##Compilation

There is a ant build file available in the root directory. For a full
recompilation, use the command

    ant clean dist

##Running

All the Hadoop job classes are be included in the `dist/TwitterAnalysis.jar`
file.

To run the class of each job is:

+ __Histogram generation__: bigdata.twitter.text.Histogram
+ __Average Tweet length__: bigdata.twitter.text.AverageLength
+ __Time Analysis__: bigdata.twitter.time.TimeAnalysis
+ __Hashtag Counter__: bigdata.twitter.hashtag.HashtagCount
+ __Support Hashtag Analysis__: bigdata.twitter.hashtag.HashtagAnalysis

There is a simple shell file which adds a function to run Hadoop jobs. The
function runs the job and downloads the merged output from the HDFS.

To use the function, first source the file with `source hadoop.sh` then just use
the runhadoop command with the last two names of the desired Hadoop job, i.e.,
`runhadoop text.AverageLength`.

##Results

The data generated by the above jobs is available on the `output` directory.
There is also a version of the data, sorted using the unix `sort` command, in
some cases, there is also a filtered version, that was generated manually.

##Report

The report pdf and it's source files are on the `report` directory.
