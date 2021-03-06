package oompa.loompa.blast;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import oompa.loompa.blast.firebase.FirebaseHelper;

/**
 * Created by Ethan on 7/15/2015.
 */
public class GroupListAdapter extends RecyclerView.Adapter<GroupListAdapter.ViewHolder> {
    public GroupManager groupManager;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mFirstLine, mSecondLine, mUid;
        public ViewHolder(RelativeLayout v) {
            super(v);
            mFirstLine = (TextView) v.findViewById(R.id.firstLine);
            mSecondLine = (TextView) v.findViewById(R.id.secondLine);
            mUid = (TextView) v.findViewById(R.id.uid);
        }
    }

    //Constructor provides data for the adapter.
    public GroupListAdapter(GroupManager groupManager) {
        this.groupManager = groupManager;
    }

    //Create new views (invoked by the layout manager)
    @Override
    public GroupListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        // create a new view
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.group_list_item, viewGroup, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder((RelativeLayout) v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final GroupListAdapter.ViewHolder viewHolder, int i) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        bindViewHolderWithMetadata(viewHolder, groupManager.groups.get(i).getMetadata());
    }

    public void bindViewHolderWithMetadata(final ViewHolder viewHolder, Group.Metadata metadata){
        System.out.println(metadata.getDisplayName());
        viewHolder.mFirstLine.setText(metadata.getDisplayName());
        viewHolder.mUid.setText(metadata.getGroupUID());
        FirebaseHelper.getOtherUserInfo(metadata.getOwnerUID(), new FirebaseHelper.UserInfoCallback() {
            @Override
            public void infoArrived(User user) {
                viewHolder.mSecondLine.setText("Owner: " + user.getDisplayName());
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if(groupManager.groups != null) {
            return groupManager.groups.size();
        }
        return 0;
    }


    //Add a group to the list.
    public void addGroup(int position) {
        notifyItemInserted(position);
        notifyItemRangeChanged(position, position);
    }

    //Remove a group from the list.
    public void removeGroup(int position, int size) {
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, size);
    }

    public void changedGroup(int position){
        notifyItemChanged(position);
    }
}