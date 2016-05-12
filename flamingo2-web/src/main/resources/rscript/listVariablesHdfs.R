#required packages: foreign, gdata, xlsx (+rjava)
# to install package xlsx in centos you need java JDK:
# yum install jpackage-utils
# download JDK 6.xx from http://java.sun.com/javase/downloads/widget/jdk6.jsp
# download the rpm.bin file. This is a self extracting archive, so chmod +x, and then ./file.bin
# do a R CMD javareconf
# now you can install.packages('xlsx');
args <- commandArgs();
Sys.setenv(HADOOP_HOME="/appl/hadoop-2.6.0");
Sys.setenv(HADOOP_BIN="/appl/hadoop-2.6.0/bin");
Sys.setenv(HADOOP_CONF_DIR="/appl/hadoop-2.6.0/etc/hadoop")
library(Rhipe);
rhinit();
library(methods);
library(rjson);
guessData <- function (filepath){
  csv = rhread(filepath, type='text');

  if (args[9]) {
    try({
      csvData = read.csv(textConnection(csv), header=T);
      return(list(data=csvData,header=T, sep=",", dec=".", type="csv"));
    });
  } else {
    try({
      csvData = read.csv(textConnection(csv), header=F);
      return(list(data=csvData,header=F, sep=",", dec=".", type="csv"));
    });
  }
}

makeRdata <- function(){
  #make a backup copy of the file:
  getFileName <- function(filepath){
      splitted <- strsplit(filepath, "/")[[1]];
      return(splitted[length(splitted)]);   #or use tail()
  }

  filename <- args[7];
  myGuess <- guessData(filename);

  #variable data in jsonstore format:
  guessData <- as.matrix(myGuess$data);
  attr(guessData,"dimnames") <- NULL;

  #guess options:
  guessedList <- list();
  guessedList$type <- myGuess$type;
  guessedList$header <- myGuess$header;
  guessedList$dec <- myGuess$dec;
  guessedList$sep <- myGuess$sep;

  #try to import real dataframe:
  if(any(myGuess$type==c("csv","csv2","delim","delim2"))){
    myType <- myGuess$type;
    myHeader <- myGuess$header;
    print(myHeader);
    myData <- myGuess$data;

  } else {
    myData <- myGuess$myData;
  }

  dataFileName <- paste(args[8],".RData",sep="");
  dataFileDest <- file.path(args[6],dataFileName);
  save(dataFileDest, myData, file=dataFileDest);
}

listVariable <- function(){
  is.Date <- function(x) {return(any(is(x)=="Date"))}

  dataFileName <- paste(args[8],".RData",sep="");
  dataFileDest <- file.path(args[6],dataFileName);

  load(dataFileDest);

  if(!exists("myData")){
    stop("myData was not found in the workspace");
  }

  outputTree <- list();
  ouputList <- list();
  factors <- names(myData)[sapply(myData,is.factor)];
  Dates <- names(myData)[sapply(myData,is.Date)];
  numerics <-  names(myData)[!sapply(myData,is.factor) & !sapply(myData,is.Date)];

  outputTree[[1]] <- list(draggable=F, id='ggfolder_Factor',text='Factor',children=list());
  outputTree[[2]] <- list(draggable=F, id='ggfolder_Numeric',text='Numeric',children=list());
  outputTree[[3]] <- list(draggable=F, id='ggfolder_Date',text='Date',children=list());
  #outputTree[[3]] <- list(id='Intercept', text='Intercept', leaf=TRUE);

  loop <- 1;
  if(length(factors) > 0){
    for(i in 1:length(factors)){
      ouputList[[loop]] <- list(value=factors[i],type='factor');
      loop <- loop + 1;
      outputTree[[1]]$children[[i]] <-  list(id=factors[i],value=factors[i],leaf=TRUE)
    }
  }
  if(length(numerics)>0){
    for(i in 1:length(numerics)){
      ouputList[[loop]] <- list(value=numerics[i],type='numeric');
      loop <- loop + 1;
      outputTree[[2]]$children[[i]] <-  list(id=numerics[i],value=numerics[i],leaf=TRUE)
    }
  }
  if(length(Dates) > 0){
    for(i in 1:length(Dates)){
      ouputList[[loop]] <- list(value=Dates[i],type='date');
      loop <- loop + 1;
      outputTree[[3]]$children[[i]] <-  list(id=Dates[i],value=Dates[i],leaf=TRUE)
    }
  }

  cat("{success:true, dataFile:\"",args[8],"\",variables:",toJSON(outputTree), ", variablelist: ", toJSON(ouputList) ,"}",sep="");
}

printFailure <- function(e){
    errorString <- toString(e$message);
    errorString <- gsub("\"","'",errorString);
    errorString <- gsub("\n"," ",errorString);
    cat("{success:false, error:\"",errorString,"\", filename:\"",args[7],"\"}",sep="");
}

#EXECUTED:
setTimeLimit(elapsed=60);

myData = "";
tryCatch(makeRdata(), error = function(e){printFailure(e)});

tryCatch(listVariable(), error = function(e){printFailure(e)});

setTimeLimit();
