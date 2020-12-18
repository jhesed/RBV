package com.jhesed.rbv.ui.prayer_journal;

import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jhesed.rbv.R;
import com.jhesed.rbv.adapters.ExpandableListAdapter;
import com.jhesed.rbv.db.PrayerContract;
import com.jhesed.rbv.db.PrayerDbHelper;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SubFragmentPJPersonal extends Fragment {

    // Labels
    private static final String PENDING_TITLE = "Pending";
    private static final String DONE_TITLE = "Done";
    private static final String ANSWERED_TITLE = "Answered";
    private static ArrayList<String> pendingItems;
    private static ArrayList<String> doneItems;
    private static ArrayList<String> answeredItems;
    private static ArrayList<Integer> pendingItemIDs;
    private static ArrayList<Integer> doneItemIDs;
    private static ArrayList<Integer> answeredItemIDs;
    private static ArrayList<String> pendingCategoryItems;
    private static ArrayList<String> doneCategoryItems;
    private static ArrayList<String> answeredCategoryItems;
    private final int RESET_DONE_ITEMS_SECONDS = 86400;  // 1 day
    List<String> groupList;
    List<String> childList;
    Map<String, List<String>> prayerCollection;
    Map<String, List<Integer>> prayerCollectionIDs;
    Map<String, List<String>> prayerCategories;
    ExpandableListView expListView;
    private PrayerDbHelper dbHelper;
    private View layout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        this.dbHelper = new PrayerDbHelper(container.getContext());

        // TODO: This should be called here but calling in RBV instead
//        this.dbHelper.prepopulateData();

        this.layout =
                inflater.inflate(
                        R.layout.sub_fragment_prayer_journal_personal,
                        container, false);

        initializeList();
        FloatingActionButton addButton =
                layout.findViewById(R.id.add_prayer);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPrayerItem();
            }
        });
        return layout;
    }

    private void initializeList() {
        /*
          Initialize expandable list of prayer items
          **/

        // Initialize lists
        pendingItems = new ArrayList<>();
        doneItems = new ArrayList<>();
        answeredItems = new ArrayList<>();
        pendingItemIDs = new ArrayList<>();
        doneItemIDs = new ArrayList<>();
        answeredItemIDs = new ArrayList<>();
        pendingCategoryItems = new ArrayList<>();
        doneCategoryItems = new ArrayList<>();
        answeredCategoryItems = new ArrayList<>();

        // Populate lists
        createGroupList();
        createCollection();

        expListView = layout.findViewById(R.id.prayer_list);
        final ExpandableListAdapter expListAdapter = new ExpandableListAdapter(
                getActivity(), groupList, prayerCollection, prayerCollectionIDs, prayerCategories);

        expListView.setAdapter(expListAdapter);

        // Open accordion based on content
        if (pendingItems.size() > 0)
            expListView.expandGroup(0);
        else if (doneItems.size() > 0)
            expListView.expandGroup(1);
        else
            expListView.expandGroup(2);

        //setGroupIndicatorToRight();

        expListView.setOnChildClickListener(new OnChildClickListener() {

            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                final Integer childId = (Integer) expListAdapter.getChildDBID(
                        groupPosition, childPosition);
                showDetails(childId);
                return true;
            }
        });

        expListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position,
                                           long id) {
                if (ExpandableListView.getPackedPositionType(id) ==
                        ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                    int groupPosition = ExpandableListView.getPackedPositionGroup(id);
                    int childPosition = ExpandableListView.getPackedPositionChild(id);
                    final Integer childId = (Integer) expListAdapter.getChildDBID(
                            groupPosition, childPosition);
                    showDeleteDialog(childId);
                    return true;
                }
                return false;
            }
        });
    }

    private void createGroupList() {
        groupList = new ArrayList<String>();

        // Fetch prayer items from database
        getPrayerItems(1, 0, dbHelper.getDayInInteger());
        getPrayerItems(0, 0, dbHelper.getDayInInteger());
        getPrayerItems(0, 1, dbHelper.getDayInInteger());

        groupList.add(PENDING_TITLE + " (" + pendingItems.size() + ")");
        groupList.add(DONE_TITLE + " (" + doneItems.size() + ")");
        groupList.add(ANSWERED_TITLE + " (" + answeredItems.size() + ")");
    }

    private void createCollection() {

        prayerCollection = new LinkedHashMap<String, List<String>>();
        prayerCollectionIDs = new LinkedHashMap<String, List<Integer>>();
        prayerCategories = new LinkedHashMap<String, List<String>>();

        prayerCollection.put(groupList.get(0), pendingItems);
        prayerCollection.put(groupList.get(1), doneItems);
        prayerCollection.put(groupList.get(2), answeredItems);

        prayerCollectionIDs.put(groupList.get(0), pendingItemIDs);
        prayerCollectionIDs.put(groupList.get(1), doneItemIDs);
        prayerCollectionIDs.put(groupList.get(2), answeredItemIDs);

        prayerCategories.put(groupList.get(0), pendingCategoryItems);
        prayerCategories.put(groupList.get(1), doneCategoryItems);
        prayerCategories.put(groupList.get(2), answeredCategoryItems);

    }

    private void getPrayerItems(int isDone, int isAnswered, int day) {

        Cursor cursor = dbHelper.selectAll(isDone, day, isAnswered);
        int epochNow = (int) (System.currentTimeMillis() / 1000L);

        while (cursor.moveToNext()) {

            // Retrieve prayer details
            int prayerId =
                    cursor.getInt(cursor.getColumnIndex(PrayerContract.PrayerEntry._ID));
            String title = cursor.getString(
                    cursor.getColumnIndex(PrayerContract.PrayerEntry.COL_TITLE));
            String category = cursor.getString(
                    cursor.getColumnIndex(PrayerContract.PrayerEntry.COL_CATEGORY));

            int lastModified = cursor.getInt(cursor.getColumnIndex(
                    PrayerContract.PrayerEntry.COL_DATE_MODIFIED)
            );
            if (epochNow - lastModified >= RESET_DONE_ITEMS_SECONDS && isAnswered != 1) {
                // Revert back Done to Pending
                this.dbHelper.prayerDone(prayerId, 0);

                if (!pendingItemIDs.contains(prayerId)) {
                    pendingItems.add(title);
                    pendingItemIDs.add(prayerId);
                    pendingCategoryItems.add(category);
                }
            } else {
                // Populate prayer item ids
                if (isDone == 0 && isAnswered == 0) {
                    if (!pendingItemIDs.contains(prayerId)) {
                        pendingItems.add(title);
                        pendingItemIDs.add(prayerId);
                        pendingCategoryItems.add(category);
                    }
                } else if (isDone == 1 && isAnswered == 0) {
                    if (!doneItemIDs.contains(prayerId)) {
                        doneItems.add(title);
                        doneItemIDs.add(prayerId);
                        doneCategoryItems.add(category);
                    }
                } else if (isAnswered == 1) {
                    if (!answeredItemIDs.contains(prayerId)) {
                        answeredItems.add(title);
                        answeredItemIDs.add(prayerId);
                        answeredCategoryItems.add(category);
                    }
                }
            }
        }
    }

    private void loadChild(ArrayList<String> prayerModels) {
        childList = new ArrayList<String>();
        childList.addAll(prayerModels);
    }

    // Convert pixel to dip
    public int getDipsFromPixel(float pixels) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }

    private void showDetails(Integer childId) {
        /*
         Displays details on prayer item
         */

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View layout = inflater.inflate(R.layout.activity_view_prayer, null);
        builder.setView(layout);
        final AlertDialog dialog = builder.create();

        // Retrieve layout objects
        final TextView titleObj = layout.findViewById(R.id.dialogTitle);
        final TextView contentObj = layout.findViewById(R.id.content);
        final TextView answeredObj = layout.findViewById(R.id.answeredText);

        // Retrieve details of prayer items from database
        Cursor cursor = dbHelper.select(childId);
        cursor.moveToNext();

        final String title =
                cursor.getString(cursor.getColumnIndex(PrayerContract.PrayerEntry.COL_TITLE));
        final String content =
                cursor.getString(cursor.getColumnIndex(PrayerContract.PrayerEntry.COL_CONTENT));
        final int isAnswered =
                cursor.getInt(cursor.getColumnIndex(PrayerContract.PrayerEntry.COL_IS_ANSWERED));
        String isAnsweredText = "Soon";
        final int prayerId = childId;

        if (isAnswered == 1)
            isAnsweredText = "Yes";

        // Update dialog content
        titleObj.setText(title);
        contentObj.setText(content);
        answeredObj.setText(isAnsweredText);

        // close dialog box on click
        Button okButton = layout.findViewById(R.id.dialogOk);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (isAnswered == 0) {
                    dbHelper.prayerDone(prayerId, 1);
                    initializeList();
                }
            }
        });

        // edit dialog box
        ImageButton editButton = layout.findViewById(R.id.edit_icon);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                editDetails(prayerId, title, content, isAnswered);
            }
        });

        dialog.show();
    }

    private void editDetails(int childId, String title, final String content,
                             final int isAnswered) {
        /*
         Edit details on prayer item
         */

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = this.getLayoutInflater();
        View layout = inflater.inflate(R.layout.activity_edit_prayer, null);
        builder.setView(layout);
        final AlertDialog dialog = builder.create();

        // Retrieve layout objects
        final TextView titleObj = layout.findViewById(R.id.prayer_title);
        final TextView contentObj = layout.findViewById(R.id.prayer_content);
        final RadioGroup answeredObj = layout.findViewById(R.id.radio_answered_group);
        final int prayerId = childId;

        // Update content
        titleObj.setText(title);
        contentObj.setText(content);
        if (isAnswered == 1)
            answeredObj.check(R.id.radio_answered_yes);
        else
            answeredObj.check(R.id.radio_answered_no);

        dialog.show();

        // close dialog box on click
        ImageView cancelButton = layout.findViewById(R.id.cancel_icon);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        // submit button on click
        Button okButton = layout.findViewById(R.id.dialogOk);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Retrieves integer representation of is answered radio button
                int selectedId = answeredObj.getCheckedRadioButtonId();
                int isAnswered = dbHelper.getIsAnsweredInteger(selectedId);

                dbHelper.update(prayerId, titleObj.getText(), contentObj.getText(), isAnswered);
                Toast.makeText(getActivity().getBaseContext(), "Success", Toast.LENGTH_LONG).show();
                dialog.dismiss();
                initializeList();
            }
        });
    }

    private void showDeleteDialog(int childId) {
        /*
         Delete details on prayer item
         */

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = this.getLayoutInflater();

        View layout = inflater.inflate(R.layout.activity_delete_prayer, null);
        builder.setView(layout);
        final AlertDialog dialog = builder.create();
        final int prayerId = childId;

        dialog.show();

        // close dialog box on click
        Button cancelButton = layout.findViewById(R.id.cancel_icon);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        // submit button on click
        Button okButton = layout.findViewById(R.id.dialogOk);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHelper.delete(prayerId);
                Toast.makeText(getActivity().getBaseContext(), "Deleted", Toast.LENGTH_LONG).show();
                dialog.dismiss();
                initializeList();
            }
        });
    }

    private void addPrayerItem() {
        /*
         Add prayer item
         */

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View layout = inflater.inflate(R.layout.activity_add_prayer, null);
        builder.setView(layout);
        final AlertDialog dialog = builder.create();

        // Fetch form objects
        final EditText titleObj = layout.findViewById(R.id.prayer_title_content);
        final EditText contentObj = layout.findViewById(R.id.prayer_content);

        // submit button on click
        Button okButton = layout.findViewById(R.id.dialogOk);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String title = titleObj.getText().toString();
                String content = contentObj.getText().toString();

                dbHelper.insert(title, content, "OTHER");  // TODO: category
                initializeList();  // Reload list
                Toast.makeText(getActivity().getBaseContext(), "Success", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });

        // close dialog box on click
        ImageView cancelButton = layout.findViewById(R.id.cancel_icon);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}