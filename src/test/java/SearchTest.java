import com.lowlevelsubmarine.ytml.YTML;
import com.lowlevelsubmarine.ytml.YTMLBuilder;
import com.lowlevelsubmarine.ytml.library.interfaces.Search;

public class SearchTest {

    public static void main(String[] args) throws Exception {
        YTML ytml = new YTMLBuilder().build().complete();
        Search search = ytml.search("faint we wont be alone").complete();
        search.getSongSection().fetchMore();
        search.getSongSection().getItems().forEach(song -> System.out.println(song.getArtist() + " / " + song.getTitle()));
    }

}
