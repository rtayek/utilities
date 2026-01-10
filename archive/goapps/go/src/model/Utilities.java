package model;
import java.awt.Point;
import java.awt.Rectangle;
public class Utilities {
	public static String toCoordinates(Point point) {
		if(!rectangle.contains(point))
			return null;
		int x = point.x;
		if (x >= ('I' - 'A')) x++; // 'I' is omitted
		String s = "" + (char) ('A' + x);
		s += point.y + 1; // offset by one
		return s;
	}
	static final Rectangle rectangle=new Rectangle(0,0,25,25);
	public static Point fromCoordinates(String coordinates) {
		String string = coordinates.toUpperCase();
		char c = string.charAt(0);
		if (!('A' <= c && c <= 'Z') || c == 'I') return null;
		int y = Integer.valueOf(coordinates.substring(1));
		if (!(1 <= y && y <= Board.maxSize)) return null;
		if (c >= 'I') c--; // 'I' is omitted
		Point point = new Point(c - 'A', y - 1);  // offset by one
		return point;
	}
	public static String method() {
		return Thread.currentThread().getStackTrace()[2].getClassName() + '.' + Thread.currentThread().getStackTrace()[2].getMethodName() + "()";
	}
	public static Point[] starPoints(int n) {
		Point[] starPoints = null;
		if (n < starPointTable.length) {
			String s = starPointTable[n];
			starPoints = new Point[s.length() / 2];
			for (int i = 0; i < s.length() / 2; i++)
				starPoints[i] = new Point(s.charAt(2 * i) - 'a' + 1, s.charAt(2 * i + 1) - 'a' + 1);
		}
		return starPoints;
	}
	private static String starPointTable[] = { "", // 0
			"aa", // 1
			"", // 2
			"", // 3
			"", // 4
			"cc", // 5
			"", // 6
			"dd", // 7
			"cccffcff", // 8
			"ggcccggc", // 9
			"hhccchhc", // 10
			"iiccciicff", // 11
			"iidddiid", // 12
			"jjdddjjdgg", // 13
			"kkdddkkd", // 14
			"lldddllddhlhhdhlhh", // 15
			"mmdddmmd", // 16
			"nndddnnddiniidinii", // 17
			"oodddood", // 18
			"ppdddppddjpjjdjpjj" // 19
	};
}
