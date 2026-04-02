public class Matrix2D {
    public static void main(String[] args)
    {
        
        int n=Integer.parseInt(args[0]);
        String forma=args[1];
        int[][] matrice = null;
        long t1=System.currentTimeMillis();
        if (forma.equals("circle"))
        {
            matrice=circle.draw(n);
        }
        else if (forma.equals("rectangle"))
        {
            matrice=rectangle.draw(n);
        }
        else {System.out.println("Forma data nu este valida");} //pica la n=25000
        long t2=System.currentTimeMillis();
        long dif=t2-t1;
        if(matrice!=null)
              System.out.println(reprezentare(matrice));


    }

    public static String reprezentare(int[][] matrice) {
        StringBuilder reprezentarestring = new StringBuilder();
        for (int i = 0; i < matrice.length; i++) {
            for (int j = 0; j < matrice[0].length; j++) {
                if (matrice[i][j] == 0) {
                    reprezentarestring.append("⬛");
                } else if (matrice[i][j]== 2) {
                    reprezentarestring.append("❎");
                }
               else reprezentarestring.append("⬜");
            }
            reprezentarestring.append("\n");
        }
        return reprezentarestring.toString();
    }
}
