public class circle {
    public static int[][] draw(int n) {
        int mijloc = n / 2;
        int raza = n/3;
        int[][] m = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++) {
                if ((i - mijloc) * (i - mijloc) + (j - mijloc) * (j - mijloc) <= raza * raza) {// catetele sunt i-mij la patrat si ipotenuza este suma celor 2 daca ipotenuza este mai mica ca raza atunci punctul se afla in cerc
                    m[i][j] = 255;
                } else m[i][j] = 0;
            }
      return m;


    }


}
