package vlm;


//--------------------------------------------------------------------
class Airfoil {

    String name;
    int nPolars;
    AirfoilPolar polar[];
    int nPtSmooth = 3;
    float minCl = (float) 9999.0;
    float maxCl = (float) -9999.0;

    //----------------------------------------------------------------
    public void clone(Airfoil af) {
        this.name = name;
        this.nPolars = nPolars;

        // polar[]; 

        this.nPtSmooth = nPtSmooth;
        this.minCl = minCl;
        this.maxCl = maxCl;
    }

    //----------------------------------------------------------------
    public Airfoil(String name, int nPolars) {
        this.name = name;
        this.nPolars = nPolars;

        polar = new AirfoilPolar[nPolars];
    }

    //----------------------------------------------------------------
    public boolean loadPolar(int i, int reynolds, int nPts,
            float cl[], float cd[], float a[]) {
        if (i < 0 || nPolars <= i) {
            return false;
        }

        polar[i] = new AirfoilPolar(nPts, reynolds, cl, cd, a);

        for (int j = 0; j < nPts; j++) {
            if (maxCl < polar[i].cl[j]) {
                maxCl = polar[i].cl[j];
            }

            if (minCl > polar[i].cl[j]) {
                minCl = polar[i].cl[j];
            }
        }

        return true;
    }

    //----------------------------------------------------------------
    public void interpol(int d, int nPts) {
        final int M = polar[d].nPts;
        final int N = 40;

        float x0 = minCl;
        float x1 = maxCl;
        double dx = (x1 - x0) / (N - 1);

        /* load vector with interpolated data */
        Vector v = new Vector(N);
        v.y[0] = x0;
        v.x[0] = (float) polar[d].clCd.interp(x0, nPts);

        for (int i = 1; i < N; i++) {
            v.y[i] = (float) (x0 + dx * i);
            v.x[i] = (float) polar[d].clCd.interp(v.y[i], nPts);
        }
    }

    // ------------------------------------------------
    private float linInterp(float x,
            float x0, float x1, float y0, float y1) {
        if (x1 == x0) {
            return y0;
        }

        return y0 + (x - x0) * (y1 - y0) / (x1 - x0);
    }

    // ----------------------------
    private float interp1Vectors(float x[], float y[], int nPts, float xx) {
        // scan up thru nPts-2 to guarantee j+1
        int j;
        for (j = 0; j < (nPts - 2); j++) {
            if (xx <= x[j + 1]) {
                break;
            }
        }

        return linInterp(xx, x[j], x[j + 1], y[j], y[j + 1]);
    }

    // ----------------------------
    private float interpVectors(float x[], float y[], int nPts, float xx) {
        int j;
        for (j = 0; j < (nPts - 3); j++) {
            if (xx <= x[j + 1]) {
                break;
            }
        }

        // System.out.println ("  "+ v.x[i] +"  "+ v.y[i]);

        float d00 = (y[j + 1] - y[j]) / (x[j + 1] - x[j]);
        float d10 = (y[j + 2] - y[j + 1]) / (x[j + 2] - x[j + 1]);
        float d01 = (d10 - d00) / (x[j + 2] - x[j]);

        float result = y[j];
        result += (xx - x[j]) * d00;
        result += (xx - x[j]) * (xx - x[j + 1]) * d00;

        return result;
    }

    // ----------------------------
    // InterPol objects were created when the polars
    // for each reynolds value were loaded.
    // this provides an interpolated Cd for any Cl
    // from raw data at a specific RN
    //
    // now, for a specific Cl across all available polars and RN,
    // we create a local InterPol object for a specific Cl,
    // providing an interpolated Cd at any RN at that specific Cl.
    public float airfoilCd2(float cl, float re) {
        float RN[] = new float[nPolars];
        float cd[] = new float[nPolars];

        // create Re/Cd vector and create 
        int k = 0;
        for (int i = 0; i < nPolars; i++) {
            if ((polar[i].cl[0] <= cl)
                    && (cl <= polar[i].cl[polar[i].nPts - 1])) {
                RN[k] = polar[i].reynolds;
                cd[k] = (float) polar[i].clCd.interp(cl, nPtSmooth);
                k++;
            }
        }

        if (k == 0) {
            return (float) -1;
        }

        // create interpolation basis and return desired value
        InterPol RNCd = new InterPol(k, RN, cd);

        if (k > nPtSmooth) {
            k = nPtSmooth;
        }

        return (float) RNCd.interp(re, k);
    }

    // ----------------------------
    // simple linear interpolation of Cd value
    // for specific Cl value between two available RN curves
    public float airfoilCd(float cl, float re) {
        // scan up thru nPolars-2 to guarantee n+2
        int n;
        for (n = 0; n < (nPolars - 2); n++) {
            if (re <= polar[n + 1].reynolds) {
                break;
            }
        }

        float cd0 = interpVectors(polar[n].cl, polar[n].cd,
                polar[n].nPts, cl);
        float cd1 = interpVectors(polar[n + 1].cl, polar[n + 1].cd,
                polar[n + 1].nPts, cl);

        float drag = linInterp(re, polar[n].reynolds,
                polar[n + 1].reynolds, cd0, cd1);

        if (false) {
            if (re < polar[n].reynolds || polar[n + 1].reynolds < re) {
                drag = -drag;
            }
        }

        return drag;
    }

    // ----------------------------
    public float airfoilAoa(float cl, float re) {
        // scan up thru nPolars-2 to guarantee n+2
        int n;
        for (n = 0; n < (nPolars - 2); n++) {
            if (re <= polar[n + 1].reynolds) {
                break;
            }
        }

        float aoa0 = interpVectors(polar[n].cl, polar[n].a,
                polar[n].nPts, cl);
        float aoa1 = interpVectors(polar[n + 1].cl, polar[n + 1].a,
                polar[n + 1].nPts, cl);

        return linInterp(re, polar[n].reynolds, polar[n + 1].reynolds,
                aoa0, aoa1);
    }

    // ----------------------------
    public float airfoilCl(float aoa, float re) {
        // scan up thru nPolars-2 to guarantee n+2
        int n;
        for (n = 0; n < (nPolars - 2); n++) {
            if (re <= polar[n + 1].reynolds) {
                break;
            }
        }

        float cl0 = interpVectors(polar[n].a, polar[n].cl,
                polar[n].nPts, aoa);
        float cl1 = interpVectors(polar[n + 1].a, polar[n + 1].cl,
                polar[n + 1].nPts, aoa);

        return linInterp(re, polar[n].reynolds, polar[n + 1].reynolds,
                cl0, cl1);
    }
}
