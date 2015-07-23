package oompa.loompa.blast;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

import oompa.loompa.blast.firebase.Message;

/**
 * Created by Ethan on 7/18/2015.
 */
public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.ViewHolder> {
    public Group group;
    public TreeMap<String,Message> mDataSet = new TreeMap<>();
    public List<Object> keys;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mFirstLine;
        public TextView mSecondLine;
        public ViewHolder(RelativeLayout v) {
            super(v);
            mFirstLine = (TextView) v.findViewById(R.id.firstLine);
            mSecondLine = (TextView) v.findViewById(R.id.secondLine);
        }
    }

    //Constructor provides data for the adapter.
    public MessageListAdapter() {

    }

    //Create new views (invoked by the layout manager)
    @Override
    public MessageListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        // create a new view
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.message_list_item, viewGroup, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder((RelativeLayout) v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MessageListAdapter.ViewHolder viewHolder, int i) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        viewHolder.mSecondLine.setText(mDataSet.get(keys.get(i)).getBody());
        viewHolder.mFirstLine.setText(mDataSet.get(keys.get(i)).getSubject());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public void setGroup(Group group){
        this.group = group;
        updateGroup(group);
    }
    public void updateGroup(Group group) {
        if(this.group==null || !group.getUID().equals(this.group.getUID())){
            //Exit if we aren't listening to group.
            return;
        }
        mDataSet = (TreeMap<String, Message>) group.getMessages();
        keys= new LinkedList<>(Arrays.asList(mDataSet.keySet().toArray()));
        notifyDataSetChanged();
    }
}
