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
package edw.olingo.util;


import edw.olingo.InvalidValueException;
import edw.olingo.constants.MimeType;
import edw.olingo.config.Configuration;
import edw.olingo.constants.EntityType;
import edw.olingo.constants.MimeType;
import edw.olingo.pojo.*;
import edw.olingo.InvalidValueException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Various utility methods for toolkit
 * <br />
 * @author Cristian Romanescu {@code cristian.romanescu _at_ eaudeweb.ro}
 * @version 1.4.0, 10/28/2011
 * @since 1.3.3
 */
public class ToolkitUtil {

    private static final Logger log = Logger.getLogger(ToolkitUtil.class.getName());

    /**
     * Read decision document - file from disk and return its content
     * @param decision_id ID of the decision (used for friendly logging errors)
     * @param mime Document mime type. Used to to determine if file is binary or text
     * @param path Path to the document
     * @return Document content
     * @throws InvalidValueException If file does not exists or other error occurr
     */
    public static byte[] readDecisionDocument(String decision_id, MimeType mime, String path) throws InvalidValueException {
        byte[] ret = null;

        File f = new File(path);
        if (!f.exists()) {
            throw new InvalidValueException(String.format("'documents' is invalid (if exists, document->disk_path must point to a valid, existing file. Check informea_decisions_documents and path on disk: %s) (Affected decision with ID:%s)", path, decision_id));
        }
        if (!f.canRead()) {
            throw new InvalidValueException(String.format("'documents' is invalid (document->disk_path is valid and pointing to a file that access is denied. Check informea_decisions_documents and path on disk: %s) (Affected decision with ID:%s)", path, decision_id));
        }

        if (mime == MimeType.PDF || mime == MimeType.DOC || mime == MimeType.RTF || mime == MimeType.ODT) {
            try {
                ret = readBinaryFile(f);
            } catch (FileNotFoundException ex) {
                throw new InvalidValueException(String.format("Cannot read decision (id: %s), missing document from disk: %s", decision_id, path), ex);
            } catch (IOException ioex) {
                throw new InvalidValueException(String.format("Error reading decision (id: %s) document from disk: %s", decision_id, path), ioex);
            }
        } else {
            // Text file
            try {
                ret = readTextFile(f, Charset.defaultCharset()); // TODO: Variable charset
            } catch (FileNotFoundException ex) {
                throw new InvalidValueException(String.format("Cannot read decision (id: %s), missing document from disk: %s", decision_id, path), ex);
            } catch (IOException ioex) {
                throw new InvalidValueException(String.format("Error reading decision (id: %s) document from disk: %s", decision_id, path), ioex);
            }
        }

        return ret;
    }


    /**
     * Read decision document - file located at an specified URL
     * @param decision_id ID of the decision (used for friendly logging errors)
     * @param mime Document mime type. Used to to determine if file is binary or text
     * @param url Valid URL to the document
     * @return Document content
     * @throws InvalidValueException If file does not exists or other error occurr
     */
    public static byte[] downloadDecisionDocument(String decision_id, MimeType mime, String url) throws InvalidValueException {
        byte[] ret = null;
        BufferedInputStream is = null;
        URLConnection uc = null;
        ByteArrayOutputStream bb = null;
        try {
            URL the_url = new URL(url);
            uc = the_url.openConnection();
            // TODO: cristiroma - Should check uc.getResponseCode() that returns 200? (May return 301 or 302 though and still be valid).
            // Still problems may encounter if header is 200, but CMS returns an 'Not found page' - see DotNetNuke etc.
            is = new BufferedInputStream(uc.getInputStream());
            bb = new ByteArrayOutputStream(uc.getContentLength());
            log.info(String.format("File details: Content-Type: %s, Content-Length: %s bytes, Content-Encoding: %s",
                    uc.getContentType(), uc.getContentLength(), uc.getContentEncoding()));
            int b;
            while((b = is.read()) != -1) {
                bb.write(b);
            }
            ret = bb.toByteArray();
            log.fine(String.format("Downloaded %s bytes", ret.length));
        } catch(MalformedURLException mex) {
            throw new InvalidValueException(mex);
        } catch(IOException ioex) {
            throw new InvalidValueException(ioex);
        } finally {
            if(is != null) { try { is.close(); } catch(IOException ex) {} }
        }
        return ret;
    }


