package com.jhesed.rbv.adapters;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.jhesed.rbv.R;
import com.jhesed.rbv.base_fragments.SubFragmentVideoFeedsContent;
import com.prof.youtubeparser.models.videos.Video;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {

    private final SubFragmentVideoFeedsContent mActivity;
    private List<Video> videos;

    public VideoAdapter(List<Video> list, SubFragmentVideoFeedsContent activity) {
        this.videos = list;
        this.mActivity = activity;
    }

    public static String unescapeXML(final String xml) {
        Pattern xmlEntityRegex = Pattern.compile("&(#?)([^;]+);");
        //Unfortunately, Matcher requires a StringBuffer instead of a StringBuilder
        StringBuffer unescapedOutput = new StringBuffer(xml.length());

        Matcher m = xmlEntityRegex.matcher(xml);
        Map<String, String> builtinEntities = null;
        String entity;
        String hashmark;
        String ent;
        int code;
        while (m.find()) {
            ent = m.group(2);
            hashmark = m.group(1);
            if ((hashmark != null) && (hashmark.length() > 0)) {
                code = Integer.parseInt(ent);
                entity = Character.toString((char) code);
            } else {
                //must be a non-numerical entity
                if (builtinEntities == null) {
                    builtinEntities = buildBuiltinXMLEntityMap();
                }
                entity = builtinEntities.get(ent);
                if (entity == null) {
                    //not a known entity - ignore it
                    entity = "&" + ent + ';';
                }
            }
            m.appendReplacement(unescapedOutput, entity);
        }
        m.appendTail(unescapedOutput);

        return unescapedOutput.toString();
    }

    private static Map<String, String> buildBuiltinXMLEntityMap() {
        Map<String, String> entities = new HashMap<String, String>(10);
        entities.put("lt", "<");
        entities.put("gt", ">");
        entities.put("amp", "&");
        entities.put("apos", "'");
        entities.put("quot", "\"");
        return entities;
    }

    public void clearData() {
        if (videos != null)
            videos.clear();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.rss_item_video, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        final Video currentVideo = videos.get(position);

        String pubDateString = currentVideo.getDate();
        final String videoTitle = currentVideo.getTitle();

        //retrieve video link
        final String videoId = currentVideo.getVideoId();
        final String link = "https://www.youtube.com/watch?v=" + videoId;

        viewHolder.title.setText(unescapeXML(currentVideo.getTitle()));
        viewHolder.pubDate.setText(pubDateString);

        Picasso.get()
                .load(currentVideo.getCoverLink())
                .placeholder(R.drawable.placeholder)
                .fit()
                .centerCrop()
                .into(viewHolder.image);

        //open the video on Youtube
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                mActivity.startActivity(intent1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return videos == null ? 0 : videos.size();
    }

    public List<Video> getList() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView pubDate;
        ImageView image;

        ViewHolder(View itemView) {

            super(itemView);
            title = itemView.findViewById(R.id.title);
            pubDate = itemView.findViewById(R.id.pubDate);
            image = itemView.findViewById(R.id.image);
        }
    }
}