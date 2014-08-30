/* Copyright 2011 UNEP (http://www.unep.org)
 * This file is part of InforMEA Toolkit project.
 * InforMEA Toolkit is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * InforMEA Toolkit is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * You should have received a copy of the GNU General Public License along with
 * InforMEA Toolkit. If not, see http://www.gnu.org/licenses/.
 */
package edw.olingo.pojo;

import edw.olingo.constants.MimeType;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Describe the attributes for a decision document.
 * @author Cristian Romanescu {@code cristian.romanescu _at_ eaudeweb.ro}
 * @version 1.4.0, 10/28/2011
 * @since 0.5
 */
public class DecisionDocument {

    private static final Logger log = Logger.getLogger(DecisionDocument.class.getName());
    private String id;
    private String url;
    private byte[] content;
    private long size;
    private MimeType mimeType;
    private String language = "en";
    private String filename;

    public DecisionDocument(String id, String url, byte[] content, long size, MimeType mimeType, String language, String filename) {
        this.id = id;
        this.url = url;
        this.content = content;
        this.size = size;
        this.mimeType = mimeType;
        this.language = language;
        this.filename = filename;
    }

    public DecisionDocument(String id, String url, long size, MimeType mimeType, String language) {
        this.id = id;
        this.url = url;
        this.size = size;
        this.mimeType = mimeType;
        this.language = language;
    }

    public DecisionDocument(String id, byte[] content, MimeType mimeType, String language) {
        this.id = id;
        this.content = content;
        this.mimeType = mimeType;
        this.language = language;
        // In content was specified, the size will default to the length of the content
        if (content != null) {
            this.size = content.length;
        }
    }

    /**
     * @return Unique ID of the document
     * @since 1.3
     */
    public String getId() {
        return id;
    }

    /**
     * @since 1.3
     * @param id Document unique id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return URL for the decision where it may be browsed online
     */
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return Binary content (file from disk) of the decision
     */
    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    /**
     * @return Size of the document on disk
     */
    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    /**
     * @return Document type
     */
    public MimeType getMimeType() {
        return mimeType;
    }

    public void setMimeType(MimeType mimeType) {
        this.mimeType = mimeType;
    }

    /**
     * @return Document language. Use the ISO 2-letter code to specify the language. Example: en, fr etc.
     */
    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * @return Entity name as will be declared in metadata
     */
    public static String getEntityName() {
        return "DecisionDocument";
    }

}
