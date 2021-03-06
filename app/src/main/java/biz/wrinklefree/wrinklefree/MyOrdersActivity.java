package biz.wrinklefree.wrinklefree;

import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

import biz.wrinklefree.wrinklefree.ResponseObjects.MyOrder;

public class MyOrdersActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    SwipeRefreshLayout main;
    RecyclerView rv;
    ArrayList<MyOrder> orderArrayList;
    OrderAdapter adapter = null;
    AlertDialog.Builder builder;
    private UserInfoPref pref = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);
        pref = new UserInfoPref(getApplication());
        orderArrayList = new ArrayList<>();
        main = (SwipeRefreshLayout) findViewById(R.id.main);
        builder = new AlertDialog.Builder(this);
        main.setOnRefreshListener(this);
        rv = (RecyclerView) findViewById(R.id.orderList);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(llm);
        getData();
        rv.addOnItemTouchListener(new RecyclerItemClick(getApplication(), rv, new RecyclerItemClick.RecyclerTouchListener() {
            @Override
            public void onItemClick(View v, int position) {
                /*LayoutInflater inflater = getLayoutInflater();
                View dialog = inflater.inflate(R.layout.customdialog, null);
                builder.setView(dialog);
                final TextView shirt = dialog.findViewById(R.id.textView10);
                final TextView shirtnum = dialog.findViewById(R.id.textView11);
                final TextView pant = dialog.findViewById(R.id.textView6);
                final TextView pantnum = dialog.findViewById(R.id.textView8);
                final TextView saree = dialog.findViewById(R.id.textView9);
                final TextView sareenum = dialog.findViewById(R.id.textView7);*/
                VRequest.getInstance(getApplicationContext()).addToRequestQueue(new JsonObjectRequest(Request.Method.GET, SignInActivity.BASE_URL + "home/" + pref.getKey("userID"), null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {



                        /*try {
                            JSONArray jArr = response.getJSONArray("orderItemDetail");
                            JSONObject jObj = jArr.getJSONObject(0);
                            JSONObject jObj1 = jArr.getJSONObject(1);
                            JSONObject jObj2 = jArr.getJSONObject(2);
                            shirt.setText((CharSequence) jObj.get("itemName"));
                            shirtnum.setText(jObj.getInt("itemCount")+"");
                            pant.setText(jObj1.getString("itemName"));
                            pantnum.setText(jObj1.getInt("itemCount")+"");
                            saree.setText(jObj2.getString("itemName"));
                            sareenum.setText(jObj2.getInt("itemCount")+"");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();*/
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MyOrdersActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }));

            }
        }));


    }

    @Override
    public void onRefresh() {
        getData();

    }

    public void getData(){
        VRequest.getInstance(this).addToRequestQueue(new JsonArrayRequest(Request.Method.GET, SignInActivity.BASE_URL + "getmyorders/" + pref.getKey("userID"),
                null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                main.setRefreshing(false);
                if(response.length() == 0){
                    Toast.makeText(getApplicationContext(), "No orders", Toast.LENGTH_SHORT).show();
                }
                Gson gson = new Gson();
                Type type = new TypeToken<ArrayList<MyOrder>>(){}.getType();
                orderArrayList = gson.fromJson(response.toString(), type);
                adapter = new OrderAdapter(orderArrayList);
                rv.setAdapter(adapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MyOrdersActivity.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        }));
    }
}
