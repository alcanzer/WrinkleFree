package biz.wrinklefree.wrinklefree;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import static biz.wrinklefree.wrinklefree.SignUpActivity.pager;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserRegThree#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserRegThree extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String countryCode = null;

    public UserRegThree() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserRegThree.
     */
    // TODO: Rename and change types and number of parameters
    public static UserRegThree newInstance(String param1, String param2) {
        UserRegThree fragment = new UserRegThree();
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
        View view = inflater.inflate(R.layout.fragment_user_reg_three, container, false);
        final UserInfoPref pref = new UserInfoPref(getActivity());
        Button btn = view.findViewById(R.id.clik2);
        Spinner spinner = view.findViewById(R.id.spinner);
        final EditText phNum = view.findViewById(R.id.phNum);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == 0){
                    countryCode = "1";
                }
                else{
                    countryCode = "91";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                countryCode = "1";
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(phNum.getText().toString() == null){
                    Toast.makeText(getContext(), "Fill all the details", Toast.LENGTH_SHORT).show();
                }
                else{

                    JSONObject mOTPReq = new JSONObject();
                    try {
                        mOTPReq.put("userId", pref.getKey("userID"));
                        mOTPReq.put("countryMobileCode", countryCode);
                        mOTPReq.put("mobileNumber", phNum.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    VRequest.getInstance(getActivity()).addToRequestQueue(new JsonObjectRequest(SignInActivity.BASE_URL + "requestotp", mOTPReq, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                pref.putKey("OTP", String.valueOf(response.getInt("otp")));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }));
                    pager.setCurrentItem(3);
                }
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

}
