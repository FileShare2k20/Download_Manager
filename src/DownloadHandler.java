
import java.io.IOException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Jana Anik
 */
public class DownloadHandler {

    Download download;

    public DownloadHandler(String url) throws IOException {
        download = new Download(url);
        Thread downloadThread = new Thread(download);
        downloadThread.start();
    }

    public void pause() throws Exception {
        if(!download.acceptRanges)
        {
            throw new Exception("Pause not supported.");
        }
        download.setStatus(Download.PAUSE);
    }

    public void resume() {
        download.setStatus(Download.DOWNLOADING);
    }
    
    

}
