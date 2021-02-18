package com.lowlevelsubmarine.ytml.library.interfaces;

import com.lowlevelsubmarine.ytml.YTML;
import com.lowlevelsubmarine.ytml.library.api_requests.SearchRequest;
import com.lowlevelsubmarine.ytml.library.api_requests.Searchable;
import com.lowlevelsubmarine.ytml.library.fields.ContentFields;
import com.lowlevelsubmarine.ytml.library.fields.SongFields;
import com.lowlevelsubmarine.ytml.library.fields.VideoFields;

import java.io.IOException;
import java.util.LinkedList;

public class Search {

    private final YTML ytml;
    private final String query;
    private final Section<SongFields> songSection;
    private final Section<VideoFields> videoSection;

    public Search(YTML ytml, String query) throws IOException {
        this.ytml = ytml;
        this.query = query;
        SearchRequest request = new SearchRequest(query);
        ytml.getRequestRunner().run(request);
        this.songSection = new Section<>(request.getSongResult());
        this.videoSection = new Section<>(request.getVideoResult());
    }

    public Section<SongFields> getSongSection() {
        return this.songSection;
    }

    public Section<VideoFields> getVideoSection() {
        return this.videoSection;
    }

    public class Section<T extends Searchable & ContentFields> {

        String detailParams;
        LinkedList<Content<T>> items = new LinkedList<>();

        public Section(SearchRequest.ResultFields<T> resultFields) {
            addFields(resultFields.getItems());
            this.detailParams = resultFields.getDetailedParams();
        }

        private void addFields(LinkedList<T> fields) {
            for (T item : fields) {
                Content<T> content = new Content<>(Search.this.ytml, item);
                if (!this.items.contains(content)) {
                    this.items.add(content);
                }
            }
        }

        public LinkedList<Content<T>> getItems() {
            return this.items;
        }

        public void fetchMore() throws IOException {
            SearchRequest request = new SearchRequest(Search.this.query, this.detailParams);
            Search.this.ytml.getRequestRunner().run(request);
            if (request.getSongResult().getItems() != null) {
                addFields((LinkedList<T>) request.getSongResult().getItems());
            } else if (request.getVideoResult().getItems() != null) {
                addFields((LinkedList<T>) request.getVideoResult().getItems());
            }
        }

    }

}
