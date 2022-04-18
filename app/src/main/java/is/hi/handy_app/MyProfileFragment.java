package is.hi.handy_app;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;

import java.util.List;

import is.hi.handy_app.Entities.Ad;
import is.hi.handy_app.Entities.User;
import is.hi.handy_app.Library.AdsAdapter;
import is.hi.handy_app.Networking.NetworkCallback;
import is.hi.handy_app.Services.AdService;
import is.hi.handy_app.Services.UserService;

public class MyProfileFragment extends Fragment {
    private Context mContext;
    private User mUser;
    private List<Ad> mAds;
    private UserService mUserService;
    private AdService mAdService;

    private EditText mNameInput;
    private EditText mEmailInput;
    private EditText mDescriptionInput;
    private GridView mActiveAds;

    public MyProfileFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = MyProfileFragment.this.getActivity();
        mUserService = new UserService(mContext);
        mAdService = new AdService(mContext);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_my_profile, container, false);

        mNameInput = view.findViewById(R.id.edit_name_myProfile);
        mEmailInput = view.findViewById(R.id.edit_email_myProfile);
        mDescriptionInput = view.findViewById(R.id.About_me_myProfile);
        mActiveAds = view.findViewById(R.id.adsTV_myProfile);

        long userId = mUserService.getLoggedInUserId();
        mUserService.getUser(userId, new NetworkCallback<User>() {
            @Override
            public void onSuccess(User result) {
                mUser = result;
                setUserInfo();
            }

            @Override
            public void onaFailure(String errorString) {

            }
        });

        mAdService.findByUser(userId, new NetworkCallback<List<Ad>>() {
            @Override
            public void onSuccess(List<Ad> result) {
                mAds = result;
                if (mAds.size() > 0) {
                    setUserActiveAds();
                }
            }

            @Override
            public void onaFailure(String errorString) {

            }
        });

        return view;
    }

    private void setUserInfo() {
        mNameInput.setText(mUser.getName());
        mEmailInput.setText(mUser.getEmail());
        mDescriptionInput.setText(mUser.getInfo());
    }

    private void setUserActiveAds() {
        AdsAdapter adapter = new AdsAdapter(mContext, mAds);
        mActiveAds.setAdapter(adapter);
        AdsAdapter.setDynamicHeight(mActiveAds);
    }
}