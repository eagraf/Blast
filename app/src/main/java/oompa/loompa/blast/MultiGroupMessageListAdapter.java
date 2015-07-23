package oompa.loompa.blast;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * Created by Da-Jin on 7/23/2015.
 */
public class MultiGroupMessageListAdapter extends MessageListAdapter {
    @Override
    public MessageListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        // create a new view
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notification_list_item, viewGroup, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder((RelativeLayout) v);
        return vh;
    }
    @Override
    public void setGroup(Group group){
        return;
    }
    @Override
    public void updateGroup(Group group){
        mDataSet.putAll(group.getMessages());
        keys= new LinkedList<>(Arrays.asList(mDataSet.keySet().toArray()));
        notifyDataSetChanged();
    }
}
