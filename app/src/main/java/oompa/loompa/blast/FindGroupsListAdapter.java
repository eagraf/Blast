package oompa.loompa.blast;

import android.util.Log;

/**
 * Created by Da-Jin on 7/23/2015.
 */
public class FindGroupsListAdapter extends GroupListAdapter{

    public FindGroupsListAdapter(GroupManager groupManager) {
        super(groupManager);
    }

    @Override
    public int getItemCount() {
        if(groupManager.allGroupsMeta!=null){
            return groupManager.allGroupsMeta.size();
        }
        return 0;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Log.i("GroupList", groupManager.allGroupsMeta.size()+" "+i);
        bindViewHolderWithMetadata(viewHolder, groupManager.allGroupsMeta.get(i));
    }
}
