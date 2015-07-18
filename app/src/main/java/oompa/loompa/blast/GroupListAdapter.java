package oompa.loompa.blast;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import oompa.loompa.blast.firebase.FirebaseGroup;

/**
 * Created by Ethan on 7/15/2015.
 */
public class GroupListAdapter extends RecyclerView.Adapter<GroupListAdapter.ViewHolder> {
    public ArrayList<Group> groups;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public ViewHolder(RelativeLayout v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.firstLine);
        }
    }

    //Constructor provides data for the adapter.
    public GroupListAdapter(ArrayList<Group> groupList) {
        groups = groupList;
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
    public void onBindViewHolder(GroupListAdapter.ViewHolder viewHolder, int i) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        viewHolder.mTextView.setText(((FirebaseGroup) groups.get(i)).getMetadata().getOwnerUID());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if(groups != null) {
            return groups.size();
        }
        return 0;
    }

    //Remove a group from the list.
    public void removeGroup(int position) {
        groups.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, groups.size());
    }
}