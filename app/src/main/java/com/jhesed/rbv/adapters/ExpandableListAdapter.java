package com.jhesed.rbv.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jhesed.rbv.R;

import java.util.List;
import java.util.Map;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private final String TAG = "ExpandableListAdapter";
    private final Activity context;
    private final Map<String, List<String>> prayerCollections;
    private final Map<String, List<Integer>> prayerCollectionIDs;
    private final Map<String, List<String>> prayerCategories;
    private final List<String> prayers;

    public ExpandableListAdapter(Activity context, List<String> prayers,
                                 Map<String, List<String>> prayerCollections,
                                 Map<String, List<Integer>> prayerCollectionIDs,
                                 Map<String, List<String>> prayerCategories) {
        this.context = context;
        this.prayerCollections = prayerCollections;
        this.prayerCollectionIDs = prayerCollectionIDs; // ids of prayer items
        this.prayerCategories = prayerCategories;
        this.prayers = prayers;
    }

    public Object getChild(int groupPosition, int childPosition) {
        return prayerCollections.get(prayers.get(groupPosition)).get(childPosition);
    }

    public Object getChildDBID(int groupPosition, int childPosition) {
        return prayerCollectionIDs.get(prayers.get(groupPosition)).get(childPosition);
    }

    public Object getChildCategory(int groupPosition, int childPosition) {
        return prayerCategories.get(prayers.get(groupPosition)).get(childPosition);
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }


    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String prayer = (String) getChild(groupPosition, childPosition);

        // Tag to Prayer with Database ID
        final Integer prayerId = (Integer) getChildDBID(groupPosition, childPosition);
        LayoutInflater inflater = context.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.accordion_item, null);
        }

        TextView item = convertView.findViewById(R.id.prayer_item);

        item.setText(prayer);
        convertView.setTag(prayerId);

        this.updateCategoryIcon(groupPosition, childPosition, convertView);

        return convertView;
    }

    public int getChildrenCount(int groupPosition) {
        return prayerCollections.get(prayers.get(groupPosition)).size();
    }

    public Object getGroup(int groupPosition) {
        return prayers.get(groupPosition);
    }

    public int getGroupCount() {
        return prayers.size();
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String prayerName = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.accordion_group,
                    null);
        }
        TextView item = convertView.findViewById(R.id.group_title);
        item.setTypeface(null, Typeface.BOLD);
        item.setText(prayerName);
        return convertView;
    }

    public boolean hasStableIds() {
        return true;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private void updateCategoryIcon(int groupPosition, int childPosition, View convertView) {
        // Update category icon
        final String category = (String) getChildCategory(groupPosition, childPosition);
        final ImageView categoryObj = convertView.findViewById(R.id.category);

//        Log.d(this.TAG, "(ExpandableListAdapter.updateCategoryIcon) Got category: " + category);
        if (category.equals("PERSONAL")) {
            categoryObj.setImageResource(R.drawable.ic_baseline_face_24);
        } else if (category.equals("FAMILY")) {
            categoryObj.setImageResource(R.drawable.ic_baseline_home_24);
        } else if (category.equals("CHURCH")) {
            categoryObj.setImageResource(R.drawable.ic_baseline_location_city_24);
        } else if (category.equals("COMMUNITY")) {
            categoryObj.setImageResource(R.drawable.ic_baseline_supervised_user_circle_24);
        } else {
            // Generic category for now
            // TODO: V2: User should be able to add their own category
            categoryObj.setImageResource(R.drawable.ic_pray);
        }
    }
}