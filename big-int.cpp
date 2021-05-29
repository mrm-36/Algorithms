#include <iostream>
#include <string>
#include <algorithm>
#include <cassert>
#include <cmath>
#include <chrono>
#include <iomanip>
#include <sstream>

/**
 * @author Miguel R. Merheb
 * @version 1.0
**/

class BigInt{
  private:
    int precision = 5;
    std::string nb = "";
    std::string dc = "";
    bool neg = false;
    friend std::ostream& operator << (std::ostream& os, const BigInt& bi);
    friend BigInt pow(const BigInt& a, const int64_t& b);
    friend BigInt floor(const BigInt& a);
    friend BigInt ceil(const BigInt& a);
    friend BigInt abs(const BigInt& a);
    friend BigInt sqrt(const BigInt& a);
    friend BigInt cbrt(const BigInt& a);

  public:
    BigInt(std::string _nb) {
      assert(_nb.size());
      if (_nb[0] == '-') neg = true, _nb = std::string(_nb.begin() + 1, _nb.end()); else neg = false;
      int i = 0; for (; i < _nb.length() && _nb[i] != '.'; ++i);
      int j = 0; for (; j + 1 < i && _nb[j] == '0'; ++j);
      nb = _nb.substr(j, i); if (i < _nb.length()) dc = _nb.substr(i + 1, _nb.length() - i);
      while (dc.length() && dc.back() == '0') dc.pop_back();
    }
    BigInt(std::string _nb, bool _neg) {(*this) = BigInt(_nb), neg = _neg;}
    BigInt(std::string _nb, std::string _dc, bool _neg = 0) {
      int i = 0; for (; i + 1 < _nb.length() && _nb[i] == '0'; ++i);
      nb = _nb.substr(i, _nb.length() - i); neg = _neg; dc = _dc;
      while (dc.length() && dc.back() == '0') dc.pop_back();
    }
    BigInt(double _nb) {
      std::ostringstream fs;
      fs << std::setprecision(15) << std::fixed << _nb;
      (*this) = BigInt(fs.str());
    }
    BigInt(double _nb, bool _neg) {(*this) = BigInt(_nb), neg = _neg;}

    bool operator > (const BigInt& rhs) const& {
      BigInt x = (*this), y = rhs;
      while (x.dc.length() || y.dc.length()){
        if (x.dc.length()) x.nb += x.dc[0], x.dc = std::string(x.dc.begin() + 1, x.dc.end());
        else x.nb += "0";
        if (y.dc.length()) y.nb += y.dc[0], y.dc = std::string(y.dc.begin() + 1, y.dc.end());
        else y.nb += "0";
      }
      if (!x.neg && y.neg) return true;
      if (x.neg && !y.neg) return false;
      if (x.nb.size() > y.nb.size()) return (x.neg ? false : true);
      if (x.nb.size() < y.nb.size()) return (x.neg ? true : false);
      return (x.nb > y.nb ? !x.neg : x.neg);
    }
    bool operator == (const BigInt& rhs) const& {return (nb == rhs.nb) && (dc == rhs.dc) && (neg == rhs.neg);}
    bool operator != (const BigInt& rhs) const& {return !((*this) == rhs);}
    bool operator >= (const BigInt& rhs) const& {return ((*this) == rhs) || ((*this) > rhs);}
    bool operator < (const BigInt& rhs) const& {return !((*this) >= rhs);}
    bool operator <= (const BigInt& rhs) const& {return !((*this) > rhs);}

