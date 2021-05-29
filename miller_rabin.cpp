#include <iostream>
#include <vector>
#include <algorithm>

int64_t mulmod(int64_t a, int64_t b, int64_t modulo){
  a %= modulo; b %= modulo;

  if (a <= 0xFFFFFFF && b <= 0xFFFFFFF) return (a * b) % modulo;
  if (b > a) std::swap(a, b);

  int64_t result = 0;
  while (a > 0 && b > 0) {
    if (b & 1) {
      result = (result + a) % modulo;
      if (result >= modulo) result -= modulo;
    }
    a <<= 1;
    if (a >= modulo) a -= modulo;
    b >>= 1;
  }
  return result;
}

int64_t powmod(int64_t base, int64_t exponent, int64_t modulo){
  int64_t result = 1;
  while (exponent > 0){
    if (exponent & 1) result = mulmod(result, base, modulo);
    base = mulmod(base, base, modulo);
    exponent >>= 1;
  }
  return result;
}

bool isPrime(int64_t p){

  const int64_t bitmaskPrimes2to31 =  (1 << 2) | (1 << 3) | (1 << 5) | (1 << 7) | (1 << 11) | 
                                      (1 << 13) | (1 << 17) | (1 << 19) | (1 << 23) | (1 << 29); 
  if (p < 31) return (bitmaskPrimes2to31 & (1 << p)) != 0;

  for (int64_t d : {2, 3, 5, 7, 11, 13, 17}) if (p % d == 0) return false;
  if (p < 17 * 19) return true;

  for (int64_t d : {
    19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97
  }) if (p % d == 0) return false;

  const int64_t STOP = 0;
  const int64_t TestAgainst1[] = {377687, STOP};
  const int64_t TestAgainst2[] = {31, 73, STOP};
  const int64_t TestAgainst3[] = {2, 7, 61, STOP};
  const int64_t TestAgainst4[] = {2, 13, 23, 1662803, STOP};
  const int64_t TestAgainst7[] = {2, 325, 9375, 28178, 450775, 9780504, 1795265022, STOP};

  const int64_t* testAgainst = TestAgainst7;
  if (p < 5329) testAgainst = TestAgainst1;
  else if (p < 9080191) testAgainst = TestAgainst2;
  else if (p < 4759123141ULL) testAgainst = TestAgainst3;
  else if (p < 1122004669633ULL) testAgainst = TestAgainst4;

  int64_t d = p - 1; d >>= 1;
  int64_t shift = 0;
  while (!(d & 1)) {shift++; d >>= 1;}

  do {
    int64_t x = powmod(*testAgainst++, d, p);
    if (x == 1 || x == p - 1) continue;

    bool maybePrime = false;
    for (int64_t r = 0; r < shift; r++) {
      x = mulmod(x, x, p);
      if (x == 1) return false;
      if (x == p - 1) {maybePrime = true; break;}
    }

    if (!maybePrime) return false;
  } while (*testAgainst != STOP);

  return true;
}