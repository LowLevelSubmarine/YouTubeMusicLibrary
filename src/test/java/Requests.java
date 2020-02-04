import com.lowlevelsubmarine.ytml.YTML;
import com.lowlevelsubmarine.ytml.library.DefaultKeyProvider;

public class Requests {

    public static void main(String[] args) {

        DefaultKeyProvider defaultKeyProvider = new DefaultKeyProvider(new YTML());
        defaultKeyProvider.getKey().queue(Requests::onKey);
        System.out.println("parsing");

    }

    private static void onKey(String s) {
        System.out.println(s);
    }

}
