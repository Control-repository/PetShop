package com.example.petshop.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.petshop.R;
import com.example.petshop.models.Product;
import com.squareup.picasso.Picasso;

public class ProductInforDialog {
    private AlertDialog.Builder builder;
    private TextView tv_name,tv_category,tv_price,tv_quantity,tv_description;
    private ImageView image_product;

    public ProductInforDialog(Context context, Product product,NavController navController){
        View view = LayoutInflater.from(context).inflate(R.layout.product_information_dialog,null);
        builder = new AlertDialog.Builder(context);
        builder.setView(view);

        tv_name = view.findViewById(R.id.tv_product_name);
        tv_category = view.findViewById(R.id.tv_product_category);
        tv_price = view.findViewById(R.id.tv_product_price);
        tv_quantity = view.findViewById(R.id.tv_product_quantity);
        tv_description = view.findViewById(R.id.tv_product_description);
        image_product = view.findViewById(R.id.img_product);

        tv_name.setText(product.getName());
        tv_category.setText(product.getCategory());
        tv_price.setText(String.valueOf(product.getPrice()));
        tv_quantity.setText(String.valueOf(product.getQuantity()));
        tv_description.setText(product.getDescription());

        if(product.getImageURL()!=null){
            Picasso.get().load(product.getImageURL()).resize(200,250).into(image_product);
        }

        builder.setPositiveButton("CHANGE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("product",product);
                navController.navigate(R.id.action_to_itemProduct,bundle);
                dialog.dismiss();
            }
        })
        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

    }

    public void show(){
        builder.create().show();
    }
    public void dismiss(){
        builder.create().dismiss();
    }
}
