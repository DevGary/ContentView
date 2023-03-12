package com.devgary.contentviewdemo.util;

import static com.devgary.contentview.ui.util.ViewUtilsKt.getAbsoluteBottom;
import static com.devgary.contentview.ui.util.ViewUtilsKt.getAbsoluteTop;
import static com.devgary.contentview.ui.util.ViewUtilsKt.getPercentVisible;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.IntRange;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// TODO [!]: Clean up this class and convert to Kotlin
public class RecyclerViewUtils {

    public static class VisibilityInfo {

        private int visibilityPercentage;
        private int distanceFromCenterY;

        public VisibilityInfo(int visibilityPercentage, int distanceFromCenterY) {
            this.visibilityPercentage = visibilityPercentage;
            this.distanceFromCenterY = distanceFromCenterY;
        }

        public int getVisibilityPercentage() {
            return visibilityPercentage;
        }

        public void setVisibilityPercentage(int visibilityPercentage) {
            this.visibilityPercentage = visibilityPercentage;
        }

        public int getDistanceFromCenterY() {
            return distanceFromCenterY;
        }

        public void setDistanceFromCenterY(int distanceFromCenterY) {
            this.distanceFromCenterY = distanceFromCenterY;
        }
    }

    public static List<Integer> getVisibleItemPositions(LinearLayoutManager linearLayoutManager) {

        ArrayList<Integer> visibleItemPositions = new ArrayList<>();

        if (linearLayoutManager == null) return visibleItemPositions;

        int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
        int lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();

        for (int i = firstVisibleItemPosition; i <= lastVisibleItemPosition; i++) {

            visibleItemPositions.add(i);
        }

        return visibleItemPositions;
    }

    public static Map<Integer, VisibilityInfo> getLinearLayoutManagerVisibilityInfo(LinearLayoutManager linearLayoutManager, RecyclerView recyclerView, int focusViewId) {

        Map<Integer, VisibilityInfo> visbilityInfo = new HashMap<>();

        if (linearLayoutManager == null || recyclerView == null) return visbilityInfo;

        List<Integer> visibleItemPositions = getVisibleItemPositions(linearLayoutManager);

        if (!visibleItemPositions.isEmpty()) {

            int recyclerViewCenterY = recyclerView.getHeight() / 2;
            int minDistanceFromCenterY = 999999;
            int index = 0;

            for (Integer visibleItemAdapterPosition : visibleItemPositions) {

                View v = linearLayoutManager.findViewByPosition(visibleItemAdapterPosition);

                if (v == null) continue;

                View focusView = v.findViewById(focusViewId);

                if (focusView == null) focusView = recyclerView.getChildAt(index);

                if (focusView == null) continue;

                Rect globalVisibleRect = new Rect();

                focusView.getGlobalVisibleRect(globalVisibleRect);

                if (focusView.getHeight() != 0) {

                    int centerY;
                    int viewTop = getAbsoluteTop(focusView);
                    int viewBottom = getAbsoluteBottom(focusView);

                    centerY = (viewTop + viewBottom) / 2;

                    int distanceFromCenterY = Math.abs(centerY - recyclerViewCenterY);

                    if (globalVisibleRect.top < recyclerView.getTop() && globalVisibleRect.bottom < recyclerView.getTop())
                        distanceFromCenterY = 999999;
                    if (globalVisibleRect.top > recyclerView.getBottom())
                        distanceFromCenterY = 999999;

                    if (distanceFromCenterY < minDistanceFromCenterY) {

                        minDistanceFromCenterY = distanceFromCenterY;
                    }

                    visbilityInfo.put(visibleItemAdapterPosition, new VisibilityInfo(getPercentVisible(focusView), distanceFromCenterY));
                }

                index++;
            }
        }

        return visbilityInfo;
    }

    public static VisibilityInfo getLinearLayoutManagerItemVisibilityInfo(LinearLayoutManager linearLayoutManager, RecyclerView recyclerView, int focusViewId, int position) {

        VisibilityInfo visibilityInfo = new VisibilityInfo(0, Integer.MAX_VALUE);

        if (linearLayoutManager == null || recyclerView == null) return visibilityInfo;

        if (!getVisibleItemPositions(linearLayoutManager).contains(position)) return visibilityInfo;

        visibilityInfo = getLinearLayoutManagerVisibilityInfo(linearLayoutManager, recyclerView, focusViewId).get(position);

        if (visibilityInfo == null) visibilityInfo = new VisibilityInfo(0, Integer.MAX_VALUE);

        return visibilityInfo;
    }

