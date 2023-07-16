package com.example.instagramclone.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.instagramclone.R;
import com.example.instagramclone.adapter.PostAdapter;
import com.example.instagramclone.databinding.ActivityFeedBinding;
import com.example.instagramclone.model.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Map;

public class FeedActivity extends AppCompatActivity {
    private ActivityFeedBinding binding;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private ArrayList<Post> posts;
    private PostAdapter postAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFeedBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        posts = new ArrayList<>();

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        getData();

        postAdapter = new PostAdapter(this, posts);

        binding.recyclerView.setAdapter(postAdapter);

    }

    @SuppressLint("NotifyDataSetChanged")
    private void getData() {
        firestore.collection("Posts").orderBy("date", Query.Direction.DESCENDING)
                .addSnapshotListener((value, error) -> {

                    if (error != null && error.getCode() != FirebaseFirestoreException.Code.PERMISSION_DENIED) {
                        Toast.makeText(FeedActivity.this, error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        return;
                    }

                    if (value != null) {
                        posts.clear();

                        for (DocumentSnapshot snapshot : value.getDocuments()) {

                            String postId = snapshot.getId();
                            Map<String, Object> data = snapshot.getData();
                            String email = (String) data.get("useremail");
                            String comment = (String) data.get("comment");
                            String downloadUrl = (String) data.get("downloadurl");
                            String date = (String) data.get("localdate");

                            Post post = new Post(email, comment, downloadUrl, date);
                            post.setPostId(postId);

                            posts.add(post);
                        }

                        postAdapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.option_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.add_post) {
            Intent uploadIntent = new Intent(FeedActivity.this, UploadActivity.class);
            startActivity(uploadIntent);
        } else if (item.getItemId() == R.id.signout) {
            auth.signOut();

            Intent signoutIntent = new Intent(FeedActivity.this, MainActivity.class);
            startActivity(signoutIntent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}