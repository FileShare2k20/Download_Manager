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
import java.util.logging.Level;
import java.util.logging.Logger;

public class Download implements Runnable {

    //public StringBuffer url;
    public StringBuffer urlString;
    private URL url;
    public long contentLength;
    public long downloaded;
    public boolean acceptRanges;
    public String progress;

    private int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    public static final int PAUSE = 1;
    public static final int DOWNLOADING = 2;
    public static final int COMPLETED = 3;
    public static final int ERROR = 4;

    
    public void run() {
        try {
            this.status = Download.DOWNLOADING;
            start();
        } catch (IOException ex) {
            //Logger.getLogger(Download.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Progress progressBar;
    public Download(String urlString) {
        this.urlString = new StringBuffer(urlString);
        try {
            if (isValid(urlString.toString())) {
                this.downloaded = 0;
                this.status = this.PAUSE;
                this.progress = "0%";
            }
        } catch (URISyntaxException ex) {
            url = null;
        } catch (MalformedURLException ex) {
            url = null;
        }
    }

    public boolean isValid(String urlStr) throws URISyntaxException, MalformedURLException {
        this.url = new URL(urlStr);
        this.url.toURI();
        return true;
    }

    public int start() throws IOException {

        //URL u = null;
        URLConnection connection = url.openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.29 Safari/537.36");
        connection.setRequestProperty("Range", "bytes" + downloaded + "-");
        connection.connect();

        // This should get you the size of the file to download (in bytes)
        contentLength = connection.getContentLength();
        double len = (double) contentLength / (double) (1024 * 1024);

        System.out.println(contentLength);
        System.out.println(len + "MB");
        System.out.println(url.getFile());
        BufferedInputStream is;
        try {
            is = new BufferedInputStream(((HttpURLConnection) connection).getInputStream());
        } catch (Exception e) {
            is = new BufferedInputStream(((HttpURLConnection) connection).getErrorStream());
        }
        /*
         * Map<String, List<String>> entries = connection.getHeaderFields();
         * entries.forEach((k, v) -> { System.out.println(k + ": " +
         * v.toString()); });
         */
        String fileType = connection.guessContentTypeFromStream(is);
        String type = connection.getContentType();
        if (fileType == null || fileType.length() > type.length()) {
            fileType = connection.getContentType();
        }
        fileType = fileType.substring(fileType.lastIndexOf('/') + 1);

        String _file = url.getFile();

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
        while (this.status == Download.DOWNLOADING) {// ((count = is.read(buffer, 0, 1024)) != -1) {
            if (((count = is.read(buffer, 0, 1024)) != -1)) {
                os.write(buffer, 0, count);
                //Progress.prg.setString(String.format("%.2f", ((double)os.getChannel().size()/(double)contentLength)*100) + "%");
                //Progress.prg.setValue((int)(((double)os.getChannel().size()/(double)contentLength)*100));
                progress = String.format("%.2f", ((double) os.getChannel().size() / (double) contentLength) * 100) + "%";
                System.out.println(progress);
                downloaded = os.getChannel().size();
                progressBar.prg.setValue((int)(((double)os.getChannel().size()/(double)contentLength)*100));
            }else{
                this.status = Download.COMPLETED;
            }
        }

        os.close();
        is.close();

        return 1;
    }
    
    public void resumeDownload() throws IOException{
        this.status = Download.DOWNLOADING;
        start();
    }

}
