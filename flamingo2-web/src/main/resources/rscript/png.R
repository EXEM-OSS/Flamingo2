#this app uses symlinks /rdata en /wwwroot
args <- commandArgs();
setTimeLimit(elapsed=40);

# you might have to make /WWW_DIRECTORY/ggplot2/plots writeable

### actual code starts here ###
library(methods);
library(ggplot2);
library(rjson);
myPlot = NULL;

drawPlot <- function(){

	######################### injection fix: ##########################
	safeRequest <- gsub("[()]","",args[6]);
	plotConfig <- fromJSON(safeRequest);
	###################################################################

	dataFile <- plotConfig$dataFile;
	dataFileDest <- file.path(args[7], paste(dataFile,'.RData',sep=""));
	load(dataFileDest);
	
	plotWidth <- plotConfig[["width"]];
	plotHeight <- plotConfig[["height"]];	
	
	x <- substring(plotConfig[["x"]],5);
	y <- substring(plotConfig[["y"]],5);

	myPlot <- ggplot(myData) + aes_string(x=x, y=y);
	
	if(!is.null(plotConfig[["weight"]])){
		weight <- substring(plotConfig[["weight"]],5);
		myPlot <- myPlot + aes_string(weight=weight);	
	}	
	
	if(!is.null(plotConfig[["group"]])){
		group <- substring(plotConfig[["group"]],5);
		myPlot <- myPlot + aes_string(group=group);
	}
	
	if(!is.null(plotConfig[["colour"]])){
		colour <- substring(plotConfig[["colour"]],5);
		myPlot <- myPlot + aes_string(colour=colour);
	}

	if(!is.null(plotConfig[["facet"]][["map"]])){
		facet <- substring(plotConfig[["facet"]][["map"]],5);
		if(!is.null(plotConfig[["facet"]][["scales"]])){
			scales = substring(plotConfig[["facet"]][["scales"]],5);
		} else {
			scales = "fixed";
		}
		if(!is.null(plotConfig[["facet"]][["nrow"]])){
			nrow = as.numeric(substring(plotConfig[["facet"]][["nrow"]],5));
		} else {
			nrow = NULL;
		}		
		myPlot <- myPlot + facet_wrap(as.formula(paste("~",facet)), nrow=nrow, scales=scales);
	}
	
	layers <- plotConfig$layers;

	if(length(layers) < 1){
		if((y=="..density..") || (y=="..count..")){
			myPlot <- myPlot + geom_blank(stat="bin");
		} else {
			myPlot <- myPlot + geom_blank();
		}
	} else{
	
		for(i in 1:length(layers)){

			thisLayer <- layers[[i]];
			thisAesthetics <- list();

			#note: loop has to be backwards because of the removing of elements by <- NULL;
			
			for(j in length(thisLayer):1){
			
				thisValue <- thisLayer[[j]];
				if(substr(thisValue,1,3) == "set"){
					thisLayer[[j]] <- substring(thisValue,5)

					#temp fixes:
					if(thisLayer[[j]] == "FALSE" || thisLayer[[j]] == "TRUE") {
						thisLayer[[j]] <- as.logical(thisLayer[[j]]);
					}
					
					thisProperty <- names(thisLayer[j]);
					if(thisProperty == "width" || thisProperty == "xintercept" || thisProperty == "yintercept" || thisProperty == "intercept" || thisProperty == "slope" || thisProperty == "binwidth" || thisProperty == "alpha" || thisProperty == "size" || thisProperty == "weight" || thisProperty == "adjust"|| thisProperty == "shape" || thisProperty == "bins" || thisProperty == "angle"){
						thisLayer[[j]] <- as.numeric(thisLayer[[j]]);
					}
					
					if(thisProperty == "number"){
						Nquantiles <- as.numeric(thisLayer[[j]]) + 2;
						quantiles <- seq(0,1,length.out=Nquantiles);
						quantiles <- quantiles[-c(1,Nquantiles)];
						thisLayer$quantiles <- quantiles;
						thisLayer[j] <- NULL;
					}					
				}

				###
				
				if(substr(thisValue,1,3) == "map"){
					mapVariable <- substring(thisValue,5);
					mapProperty <- names(thisLayer[j]);
					thisAesthetics[mapProperty] = mapVariable;
					thisLayer[j] <- NULL;
				}
			}
			if(length(thisAesthetics) > 0){
			
				myMappings <- as.call(append(thisAesthetics,aes_string,after=0));
				thisLayer$mapping <- myMappings
			}
			myPlot <- myPlot + eval(as.call(append(thisLayer,layer,after=0)));
		}
	}

	randomnum <- round(runif(1,0,100000));
	filename <- paste(randomnum,".png",sep="");
	png(filename=paste(args[8],"/",filename,sep=""),width=plotWidth, height=plotHeight);
	print(myPlot);
	dev.off();

	#ggsave(plot=myPlot,filename=filename,width=plotWidth/300, height=plotHeight/300);
	#cat("{success:true, charturl:\"",filename,"\"}",sep="");
	sink();

	imgurl <- paste("<img id=\"ggplot_img\" src=\"http://exo1.cdh.local:18080/ggplot2/", filename, "\">",sep="");
	cat("{success:true, imgurl: '", imgurl, "', filename: '", filename ,"'}",sep="");
}

printFailure <- function(e){
    sink();
    errorString <- toString(e$message);
    errorString <- gsub("\"","'",errorString);
    errorString <- gsub("\n"," ",errorString);
    cat("{success:false, error:\"",errorString,"\"}",sep="");
} 
sink(tempfile());
tryCatch(suppressMessages(drawPlot()), error = function(e){printFailure(e)});
setTimeLimit();