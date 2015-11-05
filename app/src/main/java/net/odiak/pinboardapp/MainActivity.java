package net.odiak.pinboardapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    PostsAdapter mAdapter;
    ListView mListView;

    private final OkHttpClient client = new OkHttpClient();

    private String mAuthToken = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mListView = (ListView) findViewById(R.id.postsList);

        mAdapter = new PostsAdapter(this);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onListItemClick(position);
            }
        });
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return onListItemLongClick(position);
            }
        });

        if ((mAuthToken = AuthService.getAuthToken(this)) != null) {
            loadPosts();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void onListItemClick(int position) {
        Post post = mAdapter.getItem(position);
        Intent intent;
        if (AppConstants.OPEN_URL_ON_INTERNAL_BROWSER) {
            intent = new Intent(this, BrowseActivity.class);
            intent.putExtra(BrowseActivity.EXTRA_URL, post.getUrl());
        } else {
            Uri uri = Uri.parse(post.getUrl());
            intent = new Intent(Intent.ACTION_VIEW, uri);
        }
        startActivity(intent);
    }

    private boolean onListItemLongClick(final int position) {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Menu")
                .setItems(new String[]{"Edit", "Delete", "Share"},
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        editPost(position);
                                        break;
                                    case 1:
                                        deletePost(position);
                                        break;
                                    case 2:
                                        sharePost(position);
                                        break;
                                }
                            }
                        })
                .show();

        return true;
    }

    private void loadPosts() {
        new PostsLoader().execute(new PostsLoaderParams());
    }

    private void editPost(int position) {
        Post post = mAdapter.getItem(position);
        Intent intent = new Intent(this, EditPostActivity.class);
        JSONObject json;
        try {
            json = post.toJson();
        } catch (JSONException e) {
            return;
        }
        intent.putExtra(EditPostActivity.EXTRA_POST_DATA, json.toString());
        startActivity(intent);
    }

    private void deletePost(int position) {
        Post post = mAdapter.getItem(position);
        mAdapter.remove(post);
        Toast.makeText(getApplicationContext(), "Deleted post (fake)", Toast.LENGTH_SHORT)
                .show();
    }

    private void sharePost(int position) {
        Post post = mAdapter.getItem(position);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, post.getTitle());
        intent.putExtra(Intent.EXTRA_TEXT, post.getTitle() + " " + post.getUrl());
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                new AlertDialog.Builder(this)
                        .setTitle("Log out")
                        .setMessage("Are you sure to log out?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                logout();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
                break;

            default:
                return false;
        }
        return true;
    }

    private void logout() {
        AuthService.saveAuthToken(this, "");
        startActivity(new Intent(this, LoginActivity.class));
    }

    private static class PostsLoaderParams {
    }

    private class PostsLoader extends AsyncTask<PostsLoaderParams, Integer, ArrayList<Post>> {
        protected ArrayList<Post> doInBackground(PostsLoaderParams... params) {
            ArrayList<Post> posts = new ArrayList<>();

            try {
                Request request = new Request.Builder()
                        .url(String.format(
                                "%s/posts/all?format=json&results=20&start=%d&auth_token=%s",
                                AppConstants.API_BASE_URL,
                                mAdapter.getCount(),
                                mAuthToken))
                        .build();
                Response response = client.newCall(request).execute();

                String body = response.body().string();
                JSONArray jsonArray = new JSONArray(body);

                for (int i = 0, len = jsonArray.length(); i < len; i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    posts.add(Post.fromJson(obj));
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return posts;
        }

        protected void onPostExecute(ArrayList<Post> posts) {
            mAdapter.addAll(posts);
        }
    }
}
