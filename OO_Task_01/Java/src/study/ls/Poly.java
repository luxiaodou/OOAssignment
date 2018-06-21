package study.ls;

/**
 * Created by luxia on 2016/3/2.
 */
class Poly {
    private long[] terms;
    private int deg;

    public Poly() {
        terms = new long[1];
        deg = 0;
    }

    public Poly(long c, int n) throws FormatException {
        if (n < 0)
            throw new FormatException("NegativeExponentException");
        //System.out.println("NegativeExponentException");
        if (c == 0) {
            terms = new long[1];
            deg = 0;
            return;
        }
        terms = new long[n + 1];
        for (int i = 0; i < n; i++)
            terms[i] = 0;
        terms[n] = c;
        deg = n;
    }

    private Poly(int n) {
        terms = new long[n + 1];
        deg = n;
    }

    public int degree() {
        return deg;
    }

    public long coffe(int d) {
        if (d < 0 || d > deg)
            return 0;
        else
            return terms[d];
    }

    public Poly minus() {
        Poly r = new Poly(deg);
        for (int i = 0; i <= deg; i++) {
            r.terms[i] = -terms[i];
        }
        return r;
    }

    public void info() {
        int init = 1;
        for (int i = 0; i <= deg; i++) {
            if (terms[i] != 0) {
                if (terms[i] >= 0 && init == 0)
                    System.out.print("+");
                if (i == 0) {
                    System.out.print(terms[0]);
                    init = 0;
                } else {
                    System.out.printf("%dx^%d", terms[i], i);
                    init = 0;
                }
            }
        }
        if (deg == 0 && terms[0] == 0)
            System.out.println("0");
        System.out.println("\n");
    }

    public Poly sub(Poly q) throws FormatException {
        return this.add(q.minus());
    }

    public Poly add(Poly q) throws FormatException {
        /*
        Poly p = this;
        Poly r = new Poly(0,Math.max(p.deg,q.deg));
        for(int i =0; i<= p.deg; i++)
            r.terms[i] += p.terms[i];
        for(int i =0; i<= q.deg; i++)
            r.terms[i] += q.terms[i];
        return r;*/

        Poly large, small;

        if (deg > q.deg) {    // large -> the Poly whose top degree is higher,the other one is Small
            large = this;
            small = q;
        } else {
            large = q;
            small = this;
        }

        int topdeg = large.deg;

        if (deg == q.deg) {
            for (int j = deg; j > 0; j--) {
                if (terms[j] + q.terms[j] == 0)  // When the cofee of the top exponent is opposite number, decrease the top-degree
                    topdeg--;
                else
                    break;
            }
        }

        Poly r = new Poly(topdeg);
        int i;

        for (i = 0; i <= small.deg && i <= topdeg; i++)
            r.terms[i] = large.terms[i] + small.terms[i];
        if(topdeg != 0)
            for (int j = i; j <= topdeg; j++)
                r.terms[j] = large.terms[j];
        return r;
    }
}
