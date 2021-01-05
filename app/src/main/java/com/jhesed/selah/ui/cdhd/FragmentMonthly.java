package com.jhesed.selah.ui.cdhd;

/**
 * Created by jhesed on 8/4/2018.
 */


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.jhesed.selah.R;
import com.jhesed.selah.adapters.GlimpseAdapter;
import com.jhesed.selah.models.Glimpse;
import com.jhesed.selah.pojo.cdhd.DatumList;
import com.jhesed.selah.pojo.cdhd.GlimpseListResource;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;
import static com.jhesed.selah.ui.cdhd.Common.getDayAsString;

public class FragmentMonthly extends Fragment {

    Context context;
    private ListView glimpseListView;
    private GlimpseAdapter glimpseAdapter;

    public static FragmentMonthly newInstance() {
        return new FragmentMonthly();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = this.getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_monthly, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {

        final TextView errorMessage = view.findViewById(R.id.error_message);
        final ProgressBar progressBar = view.findViewById(R.id.progress_bar);

        progressBar.setVisibility(View.VISIBLE);

        // Get Month worth of historical glimpse
        Call<GlimpseListResource> call = FragmentCDHD.getAPIInterface().getGlimpseList(
                Common.getMonthAsString());

        call.enqueue(new Callback<GlimpseListResource>() {
            @Override
            public void onResponse(Call<GlimpseListResource> call,
                                   Response<GlimpseListResource> response) {

                GlimpseListResource resource = response.body();

                List<DatumList> data;
                try {
                    data = resource.getData();
                } catch (Exception e) {
                    data = null;
                }

                progressBar.setVisibility(GONE);

                ArrayList<Glimpse> glimpseListData = new ArrayList<>();
                if (data != null) {
                    // Map response to Glimpse model object
                    for (int i = 0; i < data.size(); i++) {

                        glimpseListData.add(new Glimpse(
                                getDayAsString(data.get(i).getGlimpseDate()),
                                data.get(i).getGlimpseDate(),
                                data.get(i).getHeadingWorld(),
                                data.get(i).getHeadingPhil()));
                    }
                    // Hide error message and progress bar

                    errorMessage.setVisibility(GONE);

                    Log.e("Jhesed", "(monthly) hiding error!!");
                } else {
                    Log.e("Jhesed", "(monthly) ELSE data null!!!");
                    errorMessage.setVisibility(View.VISIBLE);
                }

                if (glimpseAdapter == null) {
                    glimpseListView = view.findViewById(R.id.glimpseList);

                    glimpseAdapter = new GlimpseAdapter(context, glimpseListData);
                    glimpseListView.setAdapter(glimpseAdapter);
                }
            }

            @Override
            public void onFailure(Call<GlimpseListResource> call, Throwable t) {
                Log.e("Jhesed", "(monthly) FAILED");
                errorMessage.setVisibility(View.VISIBLE);
                progressBar.setVisibility(GONE);
                call.cancel();
            }
        });
    }

}