    BigInt operator - (const BigInt& rhs) const& {
      if ((*this) == rhs) return BigInt(0);

      if (neg && rhs.neg) return BigInt(rhs.nb, rhs.dc) - BigInt(nb, dc);
      if (neg && !rhs.neg) {BigInt x = BigInt(nb, dc) + rhs; return BigInt(x.nb, x.dc, true);}
      if (!neg && rhs.neg) return (*this) + BigInt(rhs.nb, rhs.dc);

      if ((*this) < rhs) {BigInt x = rhs - (*this); return BigInt(x.nb, x.dc, true);}

      std::string s1 = nb, s2 = dc, s3 = rhs.nb, s4 = rhs.dc;
      int i = 0;
      while (s2.length() || s4.length()){
        if (s2.length()) s1 += s2[0], s2 = std::string(s2.begin() + 1, s2.end());
        else s1 += "0";
        if (s4.length()) s3 += s4[0], s4 = std::string(s4.begin() + 1, s4.end());
        else s3 += "0";
        ++i;
      }

      const int l1 = s1.length(), l2 = s3.length();
      const int n = std::max(l1, l2);

      std::string ans(n, '0');

      int diff = 0;
      for (int i = 0; i < n; ++i){
        int d1 = i < l1 ? (s1[l1 - i - 1] - '0') : 0;
        int d2 = i < l2 ? (s3[l2 - i - 1] - '0') : 0;
        int new_diff = (d1 - diff < d2 ? 1 : 0);
        d1 = (d1 >= diff ? d1 - diff : d1 - diff + 10);
        ans[i] = '0' + (d1 - d2 + (d1 < d2 ? 10 : 0));
        diff = new_diff;
      }
      while (ans.size() > 1 && ans.back() == '0') ans.pop_back();
      std::reverse(ans.begin(), ans.end());

      return BigInt(ans.substr(0, ans.length() - i), ans.substr(ans.length() - i, i));
    }
    BigInt operator + (const BigInt& rhs) const& {
      if (neg && !rhs.neg) return rhs - BigInt(nb, dc);
      if (!neg && rhs.neg) return (*this) - BigInt(rhs.nb, rhs.dc);

      std::string s1 = nb, s2 = dc, s3 = rhs.nb, s4 = rhs.dc;
      int i = 0;
      while (s2.length() || s4.length()){
        if (s2.length()) s1 += s2[0], s2 = std::string(s2.begin() + 1, s2.end());
        else s1 += "0";
        if (s4.length()) s3 += s4[0], s4 = std::string(s4.begin() + 1, s4.end());
        else s3 += "0";
        ++i;
      }

      const int l1 = s1.length(), l2 = s3.length();
      const int n = std::max(l1, l2) + 1;
      std::string total(n, '0');
      int carry = 0;
      for (int i = 0; i < n; ++i) {
        const int d1 = i < l1 ? (s1[l1 - 1 - i] - '0') : 0;
        const int d2 = i < l2 ? (s3[l2 - 1 - i] - '0') : 0;
        total[i] = '0' + ((d1 + d2 + carry) % 10);
        carry = (d1 + d2 + carry) / 10;
      }
      while (total.size() > 1 && total.back() == '0') total.pop_back();
      std::reverse(total.begin(), total.end());

      return BigInt(total.substr(0, total.length() - i), total.substr(total.length() - i, i), neg && rhs.neg);
    }
    BigInt operator * (const BigInt& rhs) const& {
      if ((*this) == BigInt(0) || rhs == BigInt(0)) return BigInt(0);
      
      std::string s1 = nb, s2 = dc, s3 = rhs.nb, s4 = rhs.dc;
      int i = 0;
      while (s2.length() || s4.length()){
        if (s2.length()) s1 += s2[0], s2 = std::string(s2.begin() + 1, s2.end());
        else s1 += "0";
        if (s4.length()) s3 += s4[0], s4 = std::string(s4.begin() + 1, s4.end());
        else s3 += "0";
        ++i;
      }

      const int n = std::max(s1.length(), s3.length());
      while (s1.length() < n) s1.insert(0, "0");
      while (s3.length() < n) s3.insert(0, "0");
    
      if (n == 1) return BigInt((s1[0] - '0') * (s3[0] - '0'), neg ^ rhs.neg);
    
      BigInt a(s1.substr(0, n / 2)), b(s1.substr(n / 2, n - n / 2));
      BigInt c(s3.substr(0, n / 2)), d(s3.substr(n / 2, n - n / 2));

      BigInt u = a * c, v = b * d;
      BigInt w = (a + b) * (c + d);
      BigInt t = w - u - v;

      for (int i = 0; i < 2 * (n - n / 2); ++i) u.nb.append("0");
      for (int i = 0; i < (n - n / 2); ++i) t.nb.append("0");

      BigInt ans = u + t + v;
      return BigInt(ans.nb.substr(0, ans.nb.length() - 2 * i), ans.nb.substr(ans.nb.length() - 2 * i, 2 * i), neg && rhs.neg);;
    }
    BigInt operator / (const BigInt& rhs) const& {
      if (rhs == BigInt(1)) return (*this);
      if ((*this) == rhs) return BigInt(1);

      std::string s1 = nb, s2 = dc, s3 = rhs.nb, s4 = rhs.dc;
      while (s2.length() || s4.length()){
        if (s2.length()) s1 += s2[0], s2 = std::string(s2.begin() + 1, s2.end());
        else s1 += "0";
        if (s4.length()) s3 += s4[0], s4 = std::string(s4.begin() + 1, s4.end());
        else s3 += "0";
      }

      BigInt ans("0", ""), x(s1), y(s3);
      for (int64_t d = 1e18; d >= 1; d /= 10) {
        BigInt z = y * BigInt(d);
        while (x >= z) x -= z, ans += BigInt(d);
      }
      while (ans.dc.length() <= precision && x != BigInt(0)){
        if (x.dc.size()) x.nb += x.dc[0], x.dc = std::string(x.dc.begin() + 1, x.dc.end());
        else x.nb += "0";
        int i = 0; while (x >= y) x -= y, ++i;
        ans.dc += std::to_string(i);
      }
      ans.neg = (neg ^ rhs.neg);
      return ans;
    }
    BigInt operator % (const BigInt& rhs) const& {
      assert(!(*this).dc.length() && !rhs.dc.length());
      assert(!rhs.neg);
      BigInt x(((*this) / rhs).nb); x *= rhs;
      return neg ? (*this) + x + rhs : (*this) - x;
    }

