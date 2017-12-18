package biz.wrinklefree.wrinklefree;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;

import biz.wrinklefree.wrinklefree.ResponseObjects.MyOrder;

public class MyOrdersActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    SwipeRefreshLayout main;
    RecyclerView rv;
    ArrayList<MyOrder> orderArrayList;
    OrderAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);
        orderArrayList = new ArrayList<>();
        main = (SwipeRefreshLayout) findViewById(R.id.main);
        main.setOnRefreshListener(this);
        rv = (RecyclerView) findViewById(R.id.orderList);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(llm);
        getData();


    }

    @Override
    public void onRefresh() {
        getData();

    }

    public void getData(){
        VRequest.getInstance(this).addToRequestQueue(new JsonArrayRequest(Request.Method.GET, SignInActivity.BASE_URL + "getmyorders/" + SignInActivity.userId,
                null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                main.setRefreshing(false);
                Gson gson = new Gson();
                Type type = new TypeToken<ArrayList<MyOrder>>(){}.getType();
                orderArrayList = gson.fromJson(response.toString(), type);
                adapter = new OrderAdapter(orderArrayList);
                rv.setAdapter(adapter);

                Toast.makeText(getApplicationContext(), orderArrayList.toString(), Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MyOrdersActivity.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        }));
    }
}
