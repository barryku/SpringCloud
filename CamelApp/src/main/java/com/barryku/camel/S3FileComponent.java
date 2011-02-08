package com.barryku.camel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.camel.component.file.FileEndpoint;
import org.apache.camel.component.file.GenericFileComponent;
import org.apache.camel.component.file.GenericFileConfiguration;
import org.apache.camel.component.file.GenericFileEndpoint;
import org.apache.camel.language.simple.SimpleLanguage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

@Component
public class S3FileComponent extends GenericFileComponent<File> {
	private static final Logger logger = LoggerFactory.getLogger(S3FileComponent.class);
    /**
     * GenericFile property on Camel Exchanges.
     */
    public static final String FILE_EXCHANGE_FILE = "CamelFileExchangeFile";
    
    /**
     * Default camel lock filename postfix
     */
    public static final String DEFAULT_LOCK_FILE_POSTFIX = ".camelLock";

    protected GenericFileEndpoint<File> buildFileEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        // the starting directory must be a static (not containing dynamic expressions)
        if (SimpleLanguage.hasStartToken(remaining)) {
            throw new IllegalArgumentException("Invalid directory: " + remaining
                    + ". Dynamic expressions with ${ } placeholders is not allowed."
                    + " Use the fileName option to set the dynamic expression.");
        }
        
        
        getFiles(remaining);
        File file = new File(TEMP_FOLDER);

        FileEndpoint result = new FileEndpoint(uri, this);
        result.setFile(file);

        GenericFileConfiguration config = new GenericFileConfiguration();
        config.setDirectory(file.getPath());
        result.setConfiguration(config);

        return result;
    }
    
	private static final String FOLDER_SUFFIX = "/";
	private static final String TEMP_FOLDER = "tmp";
	
	private @Value("${s3.apiKey}") String apiKey;
	private @Value("${s3.apiKeySecret}") String apiKeySecret;
	private @Value("${s3.bucket}") String bucket;
	
    private void getFiles(String path) throws IOException{
    	AmazonS3 s3 = new AmazonS3Client(
    			new BasicAWSCredentials(apiKey, apiKeySecret));
 		ObjectListing objList = s3.listObjects(bucket, path);
 		
 		for (S3ObjectSummary summary:objList.getObjectSummaries()) {
 			//ignore folders
 			if(! summary.getKey().endsWith(FOLDER_SUFFIX)){
 				S3Object obj = s3.getObject(
 						new GetObjectRequest(bucket, summary.getKey()));
 				logger.info("retrieving " + summary.getKey());
 				FileOutputStream fout = new FileOutputStream(TEMP_FOLDER + summary.getKey().substring(path.length()));
 				InputStream in = obj.getObjectContent();
 				byte[] buf = new byte[1024]; 				  
 			    int len; 			 
 			    while ((len = in.read(buf)) > 0){ 			  
 			      fout.write(buf, 0, len); 			 
 			     } 			 
 			     in.close();
 			     fout.close();
 			}
 		}       

    }
    protected void afterPropertiesSet(GenericFileEndpoint<File> endpoint) throws Exception {
        // noop
    }
}
