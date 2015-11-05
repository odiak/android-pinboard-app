package net.odiak.pinboardapp;


import org.json.JSONException;
import org.json.JSONObject;

public class Post {
    private String mTitle;
    private String mDetail;
    private String mUrl;

    public String getTitle() { return mTitle; }
    public String getDetail() { return mDetail; }
    public String getUrl() { return mUrl; }

    public void setTitle(String title) { mTitle = title; }
    public void setDetail(String detail) { mDetail = detail; }
    public void setUrl(String url) { mUrl = url; }

    public JSONObject toJson() throws JSONException {
        JSONObject obj = new JSONObject();
        obj.putOpt("description", mTitle);
        obj.putOpt("extended", mDetail);
        obj.putOpt("href", mUrl);
        return obj;
    }

    public static Post fromJson(JSONObject jsonObj) {
        Post post = new Post();
        post.setTitle(jsonObj.optString("description"));
        post.setDetail(jsonObj.optString("extended"));
        post.setUrl(jsonObj.optString("href"));
        return post;
    }
}
