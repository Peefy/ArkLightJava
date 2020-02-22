
import src.datetime.DateTime;
/**
 * main
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("Hello ArkLight Java!");
        System.out.println(DateTime.now().toString());
        System.out.println(DateTime.now().addDays(12).addMonths(-1).toString());
    }
}
