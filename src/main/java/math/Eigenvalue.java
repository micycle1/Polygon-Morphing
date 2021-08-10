package math;

public class Eigenvalue {
  public static double[] eigenvalue(double a, double b, double c) {
    int igh = 1;
    double[] wr = new double[2];
    double s = 0.0D, zz = 0.0D;
    int en = 1;
    double norm = Math.abs(a) + 2.0D * Math.abs(b) + Math.abs(c);
    while (en >= 0) {
      int na = en - 1;
      int l = en;
      if (l != 0) {
        s = Math.abs(a) + Math.abs(c);
        if (s == 0.0D)
          s = norm; 
        double tst1 = s;
        double tst2 = tst1 + Math.abs(b);
        if (tst2 != tst1)
          l--; 
      } 
      double x = c;
      double y = a;
      double w = b * b;
      double p = (-x + y) / 2.0D;
      double q = p * p + w;
      zz = Math.sqrt(Math.abs(q));
      if (q >= 0.0D) {
        zz = (p < 0.0D) ? (-zz + p) : (p + zz);
        wr[na] = x + zz;
        wr[en] = x + zz;
        if (zz != 0.0D)
          wr[en] = -(w / zz) + x; 
      } 
      en--;
      en--;
    } 
    return wr;
  }
  
  public static double[] hqr2(double[][] a, double[][] b) {
    int igh = 1;
    double[] wr = new double[2];
    double[] wi = new double[2];
    double s = 0.0D, zz = 0.0D;
    int en = 1;
    b[0][0] = 1.0D;
    b[1][1] = 1.0D;
    b[1][0] = 0.0D;
    b[0][1] = 0.0D;
    double norm = Math.abs(a[0][0]) + Math.abs(a[0][1]) + Math.abs(a[1][0]) + Math.abs(a[1][1]);
  }
  
  public static double[] hrq_tweaked(double[][] a) {
    int igh = 1;
    double[] wr = new double[2];
    double s = 0.0D, zz = 0.0D;
    int en = 1;
    double norm = Math.abs(a[0][0]) + Math.abs(a[0][1]) + Math.abs(a[1][0]) + Math.abs(a[1][1]);
    while (en >= 0) {
      int na = en - 1;
      int l = en;
      if (l != 0) {
        s = Math.abs(a[l - 1][l - 1]) + Math.abs(a[l][l]);
        if (s == 0.0D)
          s = norm; 
        double tst1 = s;
        double tst2 = tst1 + Math.abs(a[l][l - 1]);
        if (tst2 != tst1)
          l--; 
      } 
      double x = a[en][en];
      if (l == en) {
        wr[en] = x;
        en--;
        continue;
      } 
      double y = a[na][na];
      double w = a[en][na] * a[na][en];
      if (l == na) {
        double p = (-x + y) / 2.0D;
        double q = p * p + w;
        zz = Math.sqrt(Math.abs(q));
        if (q >= 0.0D) {
          zz = (p < 0.0D) ? (-zz + p) : (p + zz);
          wr[na] = x + zz;
          wr[en] = x + zz;
          if (zz != 0.0D)
            wr[en] = -(w / zz) + x; 
        } 
      } 
      en--;
      en--;
    } 
    return wr;
  }
}
