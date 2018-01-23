package biz.wrinklefree.wrinklefree;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import static biz.wrinklefree.wrinklefree.SignUpActivity.pager;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private UserInfoPref userInfoPref;

    TextView mEmail;
    TextView mAddress;
    TextView mUsername;
    TextView mPh;


    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        getActivity().setTitle("Profile");

        userInfoPref = new UserInfoPref(getContext());

        mEmail = view.findViewById(R.id.profileemail);
        mAddress = view.findViewById(R.id.profileadd);
        mUsername = view.findViewById(R.id.profileuser);
        mPh = view.findViewById(R.id.profileph);
        ImageButton addEdit = view.findViewById(R.id.editAddress);
        ImageButton phEdit = view.findViewById(R.id.editPhNum);
        final Intent intent = new Intent(getContext(), SignUpActivity.class);

        getUserInfo();

        addEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("FormUpdate", "Address");
                startActivity(intent);
            }
        });

        phEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("FormUpdate", "Mobile");
                startActivity(intent);
            }
        });
        return view;
    }

    public void getUserInfo(){
        VRequest.getInstance(getContext()).addToRequestQueue(new JsonObjectRequest(Request.Method.GET, SignInActivity.BASE_URL + "getuserinfo/" + userInfoPref.getKey("userID"), null
                , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject userInfo = response.getJSONArray("userInfo").getJSONObject(0);
                    JSONObject userAddr = response.getJSONArray("userAddressInfo").getJSONObject(0);
                    mEmail.setText(userInfo.get("emailAddress").toString());
                    String username = userInfo.get("firstName") +" " + userInfo.get("lastName");
                    mUsername.setText(username);
                    String addr = userAddr.get("addressLine1") + "\n" + userAddr.get("addressLine2") + "\n" + userAddr.get("pincode");
                    mAddress.setText(addr);
                    mPh.setText("+"+userInfo.get("countryMobileCode").toString()+userInfo.get("mobileNumber").toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }));
    }

}
