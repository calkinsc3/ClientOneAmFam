package com.example.jaz020.clientoneamfam;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;


/**
 * Created by nsr009 on 7/16/2015.
 */
public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> listHeader;
    private HashMap<String, List<String>> internNames;

    /**
     * Instantiates a new Expandable list adapter.
     *
     * @param context the context
     * @param listDataHeader the list data header
     * @param listChildData the list child data
     */
    public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<String>> listChildData) {
        this.context = context;
        this.listHeader = listDataHeader;
        this.internNames = listChildData;
    }

    /**
     * Gets child.
     *
     * @param groupPosition the group position
     * @param childPosition the child position
     * @return the child
     */
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.internNames.get(this.listHeader.get(groupPosition))
                .get(childPosition);
    }

    /**
     * Gets child id.
     *
     * @param groupPosition the group position
     * @param childPosition the child position
     * @return the child id
     */
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    /**
     * Gets child view.
     *
     * @param groupPosition the group position
     * @param childPosition the child position
     * @param isLastChild the is last child
     * @param convertView the convert view
     * @param parent the parent
     * @return the child view
     */
    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.navdrawer_child_item, null);
        }

        TextView interName = (TextView) convertView.findViewById(R.id.intern_name_text);
        interName.setText(childText);

        return convertView;
    }

    /**
     * Gets children count.
     *
     * @param groupPosition the group position
     * @return the children count
     */
    @Override
    public int getChildrenCount(int groupPosition) {
        return this.internNames.get(this.listHeader.get(groupPosition))
                .size();
    }

    /**
     * Gets group.
     *
     * @param groupPosition the group position
     * @return the group
     */
    @Override
    public Object getGroup(int groupPosition) {
        return this.listHeader.get(groupPosition);
    }

    /**
     * Gets group count.
     *
     * @return the group count
     */
    @Override
    public int getGroupCount() {
        return this.listHeader.size();
    }

    /**
     * Gets group id.
     *
     * @param groupPosition the group position
     * @return the group id
     */
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    /**
     * Gets group view.
     *
     * @param groupPosition the group position
     * @param isExpanded the is expanded
     * @param convertView the convert view
     * @param parent the parent
     * @return the group view
     */
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.navdrawer_group_item, null);
        }

        TextView listTitle = (TextView) convertView.findViewById(R.id.meet_the_interns);
        listTitle.setText(headerTitle);

        return convertView;
    }

    /**
     * Has stable ids.
     *
     * @return the boolean
     */
    @Override
    public boolean hasStableIds() {
        return false;
    }

    /**
     * Is child selectable.
     *
     * @param groupPosition the group position
     * @param childPosition the child position
     * @return the boolean
     */
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}