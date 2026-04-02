public class advanced {
    public static  int[][] findbox(int[][] matrice){
        int minrand=9999999;
        int maxrand=-1;
        int mincol=9999999;
        int maxcol=-1;
        int gasit=0;
        for (int i=0;i<=matrice.length-1;i++) // cate randuri sunt matrice.length
            for (int j=0; j<= matrice[0].length-1;j++) //cate coloane sunt matrice[0].length
            {
                if (matrice[i][j]==1)
                {
                    gasit=1;
                    if (i<minrand)
                        minrand=i;
                    if (i>maxrand)
                        maxrand=i;
                    if (j<mincol)
                        mincol=j;
                    if (j>maxcol)
                        maxcol=j;
                }
            }
        if (gasit==0) return matrice;
        for(int i=0;i<=matrice.length-1;i++)
            for (int j=0; j<= matrice[0].length-1;j++)
            {
                if(i>=minrand-1&&i<=maxrand+1 && j==mincol-1)
                    matrice[i][j]=2;
                if(i>=minrand-1&&i<=maxrand+1 && j==maxcol+1)
                    matrice[i][j]=2;
                if(j>=mincol-1&&j<=maxcol+1 && i==minrand-1)
                    matrice[i][j]=2;
                if(j>=mincol-1&&j<=maxcol+1 && i==maxrand+1)
                    matrice[i][j]=2;
            }
        return matrice;




    }
    public static int[][] findBoundary(int[][] matrice) {
        for (int i = 0; i <= matrice.length - 1; i++) // cate randuri sunt matrice.length
            for (int j = 0; j <= matrice[0].length - 1; j++) //cate coloane sunt matrice[0].length
            {
                if (matrice[i][j] == 1) {
                    if (i>0 && matrice[i - 1][j] != 1)
                        matrice[i - 1][j] = 2;
                    if (j> 0 && matrice[i][j - 1] != 1)
                        matrice[i][j - 1] = 2;
                    if (i < matrice.length-1 && matrice[i + 1][j] != 1)
                        matrice[i + 1][j] = 2;
                    if (j<matrice[0].length-1 && matrice[i][j + 1] != 1)
                        matrice[i][j + 1] = 2;

                }
            }
        return matrice;
    }
    public static void main(String args[])
    {
        int[][] matriceBox = {
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 1, 0, 0, 0, 0},
                {0, 0, 0, 1, 0, 0, 0, 0},
                {0, 0, 0, 1, 1, 1, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0}
        };
        int[][] matriceBoundary = {
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 1, 0, 0, 0, 0},
                {0, 0, 0, 1, 0, 0, 0, 0},
                {0, 0, 0, 1, 1, 1, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0}
        };
        System.out.println("Matrice initiala: \n");
        System.out.println(Matrix2D.reprezentare(matriceBox));

        int[][] rezultat = findbox(matriceBox);
        System.out.println("Matrice cu bounding box: \n");
        System.out.println(Matrix2D.reprezentare(rezultat));
        int[][] rezultatboundary = findBoundary(matriceBoundary);
        System.out.println("Matrice cu boundary: \n");
        System.out.println(Matrix2D.reprezentare(rezultatboundary));


    }
}
