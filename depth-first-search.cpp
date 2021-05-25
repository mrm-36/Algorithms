#include <iostream>
#include <algorithm>
#include <vector>

//DFS = Depth First Search

//Undirected Graph
//n vetices, m edges
//u, v are connected egdes
//p is parent of edge
//adj[u] = the vetices that are connected to u

std::vector<int> adj[10001];
bool visited[10001];

void dfs(int u, int p){
    if (visited[u]) return;
    visited[u] = true;
    std::cout << u << '\n';
    for (int v : adj[u]) {
        if (v == p) continue;
        dfs(v, u);
    }
}

int main(){
    int n, m; std::cin >> n >> m;
    for (int i = 0; i < m; ++i){
        int u, v; std::cin >> u >> v;
        adj[u].push_back(v);
        adj[v].push_back(u);
    }
    dfs(1, -1);
    //dfs starting from the first vertex
    // -1 means no parent
}
