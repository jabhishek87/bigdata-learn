readme file to assign to solution 3

problem if we say simple just count the occurence of same number 
i.e- 1		19 times
     999	14 times
     
Mr_Assignments.jar is java solution

command to run 
hadoop jar Mr_Assignments.jar /assign-3 /assign-3-output

where /assign-3  is data set on hdfs
and /assign-3-out is output in hdfs

python run

debug mapper.py
	cat dataset/patent | python mapper.py

debug reducer.py
	cat dataset/reducer_debug | python reducer.py


hadoop jar $HOME/hadoop-2.2.0/hadoop-streaming-2.2.0.2.1.0.0-92.jar \
-file mapper.py -mapper mapper.py \
-file reducer.py -reducer reducer.py \
-input /assign-3/* -output /assign_3_output_python
