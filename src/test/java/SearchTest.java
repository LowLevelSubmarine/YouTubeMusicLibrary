import com.lowlevelsubmarine.ytml.YTML;
import com.lowlevelsubmarine.ytml.library.Search;

public class SearchTest {

    public static void main(String[] args) {

        YTML ytml = new YTML();
        ytml.fetchKey();
        Search search = ytml.search("illenium").complete();
        System.out.println(search.getSongs().get().get(0).getName());

    }

}
