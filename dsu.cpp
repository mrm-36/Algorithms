#include <iostream>
#include <algorithm>

//Disjoint Set Union

int parent[100001];
int size[100001];

void make_set(int x){
    parent[x] = x;
    size[x] = 1;
}

int find(int x){
    return (parent[x] == x) ? x : (parent[x] = find(parent[x]));
}

void merge(int x, int y){
    x = find(x); y = find(y);
    if (x == y) return;
    if (rand() % 2) std::swap(x, y);
    parent[y] = x;
    size[x] += size[y];
}

int main() {
    int n, m; std::cin >> n >> m;
    // Create n disjoint sets
    for (int i = 1; i <= n; ++i) make_set(i);
    // Add the edges one by one
    for (int i = 0; i < m; ++i) {
        int u, v; std::cin >> u >> v;
        if (find(u) != find(v)) merge(u, v);
    }
    // Display the result
    for (int i = 1; i <= n; ++i) {
        if (find(i) != i) continue;
        std::cout << "We have a set with representative " << i
            << " and it has a size of " << size[i] << '\n';
    }
}
