package vlm;


class Vector {

    float x[];
    float y[];
    int size;

    //--------------------------------------------------
    public Vector(int size) {
        this.size = size;
        x = new float[size];
        y = new float[size];
    }

    //--------------------------------------------------
    public void clone(Vector v) {
        this.size = v.size;

        for (int i = 0; i < size; i++) {
            this.x[i] = v.x[i];
            this.y[i] = v.y[i];
        }
    }

    //--------------------------------------------------
    public float avgY() {
        float sum = 0;

        for (int i = 0; i < size; i++) {
            sum += y[i];
        }

        return sum / size;
    }

    //--------------------------------------------------
    public float maxX() {
        float max = x[0];

        for (int i = 1; i < size; i++) {
            if (max < x[i]) {
                max = x[i];
            }
        }

        return max;
    }

    //--------------------------------------------------
    public float minX() {
        float min = x[0];

        for (int i = 1; i < size; i++) {
            if (min > x[i]) {
                min = x[i];
            }
        }

        return min;
    }
    //--------------------------------------------------

    public float maxY() {
        float max = y[0];

        for (int i = 1; i < size; i++) {
            if (max < y[i]) {
                max = y[i];
            }
        }

        return max;
    }

    //--------------------------------------------------
    public float minY() {
        float min = y[0];

        for (int i = 1; i < size; i++) {
            if (min > y[i]) {
                min = y[i];
            }
        }

        return min;
    }
}
