package com.example.instagramclone.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagramclone.databinding.RecyclerRowBinding;
import com.example.instagramclone.model.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostHolder> {
    private final ArrayList<Post> posts;
    private final Context context;

    public PostAdapter(Context context, ArrayList<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerRowBinding recyclerRowBinding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new PostHolder(recyclerRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {
        holder.recyclerRowBinding.recyclerViewUserEmailText.setText(posts.get(position).email);
        holder.recyclerRowBinding.recyclerViewUserCommentText.setText(posts.get(position).comment);
        holder.recyclerRowBinding.recyclerViewDateText.setText(posts.get(position).date);

        Picasso.get().load(posts.get(position).downloadUrl).into(holder.recyclerRowBinding.recyclerViewImageView);

        holder.itemView.setOnLongClickListener(v -> {
            showDeleteConfirmationDialog(position);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    static class PostHolder extends RecyclerView.ViewHolder {
        RecyclerRowBinding recyclerRowBinding;

        public PostHolder(RecyclerRowBinding recyclerRowBinding) {
            super(recyclerRowBinding.getRoot());
            this.recyclerRowBinding = recyclerRowBinding;
        }
    }

    private void showDeleteConfirmationDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete Confirmation");
        builder.setMessage("Are you sure you want to delete this post?");
        builder.setPositiveButton("Delete", (dialog, which) -> {

            if (isCurrentUserPostOwner(position)) {
                deletePost(position);
            } else {
                Toast.makeText(context, "You can only delete your own posts!", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private boolean isCurrentUserPostOwner(int position) {
        String currentUserMail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        Post post = posts.get(position);
        return currentUserMail.equals(post.getEmail());
    }

    private void deletePost(int position) {
        Post post = posts.get(position);
        String postId = post.getPostId();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Posts").document(postId)
                .delete()
                .addOnSuccessListener(aVoid -> Toast.makeText(context, "Post deleted", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(context, "Failed to delete post", Toast.LENGTH_SHORT).show());
    }
}