#required packages: foreign, gdata, xlsx (+rjava)
# to install package xlsx in centos you need java JDK:
# yum install jpackage-utils
# download JDK 6.xx from http://java.sun.com/javase/downloads/widget/jdk6.jsp
# download the rpm.bin file. This is a self extracting archive, so chmod +x, and then ./file.bin
# do a R CMD javareconf
# now you can install.packages('xlsx');

Sys.setenv(HADOOP_HOME="/appl/hadoop-2.6.0");
Sys.setenv(HADOOP_BIN="/appl/hadoop-2.6.0/bin");
Sys.setenv(HADOOP_CONF_DIR="/appl/hadoop-2.6.0/etc/hadoop")
library(Rhipe);
rhinit();
args <- commandArgs();

guessData <- function (filepath){
  options <- fromJSON(args[7]);
  dataDelimiter <- options$dataDelimiter;
  dataFile = rhread(filepath, type='text');
  readData = read.delim(textConnection(dataFile), header=F, sep=dataDelimiter, dec=".");
  return(list(data=readData,header=F, sep=dataDelimiter, dec="."));
}

mainFunction <- function(){
  library(rjson);

  #make a backup copy of the file:
  getFileName <- function(filepath){
      splitted <- strsplit(filepath, "/")[[1]];
      return(splitted[length(splitted)]);   #or use tail()
  }

  filename <- args[6];
  myGuess <- guessData(filename);

  #extra check for maximal number of Variables:
  if(ncol(myGuess$data) > 100){
    stop("Your dataset contains more than the maximum of 100 variables.");
  }
  if(nrow(myGuess$data) > 100000){
    stop("Your dataset contains more than the maximum of 100.000 rows.");
  }

  outputList <- list();

  #variable names:
  outputList$variableNames <- names(myGuess$data);

  #TEMP FIX REMOVES DOTS TO PREVENT JAVASCRIPT ISSUES:
  outputList$variableNames <- gsub("\\.","_",outputList$variableNames)

  #variable data in jsonstore format:
  guessData <- as.matrix(myGuess$data);
  attr(guessData,"dimnames") <- NULL;
  outputList$variableData <- apply(guessData,1,as.list)[1:10];

  #guess options:
  guessedList <- list();
  guessedList$type <- myGuess$type;
  guessedList$header <- myGuess$header;
  guessedList$dec <- myGuess$dec;
  guessedList$sep <- myGuess$sep;
  outputList$guess <- guessedList;

  #try to import real dataframe:
  if(any(myGuess$type==c("csv","csv2","delim","delim2"))){
    myType <- myGuess$type;
    myHeader <- myGuess$header;
    print(myHeader);
    myData <- myGuess$data;

  } else {
    myData <- myGuess$myData;
  }

  #encode in JSON
  print(myData);
  cat("{success:true, output:",toJSON(outputList), "}",sep="");
}

printFailure <- function(e){
    errorString <- toString(e$message);
    errorString <- gsub("\"","'",errorString);
    errorString <- gsub("\n"," ",errorString);
    cat("{success:false, error:\"",errorString,"\", filename:\"",args[6],"\"}",sep="");
}

#EXECUTED:
setTimeLimit(elapsed=60);

myData = "";
tryCatch(mainFunction(), error = function(e){printFailure(e)});

setTimeLimit();
