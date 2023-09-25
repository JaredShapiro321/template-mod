package io.github.jaredshapiro321.templatemod.util;

public class Color extends java.awt.Color {

	public Color(String hex) {
		super(Integer.parseInt(hex.replaceAll("#", "").substring(0, 2), 16), 
				Integer.parseInt(hex.replaceAll("#", "").substring(2, 4), 16), 
				Integer.parseInt(hex.replaceAll("#", "").substring(4, 6), 16));
	}
	
	public String toHex() {
		// Ensure that the values are within the valid range (0-255)
        // = Math.min(255, Math.max(0, red));
        // = Math.min(255, Math.max(0, green));
        //blue = Math.min(255, Math.max(0, blue));
		
		
		Component b = new Component()

        // Use Integer.toHexString to convert each component to hexadecimal
        String redHex = Integer.toHexString(getRed());
        String greenHex = Integer.toHexString(getGreen());
        String blueHex = Integer.toHexString(getBlue());
        
        System.out.println(redHex);
        System.out.println(blueHex);
        System.out.println(greenHex);
        
        // Ensure that the hexadecimal strings are two characters long
        if (redHex.length() == 1) {
            redHex = "0" + redHex;
        }
        if (greenHex.length() == 1) {
            greenHex = "0" + greenHex;
        }
        if (blueHex.length() == 1) {
            blueHex = "0" + blueHex;
        }

        // Concatenate the hexadecimal strings with a '#' prefix
        return "#" + redHex + greenHex + blueHex;
	}
	
	static class Hexadecimal {
		
		public String toString() {
			return "";
		}
	}
	
	static class Component {
        int value;
        public Component(String value) {
        	this.value = Integer.parseInt(value, 16);
        }
        
        public int getInt() {
        	return value;
        }
        
        public String getHex() {
        	String hexValue = Integer.toHexString(value);
        	return (hexValue.length() == 1) ? hexValue : "0" + hexValue;
        }
        
        public String getBin() {
        	 return Integer.toBinaryString(value);
        }
    }
}
