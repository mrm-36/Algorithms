#include <iostream>
#include <vector>
#include <math.h>
#include <random>
#include <chrono>
#include <algorithm>

// Find the closest pair of points based on distance
// Complexity: O(nlog(n))

typedef std::pair<double, double> point_t;
typedef std::pair<point_t, point_t> points_t;

double dist(const point_t& a, const point_t& b) {
	const double dx = b.first - a.first, dy = b.second - a.second;
	return dx * dx + dy * dy;
}

std::pair<double, points_t> BruteForce_Closest_Pair(const std::vector<point_t>& p) {
	if (p.size() < 2) return { -1, { { 0, 0 }, { 0, 0 } } };
	double md = dist(p[0], p[1]);
	points_t mp = { p[0], p[1] };
	for (int i = 0; i + 1 < p.size(); ++i) {
		for (int j = i + 1; j < p.size(); ++j) {
			double nd = dist(p[i], p[j]);
			if (nd < md) {
				md = nd;
				mp.first = p[i], mp.second = p[j];
			}
		}
	}
	return { md, mp };
}

std::pair<double, points_t> Optimized_Closest_Pair(const std::vector<point_t>& Px, const std::vector<point_t>& Py) {
    int N = Px.size();
	if (N <= 50) return BruteForce_Closest_Pair(Px);
	
	std::vector<point_t> Lx(Px.begin(), Px.begin() + N / 2), Rx(Px.begin() + N / 2, Px.end());
	double Mx = Px[N / 2].first;
	std::vector<point_t> Ly, Ry; 
	Ly.reserve(Py.size()); Ry.reserve(Py.size());
	std::copy_if(Py.begin(), Py.end(), std::back_inserter(Ly), [&Mx](const point_t& p) {return p.first <= Mx;});
	std::copy_if(Py.begin(), Py.end(), std::back_inserter(Ry), [&Mx](const point_t& p) {return p.first > Mx;});
	
    std::pair<double, points_t> p1 = Optimized_Closest_Pair(Lx, Ly);
	std::pair<double, points_t> p2 = Optimized_Closest_Pair(Rx, Ry);
	std::pair<double, points_t> minPair = (p1.first <= p2.first) ? p1 : p2;
	
    std::vector<point_t> Sy;
	Sy.reserve(Py.size());
	std::copy_if(Py.begin(), Py.end(), std::back_inserter(Sy), [&minPair, &Mx](const point_t& p) {return std::abs(Mx - p.first) < minPair.first;});
	
    std::pair<double, points_t> result = minPair;
	for (int i = 0; i + 1 < Sy.size(); ++i) {
		for (int k = i + 1; k < Sy.size() && ((Sy[k].second - Sy[i].second) < minPair.first); ++k) {
			double nd = dist(Sy[k], Sy[i]);
			if (nd < result.first) result = { nd, { Sy[k], Sy[i] } };
		}
	}
	return result;
}

void print_point(const point_t& point) {
	std::cout << "(" << point.first << ", " << point.second	<< ")";
}

int main() {
	int n; std::cin >> n;
	std::vector<point_t> points(n);
	
	std::default_random_engine re(std::chrono::system_clock::to_time_t(std::chrono::system_clock::now()));
	std::uniform_real_distribution<double> urd(-500.0, 500.0);
	std::generate(std::begin(points), std::end(points), [&urd, &re]() {
		return point_t { 1000 + urd(re), 1000 + urd(re) };
	});

	std::cout << "List Generated\n";
	
	auto start = std::chrono::high_resolution_clock::now(); 
	//for (int i = 0; i < n; ++i) std::cin >> points[i].first >> points[i].second;

	//std::pair<double, points_t> answer = BruteForce_Closest_Pair(points);
	//std::cout << "Min distance (brute): " << answer.first << " ";
	//print_point(answer.second.first); std::cout << ", "; print_point(answer.second.second);

	std::sort(std::begin(points), std::end(points), 
        [](const point_t& a, const point_t& b) {return a.first < b.first;});
	std::vector<point_t> Px = points;

	std::sort(std::begin(points), std::end(points), 
        [](const point_t& a, const point_t& b) {return a.second < b.second;});
	std::vector<point_t> Py = points;

	std::pair<double, points_t> answer = Optimized_Closest_Pair(Px, Py);
	std::cout << "\nMin distance (optimized): " << answer.first << " ";
	print_point(answer.second.first); std::cout << ", "; print_point(answer.second.second);

	auto stop = std::chrono::high_resolution_clock::now();
	auto duration = std::chrono::duration_cast<std::chrono::microseconds>(stop - start); 
	std::cout << "\nRunning Time: " << duration.count();
	return 0;
}
