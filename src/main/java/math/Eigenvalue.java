
package math;

public class Eigenvalue {
    public static double[] eigenvalue(double a, double b, double c) {
        boolean igh = true;
        double[] wr = new double[2];
        double s = 0.0;
        double zz = 0.0;
        double norm = Math.abs(a) + 2.0 * Math.abs(b) + Math.abs(c);
        for (int en = 1; en >= 0; --en) {
            int na = en - 1;
            int l = en;
            if (l != 0) {
                double tst1;
                double tst2;
                s = Math.abs(a) + Math.abs(c);
                if (s == 0.0) {
                    s = norm;
                }
                if ((tst2 = (tst1 = s) + Math.abs(b)) != tst1) {
                    --l;
                }
            }
            double x = c;
            double y = a;
            double w = b * b;
            double p = (-x + y) / 2.0;
            double q = p * p + w;
            zz = Math.sqrt(Math.abs(q));
            if (q >= 0.0) {
                zz = p < 0.0 ? -zz + p : p + zz;
                wr[en] = wr[na] = x + zz;
                if (zz != 0.0) {
                    wr[en] = -(w / zz) + x;
                }
            }
            --en;
        }
        return wr;
    }

    public static double[] hqr2(double[][] a, double[][] b) {
        int m;
        int i;
        double r;
        double q;
        double p;
        double w;
        double y;
        double x;
        double tst1;
        double tst2;
        int na;
        boolean igh = true;
        double[] wr = new double[2];
        double[] wi = new double[2];
        double s = 0.0;
        double zz = 0.0;
        int en = 1;
        b[0][0] = 1.0;
        b[1][1] = 1.0;
        b[1][0] = 0.0;
        b[0][1] = 0.0;
        double norm = Math.abs(a[0][0]) + Math.abs(a[0][1]) + Math.abs(a[1][0]) + Math.abs(a[1][1]);
        while (en >= 0) {
            na = en - 1;
            int l = en;
            if (l != 0) {
                s = Math.abs(a[l - 1][l - 1]) + Math.abs(a[l][l]);
                if (s == 0.0) {
                    s = norm;
                }
                if ((tst2 = (tst1 = s) + Math.abs(a[l][l - 1])) != tst1) {
                    --l;
                }
            }
            x = a[en][en];
            if (l == en) {
                wr[en] = x;
                wi[en] = 0.0;
                --en;
                continue;
            }
            y = a[na][na];
            w = a[en][na] * a[na][en];
            if (l != na) continue;
            p = (-x + y) / 2.0;
            q = p * p + w;
            zz = Math.sqrt(Math.abs(q));
            if (q >= 0.0) {
                int j;
                zz = p < 0.0 ? -zz + p : p + zz;
                wr[en] = wr[na] = x + zz;
                if (zz != 0.0) {
                    wr[en] = -(w / zz) + x;
                }
                wi[na] = 0.0;
                wi[en] = 0.0;
                x = a[en][na];
                s = Math.abs(x) + Math.abs(zz);
                p = x / s;
                q = zz / s;
                r = Math.sqrt(p * p + q * q);
                p /= r;
                q /= r;
                for (j = na; j < 2; ++j) {
                    zz = a[na][j];
                    a[na][j] = q * zz + p * a[en][j];
                    a[en][j] = -(p * zz) + q * a[en][j];
                }
                for (j = 0; j <= en; ++j) {
                    zz = a[j][na];
                    a[j][na] = q * zz + p * a[j][en];
                    a[j][en] = -(p * zz) + q * a[j][en];
                }
                for (j = 0; j <= 1; ++j) {
                    zz = b[j][na];
                    b[j][na] = q * zz + p * b[j][en];
                    b[j][en] = -(p * zz) + q * b[j][en];
                }
            } else {
                wr[en] = wr[na] = x + p;
                wi[na] = zz;
                wi[en] = -zz;
            }
            --en;
            --en;
        }
        if (norm == 0.0) {
            return wr;
        }
        for (i = 0; i < 2; ++i) {
            en = 1 - i;
            p = wr[en];
            q = wi[en];
            na = en - 1;
            if (q > 0.0) continue;
            if (q == 0.0) {
                m = en;
                a[en][en] = 1.0;
                for (int j = na; j >= 0; --j) {
                    double t;
                    int ii;
                    w = -p + a[j][j];
                    r = 0.0;
                    for (ii = m; ii <= en; ++ii) {
                        r += a[j][ii] * a[ii][en];
                    }
                    if (wi[j] < 0.0) {
                        zz = w;
                        s = r;
                        continue;
                    }
                    m = j;
                    if (wi[j] == 0.0) {
                        t = w;
                        if (t == 0.0) {
                            t = tst1 = norm;
                            while ((tst2 = norm + (t *= 0.01)) > tst1) {
                            }
                        }
                        a[j][en] = -(r / t);
                    } else {
                        x = a[j][j + 1];
                        y = a[j + 1][j];
                        q = (-p + wr[j]) * (-p + wr[j]) + wi[j] * wi[j];
                        a[j][en] = t = (-(zz * r) + x * s) / q;
                        a[j + 1][en] = Math.abs(x) > Math.abs(zz) ? -(r + w * t) / x : -(s + y * t) / zz;
                    }
                    t = Math.abs(a[j][en]);
                    if (t == 0.0 || (tst2 = (tst1 = t) + 1.0 / tst1) > tst1) continue;
                    for (ii = j; ii <= en; ++ii) {
                        double[] arrd = a[ii];
                        int n = en;
                        arrd[n] = arrd[n] / t;
                    }
                }
                continue;
            }
            m = na;
            a[na][na] = q / a[en][na];
            a[na][en] = -(-p + a[en][en]) / a[en][na];
            a[en][na] = 0.0;
            a[en][en] = 1.0;
        }
        for (i = 1; i >= 0; --i) {
            m = i < 1 ? i : 1;
            for (int ii = 0; ii <= 1; ++ii) {
                zz = 0.0;
                for (int jj = 0; jj <= m; ++jj) {
                    zz += b[ii][jj] * a[jj][i];
                }
                b[ii][i] = zz;
            }
        }
        return wr;
    }

    public static double[] hrq_tweaked(double[][] a) {
        boolean igh = true;
        double[] wr = new double[2];
        double s = 0.0;
        double zz = 0.0;
        int en = 1;
        double norm = Math.abs(a[0][0]) + Math.abs(a[0][1]) + Math.abs(a[1][0]) + Math.abs(a[1][1]);
        while (en >= 0) {
            int na = en - 1;
            int l = en;
            if (l != 0) {
                double tst1;
                double tst2;
                s = Math.abs(a[l - 1][l - 1]) + Math.abs(a[l][l]);
                if (s == 0.0) {
                    s = norm;
                }
                if ((tst2 = (tst1 = s) + Math.abs(a[l][l - 1])) != tst1) {
                    --l;
                }
            }
            double x = a[en][en];
            if (l == en) {
                wr[en] = x;
                --en;
                continue;
            }
            double y = a[na][na];
            double w = a[en][na] * a[na][en];
            if (l == na) {
                double p = (-x + y) / 2.0;
                double q = p * p + w;
                zz = Math.sqrt(Math.abs(q));
                if (q >= 0.0) {
                    zz = p < 0.0 ? -zz + p : p + zz;
                    wr[en] = wr[na] = x + zz;
                    if (zz != 0.0) {
                        wr[en] = -(w / zz) + x;
                    }
                }
            }
            --en;
            --en;
        }
        return wr;
    }
}

