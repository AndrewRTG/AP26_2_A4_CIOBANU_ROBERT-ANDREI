public class main{
    public static void main(String[] args)
    {
        System.out.println("Hello World!");
        String[] languages={"C", "C++", "C#", "Python", "Go", "Rust", "JavaScript", "PHP", "Swift", "Java"};
        int n = (int) (Math.random() * 1_000_000);
        int result=n * 3;
        result=result + 0b10101;
        result=result + 0xFF;
        result=result * 6;
        System.out.println(result);
        while(result>=10)
        {
            int suma=0;
            int k=result;
            while (k>0)
            {
                suma+=k%10;
                k/=10;
            }
            result=suma;
            System.out.println("Rezultat temporar: " + result);
        }
        System.out.println("Willy-nilly, this semester I will learn " + languages[result]);
    }

}