    /**
     * Read and return text file from disk
     * @param f File to read
     * @param encoding Encoding (optional). Default is to use System charset
     * @return File conent
     * @throws java.io.FileNotFoundException If file is not found
     * @throws java.io.IOException If file cannot be read
     */
    public static byte[] readTextFile(File f, Charset encoding) throws FileNotFoundException, IOException {
        byte[] ret = null;

        Charset charset = Charset.defaultCharset();
        if (encoding != null) {
            charset = encoding;
        }

        StringBuilder content = new StringBuilder();
        BufferedReader reader = null;
        try {
            char[] buf = new char[8192];
            reader = new BufferedReader(new FileReader(f.getAbsolutePath()));
            while (reader.read(buf) != -1) {
                content.append(buf);
            }
            ret = content.toString().getBytes(charset);
        } finally {
            if (reader != null) {
                reader.close();
            }
        }

        return ret;
    }

    /**
     * Read binary file from disk
     * @param f File to read
     * @return File content
     * @throws java.io.FileNotFoundException If file is not found
     * @throws java.io.IOException If file cannot be read
     */
    public static byte[] readBinaryFile(File f) throws FileNotFoundException, IOException {
        byte[] ret = null;
        // Binary file
        FileInputStream fin = null;
        try {
            fin = new FileInputStream(f);
            ret = new byte[fin.available()];
            fin.read(ret);
        } finally {
            if (fin != null) {
                fin.close();
            }
        }
        return ret;
    }


    /**
     * Safely retrieve a value from request
     * @param name Name of the parameter
     * @param req Request object
     * @return Value, or empty string if not present
     */
    public static String getRequestValue(String name, HttpServletRequest req) {
        String ret = null;
        if (req != null) {
            ret = req.getParameter(name);
            if(ret == null) {
                ret = "";
            } else {
                ret = ret.trim();
            }
        }
        return ret;
    }


    /**
     * Safely get an integer parameter from request
     * @param name Name of the parameter
     * @param req Request object
     * @return Value, or 0 if not present
     */
    public static int getRequestInteger(String name, HttpServletRequest req) {
        int ret = 0;
        if (req != null) {
            String v = req.getParameter(name);
            if(v != null) {
                v = v.trim();
                try { ret = Integer.parseInt(v); } catch(Exception e) {}
            }
        }
        return ret;
    }


    /**
     * Check if parameter is on request
     * @param name Name of the parameter
     * @param req Request object
     * @return true if exists, it doesn't matter the value
     */
    public static boolean isOnRequest(String name, HttpServletRequest req) {
        if (req != null) {
            return req.getParameter(name) != null;
        }
        return false;
    }


    /**
     * Check if request contains a checkbox and this was checked
     * @param name Name of the parameter
     * @param req Request object
     * @return true if checked (value was "ON")
     */
    public static boolean getRequestCheckbox(String name, HttpServletRequest req) {
        if (req != null) {
            String value = req.getParameter(name);
            return value != null && "ON".equalsIgnoreCase(value.trim());
        }
        return false;
    }


