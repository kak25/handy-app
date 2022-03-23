package is.hi.handy_app;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

import is.hi.handy_app.Entities.HandyUser;
import is.hi.handy_app.Library.HandyUserAdapter;
import is.hi.handy_app.Networking.NetworkCallback;
import is.hi.handy_app.Networking.NetworkManager;

public class HandymenFragment extends Fragment {
    private ListView mListView;
    private List<HandyUser> mHandyUsers;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_handymen, container, false);
        mListView = (ListView) view.findViewById(R.id.handymen_listview);

        NetworkManager networkManager = NetworkManager.getInstance(getActivity());
        networkManager.getHandymen(new NetworkCallback<List<HandyUser>>() {
            @Override
            public void onSuccess(List<HandyUser> result) {
                mHandyUsers = result;
                HandyUserAdapter adapter = new HandyUserAdapter(HandymenFragment.this.getActivity(), mHandyUsers);
                mListView.setAdapter(adapter);
                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Toast.makeText(HandymenFragment.this.getActivity() , mHandyUsers.get(i).getName(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onaFailure(String errorString) {
                Log.d(TAG, "Failed to get handymen: " + errorString);
            }
        });

        return view;
    }
}