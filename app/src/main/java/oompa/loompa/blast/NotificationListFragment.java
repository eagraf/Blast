package oompa.loompa.blast;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import oompa.loompa.blast.firebase.FirebaseHelper;

/**
 * Created by Ethan on 7/16/2015.
 */
public class NotificationListFragment extends Fragment {

    private View view;
    private Context context;

    public RecyclerView mNotificationListView;
    public MultiGroupMessageListAdapter mNotificationListAdapter;
    public RecyclerView.LayoutManager mNotificationListLayoutManager;

    private static final String planets[] = new String[] {"Sun", "Mercury", "Venus", "Earth", "Mars", "Jupiter", "Saturn", "Uranus", "Neptune", "Pluto"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.notification_list_fragment, container, false);

        mNotificationListView = (RecyclerView) view.findViewById(R.id.notification_list_view);

        mNotificationListLayoutManager = new LinearLayoutManager(getActivity());
        mNotificationListView.setLayoutManager(mNotificationListLayoutManager);
        // specify an adapter (see also next example)
        mNotificationListAdapter = FirebaseHelper.getGroupManager().inboxAdapter;
        mNotificationListView.setAdapter(mNotificationListAdapter);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mNotificationListView.setHasFixedSize(true);
        return view;
    }

    //Remove a notification
    public void removeNotification(View v) {
        //TODO make this work through firebase and it could just update automatically
        int position = mNotificationListView.getChildAdapterPosition((RelativeLayout) v.getParent().getParent().getParent());
        mNotificationListAdapter.mDataSet.remove(mNotificationListAdapter.keys.get(position));
        mNotificationListAdapter.keys.remove(position);
        mNotificationListAdapter.notifyItemRemoved(position);
        mNotificationListAdapter.notifyItemRangeChanged(position, mNotificationListAdapter.mDataSet.size());
    }
}
