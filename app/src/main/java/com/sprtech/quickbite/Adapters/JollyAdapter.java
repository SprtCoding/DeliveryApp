package com.sprtech.quickbite.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sprtech.quickbite.Models.FoodModel;
import com.sprtech.quickbite.R;
import com.sprtech.quickbite.food_add_to_cart;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class JollyAdapter extends RecyclerView.Adapter<JollyAdapter.ImageViewHolder>{
    private Context mContext;
    private List<FoodModel> mFoods;

    public JollyAdapter(Context mContext, List<FoodModel> mFoods) {
        this.mContext = mContext;
        this.mFoods = mFoods;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.branch_food_list,parent,false);
        return new JollyAdapter.ImageViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return mFoods.size();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        FoodModel food = mFoods.get(position);
            holder.food_name.setText(food.getfName());
            holder.food_price.setText("₱"+food.getFprice());
            holder.food_aname.setText(food.getFacroname());
            Picasso.get().load(food.getFpic()).fit().centerInside().placeholder(R.drawable.userman).into(holder.food_image);

            holder.itemView.setOnClickListener(view -> {
                String fName = food.getfName();
                String fBranch = food.getBranch();
                String fDes = food.getFdescription();
                String fCat = food.getFcategory();
                String fAcro = food.getFacroname();
                String fPrice = food.getFprice();
                String fPic = food.getFpic();

                Intent intent = new Intent(mContext, food_add_to_cart.class);
                intent.putExtra("food_name", fName);
                intent.putExtra("food_branch", fBranch);
                intent.putExtra("food_description", fDes);
                intent.putExtra("food_category", fCat);
                intent.putExtra("food_acro_name", fAcro);
                intent.putExtra("food_price", fPrice);
                intent.putExtra("food_picture", fPic);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            });
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView food_image;
        TextView food_name, food_price, food_aname;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            food_image = itemView.findViewById(R.id.food_image);
            food_name = itemView.findViewById(R.id.food_name);
            food_price = itemView.findViewById(R.id.food_price);
            food_aname = itemView.findViewById(R.id.food_aname);
        }
    }
}
