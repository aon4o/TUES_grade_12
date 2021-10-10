import java.math.BigInteger;

public class Main {
    public static boolean checkPrime(int num) {
        boolean flag = false;
        for (int i = 2; i <= num / 2; ++i) {
            if (num % i == 0) {
                flag = true;
                break;
            }
        }
        return !flag;
    }

    public static void main(String[] args) {
        for (String arg : args) {
            try {
                BigInteger number = new BigInteger(arg);

//                OUT OF BOUND CHECK
                if (number.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) > 0)
                {
                    System.out.println(arg + " is out of bound");
                    continue;
                }

//                CHECKING FOR PRIME
                if (checkPrime(Integer.parseInt(arg))) {
                    System.out.println(arg + " is a prime");
                } else {
                    System.out.println(arg + " is a not prime");
                }
            }
            catch (Exception e) {
//                CHECK IF FLOATING POINT NUMBER
                try {
                    float number = Float.parseFloat(arg);
                }
                catch (Exception f) {
                    System.out.println(arg + " is not a number");
                    continue;
                }
                System.out.println(arg + " is not an integer");
            }
        }
    }
}
