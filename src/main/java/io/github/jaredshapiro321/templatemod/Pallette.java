package io.github.jaredshapiro321.templatemod;

import java.awt.Color;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.TreeSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Pallette<E> implements Collection<E> {

	Comparator<E> brightnessComparator = new Comparator<E>() {
        @Override
        public int compare(E color1, E color2) {
        	Float br1 = (Float) calculateBrightness((Color) color1); 
        	
            return br1.compareTo(calculateBrightness((Color) color2));
        }
    };
	
    private TreeSet<E> colors = new TreeSet<>(brightnessComparator);
	
	@Override
	public int size() {
        return colors.size();
	}

	@Override
	public boolean isEmpty() {
		return colors.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return colors.contains(o);
	}

	@Override
	public Iterator<E> iterator() {
		return (Iterator<E>) colors.iterator();
	}

	@Override
	public Object[] toArray() {
        return colors.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
        return colors.toArray(a);
	}

	@Override
	public boolean add(E e) {
		boolean result = colors.add(e);
		sort();
        return result;
	}

	@Override
	public boolean remove(Object o) {
        return colors.remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return colors.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		return colors.addAll(c);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return retainAll(c);
	}

	@Override
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
