package iv.util;
import lizard.tiff.Tiff;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.TreeMap;
public class Util {
	private Util() {
	}
	public static void sleep(final long time) {
		try {
			Thread.sleep(time);
		} catch(InterruptedException e) {
			System.out.println("sleep was interrupted");
		}
	}
	public static Map ImageIconMap(final URL codeBase) { // not thread safe
		final Map iconMap=new TreeMap();
		for(int i=0;i<Config.iconKeyAndName.length;i++) {
			try {
				final String string=(String)Config.iconKeyAndName[i][1];
				System.out.println("loading image icon from: "+string);
				final Icon icon=new ImageIcon(new URL(codeBase+"images/"+string));
				if(icon!=null)
					iconMap.put(Config.iconKeyAndName[i][0],icon);
				else 
					System.out.println("can not load image icon: "+string);
			} catch(MalformedURLException e){
				System.out.println(e+", ImageIcon: "+string);
			}
			
		}
		return iconMap;
	}
	public static String xy2s(final int x,final int y) {
		return "("+x+","+y+")";
	}
	public static String p2s(final Point point) {
		return "("+point.getX()+","+point.getY()+")";
	}
	public static String r2s(final Rectangle rectangle) {
		final Dimension dimension=rectangle.getSize();
		return p2s(rectangle.getLocation())+"+("+dimension.getWidth()+","+dimension.getHeight()+")";
	}
	public static URL url(final String string) {
		URL url;
		try {
			url=new URL(string);
		} catch(MalformedURLException e) {
			System.out.println(string+" caused a "+e);
			url=null;
		}
		return url;
	}
	public static Image loadImage(final String string,final Component component) {
		System.out.println("loading image from string value: "+string);
		final boolean doGC=false;
		final GC gc=doGC?new GC("loading getImage "+string):null;
		final Image image=loadImage_(string,component);
		if(image!=null) {
			if(image instanceof BufferedImage)
				System.out.println("it's a buffered image");
			//else if(image instanceof VolatileImage)
			//System.out.println("it's a volitile image");
			//else if(image instanceof sun.awt.windows.WImage) // does not exist on slak
			//System.out.println("it's a W image");
			else System.out.println("it's some other type of image");
		}
		else
			System.out.println("can not load image from: "+string);
		if(gc!=null) gc.after();
		if(gc!=null) gc.print();
		return image;
	}
	private static boolean isOk(final Image image) {
		boolean rc=false;
		if(image!=null) {
			if(image.getWidth(null)==-1||image.getHeight(null)==-1)
				System.out.println("getImage has -1 for width or height!");
			else if(image.getWidth(null)==0&&image.getHeight(null)==0)
				System.out.println("strange, getImage has 0 for width and height!");
			else if(image.getWidth(null)==0||image.getHeight(null)==0)
				System.out.println("strange, getImage has 0 for width or height!");
			else
				rc=true;
		}
		return rc;
	}
	public static Image loadImage(final URL url,final Component component) {
		System.out.println("loading image from url: "+url);
		final time t=new time();
		final Toolkit toolkit=Toolkit.getDefaultToolkit();
		final MediaTracker mediaTracker=new MediaTracker(component);
		Image image;
		if((image=toolkit.getImage(url))!=null) {
			mediaTracker.addImage(image,0);
			try {
				mediaTracker.waitForID(0);
			} catch(InterruptedException e) {
				System.out.println("can not load getImage: "+url);
				image=null;
			}
			if(!isOk(image))
				return null;
		}
		System.out.println("dt for loadImage()="+t.dt());
		if(image==null) {
			System.out.println("can not load image: "+url+" in in Util.loadImage(URL,Component)!");
		}
		return image;
	}
	public static Image loadTiffImage(final URL url) {
		System.out.println("loading till image from url: "+url);
		Image image=null;
		try {
			final InputStream inputStream = url.openStream();
			final Tiff tiff=new Tiff();
			if(tiff!=null) {
				tiff.readInputStream(inputStream);
				image=tiff.getImage(0);
			}
		}
		catch(IOException e) {
			throw new RuntimeException(e.toString());
		}
		if(image==null) {
			System.out.println("can not load til image: "+url+" in in Util.loadTiffImage(URL)!");
		}
		return image;
	}
	public static Image loadImage_(final String string,final Component component) {
		System.out.println("loading image from string value: "+string);
		Image image=null;
		final URL url=url(string);
		if(url==null) {
			System.out.println("url is null in Util.loadImage(String,Component)!");
			return null;
		}
		System.out.println("checking for tiff");
		if(string.endsWith(".tif")||string.endsWith(".tiff")) {
			image=loadTiffImage(url);
		}
		else
			image=loadImage(url,component);
		if(image==null) {
			System.out.println("can not load image: "+string+" in in Util.loadImage_(String,Component)!");
		}
		return image;
	}
	public static BufferedImage fakeBufferedImage(final int width,final int height) {
		final BufferedImage bufferedImage=new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
		final Graphics2D g=bufferedImage.createGraphics();
		fakeImage(g,width,height);
		return bufferedImage;
	}
	private static void fakeImage(final Graphics2D g,final int width,final int height) {
		final Color oldColor=g.getColor();
		g.setColor(Color.red);
		final AffineTransform old=new AffineTransform(g.getTransform());
		final AffineTransform t=new AffineTransform(old);
		double scaleFactor=.6;
		draw_(g,"R",width,height,Color.white);
		for(int i=0;i<4;i++) {
			g.setTransform(old);
			draw(g,width,height,string[i],i,t,scaleFactor,color[i]);
			for(int j=0;j<4;j++) {
				//if(i!=j)
				draw(g,width,height,string[j],j,t,scaleFactor,true?color[(i*j+j+3)%color.length]:Color.white);
			}
		}
		if(false)
			return;
		scaleFactor=.35;
		draw_(g,"R",width,height,Color.white);
		for(int i=0;i<4;i++) {
			g.setTransform(old);
			g.setTransform(AffineTransform.getTranslateInstance(width*(1-scaleFactor)/2,height*(1-scaleFactor)/2));
			draw(g,(int)(width*scaleFactor),(int)(height*scaleFactor),string[i],i,t,scaleFactor,color[i]);
			for(int j=0;j<4;j++) {
				//if(i!=j)
				try {
					draw(g,(int)(width*scaleFactor),(int)(height*scaleFactor),string[j],j,t,scaleFactor,color[(i+1)%color.length]);
				} catch(InternalError e) {
					System.out.println(e.toString());
				}
			}
		}
		g.setTransform(old);
        g.setColor(Color.blue);
		final Point origin=new Point(0,0);
		final Rectangle r=new Rectangle(origin,new Dimension(width,height));
		r.grow(-2,-2);
		g.draw(r);
		g.drawLine(0,0,width,height);
		g.drawLine(width,0,0,height);
		g.setColor(oldColor);
	}
	private static Color[] color={ Color.red,Color.blue,Color.green,Color.yellow,Color.magenta,Color.orange};
	private static String[] string={ "F","G","R","P"};
	private static void draw(final Graphics2D g,final int width,final int height,final String string,final int r,final AffineTransform t,final double scaleFactor,final Color color) {
		final double angle=Math.PI*r/2;
		g.transform(AffineTransform.getScaleInstance(scaleFactor,scaleFactor));
		g.transform(AffineTransform.getRotateInstance(r*Math.PI/2,width/2,height/2));
		switch(r%4) {
			case 0: g.transform(AffineTransform.getTranslateInstance(0,0)); break;
			case 1: g.transform(AffineTransform.getTranslateInstance(-(height/2-width/2),-(width/scaleFactor-(height/2+width/2)))); break;
			case 2: g.transform(AffineTransform.getTranslateInstance(-(width/scaleFactor-width),-(height/scaleFactor-height))); break;
			case 3: g.transform(AffineTransform.getTranslateInstance(-(height/scaleFactor-(height/2+width/2)),-(width/2-height/2))); break;
		}
		draw_(g,string,width,height,color);
	}
	private static void draw_(final Graphics2D g,final String string,final int width,final int height,final Color color) {
		final Color old=g.getColor();
		final int pointsize=height*7/8;//(int)Math.ceil(height/72.27);
		final Font font=new Font("Serif",Font.PLAIN,pointsize);
		g.setFont(font);
		final FontMetrics fontMetrics=g.getFontMetrics();
		final Rectangle2D r2d=fontMetrics.getStringBounds(string,g);
		final int setWidth=Math.round((float)r2d.getWidth());
		/*final*/ int fontHeight=fontMetrics.getHeight();
		/*final*/ int ascent=fontMetrics.getAscent();
		final int descent=fontMetrics.getDescent();
		ascent-=descent; // hack, their font metrics seem to be screwed up somehow
		fontHeight=ascent+descent; // no lead
		final int baseline=height/2-fontHeight/2+ascent;
		g.setColor(color);
		g.drawString(string,width/2-setWidth/2,baseline);
		g.setColor(old);
	}
	private static final String rcsid="$RCSfile: Util.java,v $:$Revision: 1.22 $";
}
