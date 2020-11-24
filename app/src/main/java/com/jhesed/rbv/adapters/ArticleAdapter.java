/*
 *   Copyright 2016 Marco Gomiero
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */

package com.jhesed.rbv.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jhesed.rbv.R;
import com.prof.rssparser.Article;
import com.squareup.picasso.Picasso;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {

    private static boolean isDailyBread = false;
    AlertDialog.Builder builder;
    AlertDialog progressDialog;
    private List<Article> articles;
    private Context mContext;
    private WebView articleView;

    public ArticleAdapter(List<Article> list, Context context, boolean rssIsDailyBread) {
        this.articles = list;
        this.mContext = context;
        isDailyBread = rssIsDailyBread;
    }

    public List<Article> getArticleList() {
        return articles;
    }

    @NonNull
    @Override
    public ArticleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.rss_feed, viewGroup, false);
        return new ViewHolder(v);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position) {

        Article currentArticle = articles.get(position);

        String pubDateString = currentArticle.getPubDate();

        try {
            String sourceDateString = currentArticle.getPubDate();

            if (sourceDateString != null) {
                SimpleDateFormat sourceSdf =
                        new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
                Date date = sourceSdf.parse(sourceDateString);
                if (date != null) {
                    SimpleDateFormat sdf =
                            new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
                    pubDateString = sdf.format(date);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        viewHolder.title.setText(currentArticle.getTitle());

        Picasso.get()
                .load(currentArticle.getImage())
                .placeholder(R.drawable.placeholder)
                .into(viewHolder.image);

        viewHolder.pubDate.setText(pubDateString);

        StringBuilder categories = new StringBuilder();
        for (int i = 0; i < currentArticle.getCategories().size(); i++) {
            if (i == currentArticle.getCategories().size() - 1) {
                categories.append(currentArticle.getCategories().get(i));
            } else {
                categories.append(currentArticle.getCategories().get(i)).append(", ");
            }
        }

        viewHolder.category.setText(categories.toString());

        viewHolder.itemView.setOnClickListener(view -> {

            String title = articles.get(viewHolder.getAdapterPosition()).getTitle();

            //show article content inside a dialog
            articleView = new WebView(mContext);

            articleView.getSettings().setLoadWithOverviewMode(true);
            articleView.getSettings().setJavaScriptEnabled(true);
            articleView.getSettings().setDomStorageEnabled(true);
            articleView.setHorizontalScrollBarEnabled(false);
            articleView.setWebViewClient(new WebViewClient());
            String content = articles.get(viewHolder.getAdapterPosition()).getContent();

            String link = articles.get(viewHolder.getAdapterPosition()).getLink();
            if (isDailyBread) {
                try {
                    URI uri = new URI(link);
                    link = "https://odb.org" + uri.getPath();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                articleView.loadUrl(link);
            } else if (content == null || content.isEmpty()) {
                articleView.loadUrl(link);
            } else {
                articleView.loadDataWithBaseURL(null,
                        "<style>img{display: inline; height: auto; max-width: 100%;} " +

                                "</style>\n" + "<style>iframe{ height: auto; width: auto;}" +
                                "</style>\n" + content, null, "utf-8", null);
            }
            androidx.appcompat.app.AlertDialog alertDialog =
                    new androidx.appcompat.app.AlertDialog.Builder(mContext).create();
            alertDialog.setView(articleView);
            alertDialog.setButton(androidx.appcompat.app.AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();

            ((TextView) Objects.requireNonNull(alertDialog.findViewById(android.R.id.message)))
                    .setMovementMethod(LinkMovementMethod.getInstance());
        });
    }

    public AlertDialog.Builder getDialogProgressBar() {

        if (builder == null) {
            builder = new AlertDialog.Builder(mContext);

            builder.setTitle("Loading...");

            final ProgressBar progressBar = new ProgressBar(mContext);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            progressBar.setLayoutParams(lp);
            builder.setView(progressBar);
        }
        return builder;
    }

    @Override
    public int getItemCount() {
        return articles == null ? 0 : articles.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView pubDate;
        ImageView image;
        TextView category;

        public ViewHolder(View itemView) {

            super(itemView);
            title = itemView.findViewById(R.id.title);
            pubDate = itemView.findViewById(R.id.pubDate);
            image = itemView.findViewById(R.id.image);
            category = itemView.findViewById(R.id.categories);
        }
    }
}