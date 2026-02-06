package com.tayek.util.io;

import gnu.getopt.Getopt;
import gnu.getopt.LongOpt;
import java.util.logging.Logger;

final class GetOptSupport {
    private GetOptSupport() {}

    static LongOpt[] defaultLongOpts(StringBuffer sb) {
        LongOpt[] longopts=new LongOpt[3];
        longopts[0]=new LongOpt("help",LongOpt.NO_ARGUMENT,null,'h');
        longopts[1]=new LongOpt("outputdir",LongOpt.REQUIRED_ARGUMENT,sb,'o');
        longopts[2]=new LongOpt("maximum",LongOpt.OPTIONAL_ARGUMENT,null,2);
        return longopts;
    }

    static boolean handleCommonOption(
            int c,Getopt getopt,LongOpt[] longopts,StringBuffer sb,Runnable help) {
        String arg;
        switch(c) {
            case 0:
                arg=getopt.getOptarg();
                logger.info("Got long option with value '"+(char)(Integer.parseInt(sb.toString()))
                        +"' with argument "+((arg!=null)?arg:"null"));
                help.run();
                return true;
            case 1:
                logger.info("I see you have return in order set and that "
                        +"a non-option argv element was just found "+"with the value '"+getopt.getOptarg()+"'");
                help.run();
                return true;
            case 2:
                arg=getopt.getOptarg();
                logger.info("I know this, but pretend I didn't");
                logger.info("We picked option "+longopts[getopt.getLongind()].getName()+" with value "
                        +((arg!=null)?arg:"null"));
                help.run();
                return true;
            case 'W':
                logger.info("Hmmm. You tried a -W with an incorrect long "+"option name");
                return true;
            case ':':
                logger.info("You need an argument for option "+(char)getopt.getOptopt());
                return true;
            case '?':
                logger.info("The option '"+(char)getopt.getOptopt()+"' is not valid");
                return true;
            default:
                return false;
        }
    }

    static void logLongOpts(String options,LongOpt[] longopts) {
        logger.info(options);
        for(LongOpt longopt:longopts) logger.info(String.valueOf(longopt));
    }

    static void logRemainingArgs(Getopt getopt,String[] argv) {
        for(int i=getopt.getOptind();i<argv.length;i++)
            logger.info("Non option argv element: "+argv[i]+"\n");
    }

    private static final Logger logger=Logger.getLogger(GetOptSupport.class.getName());
}
