import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Iterator;

public class PolynomialImp implements Polynomial {

    private final String string;    //stores the polynomial string
    private TermImp terms; //stores the coefficient and exponents
    private int currentSize; //to use with iterator
    ArrayList<Term> poly; //stores the polynomial string into array list form

    public PolynomialImp(String s) { //constructor
        this.string= s;
        StrtoArr(s); //calls the method inside the constructor so every polynomial will return an arraylist
    }

    public ArrayList<Term> StrtoArr(String polystr){    //turns the string polynomial into arraylist form
        poly = new ArrayList<>();
        if (polystr.endsWith("x")){ //if the polynomial ends with x add a 1 to express that the exponent is 1 ex. P(x)= 16x
            polystr= polystr+"1";
        }else if(polystr.length()!=0){
            polystr = polystr + "x0";   //if not, add x0
        }
        if (polystr.startsWith("x^")){  //if the polynomial starts without a coefficient, add 1 in front of x to indicate the coefficient is 1 ex. P(x)=x^2
            polystr= addChar(polystr,'1',0);
        }
        if (polystr.contains("x+")){    //if the polynomial does not have an exponent after the x add a one to indicate the exponent is 1
            polystr= addChar(polystr,'1',polystr.indexOf("x+")+1);
        }
        for (int i = 0; i < polystr.length(); i++) {    //for loops until i equals the length of the string
            if (polystr.contains("+x")||polystr.contains("-x")){    //every x that does not have a number in front, add a 1 to indicate the coefficient is 1;
                polystr= addChar(polystr,'1',polystr.indexOf("+x")+1);
            }
        }
        String[] num = polystr.replace("^", "").split("((?=-)|[x+])");  //turns the string into an array of string, splits the string by x+ and keeping the (-)
        num = Arrays.stream(num).filter(s -> (s != null && s.length() > 0)).toArray(String[]::new); //filters the list so that it has no null elements in the list
        for (int i = 0; i < num.length; i++) {  //loops through the entire array of strings
            if (i % 2 == 0){    //starts from even postions, so that it iterates through pairs, since the list is always going to be even
                double c = Double.parseDouble(num[i]);  //classify the even positions as coefficients
                int e = Integer.parseInt(num[i+1]); //classify the odd positions as exponents
                terms= new TermImp(c,e);    //makes a new term out of c and e
                poly.add(terms);    //adds the terms to the array list of terms
            }
        }
        currentSize= poly.size();
        return poly;
    }

    public static String addChar(String str, char ch, int i) { //adds a character to a string in a specific position
        StringBuilder stringBuilder = new StringBuilder(str);   //starts building a new string with the added character in the given position
        stringBuilder.insert(i, ch);
        return stringBuilder.toString();
    }


    @Override
    public Polynomial add(Polynomial P2) { //adds to polynomials
        Polynomial NewPol;
        ArrayList<Term> p3 = new ArrayList<>();
        double c=0; //stores new coefficients
        int e= 0; //stores new exponents
        int i=0;    //keeps the terms in order and not add one in front of the other
        int p = this.string.length();
        int pp = ((PolynomialImp) P2).string.length();
        PolynomialImp big;
        PolynomialImp small;

        if (p>pp){ //compares the lengths of the two to determine which one is the longest
            big= this;
            small= (PolynomialImp) P2;
        }else{
            big= (PolynomialImp) P2;
            small= this;
        }

        for (Term p1:big) { //the longest polynomial its the outside loop,
            for (Term p2:small) {   //shortest polynomial goes inside since its going to be the first list to end
                if (p1.getExponent() == p2.getExponent()) { //compares if the term from the two list have liked terms to add them, this case comparing the exponents
                    c = p1.getCoefficient() + p2.getCoefficient();
                    e = p1.getExponent();
                    break; //this will make sure it only finds one and moves on to the next term and does not compare with the whole list again
                }else if (p1.getExponent()!=e && p2.getExponent()==0){ //if the term from list p1 does not find a match after iterating through p2 make it a new term
                    c= p1.getCoefficient();                             //if and only if the it has gone through the while p2 list and the exponent is not the same as the saved exponent
                    e= p1.getExponent();
                }else if (p2.getExponent()!=e && p2.getExponent()==0){  //if p2 did not find any matches from p1, add them as a new term
                    c= p2.getCoefficient();
                    e= p2.getExponent();
                }
            }
            if (c==0){  //to make sure no terms have the coefficient of 0
                continue;
            }

            terms= new TermImp(c,e); //makes new terms
            p3.add(terms,i); //added to our new array list of terms
            i++;

        }
        String pol = polyconstructor(p3); //returns our new polynomial in string form
        System.out.println(pol);
        NewPol= new PolynomialImp(pol); //declare a new polynomial as the result of the sum of P1 and P2
        return NewPol;
    }

