package com.example.petshop.ui.item_product;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.petshop.R;
import com.example.petshop.models.AppMessage;
import com.example.petshop.models.Product;
import com.example.petshop.utils.ApiService;
import com.example.petshop.utils.DecimalValue;
import com.example.petshop.utils.RetroClient;
import com.example.petshop.utils.UtilClass;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemProductFragment extends Fragment {
    int SELECT_PICTURE = 200;
    private Product product;
    private TextInputLayout ip_name,ip_category,ip_price,ip_quantity;
    private EditText ip_description;
    private Button btn_create,btn_cancel,btn_update;
    private ImageView image_product;
    private  ProgressDialog dialog;
    private Uri imageURL = null;
    ApiService apiService;
    private static Gson gson = new Gson();
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
        btn_update = view.findViewById(R.id.btn_update);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RetroClient.setContext(requireContext());
        apiService = RetroClient.getApiService();
        dialog = new ProgressDialog(requireContext());
        dialog.setMessage("Sending...");

        Bundle bundle = getArguments();
        if(bundle!=null && bundle.containsKey("product")){
             product =(Product) bundle.getSerializable("product");
        }

        if(product!=null){
            ip_name.getEditText().setText(product.getName());
            ip_category.getEditText().setText(product.getCategory());
            ip_price.getEditText().setText(String.valueOf(product.getPrice()));
            ip_quantity.getEditText().setText(String.valueOf(product.getQuantity()));
            ip_description.setText(product.getDescription());
            Picasso.get().load(product.getImageURL()).resize(500,500).into(image_product);
            btn_create.setVisibility(View.GONE);
            btn_update.setVisibility(View.VISIBLE);
        }else{
            btn_create.setVisibility(View.VISIBLE);
            btn_update.setVisibility(View.GONE);
        }

        image_product.setOnClickListener(v->{
            imageChooser();
        });

        btn_cancel.setOnClickListener(v->{
            resetInput();
        });

        btn_create.setOnClickListener(v->{
                createProduct();
            UtilClass.hideKeyboard(requireActivity());
        });
        btn_update.setOnClickListener(v->{
            updateProduct(product);
            UtilClass.hideKeyboard(requireActivity());
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
    //Create product
    private void createProduct(){
        if(validateFields()){
            dialog.show();
            String name = ip_name.getEditText().getText().toString().trim();
            String price =ip_price.getEditText().getText().toString().trim();
            String quantity =ip_quantity.getEditText().getText().toString().trim();
            String category = ip_category.getEditText().getText().toString().trim();
            String description = ip_description.getText().toString().trim();

            sendRequestToCreateProduct(name,price,quantity,category,description);
        }
    }

    //Update product
    private void updateProduct(Product product){
        if(validateFields()){
            dialog.show();

            String name = ip_name.getEditText().getText().toString().trim();
            String price =ip_price.getEditText().getText().toString().trim();
            String quantity =ip_quantity.getEditText().getText().toString().trim();
            String category = ip_category.getEditText().getText().toString().trim();
            String description = ip_description.getText().toString().trim();

            product.setName(name);
            product.setPrice(Double.parseDouble(price));
            product.setQuantity(Integer.parseInt(quantity));
            product.setCategory(category);
            product.setDescription(description);

            sendRequestToUpdateProduct(product);
        }
    }
    private void sendRequestToCreateProduct(String name,String price,String quantity,String category,String description){

        // Chuyển Uri thành File
        if(imageURL!=null){
            // Trường hợp lựa chọn ảnh
            sendCreateWithImage(imageURL, name, price, quantity, category, description);
        }
        else{
            //lấy thông tin Product
            Product product = new Product();
            product.setName(name);
            product.setPrice(Double.parseDouble(price));
            product.setQuantity(Integer.parseInt(quantity));
            product.setCategory(category);
            product.setDescription(description);
            sendCreateWithoutImage(product);
        }

    }

    private void sendRequestToUpdateProduct(Product product){
            // Chuyển Uri thành File
        if(imageURL!=null){
            // Trường hợp lựa chọn ảnh
            sendUpdateWithImage(imageURL,product);
        }
        else{


            sendUpdateWithoutImage(product);
        }
    }
    //request create with image
    private void sendCreateWithImage(Uri imageUri,String name,String price,String quantity, String category,String description){
        try {
            // Truy xuất dữ liệu từ URI
            InputStream inputStream = requireActivity().getContentResolver().openInputStream(imageUri);

            // Lưu dữ liệu vào một tệp mới trong thư mục tạm thời của ứng dụng
            File tempFile = createTempFile();
            FileOutputStream outputStream = new FileOutputStream(tempFile);
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.close();
            inputStream.close();

            // Tiếp tục xử lý tệp mới ở đây, tải lên lên server
            Log.i("CHECK IMAGE PIC", tempFile.getName());
            // Tạo một MultipartBody.Part từ File để gửi lên server
            RequestBody requestFile = RequestBody.create(MediaType.parse(requireContext().getContentResolver().getType(imageUri)), tempFile);
            MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", tempFile.getName(), requestFile);
            // Chuyển các thông tin sản phẩm thành RequestBody
            RequestBody nameBody = RequestBody.create(MediaType.parse("text/plain"), name);
            RequestBody categoryBody = RequestBody.create(MediaType.parse("text/plain"), category);
            RequestBody priceBody = RequestBody.create(MediaType.parse("text/plain"), price);
            RequestBody quantityBody = RequestBody.create(MediaType.parse("text/plain"), quantity);
            RequestBody descriptionBody = RequestBody.create(MediaType.parse("text/plain"), description);
            //Gửi yêu cầu lên phía server
            Call<AppMessage> call = apiService.insertProductWithImage(nameBody,categoryBody,priceBody,quantityBody,descriptionBody,imagePart);
            call.enqueue(new Callback<AppMessage>() {
                @Override
                public void onResponse(Call<AppMessage> call, Response<AppMessage> response) {
                    dialog.dismiss();
                    if (response.isSuccessful() && response.body() != null) {
                        responseSuccessfully();
                    } else {
                        ResponseBody error = response.errorBody();
                        try {
                            AppMessage appMessage = gson.fromJson(error.string(),AppMessage.class);
                            Toast.makeText(requireContext(), appMessage.getMessage(), Toast.LENGTH_SHORT).show();
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                }
                @Override
                public void onFailure(Call<AppMessage> call, Throwable t) {
                    dialog.dismiss();
                    Log.i("ERROR INSERT PRODUCT",t.getMessage());
                    t.printStackTrace();
                    Toast.makeText(requireContext(), "Lỗi khi thêm sản phẩm: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //request create without image
    private void sendCreateWithoutImage(Product productCreate){
        //Trường hợp không sử dụng ảnh
        Call<AppMessage> call = apiService.insertProductWithoutImage(productCreate);
        call.enqueue(new Callback<AppMessage>() {
            @Override
            public void onResponse(Call<AppMessage> call, Response<AppMessage> response) {
                dialog.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    responseSuccessfully();
                } else {
                    ResponseBody error = response.errorBody();
                    try {
                        AppMessage appMessage = gson.fromJson(error.string(),AppMessage.class);
                        Toast.makeText(requireContext(), appMessage.getMessage(), Toast.LENGTH_SHORT).show();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<AppMessage> call, Throwable t) {
                dialog.dismiss();
                Log.i("ERROR INSERT PRODUCT",t.getMessage());
                t.printStackTrace();
                Toast.makeText(requireContext(), "Lỗi khi thêm sản phẩm: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    //request update with image
    private void sendUpdateWithImage(Uri imageUri,Product product){
        try {
            // Truy xuất dữ liệu từ URI
            InputStream inputStream = requireActivity().getContentResolver().openInputStream(imageUri);

            // Lưu dữ liệu vào một tệp mới trong thư mục tạm thời của ứng dụng
            File tempFile = createTempFile();
            FileOutputStream outputStream = new FileOutputStream(tempFile);
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.close();
            inputStream.close();

            // Tiếp tục xử lý tệp mới ở đây, tải lên lên server
            Log.i("CHECK IMAGE PIC", tempFile.getName());
            // Tạo một MultipartBody.Part từ File để gửi lên server
            RequestBody requestFile = RequestBody.create(MediaType.parse(requireContext().getContentResolver().getType(imageUri)), tempFile);
            MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", tempFile.getName(), requestFile);
            // Chuyển các thông tin sản phẩm thành RequestBody
            RequestBody nameBody = RequestBody.create(MediaType.parse("text/plain"), product.getName());
            RequestBody categoryBody = RequestBody.create(MediaType.parse("text/plain"), product.getCategory());
            RequestBody priceBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(product.getPrice()));
            RequestBody quantityBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(product.getQuantity()));
            RequestBody descriptionBody = RequestBody.create(MediaType.parse("text/plain"), product.getDescription());
            //Gửi yêu cầu lên phía server
            Call<AppMessage> call = apiService.updateProductWithImage(product.getId(),nameBody,categoryBody,priceBody,quantityBody,descriptionBody,imagePart);
            call.enqueue(new Callback<AppMessage>() {
                @Override
                public void onResponse(Call<AppMessage> call, Response<AppMessage> response) {
                    dialog.dismiss();
                    if (response.isSuccessful() && response.body() != null) {
                        responseSuccessfully();
                    } else {
                        ResponseBody error = response.errorBody();
                        try {
                            AppMessage appMessage = gson.fromJson(error.string(),AppMessage.class);
                            Toast.makeText(requireContext(), appMessage.getMessage(), Toast.LENGTH_SHORT).show();
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                }
                @Override
                public void onFailure(Call<AppMessage> call, Throwable t) {
                    dialog.dismiss();
                    Log.i("ERROR INSERT PRODUCT",t.getMessage());
                    t.printStackTrace();
                    Toast.makeText(requireContext(), "Lỗi khi thêm sản phẩm: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void sendUpdateWithoutImage(Product productUpdate){
        //Trường hợp không sử dụng ảnh
        Call<AppMessage> call = apiService.updateProductWithoutImage(productUpdate.getId(),productUpdate);
        call.enqueue(new Callback<AppMessage>() {
            @Override
            public void onResponse(Call<AppMessage> call, Response<AppMessage> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    responseSuccessfully();
                } else {
                    try {
                        ResponseBody error = response.errorBody();
                        AppMessage appMessage = gson.fromJson(error.string(),AppMessage.class);
                        Toast.makeText(requireContext(), appMessage.getMessage(), Toast.LENGTH_SHORT).show();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<AppMessage> call, Throwable t) {
                dialog.dismiss();
                Log.i("ERROR INSERT PRODUCT",t.getMessage());
                t.printStackTrace();
                Toast.makeText(requireContext(), "Lỗi khi thêm sản phẩm: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    //call when isSuccessfully
    private void responseSuccessfully(){
        resetInput();
        // Gửi kết quả về cho ProductFragment thông qua Fragment Result
        Bundle result = new Bundle();
        result.putString("message", "successfully!");
        getParentFragmentManager().setFragmentResult("REQUEST_ADD_PRODUCT", result);

        NavController navController = NavHostFragment.findNavController(ItemProductFragment.this);
        navController.popBackStack(R.id.nav_product,false);
    }
    //change file image
    private File createTempFile() throws IOException {
        // Tạo tên tệp mới dựa trên thời gian hiện tại
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + ".jpg";

        // Lấy đường dẫn tới thư mục tạm thời của ứng dụng
        File storageDir = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        // Tạo tệp mới
        return File.createTempFile(imageFileName, ".jpg", storageDir);
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
    // this function is triggered when
    // the Select Image Button is clicked
    void imageChooser() {

        // create an instance of the
        // intent of the type image
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    // this function is triggered when user
    // selects the image from the imageChooser
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == SELECT_PICTURE) {
                // Get the url of the image from data
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    // update the preview image in the layout
                    image_product.setImageURI(selectedImageUri);
                    //
                    imageURL = selectedImageUri;
                }
            }
        }
    }


}