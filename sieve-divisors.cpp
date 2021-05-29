#include <iostream>
#include <vector>

const int64_t MXN = 1e4;
std::vector<int64_t> all_divisors[MXN + 1];
int64_t cnt_divisors[MXN + 1];
int64_t sum_divisors[MXN + 1];

// Complexity: O(n * log(n))
void generate_all_divisors(){
  for (int d = 1; d <= MXN; ++d) {
    for (int m = d; m <= MXN; m += d) {
      all_divisors[m].push_back(d);
    }
  }
}

// Complexity: O(n * log(n))
void count_divisors(){
  for (int d = 1; d <= MXN; ++d) {
    for (int m = d; m <= MXN; m += d) {
      ++cnt_divisors[m];
    }
  }
}

// Complexity: O(n * log(n))
void sum_of_divisors(){
  for (int d = 1; d <= MXN; ++d) {
    for (int m = d; m <= MXN; m += d) {
      sum_divisors[m] += d;
    }
  }
}