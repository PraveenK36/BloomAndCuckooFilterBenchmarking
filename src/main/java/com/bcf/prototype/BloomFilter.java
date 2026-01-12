package com.bcf.prototype;

import org.apache.commons.codec.digest.MurmurHash3;

public class BloomFilter implements Filter {
    int size;
    byte[] bits;
    int totalCollisions = 0;

    public BloomFilter(int size) {
        if (size % 8 != 0) {
            throw new IllegalArgumentException("Please provide the size in multiple of 8");
        }
        this.size = size;
        bits = new byte[size / 8];
    }

    @Override
    public void add(String key) {
        if (hasKey(key)) {
            totalCollisions++;
        }
        int index = getIndex(key);
        bits[index / 8] |= (1 << (index % 8));   // set bit
    }

    @Override
    public boolean hasKey(String key) {
        int index = getIndex(key);
        return (bits[index / 8] & (1 << (index % 8))) != 0;
    }

    private int getIndex(String key) {
        int i = MurmurHash3.hash32(key);
        return (i & 0x7fffffff) % size;
    }
}
