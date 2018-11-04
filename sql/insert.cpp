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

vector<string> department;

int main(){
	// 3 instructors 7 students
	ios_base::sync_with_stdio(false);
    cin.tie(NULL);
    for(int i=0;i<10;i++){
    	//cout<<"insert into user values {\"student1\",\" \"}"<<endl;
    }
    //

    









    cout<<"insert into user values (\'student1\',\'660866787\',\'1student\');"<<endl;
    cout<<"insert into user values (\'student2\',\'64510410\',\'2student\');"<<endl;
    cout<<"insert into user values (\'student3\',\'468154042\',\'3student\');"<<endl;
    cout<<"insert into user values (\'student4\',\'871797674\',\'4student\');"<<endl;
    cout<<"insert into user values (\'student5\',\'275441297\',\'5student\');"<<endl;
    cout<<"insert into user values (\'student6\',\'679084929\',\'6student\');"<<endl;
    cout<<"insert into user values (\'student7\',\'82728552\',\'7student\');"<<endl;
    cout<<"insert into user values (\'instructor1\',\'196658592\',\'1instructor\');"<<endl;
    cout<<"insert into user values (\'instructor2\',\'143991279\',\'2instructor\');"<<endl;
    cout<<"insert into user values (\'instructor3\',\'91323966\',\'3instructor\');"<<endl;

    department.pb("CSE"); department.pb("EE");department.pb("ME");
    for(auto s : department){
    	cout<<"insert into department values (\'"<<s<<"\');"<<endl;
    }
  
  	
  	cout<<"insert into instructor values (\'I1\',\'instructor1\',\'CSE\');"<<endl;
  	cout<<"insert into instructor values (\'I2\',\'instructor2\',\'EE\');"<<endl;
  	cout<<"insert into instructor values (\'I3\',\'instructor3\',\'ME\');"<<endl;
    
  	cout<<"insert into student values (\'16005001\',\'student1\',\'S1\',\'ME\');"<<endl;
  	cout<<"insert into student values (\'16005002\',\'student2\',\'S2\',\'ME\');"<<endl;
  	cout<<"insert into student values (\'16005003\',\'student3\',\'S3\',\'ME\');"<<endl;
  	cout<<"insert into student values (\'16005004\',\'student4\',\'S4\',\'EE\');"<<endl;
  	cout<<"insert into student values (\'16005005\',\'student5\',\'S5\',\'CSE\');"<<endl;
  	cout<<"insert into student values (\'16005006\',\'student6\',\'S6\',\'CSE\');"<<endl;
  	cout<<"insert into student values (\'16005007\',\'student7\',\'S7\',\'CSE\');"<<endl;

  	cout<<"insert into course values (\'CS101\',\'Intro to C\',\'CSE\');"<<endl;
  	cout<<"insert into course values (\'ME101\',\'Intro to M\',\'ME\');"<<endl;
  	cout<<"insert into course values (\'EE101\',\'Intro to E\',\'EE\');"<<endl;

  	cout<<"insert into section values (\'ME101\',\'Fall\',\'2018\');"<<endl;
  	cout<<"insert into section values (\'ME101\',\'Spring\',\'2017\');"<<endl;
  	cout<<"insert into section values (\'CS101\',\'Fall\',\'2018\');"<<endl;
  	cout<<"insert into section values (\'CS101\',\'Spring\',\'2017\');"<<endl;
  	cout<<"insert into section values (\'EE101\',\'Fall\',\'2018\');"<<endl;
  	cout<<"insert into section values (\'EE101\',\'Spring\',\'2017\');"<<endl;

  	cout<<"insert into teaches values (\'I1\',\'CS101\',\'Fall\',\'2018\');"<<endl;
  	cout<<"insert into teaches values (\'I1\',\'CS101\',\'Spring\',\'2017\');"<<endl;
  	cout<<"insert into teaches values (\'I2\',\'EE101\',\'Fall\',\'2018\');"<<endl;
  	cout<<"insert into teaches values (\'I2\',\'EE101\',\'Spring\',\'2017\');"<<endl;
  	cout<<"insert into teaches values (\'I3\',\'ME101\',\'Fall\',\'2018\');"<<endl;
  	cout<<"insert into teaches values (\'I3\',\'ME101\',\'Spring\',\'2017\');"<<endl;
 
 // S1 S4 S5 are TA
 	cout<<"insert into TA values (\'16005001\',\'ME101\',\'Spring\',\'2017\');"<<endl;
 	cout<<"insert into TA values (\'16005001\',\'ME101\',\'Fall\',\'2018\');"<<endl;
 	cout<<"insert into TA values (\'16005004\',\'EE101\',\'Fall\',\'2018\');"<<endl;
 	cout<<"insert into TA values (\'16005005\',\'CS101\',\'Fall\',\'2018\');"<<endl;

 // S2 S3 S6 S7 take the course
    
    cout<<"insert into takes values (\'16005002\',\'ME101\',\'Fall\',\'2018\');"<<endl;
    cout<<"insert into takes values (\'16005003\',\'ME101\',\'Spring\',\'2017\');"<<endl;
    cout<<"insert into takes values (\'16005006\',\'CS101\',\'Fall\',\'2018\');"<<endl;
    cout<<"insert into takes values (\'16005007\',\'CS101\',\'Spring\',\'2017\');"<<endl;

    cout<<"insert into test values (\'ME101\',\'Fall\',\'2018\',\'1\',\'Quiz1\',10,\' Oct 1\');"<<endl;
    cout<<"insert into test values (\'ME101\',\'Fall\',\'2018\',\'2\',\'Quiz2\',10,\' Oct 2\');"<<endl;
    cout<<"insert into test values (\'CS101\',\'Fall\',\'2018\',\'1\',\'Quiz1\',20,\' Sept 1\');"<<endl;
    cout<<"insert into test values (\'ME101\',\'Fall\',\'2018\',\'1\',\'Quiz1\',7,\' Sept 1\');"<<endl;

    cout<<"insert into appears values (\'CS101\',\'Fall\',\'2018\',\'1\',\'16005006\');"<<endl;
    cout<<"insert into appears values (\'CS101\',\'Fall\',\'2018\',\'1\',\'16005007\');"<<endl;
    cout<<"insert into appears values (\'ME101\',\'Fall\',\'2018\',\'1\',\'16005002\');"<<endl;
    cout<<"insert into appears values (\'CS101\',\'Fall\',\'2018\',\'1\',\'16005003\');"<<endl;

    cout<<"insert into question values (\'CS101\',\'Fall\',\'2018\',\'1\',1,15.0,\'Who are you?\',1,\'An earthling\');"<<endl;
    cout<<"insert into question values (\'CS101\',\'Fall\',\'2018\',\'1\',2,18.0,\'Who is Jon Snow?\',2,\'King in the North\');"<<endl;
    cout<<"insert into question values (\'ME101\',\'Fall\',\'2018\',\'1\',1,10,\'Who are you?\',1,\'An earthling\');"<<endl;
    cout<<"insert into question values (\'ME101\',\'Fall\',\'2018\',\'2\',1,100,\'Why katapa killed Bahubali?\',1,\'I dont know!\');"<<endl;



}


