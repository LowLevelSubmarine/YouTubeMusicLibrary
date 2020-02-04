import com.lowlevelsubmarine.ytml.YTML;
import com.lowlevelsubmarine.ytml.library.Song;

import java.util.List;

public class SearchTest {

    public static void main(String[] args) {

        YTML ytml = new YTML();
        ytml.fetchKey();
        List<Song> songResults = ytml.search("illenium").complete().getSongs().parse().complete();
        System.out.println();
        for (Song song : songResults) {
            System.out.println(song.getName() + " by " + song.getArtists());
        }
    }

}
