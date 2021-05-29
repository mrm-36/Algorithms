#include <iostream>

// Complexity: O(log(b))
int64_t mod_pow(int64_t a, int64_t b, int64_t m){
  if (b == 0) return 1;
  if (b & 1) return a * mod_pow(a * a % m, (b - 1) >> 1, m) % m;
  return mod_pow(a * a % m, b >> 1, m) % m;
}