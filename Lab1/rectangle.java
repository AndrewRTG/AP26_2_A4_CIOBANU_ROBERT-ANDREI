public class rectangle {
    public static int[][] draw(int n){

        int[][] matrice= new int[n][n];
        for (int i=0;i<n;i++)
            for(int j=0;j<n;j++)
            {
                if(i>=n/5 && i<=n-n/5 && j>=n/5 && j<=n-n/5)
                    matrice[i][j]=0;
                else matrice[i][j]=255;
            }

       return matrice;

    }
}
