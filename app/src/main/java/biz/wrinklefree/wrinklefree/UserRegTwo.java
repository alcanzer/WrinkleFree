package biz.wrinklefree.wrinklefree;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import static biz.wrinklefree.wrinklefree.SignInActivity.BASE_URL;
import static biz.wrinklefree.wrinklefree.SignUpActivity.pager;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserRegTwo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserRegTwo extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public UserRegTwo() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserRegTwo.
     */
    // TODO: Rename and change types and number of parameters
    public static UserRegTwo newInstance(String param1, String param2) {
        UserRegTwo fragment = new UserRegTwo();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_reg_two, container, false);
        Button btn = view.findViewById(R.id.clik1);

        final UserInfoPref pref = new UserInfoPref(getActivity());

        final EditText addOne = view.findViewById(R.id.addone);
        final EditText addTwo = view.findViewById(R.id.addtwo);
        final EditText landmark = view.findViewById(R.id.landmark);
        final EditText pincode = view.findViewById(R.id.pincode);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(addOne.getText().toString() == null || addTwo.getText().toString() == null || landmark.getText().toString() == null
                        || pincode.getText().toString() == null){
                    Toast.makeText(getContext(), "Fill all the fields", Toast.LENGTH_SHORT).show();

                }
                else{

                    pref.putKey("AddressOne", addOne.getText().toString());
                    pref.putKey("AddressTwo", addTwo.getText().toString());
                    pref.putKey("Landmark", landmark.getText().toString());
                    pref.putKey("Pincode", pincode.getText().toString());
                    JSONObject addObj = new JSONObject();

                    try {
                        addObj.put("userId", SignInActivity.userId);
                        addObj.put("addressLine1", addOne.getText().toString());
                        addObj.put("addressLine2", addTwo.getText().toString());
                        addObj.put("pincode", pincode.getText().toString());
                        addObj.put("latlong", SignInActivity.lat+","+SignInActivity.lng);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    VRequest.getInstance(getActivity()).addToRequestQueue(new JsonObjectRequest(BASE_URL + "updateaddress", addObj, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                pref.putKey("AddressId", String.valueOf(response.getJSONArray("userAddressInfo").getJSONObject(0).getInt("addressId")));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                if (!response.getBoolean("iSServiceAvailable")) {
                                    Toast.makeText(getContext(), response.getString("comments").toString(), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }));

                    pager.setCurrentItem(2);

                }
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

}
