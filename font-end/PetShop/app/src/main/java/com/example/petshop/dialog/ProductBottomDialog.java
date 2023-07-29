package com.example.petshop.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;

import com.example.petshop.R;
import com.example.petshop.models.Product;
import com.example.petshop.models.User;
import com.google.android.material.bottomsheet.BottomSheetDialog;


public class ProductBottomDialog extends BottomSheetDialog {
    private OnDeleteClickListener onDeleteClickListener;
    private OnEditClickListener onEditClickListener;
    private OnShowClickListener onShowClickListener;
    private Product product;
    private User user;

    public ProductBottomDialog(@NonNull Context context,User user,Product product) {
        super(context);
        this.product = product;
        this.user = user;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottom_sheet_layout);
        // Set window animations for the dialog
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            params.windowAnimations = R.style.DialogAnimation;
            window.setAttributes(params);
        }
// Get references to your views inside the custom layout
        LinearLayout layout_show = findViewById(R.id.layout_show);
        LinearLayout layout_edit = findViewById(R.id.layout_edit);
        LinearLayout layout_delete = findViewById(R.id.layout_delete);

        // Hide edit and delete options if the user has role 1
        if (user.getRole() == 1) {
            layout_edit.setVisibility(View.GONE);
            layout_delete.setVisibility(View.GONE);
        } else {
            layout_edit.setVisibility(View.VISIBLE);
            layout_delete.setVisibility(View.VISIBLE);

            // Set click listener for delete option
            layout_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onDeleteClickListener != null) {
                        onDeleteClickListener.OnItemClick(product);
                        dismiss();
                    }
                }
            });
            layout_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onEditClickListener !=null){
                        onEditClickListener.OnItemClick(product);
                        dismiss();
                    }
                }
            });
            layout_show.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onShowClickListener !=null){
                        onShowClickListener.OnItemClick(product);
                        dismiss();
                    }
                }
            });
        }
    }

    // Set the onDeleteClickListener to handle delete option click
    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.onDeleteClickListener = listener;
    }
    public interface OnDeleteClickListener{
        void OnItemClick(Product product);
    }
    public void setOnEditClickListener(OnEditClickListener listener) {
        this.onEditClickListener = listener;
    }
    public interface OnEditClickListener{
        void OnItemClick(Product product);
    }

    public interface OnShowClickListener{
        void OnItemClick(Product product);
    }
    public void setOnShowClickListener(OnShowClickListener listener) {
        this.onShowClickListener = listener;
    }

}
