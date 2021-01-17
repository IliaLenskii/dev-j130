
import com.company.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        System.out.println("Will App start as server? (default is no) [no/yes]:");

        Scanner options = new Scanner(System.in);
        String line1 = options.nextLine();

        try {

            if(line1.equals("yes"))
                new Server();
            else
                new Client();

        } catch (ChatException err) {

            err.printStackTrace();
        }

        System.out.println("End");
    }
}