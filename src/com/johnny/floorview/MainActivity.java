package com.johnny.floorview;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.johnny.floorview.utils.DateFormatUtils;
import com.johnny.floorview.view.FloorView;
import com.johnny.floorview.view.SubComments;
import com.johnny.floorview.view.SubFloorFactory;
import com.johnny.floorview.R;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

    private LinearLayout container;
    private LayoutInflater inflater;
    private List<Comment> datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inflater = this.getLayoutInflater();
        container = (LinearLayout) findViewById(R.id.container);
        datas = new CommentData(this).getComments();
        for (Comment cmt : datas) {
            addComment(cmt);
        }

        Log.d("systemtime", DateFormatUtils.format(new Date(System.currentTimeMillis())));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void addComment(Comment cmt) {
        ViewGroup floor = (ViewGroup) inflater.inflate(R.layout.comment_list_item, null);
        TextView floor_date = (TextView) floor.findViewById(R.id.floor_date);
        TextView floor_username = (TextView) floor.findViewById(R.id.floor_username);
        TextView floor_content = (TextView) floor.findViewById(R.id.floor_content);
        floor_date.setText(DateFormatUtils.formatPretty(cmt.getDate()));
        floor_username.setText(cmt.getUserName());
        floor_content.setText(cmt.getContent());
        FloorView subFloors = (FloorView) floor.findViewById(R.id.sub_floors);
        if (cmt.getParentId() != Comment.NULL_PARENT) {
            SubComments cmts = new SubComments(addSubFloors(cmt.getParentId(), cmt.getFloorNum() - 1));
            subFloors.setComments(cmts);
            subFloors.setFactory(new SubFloorFactory());
            subFloors.setBoundDrawer(this.getResources().getDrawable(R.drawable.bound));
            subFloors.init();
        } else {
            subFloors.setVisibility(View.GONE);
        }
        container.addView(floor);
    }

    private List<Comment> addSubFloors(long parentId, int num) {
        if (num == 0) return null;
        Comment[] cmts;
/*        if ( num >= 7 ) {
            cmts = new Comment [ 4 ] ;
            for ( Comment cmt : datas ) {
                if ( cmt.getId () == parentId ) cmts[0] = cmt ;
                if ( cmt.getParentId () == parentId && cmt.getFloorNum () <= 3 ) cmts[ cmt.getFloorNum () - 1 ] = cmt ;
                if ( cmt.getParentId () == parentId && cmt.getFloorNum () == num ) cmts[ 3 ] = cmt ;
            }
        } else {*/
        cmts = new Comment[num];
        for (Comment cmt : datas) {
            if (cmt.getId() == parentId) cmts[0] = cmt;
            if (cmt.getParentId() == parentId && cmt.getFloorNum() <= num)
                cmts[cmt.getFloorNum() - 1] = cmt;
        }
/*        }*/
        ArrayList<Comment> list = new ArrayList<Comment>();
        for (int i = 0; i < cmts.length; i++) {
            list.add(cmts[i]);
        }
        return list;
    }
}
