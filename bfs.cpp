#include <iostream>
#include <algorithm>
#include <vector>
#include <cstring>
#include <queue>

//BFS = Breadth First Search

//n vetices, m edges
//u, v are connected egdes
//p is parent of edge
//adj[u] = the vetices that are connected to u

std::vector<int> adj[10001];
bool visited[10001];

void bfs(int u){
    std::queue<int> Q;
    Q.push(u);
    visited[u] = true;
    while (!Q.empty()) {
        const int u = Q.front();
        Q.pop();
        std::cout << u << '\n';
        for (int v : adj[u]) {
            if (!visited[v]) {
                Q.push(v);
                visited[v] = true;
            }
        }
    }
}

int main(){
    int n, m; std::cin >> n >> m;
    for (int i = 0; i < m; ++i){
        int u, v; std::cin >> u >> v;
        adj[u].push_back(v);
        adj[v].push_back(u);
    }
    bfs(2);
    //bfs starting from the vertex 2
}