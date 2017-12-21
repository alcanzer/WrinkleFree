package biz.wrinklefree.wrinklefree;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by alcanzer on 12/20/17.
 */

public class RecyclerItemClick implements RecyclerView.OnItemTouchListener {


    private RecyclerTouchListener listener;
    private GestureDetector gd;

    public interface RecyclerTouchListener{
        public void onItemClick(View v, int position);
    }


    public RecyclerItemClick(Context ctx, final RecyclerView rv, final RecyclerTouchListener listener){

        this.listener = listener;
        gd = new GestureDetector(ctx, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent motionEvent) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent motionEvent) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent motionEvent) {
                View v = rv.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

                listener.onItemClick(v, rv.getChildAdapterPosition(v));
                return true;
            }

            @Override
            public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent motionEvent) {

            }

            @Override
            public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
                return false;
            }
        });
    }


    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View child = rv.findChildViewUnder(e.getX(), e.getY());
        return (child != null && gd.onTouchEvent(e));
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
