#include <iostream>
#include <vector>
using namespace std;

int suma_vector(const vector<int>& v) {
    int suma = 0;
    for (int i = 0; i < v.size(); ++i) {
        suma += v[i];
    }
    return suma;
}

void swap(int& a, int& b) {
    int z;
    z = a;
    a = b;
    b = z;
}

void ordena(vector<int>& v) {
    for (int i = 0; i + 1 < v.size(); ++i) {
        for (int j = 0; j + 1 < v.size() - i; ++j) {
            if (v[j] > v[j + 1]) {
                swap(v[j], v[j + 1]);
            }
        }
    }
}

void printvect(const vector<int>& v) {
    for(int i = 0; i < v.size(); i++) {
        cout << v[i] << " ";
    }
    cout << endl;
}

int main() {
    int a, result;
    vector<int> v;

    while(cin >> a) {
        v.push_back(a);
    }
    
    result = suma_vector(v);

    cout << result << endl;

    ordena(v);

    printvect(v);
}