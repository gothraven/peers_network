package com.upec.peers.network.nio;

import java.util.Collection;

public final class Bytes {

	public static byte[] toArray(Collection<? extends Number> collection) {
		Object[] boxedArray = collection.toArray();
		int len = boxedArray.length;
		byte[] array = new byte[len];

		for (int i = 0; i < len; ++ i) {
			array[i] = ((Number) checkNotNull(boxedArray[i])).byteValue();
		}
		return array;
	}

	private static <T> T checkNotNull(T reference) {
		if (reference == null) {
			throw new NullPointerException();
		} else {
			return reference;
		}
	}
}