    @Override
    public Polynomial subtract(Polynomial P2) {
        Polynomial NewPol;
        if (this.equals(P2)){   //if the two polynomials that are being subtracted have the same string, returns 0 as a polynomial
            NewPol= new PolynomialImp("0");
            return NewPol;
        }
        ArrayList<Term> p3 = new ArrayList<>();
        int p = this.string.length();
        int pp = ((PolynomialImp) P2).string.length();
        int i=0;
        ArrayList<Term> big;
        ArrayList<Term> small;


        ArrayList<Term> temp = new ArrayList<>();
        int t= 0;
        for (Term p2:P2) { //changes the signs of the polynomial terms, so that way it doesnt matter which list is bigger P2 will always be in a negative sign
            terms= new TermImp(-1*p2.getCoefficient(),p2.getExponent());
            temp.add(terms,t);
            t++;
        }
            //similar process to the add method
        if (p>pp){
            big= this.poly;
            small= temp;
        }else{
            big= temp;
            small= this.poly;
        }

        double c=0;
        int e= 0;
        for (int j = 0; j < big.size(); j++) {
            for (int k = 0; k < small.size(); k++) {
                if (big.get(j).getExponent() == small.get(k).getExponent()) {
                    c = big.get(j).getCoefficient() + small.get(k).getCoefficient();    //the term is already negative it will change the signs if i subtract them
                    e = big.get(j).getExponent();
                    break;
                }else if (big.get(j).getExponent()!=e && small.get(k).getExponent()==0){
                    c= big.get(j).getCoefficient();
                    e= big.get(j).getExponent();
                }else if (small.get(k).getExponent()!=e && small.get(k).getExponent()==0){
                    c= small.get(k).getCoefficient();
                    e= small.get(k).getExponent();
                }
            }
            if (c==0){
                continue;
            }
            terms= new TermImp(c,e);
            p3.add(terms,i);
            i++;

        }

        String pol = polyconstructor(p3);
        System.out.println(pol);
        NewPol= new PolynomialImp(pol);
        return NewPol;
    }

    @Override
    public Polynomial multiply(Polynomial P2) {
        Polynomial NewPol;
        if ((((PolynomialImp) P2).poly.get(0).getCoefficient()==0)){    //if the coefficient of the first term of polynomial P2
            NewPol= new PolynomialImp("0");                             //is 0, return 0 as a polynomial
            return NewPol;
        }
        ArrayList<Term> p3 = new ArrayList<>();
        ArrayList<Term> temp= new ArrayList<>();
        int p = this.string.length();
        int pp = ((PolynomialImp) P2).string.length();

        PolynomialImp big;
        PolynomialImp small;

        if (p>pp){
            big= this;
            small= (PolynomialImp) P2;
        }else{
            big= (PolynomialImp) P2;
            small= this;
        }

        double cc= 0;
        int ee=0;
        int i=0;
        for (Term p1:big) { //loops through both list and mutiply each term with the other list
            for (Term p2:small) {
                cc = p1.getCoefficient() * p2.getCoefficient();
                ee = p1.getExponent() + p2.getExponent();
                terms= new TermImp(cc,ee );
                temp.add(terms,i);  //making our first array list of multiplied terms
                i++;
            }
        }

        double c=0;
        int e= 0;
        int u=0;
        for (int j = 0; j < temp.size(); j++) { //since there might be s=terms with the same exponents we need to add them
            c= temp.get(j).getCoefficient();
            e = temp.get(j).getExponent();
            for (int k = 0; k < temp.size(); k++) { //loops within the same array
                if (j!=k){
                    if (temp.get(j).getExponent() == temp.get(k).getExponent()) {
                        c = c+ temp.get(k).getCoefficient();
                        temp.remove(temp.get(k));   //once it finds the same exponent we don't want it to through that term again so we remove it from our first list
                    }
                }
            }
            if (c != 0) {
                terms= new TermImp(c,e);
                p3.add(terms,u);    //term is added to our new multiplied and added polynomial
                u++;
            }
        }
        for (int j = 0; j < p3.size()-1; j++) { //loops within our new array, in the adding process it could've unsorted the array
            Term tempo;
                if (p3.get(j).getExponent() < p3.get(j + 1).getExponent()) {    //compares if the term next to it is bigger than the term were comparing
                    tempo = p3.get(j);
                    p3.replace(j,p3.get(j+1));  //switches term positions
                    p3.replace(j+1,tempo);
                    break;
            }
        }
        String pol = polyconstructor(p3);
        System.out.println(pol);
        NewPol= new PolynomialImp(pol);
        return NewPol;
    }

