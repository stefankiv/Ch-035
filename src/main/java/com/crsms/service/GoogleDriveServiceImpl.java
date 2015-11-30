package com.crsms.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.ParentReference;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class GoogleDriveServiceImpl implements GoogleDriveService {
	
	/** Application name. */
    private static final String APPLICATION_NAME =
        "CrsMS softserve IT Academy web app";
    
    /** Path to client_secret.json */
    private static final String CLIENT_SECRET_PATH = "/drive_client_secret.json";
    
    /** Path to drive.properties */
    private static final String DRIVE_PROPERTIES_PATH = "/drive.properties";
    
    /** Directory to store user credentials for this application. */
    private static final java.io.File DATA_STORE_DIR = new java.io.File(
       FileServiceImpl.STORAGE_PATH, "credentials/google-drive");

    /** Global instance of the {@link FileDataStoreFactory}. */
    private static final FileDataStoreFactory DATA_STORE_FACTORY;

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY =
        JacksonFactory.getDefaultInstance();

    /** Global instance of the HTTP transport. */
    private static final HttpTransport HTTP_TRANSPORT;

    /** Global instance of the scopes required by this service. */
    private static final List<String> SCOPES =
        Arrays.asList(DriveScopes.DRIVE);
    
    /** Drive properties */
    private static final java.util.Properties DRIVE_PROPERTIES;
    
    /** Drive storage parent folder object */
    private static final ParentReference DRIVE_GLOBAL_PARENT_REFERENCE;
    
    static {
    	try {
    		HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
    		DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
    		DRIVE_PROPERTIES = getDriveProperties();
    		DRIVE_GLOBAL_PARENT_REFERENCE = getDriveGlobalParentReference();
    	} catch (Exception e) {
    		throw new ExceptionInInitializerError(e);
    	}
    }
    
    private final Logger logger = LogManager.getLogger(GoogleDriveServiceImpl.class);
    
    private static java.util.Properties getDriveProperties() throws IOException {
    	java.util.Properties properties = new java.util.Properties();
		properties.load(GoogleDriveServiceImpl.class.getResourceAsStream(DRIVE_PROPERTIES_PATH));
		return properties;
    }
    
    private static ParentReference getDriveGlobalParentReference() throws IOException {
		return new ParentReference().setId(DRIVE_PROPERTIES.getProperty("storage.folder.id"));
    }
    
    /**
     * Creates an authorized Credential object.
     * @return an authorized Credential object.
     * @throws IOException
     */
    public Credential authorize() throws IOException {
        // Load client secrets.
        InputStream driveClientInputStream =
        		GoogleDriveServiceImpl.class.getResourceAsStream(CLIENT_SECRET_PATH);
        GoogleClientSecrets clientSecrets =
            GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(driveClientInputStream));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(
                        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(DATA_STORE_FACTORY)
                .setAccessType("offline")
                .build();
        Credential credential = new AuthorizationCodeInstalledApp(
            flow, new LocalServerReceiver()).authorize("user");
        logger.debug(
                "Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
        return credential;
    }

    /**
     * Build and return an authorized Drive client service.
     * @return an authorized Drive client service
     * @throws IOException
     */
    public Drive getDriveAPIClientService() throws IOException {
        return new Drive.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, authorize())
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
    
    public File uploadToDrive(java.io.File IOFile) throws IOException {
    	Drive driveAPIClientService = getDriveAPIClientService();
    	String mimeType = java.nio.file.Files.probeContentType(IOFile.toPath());
    	InputStreamContent mediaContent =
    		    new InputStreamContent(mimeType,
    		        new BufferedInputStream(new FileInputStream(IOFile)))
    			.setLength(IOFile.length());
    	File fileMetadata = new File()
    			.setParents(Arrays.asList(DRIVE_GLOBAL_PARENT_REFERENCE))
    			.setTitle(IOFile.getName());
		File responseFile = driveAPIClientService.files().insert(fileMetadata, mediaContent).execute(); 
		logger.info("successfully uploaded file to Google Drive. id:" + responseFile.getId() 
				+ ", title:" + responseFile.getTitle());
		return responseFile;
    }
    
}