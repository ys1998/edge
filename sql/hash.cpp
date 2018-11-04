#define rep(i,a,b) for(int i=a;i<b;++i)
#define repr(i,a,b) for(int i=a,i > b;--i)
#define mm(lamb, tttt) memset(lamb, tttt, sizeof lamb)

#define null NULL
#define eps 0.000000001
#define mod 1000000007
#define PI 3.14159265358979323846
#define pb push_back
#define pf push_front
#define mp make_pair
#define fi first
#define se second
#define ALL(V) V.begin(), V.end()
#define sz(V) (ll)V.size()
#define _ <<" "<<

#include <iostream>
#include <fstream>
#include <stdio.h>
#include <stack>
#include <queue>
#include <deque>
#include <vector>
#include <iterator>
#include <bitset>
#include <list>
#include <map>
#include <set>
#include <unordered_map>
#include <unordered_set>
#include <cstring>
#include <string>
#include <algorithm>
#include <cmath>
#include <cassert>
#include <limits.h>
//#include <iomanip>
#include <cctype>
#include <numeric>
#include <complex>

using namespace std;

typedef long long ll;
typedef vector <int> vi;
typedef pair <int, int> ii;
typedef pair<int, pair<int,int> > iii;
typedef vector<int> vii;



int customHash(string textToHash) {
    	ll p = 31;
        ll m = 1000000009;
        ll hash_value = 0L;
        ll p_pow = 1L;
        for (int i=0; i<textToHash.length(); ++i) {
            hash_value = (hash_value + (textToHash[i] - 'a' + 1) * p_pow) % m;
            p_pow = (p_pow * p) % m;
        }
        return hash_value;
}

int main(){
	cout<<customHash("student11student")<<endl;
	cout<<customHash("student22student")<<endl;
	cout<<customHash("student33student")<<endl;
	cout<<customHash("student44student")<<endl;
	cout<<customHash("student55student")<<endl;
	cout<<customHash("student66student")<<endl;
	cout<<customHash("student77student")<<endl;
	cout<<customHash("instructor11instructor")<<endl;
	cout<<customHash("instructor22instructor")<<endl;
	cout<<customHash("instructor33instructor")<<endl;
}