    /**
     * Output the URL to a resource on server (i.e. image)
     * @param request Request object
     * @param path relative path to the file (optional)
     * @return Full URL to the resource
     */
    public static String url(HttpServletRequest request, String path) {
        if(path != null) {
            return String.format("%s%s", request.getContextPath(), path);
        }
        return request.getContextPath();
    }
//
//
//    /**
//     * Validate that all decisions are working
//     * @param p Producer to retrieve data from
//     * @return null if success, error message if failed
//     */
//    public static String validateDecisions(Producer p) {
//        String ret = null;
//        IDataProvider dataProvider = null;
//        try {
//            dataProvider = DataProviderFactory.getDataProvider(IDecision.class);
//            dataProvider.openResources();
//            List<AbstractDecision> obs = p.getDecisions(dataProvider, null, 0, null);
//            for(AbstractDecision ob : obs) {
//                ob.validate();
//            }
//        } catch(Exception e) {
//            ret = e.getMessage();
//        } finally {
//            if(dataProvider != null) {
//                dataProvider.closeResources();
//            }
//        }
//        return ret;
//    }
//
//
//    /**
//     * Validate that all meetings are working
//     * @param p Producer to retrieve data from
//     * @return null if success, error message if failed
//     */
//    public static String validateMeetings(Producer p) {
//        String ret = null;
//        IDataProvider dataProvider = null;
//        try {
//            dataProvider = DataProviderFactory.getDataProvider(IMeeting.class);
//            dataProvider.openResources();
//            List<AbstractMeeting> obs = p.getMeetings(dataProvider, null, 0, null);
//            for(AbstractMeeting ob : obs) {
//                ob.validate();
//            }
//        } catch(Exception e) {
//            ret = e.getMessage();
//        } finally {
//            if(dataProvider != null) {
//                dataProvider.closeResources();
//            }
//        }
//        return ret;
//    }
//
//
//    /**
//     * Validate that all contacts are working
//     * @param p Producer to retrieve data from
//     * @return null if success, error message if failed
//     */
//    public static String validateContacts(Producer p) {
//        String ret = null;
//        IDataProvider dataProvider = null;
//        try {
//            dataProvider = DataProviderFactory.getDataProvider(IContact.class);
//            dataProvider.openResources();
//            List<AbstractContact> obs = p.getContacts(dataProvider, null, 0, null);
//            for(AbstractContact ob : obs) {
//                ob.validate();
//            }
//        } catch(Exception e) {
//            ret = e.getMessage();
//        } finally {
//
//        }
//        return ret;
//    }
//
//
//    /**
//     * Validate that all country reports are working
//     * @param p Producer to retrieve data from
//     * @return null if success, error message if failed
//     */
//    public static String validateCountryReports(Producer p) {
//        String ret = null;
//        IDataProvider dataProvider = null;
//        try {
//            dataProvider = DataProviderFactory.getDataProvider(ICountryReport.class);
//            dataProvider.openResources();
//            List<AbstractCountryReport> obs = p.getCountryReports(dataProvider, null, 0, null);
//            for(AbstractCountryReport ob : obs) {
//                ob.validate();
//            }
//        } catch(Exception e) {
//            ret = e.getMessage();
//        } finally {
//            if(dataProvider != null) {
//                dataProvider.closeResources();
//            }
//        }
//        return ret;
//    }
//
//
//    /**
//     * Validate that all country profiles are working
//     * @param p Producer to retrieve data from
//     * @return null if success, error message if failed
//     */
//    public static String validateCountryProfiles(Producer p) {
//        String ret = null;
//        IDataProvider dataProvider = null;
//        try {
//            dataProvider = DataProviderFactory.getDataProvider(ICountryProfile.class);
//            dataProvider.openResources();
//            List<AbstractCountryProfile> obs = p.getCountryProfiles(dataProvider, null, 0, null);
//            for(AbstractCountryProfile ob : obs) {
//                ob.getTreaty();
//                ob.getCountry();
//                ob.getEntryIntoForce();
//                ob.getUpdated();
//            }
//        } catch(Exception e) {
//            ret = e.getMessage();
//        } finally {
//            if(dataProvider != null) {
//                dataProvider.closeResources();
//            }
//        }
//        return ret;
//    }
//
//
//    /**
//     * Validate that all national plans are working
//     * @param p Producer to retrieve data from
//     * @return null if success, error message if failed
//     */
//    public static String validateNationalPlans(Producer p) {
//        String ret = null;
//        IDataProvider dataProvider = null;
//        try {
//            dataProvider = DataProviderFactory.getDataProvider(INationalPlan.class);
//            dataProvider.openResources();
//            List<AbstractNationalPlan> obs = p.getNationalPlans(dataProvider, null, 0, null);
//            for(AbstractNationalPlan ob : obs) {
//                ob.getId();
//                ob.getTreaty();
//                ob.getCountry();
//                ob.getType();
//                ob.getTitle();
//                ob.getUrl();
//                ob.getSubmission();
//                ob.getUpdated();
//            }
//        } catch(Exception e) {
//            ret = e.getMessage();
//        } finally {
//            if(dataProvider != null) {
//                dataProvider.closeResources();
//            }
//        }
//        return ret;
//    }
//
//
//    /**
//     * Validate that all sites are working
//     * @param p Producer to retrieve data from
//     * @return null if success, error message if failed
//     */
//    public static String validateSites(Producer p) {
//        String ret = null;
//        IDataProvider dataProvider = null;
//        try {
//            dataProvider = DataProviderFactory.getDataProvider(ISite.class);
//            dataProvider.openResources();
//            List<AbstractSite> obs = p.getSites(dataProvider, null, 0, null);
//            for(AbstractSite ob : obs) {
//                ob.validate();
//            }
//        } catch(Exception e) {
//            ret = e.getMessage();
//        } finally {
//            if(dataProvider != null) {
//                dataProvider.closeResources();
//            }
//        }
//        return ret;
//    }
//
//
//    /**
//     * Do HTTP POST submission to an URL and return the result. At the moment uses URLConnection. Could be improved to use HttpURLConnection to handle HTTP errors.
//     * @param url URL to submit to
//     * @param params HTTP POST parameters key=value
//     * @return The output or an empty string if error occurrs.
//     */
//    public static String httpPost(String url, Map<String, String> params) {
//        StringBuilder ret = new StringBuilder(2000);
//        try {
//            // Construct data
//            StringBuilder post = new StringBuilder(500);
//            int c = 0, max = params.keySet().size();
//            for(Map.Entry<String, String> item : params.entrySet()) {
//                post.append(URLEncoder.encode(item.getKey(), "UTF-8"));
//                post.append("=");
//                post.append(URLEncoder.encode(item.getValue(), "UTF-8"));
//                if(c < max - 1) {
//                    post.append("&");
//                }
//                c++;
//            }
//            // Send data
//            URL urlOb = new URL(url);
//            URLConnection conn = urlOb.openConnection();
//            conn.setDoOutput(true);
//            conn.setConnectTimeout(4000);
//            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
//            wr.write(post.toString());
//            wr.flush();
//
//            // Get the response
//            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//            String line;
//            while ((line = rd.readLine()) != null) {
//                ret.append(line);
//            }
//            wr.close();
//            rd.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return ret.toString();
//    }
//
//
//    /**
//     * Provide an status for each entity
//     * @param request HTTP request
//     * @param endpointURL Service endpoint (.svc)
//     * @return Status per entity type
//     */
//    public static Map<EntityType, Map<String, Object>> getEntityStatus(HttpServletRequest request, String endpointURL) {
//        IDataProvider dp = null;
//        Map<EntityType, Map<String, Object>> ret = new HashMap<EntityType, Map<String, Object>>();
//        Configuration cfg = Configuration.getInstance();
//        List<EntityType> entities = EntityType.getEntities();
//
//        Producer p = new Producer();
//        for(EntityType key : entities) {
//            try {
//                boolean inUse = cfg.isUse(key);
//                Integer count = 0;
//                String url = "";
//                Map<String, Object> itemCfg = new HashMap<String, Object>();
//                itemCfg.put("inUse", inUse);
//                itemCfg.put("count", count);
//                itemCfg.put("url", url);
//                if(inUse) {
//                    dp = DataProviderFactory.getDataProvider(key);
//                    dp.openResources();
//                    count = p.getCount(key, dp);
//                    url = EntityType.getEndpointURL(request, key, endpointURL);
//                    itemCfg.put("count", count);
//                    itemCfg.put("url", url);
//                }
//                ret.put(key, itemCfg);
//            } catch(Exception ex) {
//                ex.printStackTrace();
//                log.log(Level.SEVERE, "Error getting entity status", ex);
//            } finally {
//                if(dp != null) {
//                    dp.closeResources();
//                }
//            }
//        }
//        return ret;
//    }
//
//    /**
//     * Check whether the user has selected at least one entity when choosing in configuration screen.
//     * @param request HTTP request
//     * @return true if selection is valid
//     */
//    public static boolean isValidEntitiesSelection(HttpServletRequest request) {
//        boolean useDecisions = ToolkitUtil.getRequestCheckbox("useDecisions", request);
//        boolean useMeetings = ToolkitUtil.getRequestCheckbox("useMeetings", request);
//        boolean useContacts = ToolkitUtil.getRequestCheckbox("useContacts", request);
//        boolean useCountryReports = ToolkitUtil.getRequestCheckbox("useCountryReports", request);
//        boolean useCountryProfiles = ToolkitUtil.getRequestCheckbox("useCountryProfiles", request);
//        boolean useNationalPlans = ToolkitUtil.getRequestCheckbox("useNationalPlans", request);
//        boolean useSites = ToolkitUtil.getRequestCheckbox("useSites", request);
//
//        return useDecisions || useMeetings || useContacts || useCountryReports
//                || useCountryProfiles || useNationalPlans || useSites;
//    }
//
//    /**
//     * Save user selection on HTTP session
//     * @param session HTTP Session
//     * @param request HTTP Request
//     */
//    public static void saveEntitiesSelectionOnSession(HttpSession session, HttpServletRequest request) {
//        Configuration cfg = Configuration.getInstance();
//
//        cfg.setUseDecisions(ToolkitUtil.getRequestCheckbox("useDecisions", request));
//        boolean useMeetings = ToolkitUtil.getRequestCheckbox("useMeetings", request);
//        boolean useContacts = ToolkitUtil.getRequestCheckbox("useContacts", request);
//        boolean useCountryReports = ToolkitUtil.getRequestCheckbox("useCountryReports", request);
//        boolean useCountryProfiles = ToolkitUtil.getRequestCheckbox("useCountryProfiles", request);
//        boolean useNationalPlans = ToolkitUtil.getRequestCheckbox("useNationalPlans", request);
//        boolean useSites = ToolkitUtil.getRequestCheckbox("useSites", request);
//
//        //TODO: session.setAttribute("", new Boolean(useDecisions));
//        //TODO: session.setAttribute(Configuration.USE_MEETINGS, new Boolean(useMeetings));
//        //TODO: session.setAttribute(Configuration.USE_CONTACTS, new Boolean(useContacts));
//        //TODO: session.setAttribute(Configuration.USE_COUNTRY_REPORTS, new Boolean(useCountryReports));
//        //TODO: session.setAttribute(Configuration.USE_COUNTRY_PROFILES, new Boolean(useCountryProfiles));
//        //TODO: session.setAttribute(Configuration.USE_NATIONAL_PLANS, new Boolean(useNationalPlans));
//        //TODO: session.setAttribute(Configuration.USE_SITES, new Boolean(useSites));
//    }
//


