package io.github.jaredshapiro321.templatemod.util;

import java.util.Collection;
import java.util.Comparator;

public class Color extends java.awt.Color {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9103766616635786165L;
	
	public Color(String hex) {
		super(Integer.parseInt(hex.replaceAll("#", "").substring(0, 2), 16), 
				Integer.parseInt(hex.replaceAll("#", "").substring(2, 4), 16), 
				Integer.parseInt(hex.replaceAll("#", "").substring(4, 6), 16));
	}
	
	public Color(int r, int g, int b) {
		super(r, g, b);
	}
	
	public Color(int r, int g, int b, int a) {
		super(r, g, b, a);
	}
	
	public Color(float r, float g, float b) {
		super(r, g, b);
	}
	
	public Color(float r, float g, float b, float a) {
		super(r, g, b, a);
	}
	
	public String toHexString() {
		Component r = new Component(getRed());
		Component g = new Component(getGreen());
		Component b = new Component(getBlue());

		String redHex = r.toHexString();
		String greenHex = g.toHexString();
		String blueHex = b.toHexString();
        
        return redHex + greenHex + blueHex;
	}
	
    public static double getRedMeanDistance(Color c1, Color c2) {
    	int r1 = c1.getRed();
    	int g1 = c1.getGreen();
    	int b1 = c1.getBlue();
    	
    	int r2 = c2.getRed();
    	int g2 = c2.getGreen();
    	int b2 = c2.getBlue();
    	
    	return getRedMeanDistance(r1, g1, b1, r2, g2, b2);
    }
    
    
    public static double getRedMeanDistance(int r1, int g1, int b1, int r2, int g2, int b2) {
    	int deltaR = r1 - r2;
    	int deltaG = g1 - g2;
    	int deltaB = b1 - b2;
    	
    	double rMean = 0.5 * (r1 + r2);

    	return Math.sqrt((2 + (rMean / 256)) * (deltaR * deltaR) + 4 * (deltaG * deltaG) + ((2 + ((255 - rMean) / 256)) * (deltaB * deltaB)));
    }

    public static double getMaxDistance(Color color, Collection<Color> colors) {
    	double distance = 0.0;
    	
    	for (Color color1 : colors) {
    		double newDistance = getDistanceSquared(color, color1);
    		if (newDistance > distance) distance = newDistance;
    	}
    	
    	return distance;
    }
    
    
    public double getMaxDistance(Collection<Color> colors) {
    	return getMaxDistance(this, colors);
    }
    
    public double getRedMeanDistance(Color color) {
    	int r1 = getRed();
    	int g1 = getGreen();
    	int b1 = getBlue();
    	
    	int r2 = color.getRed();
    	int g2 = color.getGreen();
    	int b2 = color.getBlue();
    	
    	return getRedMeanDistance(r1, g1, b1, r2, g2, b2);
    }
	
    public int getDistanceSquared(Color color) {
    	int r1 = getRed();
    	int g1 = getGreen();
    	int b1 = getBlue();
    	
    	int r2 = color.getRed();
    	int g2 = color.getGreen();
    	int b2 = color.getBlue();
    	
    	return getDistanceSquared(r1, g1, b1, r2, g2, b2);
    }
    
    public static int getDistanceSquared(Color color1, Color color2) {
    	int r1 = color1.getRed();
    	int g1 = color1.getGreen();
    	int b1 = color1.getBlue();
    	
    	int r2 = color2.getRed();
    	int g2 = color2.getGreen();
    	int b2 = color2.getBlue();
    	
    	return getDistanceSquared(r1, g1, b1, r2, g2, b2);
    }
    
    
    public static int getDistanceSquared(int r1, int g1, int b1, int r2, int g2, int b2) {
    	int deltaR = r2 - r1;
    	int deltaG = g2 - g1;
    	int deltaB = b2 - b1;
    	
    	return (deltaR * deltaR) + (deltaG * deltaG) + (deltaB * deltaB);
    }
    
	
	
	static class Component {
        int value;
        public Component(String binaryOrHexString) {
        	if (isBinaryString(binaryOrHexString)) {
        		this.value = Integer.parseInt(binaryOrHexString, 2);
        	} else {
            	this.value = Integer.parseInt(binaryOrHexString, 16);
        	}
        }
        
        public Component(int value) {
        	this.value = value;
        }
        
        public int toInt() {
        	return value;
        }
        
        public String toHexString() {
        	String hexValue = Integer.toHexString(value);
        	return (hexValue.length() == 1) ? "0" + hexValue : hexValue ;
        }
        
        public String toBinaryString() {
        	 return Integer.toBinaryString(value);
        }
        
        private static boolean isBinaryString(String str) {
            // Remove leading and trailing whitespace
            str = str.trim();

            // Check if the string contains only '0' and '1' characters
            return str.matches("[01]+");
        }
        
        
    }
}