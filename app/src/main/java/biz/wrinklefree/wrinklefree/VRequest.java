package biz.wrinklefree.wrinklefree;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by alcanzer on 11/13/17.
 */

public class VRequest {
    private static VRequest instance;
    private RequestQueue queue;
    private static Context ctx;

    private VRequest(Context ctx){
        this.ctx = ctx;
        queue = getRequestQueue();
    }


    public RequestQueue getRequestQueue(){
        if (queue == null){
            queue = Volley.newRequestQueue(ctx);
        }
        return queue;
    }

    public static synchronized VRequest getInstance(Context ctx){
        if(instance == null){
            instance = new VRequest(ctx);
        }
        return instance;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}
