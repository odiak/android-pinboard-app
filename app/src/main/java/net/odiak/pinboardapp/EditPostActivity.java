package net.odiak.pinboardapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

public class EditPostActivity extends AppCompatActivity {
    public static final String EXTRA_POST_DATA = "net.odiak.pinboardapp.POST_DATA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_post);

        setSupportActionBar(setupToolbar());

        Intent intent = getIntent();
        Post post;
        try {
            post = Post.fromJson(new JSONObject(intent.getStringExtra(EXTRA_POST_DATA)));
        } catch (JSONException e) {
            post = new Post();
        }

        EditText titleInput = (EditText) findViewById(R.id.titleInput);
        EditText urlInput = (EditText) findViewById(R.id.urlInput);
        EditText noteInput = (EditText) findViewById(R.id.noteInput);

        titleInput.setText(post.getTitle());
        urlInput.setText(post.getUrl());
        noteInput.setText(post.getDetail());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_post_menu, menu);
        return true;
    }

    private Toolbar setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_action_navigation_arrow_back);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditPostActivity.super.onBackPressed();
            }
        });

        return toolbar;
    }
}
