package net.odiak.pinboardapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class PostsAdapter extends ArrayAdapter<Post> {
    LayoutInflater mLayoutInflater;

    public PostsAdapter (Context context) {
        super(context, 0);
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView (int pos, View convertView, ViewGroup parent) {
        PostViewHolder holder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.postrow, parent, false);
            holder = new PostViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.url = (TextView) convertView.findViewById(R.id.url);
            convertView.setTag(holder);
        } else {
            holder = (PostViewHolder) convertView.getTag();
        }

        Post post = getItem(pos);
        holder.title.setText(post.getTitle());
        holder.url.setText(post.getUrl());

        return convertView;
    }

    private static class PostViewHolder {
        TextView title;
        TextView url;
    }
}
