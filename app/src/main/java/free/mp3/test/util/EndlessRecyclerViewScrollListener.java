package free.mp3.test.util;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public abstract class EndlessRecyclerViewScrollListener extends RecyclerView.OnScrollListener {
    private final static int VISIBLE_THRESHOLD = 5;
    private int currentPage = 0;
    private int previousTotalItemCount = 0;
    private final static int startingPageIndex = 0;
    private boolean loading = true;

    private RecyclerView.LayoutManager mLayoutManager;

    protected EndlessRecyclerViewScrollListener(LinearLayoutManager layoutManager) {
        this.mLayoutManager = layoutManager;
    }

//    public int getLastVisibleItem(int[] lastVisibleItemPositions) {
//        int maxSize = 0;
//        for (int i = 0; i < lastVisibleItemPositions.length; i++) {
//            if (i == 0) {
//                maxSize = lastVisibleItemPositions[i];
//            } else if (lastVisibleItemPositions[i] > maxSize) {
//                maxSize = lastVisibleItemPositions[i];
//            }
//        }
//        return maxSize;
//    }

    @Override
    public void onScrolled(RecyclerView view, int dx, int dy) {
        int totalItemCount = mLayoutManager.getItemCount();
        int lastVisibleItemPosition = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();

        if (totalItemCount < previousTotalItemCount) {
            this.currentPage = startingPageIndex;
            this.previousTotalItemCount = totalItemCount;
            if (totalItemCount == 0) {
                this.loading = true;
            }
        }
        if (loading && (totalItemCount > previousTotalItemCount)) {
            loading = false;
            previousTotalItemCount = totalItemCount;
        }
        if (!loading && (lastVisibleItemPosition + VISIBLE_THRESHOLD) > totalItemCount) {
            currentPage++;
            onLoadMore(currentPage, totalItemCount, view);
            loading = true;
        }
    }

//    public void resetState() {
//        this.currentPage = this.startingPageIndex;
//        this.previousTotalItemCount = 0;
//        this.loading = true;
//    }

    public abstract void onLoadMore(int page, int totalItemsCount, RecyclerView view);

}
