package com.jhesed.selah.ui.cdhd;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jhesed.selah.R;
import com.jhesed.selah.api.cdhd.APIInterface;
import com.jhesed.selah.pojo.cdhd.DatumDetails;
import com.jhesed.selah.pojo.cdhd.GlimpseDetailsResource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;

/**
 * Created by jhesed on 8/4/2018.
 */

public class Common {


    public static void getGlimpse(String date, View view, APIInterface apiInterface) {

        // Progress bar spinner
        final ProgressBar progressBar = view.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        // Container layouts
        final LinearLayout rContainerWh = view.findViewById(R.id.container_wh);
        final LinearLayout rContainerPh = view.findViewById(R.id.container_ph);
//        final Button rShareButton = view.findViewById(R.id.share_button);
        rContainerWh.setVisibility(GONE);
        rContainerPh.setVisibility(GONE);
        final LinearLayout groupFeaturedQuoteWh = view.findViewById(R.id.group_featured_quote_wh);
        final LinearLayout groupFeaturedQuotePh = view.findViewById(R.id.group_featured_quote_ph);
        final LinearLayout groupGenericQuoteWh = view.findViewById(R.id.group_generic_quote_wh);
        final LinearLayout groupGenericQuotePh = view.findViewById(R.id.group_generic_quote_ph);
        final LinearLayout groupReferenceWh = view.findViewById(R.id.group_reference_wh);
        final LinearLayout groupReferencePh = view.findViewById(R.id.group_reference_ph);
        final LinearLayout groupTransitionWh = view.findViewById(R.id.group_featured_transition_wh);
        final LinearLayout groupTransitionPh = view.findViewById(R.id.group_featured_transition_ph);
        // Retrieve layout objects (world history)
        final TextView rHeadingWh = view.findViewById(R.id.title_wh);
        final TextView rFeaturedVerseWh = view.findViewById(R.id.featured_verse_wh);
        final TextView rContentWh = view.findViewById(R.id.content_wh);
        final TextView rPrayerFocusWh = view.findViewById(R.id.prayer_focus_wh);
        final TextView rFeaturedQuoteWh = view.findViewById(R.id.featured_quote_wh);
        final TextView rGenericQuoteWh = view.findViewById(R.id.generic_quote_wh);
        final TextView rReferenceWh = view.findViewById(R.id.references_wh);
        final TextView rTransitionWh = view.findViewById(R.id.featured_transition_wh);

        // Retrieve layout objects (philippine history)
        final TextView rHeadingPhil = view.findViewById(R.id.title_phil);
        final TextView rFeaturedVersePhil = view.findViewById(R.id.featured_verse_phil);
        final TextView rContentPhil = view.findViewById(R.id.content_phil);
        final TextView rPrayerFocusPhil = view.findViewById(R.id.prayer_focus_phil);
        final TextView rFeaturedQuotePhil = view.findViewById(R.id.featured_quote_phil);
        final TextView rGenericQuotePhil = view.findViewById(R.id.generic_quote_ph);
        final TextView rReferencePhil = view.findViewById(R.id.references_ph);
        final TextView rTransitionPh = view.findViewById(R.id.featured_transition_ph);
        final TextView errorMessage = view.findViewById(R.id.error_message);
        errorMessage.setVisibility(GONE);

        // date title
        final TextView dateTitle = view.findViewById(R.id.title_date);
        if (dateTitle != null) {
            String humanDate = dateToHuman(date);
            dateTitle.setText(humanDate);
        }

        // Images
        final Context context = view.getContext();
        final ImageView philImage = view.findViewById(R.id.image_phil);
        final ImageView whImage = view.findViewById(R.id.image_wh);

        // Get today's historical glimpse
        Call<GlimpseDetailsResource> call = apiInterface.getGlimpseToday(date);

        call.enqueue(new Callback<GlimpseDetailsResource>() {
            @Override
            public void onResponse(Call<GlimpseDetailsResource> call,
                                   Response<GlimpseDetailsResource> response) {

                GlimpseDetailsResource resource = response.body();
                List<DatumDetails> data;
                try {
                    data = resource.getData();


                } catch (Exception e) {
                    data = null;


                }
                DatumDetails whData = null;
                DatumDetails phData = null;
                if (data != null) {
                    if (data.size() >= 1)
                        whData = data.get(0);
                    if (data.size() == 2)
                        phData = data.get(1);
                }

                // -----------------------  SECTION :: World History -------------------------------

                if (whData != null) {
                    String headingWh = whData.getHeading();
                    String featuredVerseWh = whData.getFeaturedVerse();
                    String contentWh = whData.getContent();
                    String prayerFocusWh = whData.getPrayerFocus();
                    String featuredQuoteWh = whData.getFeaturedQuote();
                    String genericQuoteWh = whData.getGenericQuote();
                    String imageLinkWh = whData.getImageLink();
                    String referenceWh = whData.getReference();
                    String transitionWh = whData.getTransition();

                    // Update dynamic content
                    rHeadingWh.setText(fromHtml(headingWh));
                    rFeaturedVerseWh.setText(fromHtml(featuredVerseWh));
                    rContentWh.setText(fromHtml(contentWh));
                    rPrayerFocusWh.setText(fromHtml(prayerFocusWh));
                    rFeaturedQuoteWh.setText(fromHtml(featuredQuoteWh));
                    rTransitionWh.setText(fromHtml(transitionWh));

                    if (transitionWh != null && !transitionWh.trim().equals("") &&
                            transitionWh.length() != 0) {
                        rTransitionWh.setText(fromHtml(transitionWh));
                        groupTransitionWh.setVisibility(View.VISIBLE);
                    } else {
                        groupTransitionWh.setVisibility(GONE);
                    }
                    if (featuredQuoteWh != null && !featuredQuoteWh.trim().equals("") &&
                            featuredQuoteWh.length() != 0) {
                        rFeaturedQuoteWh.setText(fromHtml(featuredQuoteWh));
                        groupFeaturedQuoteWh.setVisibility(View.VISIBLE);
                    } else {
                        groupFeaturedQuoteWh.setVisibility(GONE);
                    }

                    if (imageLinkWh != null) {
                        try {
                            Glide.with(context).load(imageLinkWh)
                                    .fitCenter()
                                    .placeholder(R.drawable.placeholder)
                                    .into(whImage);
                            whImage.setVisibility(View.VISIBLE);
                        } catch (Exception e) {
                            // Pass, i.e. error loading image
                            whImage.setVisibility(View.GONE);
                        }
                    }

                    if (genericQuoteWh != null) {
                        rGenericQuoteWh.setText(fromHtml(genericQuoteWh));
                        groupGenericQuoteWh.setVisibility(View.VISIBLE);
                    }
                    if (referenceWh != null) {
                        rReferenceWh.setText(fromHtml(referenceWh));
                        groupReferenceWh.setVisibility(View.VISIBLE);
                    }

                    rContainerWh.setVisibility(View.VISIBLE);
//                    rShareButton.setVisibility(View.VISIBLE);
                    errorMessage.setVisibility(View.GONE);
                }


                // -----------------------  SECTION :: Philippine History --------------------------

                if (phData != null) {
                    String headingPhil = phData.getHeading();
                    String featuredVersePhil = phData.getFeaturedVerse();
                    String contentPhil = phData.getContent();
                    String prayerFocusPhil = phData.getPrayerFocus();
                    String featuredQuotePhil = phData.getFeaturedQuote();
                    String imageLinkPhil = phData.getImageLink();
                    String genericQuotePhil = phData.getGenericQuote();
                    String referencePhil = phData.getReference();
                    String transitionPh = phData.getTransition();

                    // Update dynamic content
                    rHeadingPhil.setText(fromHtml(headingPhil));
                    rFeaturedVersePhil.setText(fromHtml(featuredVersePhil));
                    rContentPhil.setText(fromHtml(contentPhil));
                    rPrayerFocusPhil.setText(fromHtml(prayerFocusPhil));
                    rTransitionPh.setText(fromHtml(transitionPh));


                    if (transitionPh != null && !transitionPh.trim().equals("") &&
                            transitionPh.length() != 0) {
                        rTransitionPh.setText(fromHtml(transitionPh));
                        groupTransitionPh.setVisibility(View.VISIBLE);
                    } else {
                        groupTransitionPh.setVisibility(GONE);
                    }

                    if (featuredQuotePhil != null && !featuredQuotePhil.trim().equals("")
                            && featuredQuotePhil.length() != 0) {
                        rFeaturedQuotePhil.setText(fromHtml(featuredQuotePhil));
                        groupFeaturedQuotePh.setVisibility(View.VISIBLE);
                    } else {
                        groupFeaturedQuotePh.setVisibility(GONE);
                    }
                    if (imageLinkPhil != null) {
                        try {
                            Glide.with(context).load(imageLinkPhil)
                                    .fitCenter()
                                    .placeholder(R.drawable.placeholder)
                                    .into(philImage);
                            philImage.setVisibility(View.VISIBLE);
                        } catch (Exception e) {
                            // Error loading image
                            philImage.setVisibility(View.GONE);
                        }
                    }

                    if (genericQuotePhil != null) {
                        rGenericQuotePhil.setText(fromHtml(genericQuotePhil));
                        groupGenericQuotePh.setVisibility(View.VISIBLE);
                    }
                    if (referencePhil != null) {
                        rReferencePhil.setText(fromHtml(referencePhil));
                        groupReferencePh.setVisibility(View.VISIBLE);
                    }

                    rContainerPh.setVisibility(View.VISIBLE);
//                    rShareButton.setVisibility(View.VISIBLE);
                    errorMessage.setVisibility(View.GONE);
                }

                // -----------------------  SECTION :: Error Message -------------------------------

                if (whData == null && phData == null) {
                    Log.e("Jhesed", "(common) whdata and phdata null!!!");
                    errorMessage.setVisibility(View.VISIBLE);
//                    rShareButton.setVisibility(GONE);
                }

                progressBar.setVisibility(GONE);
            }

            @Override
            public void onFailure(Call<GlimpseDetailsResource> call, Throwable t) {
                Log.e("Jhesed", "(common) failed!!!");

                errorMessage.setVisibility(View.VISIBLE);
                progressBar.setVisibility(GONE);
                call.cancel();
            }
        });
    }

    public static String getTodayAsString() {
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        return df.format(c);
    }

    public static String getMonthAsString() {
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c);
        formattedDate = formattedDate.substring(0, formattedDate.lastIndexOf("-"));

        return formattedDate;
    }

    public static String getDayAsString(String date) {
        return date.substring(date.lastIndexOf("-") + 1, date.length());
    }

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String source) {
        return Html.fromHtml(source);
    }

    public static String dateToHuman(String date) {

        String stringDate = "";
        try {
            // obtain date and time from initial string
            Date dateFormatted = new SimpleDateFormat("yyyy-MM-dd").parse(date);
            // set date string
            stringDate = new SimpleDateFormat("MMMM dd").format(dateFormatted);
            // set time string
        } catch (ParseException e) {
            // wrong input
        }
        return stringDate;

    }
}
