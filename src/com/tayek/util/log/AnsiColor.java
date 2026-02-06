package com.tayek.util.log;
import java.util.logging.Logger;
public class AnsiColor {
    // this does not work very well
    //https://stackoverflow.com/questions/5762491/how-to-print-color-in-console-using-system-out-println/5762502#5762502
    public static void main(String[] args) {
        logger.info("\\033[XXm");
        for(int i=20;i<37+1;++i) { logger.info("\033["+i+"m"+i+"\t\t\033["+(i+60)+"m"+(i+60)); }
        logger.info("\033[39m\\033[49m - Reset colour");
        logger.info("\\033[2K - Clear Line");
        logger.info("\\033[<L>;<C>H OR \\033[<L>;<C>f puts the cursor at line L and column C.");
        logger.info("\\033[<N>A Move the cursor up N lines");
        logger.info("\\033[<N>B Move the cursor down N lines");
        logger.info("\\033[<N>C Move the cursor forward N columns");
        logger.info("\\033[<N>D Move the cursor backward N columns");
        logger.info("\\033[2J Clear the screen, move to (0,0)");
        logger.info("\\033[K Erase to end of line");
        logger.info("\\033[s Save cursor position");
        logger.info("\\033[u Restore cursor position");
        logger.info(" ");
        logger.info("\\033[4m  Underline on");
        logger.info("\\033[24m Underline off");
        logger.info("\\033[1m  Bold on");
        logger.info("\\033[21m Bold off");
    }
    private static final Logger logger=Logger.getLogger(AnsiColor.class.getName());
}
