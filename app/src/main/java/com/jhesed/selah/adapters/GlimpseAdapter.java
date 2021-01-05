package com.jhesed.selah.adapters;

/**
 * Created by jhesed on 7/28/2018.
 */


import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.jhesed.selah.R;
import com.jhesed.selah.models.Glimpse;
import com.jhesed.selah.ui.cdhd.Common;
import com.jhesed.selah.ui.cdhd.FragmentCDHD;

import java.util.ArrayList;

public class GlimpseAdapter extends ArrayAdapter<Glimpse> {

    /* SECTION: Variable Declarations */

    private Context context;

    public GlimpseAdapter(Context adapterContext, ArrayList<Glimpse> glimpseList) {
        super(adapterContext, R.layout.glimpse_entry, glimpseList);
        context = adapterContext;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        /*
         * Overrides parent for retrieving view
         * */

        View view = convertView;
        final ViewHolder viewHolder;
        Glimpse glimpse = getItem(position);

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.glimpse_entry, null);
            viewHolder = new ViewHolder();
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        try {
            // Populate the data into the template view using the data object
            viewHolder.glimpseDate = glimpse.glimpseDate;

            viewHolder.glimpseDay = view.findViewById(R.id.glimpse_day);
            viewHolder.glimpseDay.setText(Common.fromHtml(glimpse.glimpseDay));

            viewHolder.headingWorld = view.findViewById(R.id.title_world);
            viewHolder.headingWorld.setText(Common.fromHtml(glimpse.headingWorld));

            viewHolder.headingPhil = view.findViewById(R.id.title_phil);
            viewHolder.headingPhil.setText(Common.fromHtml(glimpse.headingPhil));

            view.setTag(viewHolder);
            view.setLongClickable(true);

            /* SECTION : Events */

            view.setOnClickListener(new View.OnClickListener() {
                // TODO

                @Override
                public void onClick(View view) {
                    popUpGlimpseDetails(context, viewHolder.glimpseDate);
                }
            });
        } catch (Exception exc) {
            // TODO, error on pagination

        }
        return view;
    }

    protected void popUpGlimpseDetails(Context context, String date) {
        /**
         * Pop up historic glimpse
         */

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // Get the layout inflater
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View layout = inflater.inflate(R.layout.fragment_day_dialog, null);
        builder.setView(layout);
        final AlertDialog dialog = builder.create();

        TextView dialogTitle = layout.findViewById(R.id.dialogTitle);
        dialogTitle.setText(Common.fromHtml(date));
        Common.getGlimpse(date, layout, FragmentCDHD.getAPIInterface());

        // close dialog box on click

        Button okButton = layout.findViewById(R.id.ok_button);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        ImageView cancelButton = layout.findViewById(R.id.cancel_icon);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private static class ViewHolder {
        /**
         * Private class for recyclable information
         */
        String glimpseDate;

        Button glimpseDay;
        TextView headingWorld;
        TextView headingPhil;

    }

}