package iv.util;
import java.io.*;
import java.net.URL;
public class Config {
	public static void main(final String[] argument) {
	}
	public static URL getCodeBase() {
		return Util.url((useFilePath?("file:///"+fPath):("http://"+server+hPath))+'/');
	}
	public static boolean useFilePath=false;
	public static boolean isOfficial=false;
	public static boolean debuggingGui=false;
	public static boolean firstViewIsScrollable=true;
	public static boolean usingSecondView=false;
	public static int frameWidth=225,frameHeight=530,plaf=-1;
	public static String imageFilename="images/sample.jpeg",splashImageFilename="images/splash.gif";;
	public static final Runtime runtime=Runtime.getRuntime();
	public static final String osName=System.getProperty("os.name");
	public static final String osArch=System.getProperty("os.arch");
	public static final String osVersion=System.getProperty("os.version");
	public static final Object[][] iconKeyAndName={
		{ "+","zoomin.png" },
		{ "-","zoomout.png" },
		{ "+R90","rotateright.png" },
		{ "+R270","rotateleft.png" },
		{ "+V","flipv.png" },
		{ "+H","fliph.png" },
		{ "Print","print.png" },
		{ "Annotate","annotate.png" },
	};
	static {
		System.out.println("os.name="+osName);
		System.out.println("os.arch="+osArch);
		System.out.println("os.version="+osVersion);
	}
	//public static final String fPath=""+(osName.equals("Windows 98")?"h:/htdocs/jj/take3/imageViewer":osName.equals("Windows XP")?"I:/ray/workspace/imageViewer":/* add more here for your platform*/".");
	public static final String fPath=""+(osName.equals("Windows 98")?"h:/htdocs/jj/take3/imageViewer":osName.equals("Windows XP")?"D:/home/ray/dev/workspace.old2/imageViewer":/* add more here for your platform*/".");
	public static final boolean atHere=true;
	public static final String hPath=""+(atHere?"/~ray/jj/take3/imageViewer":"/your/path");
	public static final String server=""+(atHere?"tayek.com":"jjdev.com");
	static {
		File path=new File(fPath);
		System.out.println(path);
		if(false&&!path.exists()) // test for applet security
			throw new RuntimeException("path: "+path+" is null or does not exist!");
		if(false) // test for applet security
			try {
				File foo=new File(path, "foo.txt");
				OutputStream o=new FileOutputStream(foo);
				System.out.println("writing to foo.txt");
				o.write(0);
				o.flush();
				o.close();
			}
			catch(FileNotFoundException e) {
				System.out.println("e.toString()");
			}
			catch(IOException e) {
			System.out.println("e.toString()");
			}
	}
	private Config() {
	}
	private static final String rcsid="$RCSfile: Config.java,v $:$Revision: 1.27 $";
}
