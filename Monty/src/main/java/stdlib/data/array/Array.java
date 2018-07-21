package stdlib.data.array;

import java.util.Iterator;

import parser.MontyException;

public class Array implements Iterable<Object>, Cloneable {
	protected Object[] array;

	/*
	 * Description.
	 */
	public Array() {
		array = new Object[0];
	}

	/*
	 * Description.
	 */
	public Array(int length) {
		array = new Object[length];
		for (int i = 0; i < length; i++)
			array[i] = null;
	}

	/*
	 * Description.
	 */
	public Array(Array array) {
		this.array = array.toArray();
	}

	/*
	 * Description.
	 */
	public Array(Object array[]) {
		this.array = new Object[array.length];
		int i = 0;
		for (Object e : array)
			this.array[i++] = e;
	}

	/*
	 * Description.
	 */
	public boolean isEmpty() {
		for (Object e : array)
			if (e != null)
				return true;
		return false;
	}

	/*
	 * Description.
	 */
	public boolean contains(Object toSearch) {
		for (Object e : array)
			if (e.equals(toSearch))
				return true;
		return false;

	}

	/*
	 * Description.
	 */
	public Array subArray(int begin, int end) {
		int length = end - begin + 1;
		Array newArray = new Array(length);
		for (int i = 0; i < length; i++)
			newArray.set(i, array[i]);

		return newArray;
	}

	/*
	 * Description.
	 */
	public Array subArray(int begin) {
		int length = array.length - begin + 1;
		Array newArray = new Array(length);
		for (int i = 0; i < length; i++)
			newArray.set(i, array[i]);

		return newArray;
	}

	/*
	 * Description.
	 */
	public Array setLength(int length) {
		Object[] newArray = new Object[length];
		int i = 0;
		while (i < newArray.length && i < array.length)
			newArray[i] = array[i++];

		while (i < length)
			newArray[i++] = null;

		array = newArray;
		return this;
	}

	/*
	 * Description.
	 */
	public Array append(Object element) {
		var newArray = new Object[array.length + 1];
		newArray[array.length] = element;
		int i = 0;
		for (Object e : array)
			newArray[i++] = e;
		array = newArray;
		return this;
	}

	/*
	 * Description.
	 */
	public Array append(Object[] elements) {
		var newArray = new Object[array.length + elements.length];
		int i = 0;
		for (Object e : array)
			newArray[i++] = e;

		for (Object e : elements)
			newArray[i++] = e;
		array = newArray;

		return this;
	}

	/*
	 * Description.
	 */
	public Array append(Array elements) {
		var newArray = new Object[array.length + elements.length()];
		int i = 0;
		for (Object e : array)
			newArray[i++] = e;

		for (Object e : elements)
			newArray[i++] = e;
		array = newArray;

		return this;
	}

	/*
	 * Description.
	 */
	public Object[] toArray() {
		return array;
	}

	/*
	 * Description.
	 */
	public int lastIndex() {
		return array.length - 1;
	}

	/*
	 * Description.
	 */
	public Object lastElement() {
		return array[array.length - 1];
	}

	/*
	 * Description.
	 */
	public Object get(int index) {
		if (index >= array.length)
			new MontyException("Index " + index + " is too large for length " + array.length);
		return array[index];
	}

	/*
	 * Description.
	 */
	public Array set(int index, Object element) {
		array[index] = element;
		return this;
	}

	/*
	 * Description.
	 */
	public Array set(Object[] elements) {
		array = elements;
		return this;
	}

	/*
	 * Description.
	 */
	public int length() {
		return array.length;
	}

	/*
	 * Description.
	 */
	public Array replaceAll(Object toBeReplaced, Object replacement) {
		for (int i = 0; i < array.length; i++)
			if (array[i].equals(toBeReplaced))
				array[i] = replacement;
		return this;
	}

	/*
	 * Description.
	 */
	public Array replaceFirst(Object toBeReplaced, Object replacement) {
		for (int i = 0; i < array.length; i++)
			if (array[i].equals(toBeReplaced)) {
				array[i] = replacement;
				break;
			}
		return this;
	}

	/*
	 * Description.
	 */
	public Array replaceLast(Object toBeReplaced, Object replacement) {
		for (int i = array.length - 1; i < array.length; i++)
			if (array[i].equals(toBeReplaced)) {
				array[i] = replacement;
				break;
			}
		return this;
	}

	/*
	 * Description.
	 */

	public Array fit() {
		int i = array.length;
		while (array[--i] == null)
			;
		var newArray = new Object[++i];
		while (--i >= 0)
			newArray[i] = array[i];

		array = newArray;
		return this;

	}

	/*
	 * Description.
	 */
	public boolean equals(Array array) {
		if (array.length() != this.array.length)
			return false;
		int i = 0;
		for (Object e : array)
			if (!this.array[i++].equals(e))
				return false;
		return true;

	}

	public Array copy() {
		try {
			return (Array) clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String toString() {
		var stringBuilder = new StringBuilder();
		stringBuilder.append('[');
		int i = 0;
		for (Object e : array) {
			stringBuilder.append(e.toString());
			if (i + 1 < array.length)
				stringBuilder.append(',');
			i++;
		}
		stringBuilder.append(']');
		return stringBuilder.toString();

	}

	@Override
	public Iterator<Object> iterator() {
		return new Iterator<Object>() {
			int counter = 0;

			@Override
			public boolean hasNext() {
				return counter < array.length;

			}

			@Override
			public Object next() {
				return array[counter++];
			}
		};
	}
}