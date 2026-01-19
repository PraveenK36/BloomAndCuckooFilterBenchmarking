package com.bcf.prototype;

import org.apache.commons.codec.digest.MurmurHash3;

import java.util.concurrent.ThreadLocalRandom;

public class CuckooFilter implements Filter {

    int fingerPrintBits = 8;
    int slotCount;
    int size;
    byte[][] buckets;
    int maxKicks = 100;
    int keyStoredAtIndex1;
    int keyStoredAtIndex2;
    int keysStoredWithKicking;
    int keysDroppedWithKicking = 0;

    public CuckooFilter(int slotCount, int size) {
        if ((size & (size - 1)) != 0) {
            throw new IllegalArgumentException("size must be a power of two");
        }
        this.slotCount = slotCount;
        this.size = size;
        this.buckets = new byte[size][slotCount];
    }

    @Override
    public boolean add(String key) {

        int index1 = index1(key);
        byte fp = getFp(key);

        int index2 = index2(index1, fp);

        if (insertIntoBucket(index1, fp)) {
            keyStoredAtIndex1++;
            return true;
        } else if (insertIntoBucket(index2, fp)) {
            keyStoredAtIndex2++;
            return true;
        }
        int index = ThreadLocalRandom.current().nextBoolean() ? index1 : index2;
        byte fpToReplace = fp;
        for (int i = 0; i < maxKicks; i++) {
            int random = ThreadLocalRandom.current().nextInt(slotCount);
            byte temp = buckets[index][random];
            buckets[index][random] = fpToReplace;
            fpToReplace = temp;
            index = index2(index, fpToReplace);
            if (insertIntoBucket(index, fpToReplace)) {
                keysStoredWithKicking++;
                return true;
            }
        }

        // indicates that we are not able to find a free spot and replaced an existing key
        keysDroppedWithKicking++;

        return false;
    }

    @Override
    public boolean hasKey(String key) {
        int index1 = index1(key);
        byte fp = getFp(key);

        int index2 = index2(index1, fp);
        return hasKey(index1, fp) || hasKey(index2, fp);
    }

    @Override
    public boolean delete(String key) {
        if (hasKey(key)) {
            int index1 = index1(key);
            byte fp = getFp(key);
            if (!deleteFromBucket(index1, fp)) {
                int index2 = index2(index1, fp);
                deleteFromBucket(index2, fp);
            }
            return true;
        }
        return false;
    }

    private byte getFp(String key) {
        int hash = MurmurHash3.hash32(key);
        int fp = (hash & ((1 << fingerPrintBits) - 1));
        if (fp == 0) {
            fp = 1;
        }
        return (byte) fp;
    }

    private int index1(String key) {
        return MurmurHash3.hash32(key) & (size - 1);
    }

    private int index2(int index1, byte fp) {

        return ((index1 ^ MurmurHash3.hash32(fp)) & (size - 1));
    }

    boolean insertIntoBucket(int index, byte fp) {
        for (int i = 0; i < slotCount; i++) {
            if (buckets[index][i] == 0) {
                buckets[index][i] = fp;
                return true;
            }
        }
        return false;
    }

    boolean deleteFromBucket(int index, byte fp) {
        for (int i = 0; i < slotCount; i++) {
            if (buckets[index][i] == fp) {
                buckets[index][i] = 0;
                return true;
            }
        }
        return false;
    }

    private boolean hasKey(int index, byte fp) {
        for (int i = 0; i < slotCount; i++) {
            if (buckets[index][i] == fp) {
                return true;
            }
        }
        return false;
    }
}