    @Override
    public Polynomial multiply(double c) {
        Polynomial NewPol;
        if (c==0){  //if number that is going to be multiplied by the polynoial is 0 then it will make a polynomial of 0
            NewPol= new PolynomialImp("0");
            return NewPol;
        }
        ArrayList<Term> p3 = new ArrayList<>();
        int i=0;
        for (Term p:this) {
            double coe = p.getCoefficient() * c;    //multiplies each coefficient term with c
            int expo = p.getExponent();
            terms= new TermImp(coe,expo);
            p3.add(terms,i);
            i++;

        }
        String pol = polyconstructor(p3);
        System.out.println(pol);
        NewPol= new PolynomialImp(pol);
        return NewPol;
    }

    @Override
    public Polynomial derivative() {    //d/dx
        double c = 0;
        int e=0;
        Polynomial NewPol;
        ArrayList<Term> p3 = new ArrayList<>();
        int i=0;
        for (Term p:this) {             //x*n^n-1
            if (p.getExponent()!=0) {   //if the exponent if the term is 0 it will not go through the process of derivation and will not be added to the list
                c = p.getCoefficient() * p.getExponent();  //multiplies coefficient with exponent
                e = p.getExponent() - 1;    //subtract -1 to the exponent
                if (c != 0) {
                    terms= new TermImp(c,e);
                    p3.add(terms,i);
                    i++;
                }
            }
        }
        String pol = polyconstructor(p3);
        System.out.println(pol);
        NewPol= new PolynomialImp(pol);
        return NewPol;
    }

    @Override
    public Polynomial indefiniteIntegral() {    //Sdx
        Polynomial NewPol;
        ArrayList<Term> p3 = new ArrayList<>();
        int i=0;
        for (Term p:this) {                                     //x^n+1/n+1
            double c = p.getCoefficient()/(p.getExponent()+1);  //divides the coefficient by the exponent+1
            int e = p.getExponent() + 1;    //adds +1 to the exponent
            if (c != 0) {
                terms= new TermImp(c,e);
                p3.add(terms,i);
                i++;
            }
        }

        String pol = polyconstructor(p3)+"+1";  //it adds +1 to the end of the string representing +C
        System.out.println(pol);
        NewPol= new PolynomialImp(pol);
        return NewPol;
    }

    @Override
    public double definiteIntegral(double a, double b) {    //S[a,b]dx -> d(b)-d(a)
        double result;
        double r1;
        double r2;
        Polynomial ind = this.indefiniteIntegral(); //since we already have the indefinite method done we use it
        r1= ind.evaluate(b);    //we need to evaluate the polynoial using the interval of a and b, and we already have the method done
        r2= ind.evaluate(a);
        result= r1-r2;      //then we subtract the result of both evaluations
        System.out.println(result);
        return result;
    }

    @Override
    public int degree() {   //the highest exponent of the polynomial
        int deg = poly.get(0).getExponent();    //assuming the polynomial is sorted, it would be the found in the first term
        System.out.println(deg);
        return deg;
    }

    @Override
    public double evaluate(double x) {  //evaluates the polynomial corresponding with the number x
        double result = 0;              //P(x)
        for (Term p:this) {
            if (p.getExponent()!=0){    //will not evaluate exponents that are 0
                result= result+ p.getCoefficient()*Math.pow(x,p.getExponent());
            }else{
                result= result+p.getCoefficient();
            }
        }
        return result;
    }

