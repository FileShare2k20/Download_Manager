
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
public class Download_Tester {

    //public static Progress progress;

    public static void main(String[] args) throws IOException {
        /*Download down = new Download("https://funksyou.com/fileDownload/Songs/128/27251.mp3");
        progress = new Progress();
        progress.setVisible(true);
        //progress.prg.setValue(0);
        if(down.start()==1){
            progress.setVisible(false);
            System.exit(0);
        }
         */
        //Download down = new Download("https://www.researchgate.net/profile/Dr_DK_Kaushik/publication/264005162_An_Introduction_to_Microprocessor_8085/links/53fb5d750cf2364ccc03d728/An-Introduction-to-Microprocessor-8085.pdf");
        //down.start();
        Runnable r1 = new Download("https://www.researchgate.net/profile/Dr_DK_Kaushik/publication/264005162_An_Introduction_to_Microprocessor_8085/links/53fb5d750cf2364ccc03d728/An-Introduction-to-Microprocessor-8085.pdf");
        Thread th1 = new Thread(r1);
        //Thread th2 = new Thread(new Download("http://www.lamission.edu/lifesciences/lecturenote/AliPhysio1/Heart.pdf"));
        Progress prg1 = new Progress((Download) r1);
        prg1.setVisible(true);
        //Progress prg2 = new Progress();
        th1.run();
    }

}


