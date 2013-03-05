package vlm;


import java.text.*;
//NN import Vector.*;

/*------------------------------------------------------------------*/
/* newton's divided difference interpolation */
class InterPol {

    int N;		// number of points in vector
    double xV[];	// vector containing the raw data
    double f[][];	// matrix of difference values

    /*----------------------------------------------------------*/
    public InterPol(Vector v) {
        N = v.size;
        f = new double[N][N];
        xV = new double[N];

        for (int j = 0; j < N; j++) {
            f[j][0] = v.y[j];
            xV[j] = v.x[j];
        }

        for (int m = 1; m < N; m++) {
            for (int j = 0; j < N - m; j++) {
                f[j][m] = (f[j + 1][m - 1] - f[j][m - 1]) / (xV[j + m] - xV[j]);
            }
        }
    }

    //--------------------------------------
    public InterPol(int size, float x[], float y[]) {
        N = size;
        f = new double[N][N];
        xV = new double[N];

        for (int j = 0; j < N; j++) {
            f[j][0] = y[j];
            xV[j] = x[j];
        }

        for (int m = 1; m < N; m++) {
            for (int j = 0; j < N - m; j++) {
                f[j][m] = (f[j + 1][m - 1] - f[j][m - 1]) / (xV[j + m] - xV[j]);
            }
        }
    }

    //------------------------------------------------------------
    // interpolate over L points  (e.g. L = 2 means linear)
    public double interp(float x, int L) {
        if (L > N) {
            L = N;
        }

        // display parameters
        DecimalFormat fltFmt = new DecimalFormat(" 0.000000;-0.000000");

        // find first point based on l
        int n = 0;

        if (N - L > 1) {
            for (n = 0; n < (N - L); n++) {
                if (x <= xV[n + 1]) {
                    break;
                }
            }
        }

        // start with 0th order value (i.e. closest value <= x)
        double p[] = new double[N];
        p[0] = f[n][0];

        // System.out.println ("    "+ n +" "+ fltFmt.format (p[0]));

        // use L difference values relative to k
        for (int k = 1; k < L; k++) {
            double t = f[n][k];

            // System.out.println ("    "+ k +" "+ fltFmt.format (t));

            for (int i = 0; i < k; i++) {
                t *= x - xV[n + i];
            }

            p[k] = p[k - 1] + t;
        }

        // System.out.println ("  "+ fltFmt.format(p[L-1]) );

        return p[L - 1];
    }

    /*----------------------------------------------------------*/
    public double interp(float x) {
        double p[] = new double[N];
        p[0] = f[0][0];

        for (int k = 1; k < N; k++) {
            double t = f[0][k];

            for (int i = 0; i < k; i++) {
                t *= x - xV[i];
            }

            p[k] = p[k - 1] + t;
        }

        return p[N - 1];
    }
}
