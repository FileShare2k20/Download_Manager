/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Jana Anik
 */
import java.io.*;
import java.net.*;
import java.net.URL;
import java.lang.Object;
import java.util.List;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;

public class Download {

    private URL url = null;
    private String str = null;
    private StringBuffer m_url;

    public Download(String m_url) {
        this.m_url = new StringBuffer(m_url);
    }

    public static boolean isValid(StringBuffer url) {
        try {
            new URL(url.toString()).toURI();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    private boolean isPausable(URLConnection connection)
    {
        try
        {
            return(connection.getHeaderField("Accept-Ranges")!=null);
        } catch(Exception ex) {
            return false;
        }
    }

    //Validation and Conversion of url
    /*public static URL isValid(String s) {
        if (!s.startsWith("https://")) {
            return null;
        }
        URL u = null;

        try {
            u = new URL(m_url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return u;
    }*/
    public int start() throws IOException {

        //Download u = new Download();
        URL u = null;

        //String n_url = URLEncoder.encode(m_url.toString(), "UTF-8");
        //System.out.println(n_url);

       // m_url = new StringBuffer(n_url);
        if (isValid(m_url)) {
            try {
                u = new URL(m_url.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        URLConnection connection = u.openConnection();
        /*if (connection instanceof HttpsURLConnection) {
            connection = (HttpsURLConnection) u.openConnection();
        } else if (connection instanceof HttpURLConnection) {
            connection = (HttpURLConnection) u.openConnection();
        }*/
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.29 Safari/537.36");
        connection.connect();
        

        // This should get you the size of the file to download (in bytes)
        int contentLength = connection.getContentLength();

        System.out.println(contentLength);
        System.out.println(u.getFile());
        BufferedInputStream is;
        try {
            is = new BufferedInputStream(((HttpURLConnection) connection).getInputStream());
        } catch (Exception e) {
            is = new BufferedInputStream(((HttpURLConnection) connection).getErrorStream());
        }
        Map<String, List<String>> entries = connection.getHeaderFields();
        entries.forEach((k, v) -> {
            System.out.println(k + ": " + v.toString());
        });
        String fileType = connection.guessContentTypeFromStream(is);
        String type = connection.getContentType();
        if (fileType == null || fileType.length() > type.length()) {
            fileType = connection.getContentType();
        }
        fileType = fileType.substring(fileType.lastIndexOf('/') + 1);
        String _file = u.getFile();

        System.out.println(_file + " " + fileType);
        _file = _file.substring(_file.lastIndexOf('/') + 1) + ((_file.contains(".")) ? "" : ("." + fileType));

        File file = new File(_file);
        if (!file.getAbsoluteFile().getParentFile().exists()) {
            System.out.println(file.getAbsoluteFile().getParentFile().mkdirs());

        }
        System.out.println(file.getName());

        FileOutputStream os = new FileOutputStream((_file).trim());

        byte[] buffer = new byte[1024];
        int count = 0;
        while ((count = is.read(buffer, 0, 1024)) != -1) {
            os.write(buffer, 0, count);
            System.out.println(((double)os.getChannel().size() / (double)contentLength) * 100);
        }

        os.close();
        is.close();

        return 1;
    }

}
