package oompa.loompa.blast;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import oompa.loompa.blast.firebase.FirebaseGroup;
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
        public TextView mTextView, mSecondLine;
        public ViewHolder(RelativeLayout v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.firstLine);
            mSecondLine = (TextView) v.findViewById(R.id.secondLine);
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
        /*
        try {
            Thread.sleep(1);
        }
        catch(InterruptedException e) {

        }*/
        viewHolder.mTextView.setText(((FirebaseGroup) groupManager.groups.get(i)).getMetadata().getDisplayName());
        viewHolder.mTextView.setTag(((FirebaseGroup) groupManager.groups.get(i)).getUID());
        FirebaseHelper.getOtherUserInfo(((FirebaseGroup) groupManager.groups.get(i)).getMetadata().getOwnerUID(), new FirebaseHelper.UserInfoCallback() {
            @Override
            public void infoArrived(User user) {
                viewHolder.mSecondLine.setText("Owner: "+user.getDisplayName());
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
        notifyItemRangeChanged(position, groupManager.groups.size()-1);
    }

    //Remove a group from the list.
    public void removeGroup(int position) {
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, groupManager.groups.size()-1);
    }

    public void changedGroup(int position){
        notifyItemChanged(position);
    }
}