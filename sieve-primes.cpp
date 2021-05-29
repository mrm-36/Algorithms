#include <iostream>
#include <cstring>

const int64_t MXN = 1e5;
const int64_t MX2 = (MXN >> 1) + 1;

bool is_prime[MX2 + 1];

// Complexity: O(n * log(n))
void fill_sieve(){
  std::memset(is_prime, 1, sizeof(is_prime));
  is_prime[0] = 0;
  for (int64_t i = 1; 2 * i * i < MX2; ++i){
    if (!is_prime[i]) continue;
    for (int64_t m = 3 * i + 1; m < MX2; m += 2 * i + 1) is_prime[m] = 0;
  }
}

bool isPrime(int64_t x){
  if (!(x & 1)) return x == 2;
  return is_prime[x >> 1];
}