package com.sprtech.quickbite.Adapters;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sprtech.quickbite.Models.FoodModel;
import com.sprtech.quickbite.R;
import com.sprtech.quickbite.edit_food_activity;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ImageViewHolder> {
    private Context mContext;
    private List<FoodModel> mFoods;

    public FoodAdapter(Context mContext, List<FoodModel> mFoods) {
        this.mContext = mContext;
        this.mFoods = mFoods;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_foods_list,parent,false);
        return new ImageViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        FoodModel food = mFoods.get(position);
        holder.food_name.setText(food.getfName());
        holder.food_price.setText("₱"+food.getFprice());
        holder.food_aname.setText(food.getFacroname());
        Picasso.get().load(food.getFpic()).fit().centerInside().placeholder(R.drawable.userman).into(holder.food_image);

        FirebaseDatabase mDb = FirebaseDatabase.getInstance();
        DatabaseReference foodRef = mDb.getReference("Foods");

        holder.editBtn.setOnClickListener(view -> {
            String foodName = food.getfName();
            String foodBranch = food.getBranch();
            String foodAName = food.getFacroname();
            String foodDescription = food.getFdescription();
            String foodCategory = food.getFcategory();
            String foodPrice = food.getFprice();
            String foodPic = food.getFpic();
            String foodID = food.getFoodUID();

            Intent i = new Intent(mContext, edit_food_activity.class);
            i.putExtra("foodName", foodName);
            i.putExtra("foodBranch", foodBranch);
            i.putExtra("foodAName", foodAName);
            i.putExtra("foodDescription", foodDescription);
            i.putExtra("foodCategory", foodCategory);
            i.putExtra("foodPrice", foodPrice);
            i.putExtra("foodPic", foodPic);
            i.putExtra("foodID", foodID);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(i);
        });

        ProgressDialog loadingBar;
        loadingBar = new ProgressDialog(mContext);
        loadingBar.setTitle("Deleting Foods!");
        loadingBar.setMessage("Please wait...");
        loadingBar.setCanceledOnTouchOutside(true);

        holder.deleteBtn.setOnClickListener(view -> {
            loadingBar.show();
            String foodID = food.getFoodUID();
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("Delete")
                    .setMessage("Are you sure you want to delete " + food.getfName() + "?")
                    .setCancelable(true)
                    .setPositiveButton("Yes", (dialogInterface, i) -> {
                        android.os.Handler handler = new Handler();
                        handler.postDelayed(() -> {
                            foodRef.child(foodID).removeValue().addOnCompleteListener(task -> {
                                Toast.makeText(mContext, food.getfName() + " Deleted successfully!", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            });
                        }, 3000);
                    })
                    .setNegativeButton("No", (dialogInterface, i) -> {
                        dialogInterface.cancel();
                    })
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return mFoods.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView food_image;
        TextView food_name, food_price, food_aname;
        MaterialButton editBtn, deleteBtn;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            food_image = itemView.findViewById(R.id.food_image);
            food_name = itemView.findViewById(R.id.food_name);
            food_price = itemView.findViewById(R.id.food_price);
            food_aname = itemView.findViewById(R.id.food_aname);
            editBtn = itemView.findViewById(R.id.editBtn);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
        }
    }
}
