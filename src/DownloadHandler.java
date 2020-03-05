
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
    Thread downloadThread;

    public DownloadHandler(String url) throws IOException {
        download = new Download(url);
        downloadThread = new Thread(download);
        
        //Added code for progress bar.
        Progress prg = new Progress(this);
        download.progressBar = prg;
        //Progress code ends here
        
        downloadThread.start();
    }

    public void pause() throws Exception {
        if(!download.acceptRanges)
        {
            throw new Exception("Pause not supported.");
        }
        download.setStatus(Download.PAUSE);
        
        System.out.println(download.downloaded);
    }

    public void resume() {
        download.setStatus(Download.DOWNLOADING);
        downloadThread = new Thread(download);
        System.out.println(download.downloaded);
        downloadThread.start();
    }
    
    

}
