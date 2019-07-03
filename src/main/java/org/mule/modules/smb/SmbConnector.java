/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.modules.smb;

import java.util.List;

import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
import org.mule.api.ConnectionException;
import org.mule.api.annotations.Config;
import org.mule.api.annotations.Connector;
import org.mule.api.annotations.Processor;
import org.mule.api.annotations.display.FriendlyName;
import org.mule.api.annotations.param.ConnectionKey;
import org.mule.api.annotations.param.Default;
import org.mule.api.annotations.param.Optional;
import org.mule.api.annotations.param.RefOnly;
import org.mule.modules.smb.config.SmbConnectorConfig;
import org.mule.modules.smb.utils.Utilities;

@Connector(name = "smb", friendlyName = "SMB Connector")
public class SmbConnector {

    @Config
    SmbConnectorConfig config;

    /**
     * Read file processor for reading in the contents of a file
     *
     * @param fileName
     *            File name to read in
     * @param autoDelete
     *            Should the file be deleted after reading
     * @return The file contents as a byte[]
     */
    @Processor
    public byte[] fileRead(@ConnectionKey @FriendlyName("File Name") String fileName,
    			@Default("") @FriendlyName("Directory Name") String dirName,
    			@Default("false") @FriendlyName("Delete after reading") boolean autoDelete) throws ConnectionException {
        return this.getConfig().getSmbClient().readFile(fileName, dirName, autoDelete);
    }

    /**
     * Write file processor for writing out data to a file
     *
     * @param fileName
     *            File name to be used for the write operation.
     * @param append
     *            Append payload to file, if it exists already
     * @param fileContent
     *            A byte[], String or InputStream containing the contents of the file to write.
     * @param encoding
     * 			  Character encoding of contents to write
     * @return void
     */
    @Processor
    public void fileWrite(@ConnectionKey @FriendlyName("File Name") @Required String fileName, 
    			@Default("") @FriendlyName("Directory Name") String dirName,
    			@Default("false") @FriendlyName("Append to file") boolean append,
    			@RefOnly @Default("#[payload]") Object fileContent, @Required @Default("UTF-8") String encoding) throws ConnectionException {
        this.getConfig().getSmbClient().writeFile(fileName, dirName, append, fileContent, encoding);
    }

    /**
     * Delete file processor for deleting a file
     *
     * @param fileName
     *            File name to be used for the delete operation.
     * @return boolean
     * 			  Indicates success or failure of operation
     */
    @Processor
    public boolean fileDelete(@ConnectionKey @FriendlyName("File Name") String fileName,
    			@Default("") @FriendlyName("Directory Name") String dirName) throws ConnectionException {
        return this.getConfig().getSmbClient().deleteFile(fileName, dirName);
    }

    /**
     * List directory processor for retrieving the contents of a directory
     *
     * @param dirName
     *            Folder name to be used for the list operation.
     * @param wildcard
     *            DOS style wildcard filter
     * @return A list of Maps, each Map containing attributes for each file
     */
    @Processor
    public List<String> directoryList(@ConnectionKey @FriendlyName("Folder Name") @Optional String dirName,
    			@Default("*.*") @FriendlyName("Wildcard") String wildcard) 
    			throws ConnectionException {
        String w = wildcard;
        if (!Utilities.isNotBlankOrEmptyOrNull(w)) {
            w = "*.*";
        }
        return this.getConfig().getSmbClient().listDirectory(dirName, w);
    }

    /**
     * Create directory processor for creating a directory
     *
     * @param dirName
     *            Folder name to be used for the write operation.
     * @return void
     */
    @Processor
    public boolean directoryCreate(@ConnectionKey @Required @FriendlyName("Folder Name") String dirName) throws ConnectionException {
        return this.getConfig().getSmbClient().createDirectory(dirName);
    }

    /**
     * Delete directory processor for deleting a directory
     *
     * @param dirName
     *            Directory name to be used for the delete operation.
     * @return boolean
     * 			  Indicates success or failure of operation 
     */
    @Processor
    public boolean directoryDelete(@ConnectionKey @FriendlyName("Directory Name") String dirName,
    		@Default("false") @FriendlyName("Recursive delete") boolean recursive) throws ConnectionException {
        return this.getConfig().getSmbClient().deleteDir(dirName, recursive);
    }

    /**
     * Set the config
     *
     * @param config
     *            SMBConnectorConfig to be used
     * @return void
     */
    public void setConfig(SmbConnectorConfig config) {
        this.config = config;
    }

    /**
     * Convenience method for getting the config
     *
     * @return The config as an SMBConnectorConfig
     */
    public SmbConnectorConfig getConfig() {
        return config;
    }
}