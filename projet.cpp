#include <iostream>
#include <sstream>
#include <string>
using namespace std;

struct Voiture {
    string nom;
    int position;
    int vitesse;
    int probaf;
};

int vMax = 5;
const int taille = 80;
using route = Voiture[taille];

void initRoute(route r) { // cho nay co de & hay ko
    for (int i=0; i<taille; i++)
        r[i].nom = "_";
}

bool estVide (route r, int pos) {
    if (r[pos].nom != "_") return false;
    else return true;
}

int comptV (route r) {
    int c=0;
    for (int i=0; i<taille; i++) {
        if (estVide(r, i)) c = c;
        else c++;
    }
    return c;
}

string nomVAjoutee(route r) {
    string nom;
    stringstream convert;
    convert << comptV(r);
    return "V"+convert.str();
}

void ajouteV(route &r, int pos, int v0) { // dat dk so xe hien co trong dg <=78??
    if (pos>=0 and pos<80) {
        if (estVide(r, pos)) {
            r[pos].nom = nomVAjoutee(r);
            //cout << endl << "Son nom sera :" << r[pos].nom << endl;
            r[pos].position = pos;
            r[pos].vitesse = v0;
        }
        else {cout << "La case " << pos << " est occupee." << endl;}
    }
    else {cout << "La position doit etre entre [0;79]." << endl;}
}

int dVSuiv (Voiture v, route r) {
    int d=0;
    using route1 = Voiture[160];
    for (int i=0; i<taille; i++) route1[i] = route[i];
    for (int j=taille-1; j<taille*2; j++) route1[j] = route[j-taille];
    for (int a=v.position; a<taille*2; a++) {
        if (estVide(route1, a+1)) d++;
        else return d;
    }
}

void acceleration(Voiture &v) {
    if (v.vitesse<vMax) v.vitesse++;
}

void affiche(route r){
    for (int i=0; i<taille; i++) {  //khi ngat ct chu y phan biet giua "taille" va "route.size()"
            if (estVide(r, i)) cout << "_";
            else cout << r[i].nom;
    }
}



int main(){
    route R;
    initRoute(R);
    affiche(R);
    ajouteV( R, 2, 3);
    affiche(R);
    //ajouteV( R, 10, 3);
    //affiche(R);
    //ajouteV( R, 10, 3);
    //affiche(R);
    //ajouteV( R, 10, 3);
    //affiche(R);
    cout << endl;
    return 0;
}
