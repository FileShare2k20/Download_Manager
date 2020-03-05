
import java.io.*;
import java.net.*;
import java.net.URL;

public class Download implements Runnable {

    //public StringBuffer url;
    public StringBuffer urlString;
    private URL url;
    public int contentLength;
    public int downloaded;
    public boolean acceptRanges;
    public String progress;
    public Progress progressBar;

    private int status;

    public static final int PAUSE = 1;
    public static final int DOWNLOADING = 2;
    public static final int COMPLETED = 3;
    public static final int ERROR = 4;

    public void run() {
        try {
            this.status = Download.DOWNLOADING;

            //progressBar initiation code
            progressBar.setVisible(true);
            progressBar.setValue(0);
            progressBar.setString("0%");
            //progressBar initiation code ends here

            start();
            //System.exit(0);
        } catch (IOException ex) {
            //Logger.getLogger(Download.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //
    public Download(String urlString) throws IOException {
        this.urlString = new StringBuffer(urlString);
        try {
            if (isValid(urlString.toString())) {
                this.downloaded = 0;
                this.status = this.PAUSE;
                this.progress = "0%";
                this.contentLength = -1;
            }
            acceptRanges = isPausable(this.url.openConnection());
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    private boolean isPausable(URLConnection connection) {

        //@author Ayush Tripathi
        try {
            return (connection.getHeaderField("Accept-Ranges") != null);
        } catch (Exception ex) {
            return false;
        }

    }

    public int start() throws IOException {

        URLConnection connection = url.openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.29 Safari/537.36");
        if (this.acceptRanges) {
            connection.setRequestProperty("Range", "bytes=" + downloaded + "-");
        } else {
            progressBar.setUnPausable(true);
        }
        connection.connect();

        if (contentLength == -1) {
            contentLength = connection.getContentLength();
        }

        RandomAccessFile outFile = null;

        BufferedInputStream is;
        try {
            is = new BufferedInputStream(((HttpURLConnection) connection).getInputStream());
        } catch (Exception e) {
            is = new BufferedInputStream(((HttpURLConnection) connection).getErrorStream());
        }

        String fileType = connection.guessContentTypeFromStream(is);
        String type = connection.getContentType();
        if (fileType == null || fileType.length() > type.length()) {
            fileType = connection.getContentType();
        }
        fileType = fileType.substring(fileType.lastIndexOf('/') + 1);

        String fileName = url.getFile();

        fileName = fileName.substring(fileName.lastIndexOf('/') + 1) + ((fileName.contains(".")) ? "" : ("." + fileType));

        //If this doesn't work, just remove it!
        File file = new File(fileName);
        if (!file.getAbsoluteFile().getParentFile().exists()) {
            file.getAbsoluteFile().getParentFile().mkdirs();
        }

        //try {
        outFile = new RandomAccessFile(file.getName(), "rw");
        outFile.seek(downloaded);
        //} catch()

        byte[] buffer;
        int count = 0;
        while (this.status == Download.DOWNLOADING) {
            if (contentLength - downloaded >= 1024) {
                buffer = new byte[1024];
            } else {
                System.out.println(downloaded);
                System.out.println(contentLength);
                buffer = new byte[contentLength - downloaded];
            }
            if (((count = is.read(buffer)) != -1)) {
                outFile.write(buffer, 0, count);
                downloaded += count;
                double completedPercent = ((double) outFile.getChannel().size() / (double) contentLength) * 100;
                progress = String.format("%.2f", completedPercent) + "%";
                progressBar.setValue((int) completedPercent);
                progressBar.setString(progress);
                System.out.println(progress);
            }
            if (downloaded == contentLength) {
                this.status = Download.COMPLETED;
            }
        }

        outFile.close();
        is.close();

        return 1;
    }

}
