package com.htcursos.cameraapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.camera.CropImageIntentBuilder;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Virmerson on 07/08/15.
 */
public class RegisterActivity  extends AppCompatActivity{


    private final int CAMERA_REQUEST =123;
    private static final int CROP_REQUEST = 456 ;

    @Bind(R.id.user_image)
    ImageView userImage;

    @Bind(R.id.name)
    EditText userName;

    @Bind(R.id.email)
    EditText userEmail;

    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.user_image)
    public void changeImage(){
        //Arquivo da Imagem temporaria original
        File image =  new File(getExternalCacheDir(), "temp");
        imageUri = Uri.fromFile(image);

        //Carregando a camera
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //Pedindo para a camera gravar no arquivo
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        //Retorno da Camera
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Aquivo depois do crop
        File croppedImage = new File(getFilesDir(), "imgCropped.jpg");
        if(requestCode==CAMERA_REQUEST && resultCode==RESULT_OK){

            Uri croppedImageUri = Uri.fromFile(croppedImage);
            //Configurando Crop
            CropImageIntentBuilder crop = new CropImageIntentBuilder(200,200, croppedImageUri);
            crop.setOutlineCircleColor(0XFF03A9F4);
            crop.setSourceImage(imageUri); //imagem original
            //Executando a intent do Crop
            startActivityForResult(crop.getIntent(RegisterActivity.this), CROP_REQUEST);

        }else if (requestCode==CROP_REQUEST && resultCode== RESULT_OK) {
                //Retorno do CROP
            //Convertendo o arquivo parq Bitmap e carregando no layout
            userImage.setImageBitmap(BitmapFactory.decodeFile(croppedImage.getAbsolutePath()));
        }
    }
}
