package com.bcf.prototype;

import java.util.UUID;

public class Main {

    public static void main(String[] args) {
        // Bloom filter takes 1Mb, i,e 8 million bits
        System.out.println(">>>>>>>>>>>>>>>>Cuckoo Benchmarking>>>>>>>>>>>>>>>>");
        benchmarkCuckoo();

        System.out.println(">>>>>>>>>>>>>>>>Bloom Benchmarking>>>>>>>>>>>>>>>>");
        benchmarkBloom();
    }

    private static void benchmarkCuckoo() {

        int size = (int) Math.pow(2, 23); // about 8  million

        for (int i = 1; i <= 8; i++) {
            System.out.println("=======================BEGIN RUN=======================");
            CuckooFilter filter = new CuckooFilter(4, size);
            System.out.println("Running with bucket " + size + " and total keys " + (i * 1000 * 1000));
            for (int j = 0; j < i * 1000 * 1000; j++) {
                UUID uuid = UUID.randomUUID();
                filter.add(uuid.toString());
            }
            System.out.println("Keys Stored at index 1 count is " + (filter.keyStoredAtIndex1));
            System.out.println("Keys Stored at index 2 count is " + (filter.keyStoredAtIndex2));
            System.out.println("Key Stored after kicking existing entries count is " + (filter.keysStoredWithKicking));
            System.out.println("Dropped keys count is " + (filter.keysDroppedWithKicking));
            System.out.println("=======================END RUN=======================");
        }
    }

    private static void benchmarkBloom() {

        int size = (int) Math.pow(2, 23); // about 8  million
        for (int i = 1; i <= 8; i++) {
            System.out.println("=======================BEGIN RUN=======================");
            BloomFilter filter = new BloomFilter(size);
            System.out.println("Running with size " + size + " and total keys " + (i * 1000 * 1000) + " and 1 hash function");
            for (int j = 0; j < i * 1000 * 1000; j++) {
                UUID uuid = UUID.randomUUID();
                filter.add(uuid.toString());
            }
            System.out.println("Total collisions are " + filter.totalCollisions);
            System.out.println("=======================END RUN=======================");
        }
    }
}
