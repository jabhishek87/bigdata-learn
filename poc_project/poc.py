import subprocess
from string import digits

def check_hadoop():
	p = subprocess.Popen("jps", stdout=subprocess.PIPE, shell=True)
	(output, err) = p.communicate()
	out_list = output.split('\n')
	
	for item in out_list:
		print item
	
	
	# for index, item in enumerate(out_list):
		# out_list[index] = item.translate(None, digits)
		
def make_folders_in_hdfs():
	
	print "Setting up environment"
	commands_to_run=[
		"hdfs dfs -mkdir -p /poc_project",
		"hdfs dfs -mkdir -p /poc_project/dataset",
		"hdfs dfs -mkdir -p /poc_project/map_reduce_out",
		"hdfs dfs -mkdir -p /poc_project/pig_out",
		"hdfs dfs -mkdir -p /poc_project/hive_out",
	
	]
	for item in commands_to_run:
		p = subprocess.Popen(item, stdout=subprocess.PIPE, shell=True)
		(output, err) = p.communicate()
		print output
	
	subprocess.call("clear")
		
def clean_upload_data_hdfs():
	
	print "cleaning data"
	# cleaning book data
	clean_book = "sed 's/\&amp;/\&/g' dataset/BX-Books.csv | sed -e '1d' | sed 's/\"\;\"/\t/g' | sed 's/\"//g' > dataset/BX-BooksCorrected.csv"
	p = subprocess.Popen(clean_book, stdout=subprocess.PIPE, shell=True)
	(output, err) = p.communicate()
	print output
	
	# cleaning rating data
	clean_book_rating = "sed 's/\"\;\"/\t/g' dataset/BX-Book-Ratings.csv | sed -e '1d' | sed 's/\"//g' > dataset/BX-BooksRatingCorrected.csv"
	p = subprocess.Popen(clean_book_rating, stdout=subprocess.PIPE, shell=True)
	(output, err) = p.communicate()
	print output
		
	# uploading book data
	upload_book_data = "hdfs dfs -put dataset/BX-BooksCorrected.csv  /poc_project/dataset/BX-Books.csv"
	p = subprocess.Popen(upload_book_data, stdout=subprocess.PIPE, shell=True)
	(output, err) = p.communicate()
	print output
	
	print "Uploading data"
	
	# uploading rating data
	upload_book_data = "hdfs dfs -put dataset/BX-BooksRatingCorrected.csv  /poc_project/dataset/BX-Book-Ratings.csv"
	p = subprocess.Popen(upload_book_data, stdout=subprocess.PIPE, shell=True)
	(output, err) = p.communicate()
	print output

def run_pig_script():
	
	print "starting Pig Script"
	pig_script = "pig -f poc_pig_script.pig -param book_data=/poc_project/dataset/BX-Books.csv -param book_rating_data=/poc_project/dataset/BX-Book-Ratings.csv -param out_path=/poc_project/pig_out"
	p = subprocess.Popen(pig_script, stdout=subprocess.PIPE, shell=True)
	(output, err) = p.communicate()
	print output
	
def run_hive_script():
	print "starting Hive Script"
	hive_script = "hive -f hive_script.sql"
	p = subprocess.Popen(hive_script, stdout=subprocess.PIPE, shell=True)
	(output, err) = p.communicate()
	print output
	
	upload_hive_output = "hdfs dfs -put hive_out/ /poc_project"
	p = subprocess.Popen(upload_hive_output, stdout=subprocess.PIPE, shell=True)
	(output, err) = p.communicate()
	print output



if __name__ == "__main__":
    #check_hadoop()
    make_folders_in_hdfs()
    
    #clean_upload_data_hdfs()
    #run_pig_script()
    #run_hive_script()
