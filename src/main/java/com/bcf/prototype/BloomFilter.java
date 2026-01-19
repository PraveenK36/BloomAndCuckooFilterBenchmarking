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
    public boolean add(String key) {
        if (hasKey(key)) {
            totalCollisions++;
        }
        int index = getIndex(key);
        bits[index / 8] |= (1 << (index % 8));   // set bit
        return true;
    }

    @Override
    public boolean hasKey(String key) {
        int index = getIndex(key);
        return (bits[index / 8] & (1 << (index % 8))) != 0;
    }

    @Override
    public boolean delete(String key) {
        throw new IllegalArgumentException("Bloom filter does not support deletes");
    }

    private int getIndex(String key) {
        int i = MurmurHash3.hash32(key);
        return (i & 0x7fffffff) % size;
    }
}