    public static int calculateFocusedRecyclerViewItemPosition(RecyclerView recyclerView, int focusViewId) {

        return calculateFocusedRecyclerViewItemPosition(recyclerView, focusViewId, 65);
    }

    public static int calculateFocusedRecyclerViewItemPosition(RecyclerView recyclerView, int focusViewId, @IntRange(from=0, to=100) int maxPercentDistanceFromCenterY) {

        if (recyclerView == null || recyclerView.getAdapter().getItemCount() == 0) return -1;
        if (!(recyclerView.getLayoutManager() instanceof LinearLayoutManager)) return -1;

        Map<Integer, VisibilityInfo> visibilityInfo =
                getLinearLayoutManagerVisibilityInfo((LinearLayoutManager) recyclerView.getLayoutManager(), recyclerView, focusViewId);

        if (visibilityInfo.isEmpty()) return -1;

        int focusedPosition = -1;
        int minDistanceFromCenterY = 999999;
        int maxPossibleDistanceFromCenterY = recyclerView.getHeight() / 2;

        for (Integer position : visibilityInfo.keySet()) {

            VisibilityInfo infoOfPosition = visibilityInfo.get(position);

            int distanceFromCenterY = infoOfPosition.getDistanceFromCenterY();

            if (distanceFromCenterY < minDistanceFromCenterY) {

                minDistanceFromCenterY = distanceFromCenterY;
                focusedPosition = position;
            }
        }

        // If distance from center is more than a certain percent of max distance, don't count it as focused
        int distanceFromCenterYThreshold = (int) ((double) maxPercentDistanceFromCenterY / 100 * maxPossibleDistanceFromCenterY);

        if (minDistanceFromCenterY > distanceFromCenterYThreshold) {
            focusedPosition = -1;
        }

        return focusedPosition;
    }

    public static int calculateFocusedRecyclerViewItemPosition(RecyclerView recyclerView) {

        return calculateFocusedRecyclerViewItemPosition(recyclerView, 0);
    }

    public static int calculateCenteredRecyclerViewItemPosition(RecyclerView recyclerView, int focusViewId) {

        if (recyclerView == null || recyclerView.getAdapter().getItemCount() == 0) return -1;
        if (!(recyclerView.getLayoutManager() instanceof LinearLayoutManager)) return -1;

        Map<Integer, VisibilityInfo> visibilityInfo =
                getLinearLayoutManagerVisibilityInfo((LinearLayoutManager) recyclerView.getLayoutManager(), recyclerView, focusViewId);

        if (visibilityInfo.isEmpty()) return -1;

        int positionClosestToCenterY = -1;
        int minDistanceFromCenterY = 9999999;
        int maxPossibleDistanceFromCenterY = recyclerView.getHeight() / 2;

        for (Integer position : visibilityInfo.keySet()) {

            VisibilityInfo infoOfPosition = visibilityInfo.get(position);

            int distanceFromCenterY = infoOfPosition.getDistanceFromCenterY();

            if (distanceFromCenterY < minDistanceFromCenterY) {

                minDistanceFromCenterY = distanceFromCenterY;
                positionClosestToCenterY = position;
            }
        }

        return positionClosestToCenterY;
    }

    public static int calculateCenteredRecyclerViewItemPosition(RecyclerView recyclerView) {

        return calculateCenteredRecyclerViewItemPosition(recyclerView, 0);
    }

    public static void smoothScrollToPosition(RecyclerView recyclerView, int position) {

        position = Math.min(position, recyclerView.getAdapter().getItemCount());
        position = Math.max(position, 0);

        recyclerView.smoothScrollToPosition(position);
    }

    public static String readableScrollState(int scrollState){

        switch (scrollState){

            case RecyclerView.SCROLL_STATE_IDLE:

                return "IDLE";

            case RecyclerView.SCROLL_STATE_DRAGGING:

                return "DRAGGING";

            case RecyclerView.SCROLL_STATE_SETTLING:

                return "SETTLING";

            default:

                return "UNKNOWN (" + scrollState + ")";
        }
    }

    public static List<RecyclerView.ViewHolder> getAllVisibleViewHolders(RecyclerView recyclerView){

        List<RecyclerView.ViewHolder> viewHolders = new ArrayList<>();

        for (int childCount = recyclerView.getChildCount(), i = 0; i < childCount; ++i) {

            final RecyclerView.ViewHolder holder = recyclerView.getChildViewHolder(recyclerView.getChildAt(i));

            viewHolders.add(holder);
        }

        return viewHolders;
    }
}
