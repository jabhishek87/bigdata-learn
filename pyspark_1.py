from pyspark.context import SparkContext

sc = SparkContext(...)
lines = sc.textFile(sys.argv[2],1)

counts = lines.flatMap(lambda x: x.split(' ')) \
.map(lambda x: (x,1)) \
.reduceByKey(lambda x, y: x+y)

for (word, count) in counts.collect():
    print "%s:%i" %(word, count)
