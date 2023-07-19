package com.example.petshop.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petshop.R;
import com.example.petshop.models.Product;
import com.example.petshop.utils.DecimalValue;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder>{
    private List<Product> productList;

    public ProductAdapter (List<Product> productList){
        this.productList = productList;
    }

    public void setProductList(List<Product> list){
        this.productList = list;
    }
    @NonNull
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_product,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ViewHolder holder, int position) {
        if(productList!=null){
            Product product = productList.get(position);

            holder.tv_name.setText(product.getName());
            holder.tv_price.setText("$ "+ DecimalValue.formatPrice(product.getPrice()));
            holder.tv_category.setText(product.getCategory());
            if(!product.getImageURL().isEmpty()){
                Picasso.get().load(product.getImageURL()).resize(200,200).into(holder.image_product);
            }
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image_product;
        TextView tv_name,tv_price,tv_category;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image_product = itemView.findViewById(R.id.image_product);
            tv_name = itemView.findViewById(R.id.tv_product_name);
            tv_price = itemView.findViewById(R.id.tv_product_price);
            tv_category = itemView.findViewById(R.id.tv_product_category);

        }
    }
}