    @Override
    public Iterator<Term> iterator() {
        Iterator<Term> it = new Iterator<Term>() {

            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < currentSize && poly.get(currentIndex) != null;
            }

            @Override
            public Term next() {
                return poly.get(currentIndex++);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
        return it;
    }

    public String toString(){
        StringBuilder s= new StringBuilder();   //where we will be building the polynomial string
        DecimalFormat decim = new DecimalFormat("0.00");    //used to put the coefficients in format like the test case
        ArrayList<Term> p1 = this.poly;                         //meaning the double will maintain at a significant number of 2,2.00 or 1.33

        for (int i = 0; i < p1.size(); i++) {
            if (p1.get(i).getExponent()>1){ //terms that the exponent is larger than 1
                if (i == p1.size() - 1) {   //if its at the last term of the polynomial, meaning 3x^2+6x
                    s.append(decim.format(p1.get(i).getCoefficient())).append("x^").append(p1.get(i).getExponent());    //will not be adding a + at the end
                }else if (p1.get(i).getCoefficient()==1) {  //if the coefficient is 1 meaning x^3
                    s.append("x^").append(p1.get(i).getExponent()).append("+"); //coefficient value is not added
                } else {    //coefficient is larger than 1 and term is not that last one from the polynomial
                    s.append(decim.format(p1.get(i).getCoefficient())).append("x^").append(p1.get(i).getExponent()).append("+");
                }
            }
            if (p1.get(i).getExponent()== 1) {  //if exponent value is 1 meaning 3x
                if (i==p1.size()-1){
                    if (p1.get(i).getCoefficient()==1){ //if coefficient is also 1
                        s.append("x");  //coefficient value and exponent value are not added and will not add the +
                    }else { //coefficient is larger than 1
                        s.append(decim.format(p1.get(i).getCoefficient())).append("x");
                    }
                }else if (p1.get(i).getCoefficient()==1){   //term is not the one from the polynomial and coefficient value is 1
                    s.append("x+"); //coefficient and exponent value will not be added
                }else { //coeffcient is larger than 1
                    s.append(decim.format(p1.get(i).getCoefficient())).append("x+");    //exponent value will not be added
                }
            }
            if (p1.get(i).getExponent()==0){    //if exponent value is 0
                s.append(decim.format(p1.get(i).getCoefficient()));     //just add the coefficient value
            }
        }
        return s.toString();    //finished polynomials string
    }

    public String polyconstructor(ArrayList<Term> p){   //the same process with toString but instead it takes in arrays, not polynomials
        StringBuilder s= new StringBuilder();
        for (int i = 0; i < p.size(); i++) {
            if (p.get(i).getExponent()>1){
                if (p.get(i).getCoefficient()==1) {
                    s.append("x^").append(p.get(i).getExponent()).append("+");
                } else if(isAwholeNumber(p.get(i).getCoefficient())) {  //checks if the coefficient is a whole number and not a decimal
                    s.append((int)p.get(i).getCoefficient()).append("x^").append(p.get(i).getExponent()).append("+");   //converts double to integer
                }else{
                    s.append(p.get(i).getCoefficient()).append("x^").append(p.get(i).getExponent()).append("+");
                }
            }
            if (p.get(i).getExponent()== 1) {
                if (i==p.size()-1){
                    if (p.get(i).getCoefficient()==1){
                        s.append("x");
                    }else {
                        s.append((int)p.get(i).getCoefficient()).append("x");   //converted coefficient to integer
                    }
                }else if ((int)p.get(i).getCoefficient()==1){
                    s.append("x+");
                }else {
                    s.append((int) p.get(i).getCoefficient()).append("x+");
                }
            }
            if (p.get(i).getExponent()==0){
                s.append((int)p.get(i).getCoefficient());
            }
        }
        return s.toString();
    }

    public boolean equals(Polynomial P2){   //for testers to be able to run the tests, since the java equals compares objects not polyomials
        PolynomialImp p2= (PolynomialImp) P2;
        return this.string.equals(p2.string);   //compares the string of the polynomials to see if they match
    }

    public static boolean isAwholeNumber(double number){ //returns true if a double is an intiger, as in a whole number and not a decimal
        return Math.ceil(number) == Math.floor(number); //if both match then its an integer, if not that means that the decimal and integer version are different
    }                                   //if the round up and round down of the double equal


}
