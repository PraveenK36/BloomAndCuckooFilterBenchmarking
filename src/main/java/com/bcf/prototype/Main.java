package com.bcf.prototype;

import java.util.UUID;

public class Main {

    public static void main(String[] args) {
        // Bloom filter takes 1Mb, i,e 8 million bits

        for (int i = 1; i <= 8; i++) {
            BloomFilter filter = new BloomFilter(8000 * 1000);

            for (int j = 0; j < i * 1000 * 1000; j++) {
                UUID uuid = UUID.randomUUID();
                filter.add(uuid.toString());
            }
            System.out.println("Total collisions with keys " + (i * 1000 * 100) + " are " + filter.totalCollisions);
        }

    }
}
