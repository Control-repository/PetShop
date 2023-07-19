package com.example.petshop.ui.item_product;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.petshop.R;
import com.example.petshop.viewmodel.UserViewModel;
import com.google.android.material.textfield.TextInputLayout;

public class ItemProductFragment extends Fragment {
    private int GALLERY_PERMISSION_CODE = 1001;
    private ItemProductViewModel mViewModel;
    private UserViewModel userViewModel;
    public static ItemProductFragment newInstance() {
        return new ItemProductFragment();
    }

    private TextInputLayout ip_name,ip_category,ip_price,ip_quantity;
    private EditText ip_description;
    private Button btn_create,btn_cancel;
    private ImageView image_product;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_product, container, false);
        ip_name = view.findViewById(R.id.ip_name_product);
        ip_category= view.findViewById(R.id.ip_category_product);
        ip_price= view.findViewById(R.id.ip_price_product);
        ip_quantity= view.findViewById(R.id.ip_quantity_product);
        ip_description= view.findViewById(R.id.ip_description_product);
        image_product= view.findViewById(R.id.image_product);
        btn_create = view.findViewById(R.id.btn_create);
        btn_cancel = view.findViewById(R.id.btn_cancel);

        mViewModel = new ViewModelProvider(this).get(ItemProductViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        image_product.setOnClickListener(v->{
            openGallery();
        });

        btn_cancel.setOnClickListener(v->{
            resetInput();
        });

        btn_create.setOnClickListener(v->{

        });
    }

    private void resetInput(){
        ip_category.getEditText().setText("");
        ip_name.getEditText().setText("");
        ip_price.getEditText().setText("");
        ip_quantity.getEditText().setText("");
        ip_description.setText("");
        image_product.setImageURI(null);
    }
    private void createProduct(){
        String name = ip_name.getEditText().getText().toString().trim();
        double price = Double.parseDouble(ip_price.getEditText().getText().toString().trim());
        int quantity = Integer.parseInt(ip_quantity.getEditText().getText().toString().trim());
        String category = ip_category.getEditText().getText().toString().trim();
        String description = ip_description.getText().toString().trim();

        if(validateFields()){

        }
    }

        private boolean validateFields() {
            String name = ip_name.getEditText().getText().toString().trim();
            String priceStr = ip_price.getEditText().getText().toString().trim();
            String quantityStr = ip_quantity.getEditText().getText().toString().trim();
            String category = ip_category.getEditText().getText().toString().trim();

            // Kiểm tra các điều kiện
            if (name.isEmpty()) {
                ip_name.setError("Please enter the product name.");
                return false;
            }else{
                ip_name.setError(null);
            }

            if (priceStr.isEmpty()) {
                ip_price.setError("Please enter the product price.");
                return false;
            }else{
                ip_price.setError(null);
            }

            if (quantityStr.isEmpty()) {
                ip_quantity.setError("Please enter the product quantity.");
                return false;
            }else{
                ip_quantity.setError(null);
            }

            try {
                double price = Double.parseDouble(priceStr);
                int quantity = Integer.parseInt(quantityStr);

                // Kiểm tra giá trị hợp lệ của số lượng và giá
                if (price <= 0) {
                    ip_price.setError("Please enter a valid product price.");
                    return false;
                }else{
                    ip_price.setError(null);
                }

                if (quantity <= 0) {
                    ip_quantity.setError("Please enter a valid product quantity.");
                    return false;
                }else{
                    ip_quantity.setError(null);
                }
            } catch (NumberFormatException e) {
                // Xử lý nếu giá hoặc số lượng không phải là số
                ip_price.setError("Please enter a valid product price.");
                ip_quantity.setError("Please enter a valid product quantity.");
                return false;
            }

            if (category.isEmpty()) {
                ip_category.setError("Please enter the product category.");
                return false;
            }else{
                ip_category.setError(null);
            }

            // Hợp lệ
            return true;
        }

    private void openGallery() {
            // Quyền đã được cấp, mở Gallery
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            openGallery.launch(intent);

    }


    ActivityResultLauncher<Intent> openGallery = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Uri uriImage = result.getData().getData();
                    Log.i("CHECK URI",uriImage.toString());
                    image_product.setImageURI(uriImage);
                }
            });
}