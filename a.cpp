#include <iostream>

int main(){
    int64_t n, m, a; std::cin >> n >> m >> a;
    std::cout << ((n / a + (n % a == 0 ? 0 : 1))
                * (m / a + (m % a == 0 ? 0 : 1)));
    return 0;
}