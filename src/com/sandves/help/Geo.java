package com.sandves.help;

import android.annotation.SuppressLint;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Geo {

	public static String parseCoordinates2(double lat, double lon) {
		int degreeLat = (int) lat;
		int degreeLon = (int) lon;

		int minLat = (int)((lat-degreeLat)*60);
		double secLat = (((lat-degreeLat)*60)-minLat)*60;
		
		int minLon = (int)((lon-degreeLon)*60);
		double secLon = (((lon-degreeLon)*60)-minLon)*60;
		
		char latDir = degreeLat > 0 ? 'N' : 'S';
		char lonDir = degreeLon > 0 ? 'E' : 'W';

		return degreeLat + "\u00B0 " + minLat + "' " + roundTwoDecimals(secLat) + "\" " + latDir + ",\n" + 
		degreeLon + "\u00B0 " + minLon + "' " + roundTwoDecimals(secLon) + "\" " + lonDir;
	}

	public static String parseCoordinates(double lat, double lon) {
		int degreeLat = (int) lat;
		int degreeLon = (int) lon;

		String minLat = roundTwoDecimals((lat-degreeLat)*60.0);
		String minLon = roundTwoDecimals((lon-degreeLon)*60.0);
		
		char latDir = degreeLat > 0 ? 'N' : 'S';
		char lonDir = degreeLon > 0 ? 'E' : 'W';

		return degreeLat + "\u00B0 " + minLat + "' " + latDir + ", "  + 
		degreeLon + "\u00B0 " + minLon + "' " + lonDir;
	}

	private static String roundTwoDecimals(double d) {
		DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.US);
		otherSymbols.setDecimalSeparator('.');
		
		DecimalFormat twoDForm = new DecimalFormat("##.##", otherSymbols);
		twoDForm.setRoundingMode(RoundingMode.HALF_UP);
		return twoDForm.format(d);
	}
	
	public static String parseDecimalLocation(double lat, double lon) {
		DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.US);
		otherSymbols.setDecimalSeparator('.');
		
		DecimalFormat fourDForm = new DecimalFormat("##.####", otherSymbols);
		fourDForm.setRoundingMode(RoundingMode.HALF_UP);
		return fourDForm.format(lat) + ", " + fourDForm.format(lon);
	}
	
	@SuppressLint("SimpleDateFormat")
	public static String parseTime(long time) {
		
		Date date = new Date(time);
		
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss, dd/MM/yy");
		return sdf.format(date);
		 // "yyyy-MM-dd HH:mm:ss" -> prints something like 2011-01-08 13:35:48
	}

}