    BigInt operator += (const BigInt& rhs) {return (*this) = (*this) + rhs;}
    BigInt operator -= (const BigInt& rhs) {return (*this) = (*this) - rhs;}
    BigInt operator *= (const BigInt& rhs) {return (*this) = (*this) * rhs;}
    BigInt operator /= (const BigInt& rhs) {return (*this) = (*this) / rhs;}
    BigInt operator %= (const BigInt& rhs) {return (*this) = (*this) % rhs;}

    BigInt operator ++ () {return (*this) += BigInt(1);}
    BigInt operator -- () {return (*this) -= BigInt(1);}
};

BigInt floor(const BigInt& a) {
  if (!a.neg) return BigInt(a.nb);
  BigInt x = BigInt(a.nb, a.neg);
  if (a.dc.size()) --x;
  return x;
}
BigInt ceil(const BigInt& a) {
  if (a.neg) return BigInt(a.nb, a.neg);
  BigInt x = BigInt(a.nb);
  if (a.dc.size()) ++x;
  return x;
}
BigInt abs(const BigInt& a) {return BigInt(a.nb, a.dc);}

BigInt pow(const BigInt& a, const int64_t& b) {
  if (b == 0) return 1;
  if (b & 1) return a * pow(a * a, (b - 1) >> 1);
  return pow(a * a, b >> 1);
}

BigInt sqrt(const BigInt& a) {
  assert(!a.neg);
  BigInt lo(0), hi = a, mid(0), ans(0);

  while (lo <= hi) {
    mid = floor((lo + hi) / 2);
    const BigInt x = mid * mid;
    if (x == a) return mid;
    if (x < a) {ans = mid; lo = mid + 1;}
    else hi = mid - 1;
  }

  BigInt inc("0.1");
  for (int i = 0; i < a.precision; ++i){
    while (ans * ans <= a) ans += inc;
    ans -= inc; inc /= BigInt(10);
  }
  return ans;
}
BigInt cbrt(const BigInt& a) {
  const BigInt z = abs(a);
  BigInt lo(0), hi = z, mid(0), ans(0);

  while (lo <= hi) {
    mid = floor((lo + hi) / 2);
    const BigInt x = mid * mid * mid;
    if (x == z) return mid;
    if (x < z) {ans = mid; lo = mid + 1;}
    else hi = mid - 1;
  }

   BigInt inc("0.1");
  for (int i = 0; i < a.precision; ++i){
    while (ans * ans * ans <= z) ans += inc;
    ans -= inc; inc /= BigInt(10);
  }
  ans.neg = a.neg;
  return ans;
}

std::ostream& operator << (std::ostream& os, const BigInt& bi){
  os << (bi.neg ? "-" : "") << bi.nb;
  if (bi.dc.size()) {
    int x = std::cout.precision(), i;
    if (x) os << ".";
    for (i = 0; i < x; ++i) os << (i < bi.dc.length() ? bi.dc[i] : '0');
  }
  return os;
}

int main(){
  std::cout << std::setprecision(5) << std::fixed;
    
  auto start = std::chrono::high_resolution_clock::now();

  BigInt x(2);
  std::cout << pow(x, 300) << '\n';

  auto end = std::chrono::high_resolution_clock::now();
  auto duration = std::chrono::duration_cast<std::chrono::milliseconds>(end - start);
  std::cout << "----- " << duration.count() << " milliseconds -----\n";
}