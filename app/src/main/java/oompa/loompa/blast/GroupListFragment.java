package oompa.loompa.blast;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import oompa.loompa.blast.firebase.FirebaseHelper;

/**
 * Created by Ethan on 7/16/2015.
 */
public class GroupListFragment extends Fragment {
    private View view;
    private Context context;

    public RecyclerView mGroupListView;
    public GroupListAdapter mGroupListAdapter;
    public RecyclerView.LayoutManager mGroupListLayoutManager;

    private static final String houses[] = new String[] {"Stark", "Tully", "Arryn", "Greyjoy", "Lannister", "Targaryen", "Baratheon", "Tyrell", "Martell"};
    private GroupManager groupManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.group_list_fragment, container, false);

        //group manager
        groupManager = FirebaseHelper.getGroupManager();

        mGroupListView = (RecyclerView) view.findViewById(R.id.group_list_view);

        mGroupListLayoutManager = new LinearLayoutManager(getActivity());
        mGroupListView.setLayoutManager(mGroupListLayoutManager);

        // specify an adapter (see also next example)
        mGroupListAdapter = groupManager.adapter;
        mGroupListView.setAdapter(mGroupListAdapter);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mGroupListView.setHasFixedSize(true);
        return view;
    }
}