    /**
     * Retrieve the toolkit version as a string
     * @return
     */
    public static String getToolkitVersion() {
        return String.format("%s.%s.%s%s",
                Configuration.MAJOR, Configuration.MINOR, Configuration.REVISION,
                Configuration.BETA ? " beta" : "");
    }

    /**
     * Check for toolkit update
     * @return Structure containing update information
     */
    public static Map<String, Object> checkUpdate() {
        Map<String, Object> ret = new HashMap<String, Object>();
        boolean needsUpdate = false;
        ret.put("needsUpdate", needsUpdate);
        ret.put("success", false);
        ret.put("remoteVersion", "");
        ret.put("changes", "");
        try {
            URL url = new URL(Configuration.UPDATE_URL);
            URLConnection c = url.openConnection();
            c.setConnectTimeout(4000);
            java.io.InputStream fis = c.getInputStream();
            java.util.Properties props = new java.util.Properties();
            props.load(fis);

            int major = Integer.parseInt(props.getProperty("MAJOR"));
            int minor = Integer.parseInt(props.getProperty("MINOR"));
            int revision = Integer.parseInt(props.getProperty("REVISION"));
            boolean beta = Boolean.parseBoolean(props.getProperty("BETA"));
            String changes = props.getProperty("CHANGES");

            if(major > Configuration.MAJOR ) {
                needsUpdate = true;
            } else {
                if(minor > Configuration.MINOR) {
                    needsUpdate = true;
                } else if(minor == Configuration.MINOR) {
                    if(revision > Configuration.REVISION) {
                        needsUpdate = true;
                    } else if(revision == Configuration.REVISION) {
                        if(Configuration.BETA && !beta) {
                            needsUpdate = true;
                        }
                    }
                }
            }
            ret.put("remoteVersion", String.format("%s.%s.%s%s", major, minor, revision, beta ? " beta" : ""));
            ret.put("needsUpdate", needsUpdate);
            ret.put("success", true);
            ret.put("changes", changes);
        } catch(Exception ex) {
            ret.put("success", false);
            log.log(Level.WARNING, "Cannot check for new version!", ex);
        }
        return ret;
    }


    public static boolean compareStrings(String a, String b) {
        if(a == null && b == null) {
            return true;
        } else if(a == null || b == null) {
            return false;
        }
        return a.equals(b);
    }
}