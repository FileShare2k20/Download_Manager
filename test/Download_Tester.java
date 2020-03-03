
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Jana Anik
 */
public class Download_Tester {

    public static void main(String[] args) throws IOException {
        Download down = new Download("https://funksyou.com/fileDownload/Songs/128/27251.mp3");
        down.start();

        Thread thread = new Thread(new Runnable(){
            @Override
            public void run(){
                Download down = new Download("https://funksyou.com/fileDownload/Songs/128/27251.mp3");
                try {
                    down.start();
                } catch (IOException ex) {
                    Logger.getLogger(Download_Tester.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        Thread thread2 = new Thread(new Runnable(){
            @Override
            public void run(){
                Download down = new Download("https://funksyou.com/fileDownload/Songs/128/27251.mp3");
                try {
                    down.start();
                } catch (IOException ex) {
                    Logger.getLogger(Download_Tester.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        thread.start();
        thread2.start();
    }

}
