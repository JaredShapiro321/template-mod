package io.github.jaredshapiro321.templatemod.util;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Pallette {

	static Comparator<Color> brightnessComparator = new Comparator<Color>() {
        @Override
        public int compare(Color color1, Color color2) {
        	Float br1 = (Float) calculateBrightness(color1); 
        	
            return br1.compareTo(calculateBrightness(color2));
        }
    };
	
 
    private static TreeSet<Color> colors = new TreeSet<>(brightnessComparator);
	private static HashMap<Color, Double> maxDistances = new HashMap<>();
    
	public static void populateMaxDistances(Collection<Color> colors) {
    	maxDistances = getMaxDistances(colors);
	}
	
	public void populateMaxDistances() {
		populateMaxDistances(colors);
	}
	
	
    public static HashMap<Color, Double> getMaxDistances(Collection<Color> colors) {
    	HashMap<Color, Double> maxDistances = new HashMap<>();
    	
    	for(Color color : colors) {
    		double distance = color.getMaxDistance(colors);
    		maxDistances.put(color, distance);
		}
    	
    	return maxDistances;
    }
    
    public HashMap<Color, Double> getMaxDistances() {
		return getMaxDistances(colors);
	}
	
	public int size() {
        return colors.size();
	}
	
	public boolean isEmpty() {
		return colors.isEmpty();
	}

	public boolean contains(Color color) {
		return colors.contains(color);
	}

	public Iterator<Color> iterator() {
		return colors.iterator();
	}

	public Object[] toArray() {
        return colors.toArray();
	}

	public Color[] toArray(Color[] color) {
        return colors.toArray(color);
	}

	public boolean add(Color color) {
		boolean result = colors.add(color);
		sort();
        return result;
	}

	public boolean remove(Color color) {
        return colors.remove(color);
	}


	public boolean containsAll(Collection<Color> c) {
		return colors.containsAll(c);
	}


	public boolean addAll(Collection<? extends Color> color) {
		return colors.addAll(color);
	}


	public boolean removeAll(Collection<Color> color) {
		return removeAll(color);
	}


	public boolean retainAll(Collection<?> color) {
		return retainAll(color);
	}


	public void clear() {
		colors.clear();
	}
	
	private void sort() {

	}
	
	private static float calculateBrightness(Color color) {
        // Calculate brightness using the formula: 0.299*R + 0.587*G + 0.114*B
		float[] hsbvals = new float[3];
		Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsbvals);
		return hsbvals[2];
    }
}
