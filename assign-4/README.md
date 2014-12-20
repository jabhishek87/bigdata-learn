readme file to assign to solution 3

read pdf :- in simple way calculate maximum temp for every year
     
Mr_Assignments.jar is java solution

command to run 
hadoop jar Mr_Assignments.jar /assign-3 /assign-3-output

where /assign-3  is data set on hdfs
and /assign-3-out is output in hdfs

python run

debug mapper.py
	cat dataset/reducer_debug | python max_temp_mapper.py

debug reducer.py
	cat dataset/reducer_debug | python max_temp_reducer.py


hadoop jar $HOME/hadoop-2.2.0/hadoop-streaming-2.2.0.2.1.0.0-92.jar \
-file max_temp_mapper.py -mapper max_temp_mapper.py \
-file max_temp_reducer.py -reducer max_temp_reducer.py \
-input /assign_4_dataset/* -output /assign_4_output_python
