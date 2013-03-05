package vlm;



//--------------------------------------------------------------------
class AirfoilPolar {

    int reynolds;
    int nPts;
    float a[];
    float cd[];
    float cl[];
    InterPol clCd;

    public AirfoilPolar(int nPts, int reynolds) {
        this.nPts = nPts;
        this.reynolds = reynolds;
        a = new float[nPts];
        cd = new float[nPts];
        cl = new float[nPts];
    }

    public AirfoilPolar(int nPts, int reynolds,
            float cl[], float cd[], float a[]) {
        this.nPts = nPts;
        this.reynolds = reynolds;

        this.cl = new float[nPts];
        this.cd = new float[nPts];
        this.a = new float[nPts];

        this.cl = cl;
        this.cd = cd;
        this.a = a;

        // create InterPol object allowing interpolated Cd values based on Cl
        clCd = new InterPol(nPts, cl, cd);
    }

    public void prep() {
        clCd = new InterPol(nPts, cl, cd);
    }

    public void clone(AirfoilPolar af) {
        this.nPts = af.nPts;

        for (int i = 0; i < nPts; i++) {
            this.a[i] = af.a[i];
            this.cd[i] = af.cd[i];
            this.cl[i] = af.cl[i];
        }
    }
}
