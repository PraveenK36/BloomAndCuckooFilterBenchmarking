# Approximate Membership Filters (Java)

Minimal Java implementations of **Bloom Filter** and **Cuckoo Filter** for experimentation and benchmarking.


## Bloom Filter
- Insert and lookup only
- Uses only 1 hash function
- False positives allowed
- No false negatives
- Deletions not supported

## Cuckoo Filter
- Insert, lookup, delete
- Fixed-size buckets with cuckoo kicking
- 8-bit fingerprints
- Insertions may replace existing keys in the high load
- Bucket count must be a power of two

## Notes
- Uses MurmurHash3
- Intended for learning and benchmarking, not production

## Tests
### Bloom Filter
| Size | Total Keys | Hash Functions | Total Collisions |
|-----:|-----------:|---------------:|-----------------:|
| 8,388,608 | 1,000,000 | 1 | 57,105 |
| 8,388,608 | 2,000,000 | 1 | 220,853 |
| 8,388,608 | 3,000,000 | 1 | 478,015 |
| 8,388,608 | 4,000,000 | 1 | 817,638 |
| 8,388,608 | 5,000,000 | 1 | 1,233,365 |
| 8,388,608 | 6,000,000 | 1 | 1,713,216 |
| 8,388,608 | 7,000,000 | 1 | 2,252,917 |
| 8,388,608 | 8,000,000 | 1 | 2,844,401 |

### Cuckoo Filter

| Buckets | Total Keys | Stored @ Index1 | Stored @ Index2 | Stored via Kicking | Dropped Keys |
|--------:|-----------:|----------------:|----------------:|-------------------:|-------------:|
| 8,388,608 | 1,000,000 | 999,999 | 1 | 0 | 0 |
| 8,388,608 | 2,000,000 | 1,999,959 | 41 | 0 | 0 |
| 8,388,608 | 3,000,000 | 2,999,654 | 346 | 0 | 0 |
| 8,388,608 | 4,000,000 | 3,998,782 | 1,216 | 2 | 0 |
| 8,388,608 | 5,000,000 | 4,996,410 | 3,579 | 11 | 0 |
| 8,388,608 | 6,000,000 | 5,991,741 | 8,182 | 76 | 1 |
| 8,388,608 | 7,000,000 | 6,983,495 | 16,289 | 216 | 0 |
| 8,388,608 | 8,000,000 | 7,969,873 | 29,496 | 627 | 4 |


