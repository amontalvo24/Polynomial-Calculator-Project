public class TermImp implements Term {

    double coefficient;
    int exponent;

    public TermImp(double coefficient, int exponent) {
        this.coefficient=coefficient;
        this.exponent=exponent;
    }


        @Override
        public double getCoefficient () {
        return coefficient;
        }

        @Override
        public int getExponent () {
        return exponent;
        }

        @Override
        public double evaluate ( double x){

        return this.coefficient*Math.pow(x,this.exponent);
        }



    }

