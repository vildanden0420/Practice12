package com.example.vil.myapplication;

import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    MyCanvas canvas;
    CheckBox chStamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission();

        canvas = (MyCanvas)findViewById(R.id.canvas);
        chStamp = (CheckBox)findViewById(R.id.chStamp);
        //canvas.checked = true;

        chStamp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    canvas.checked = false;
                }else{
                    canvas.checked = true;
                }

            }
        });

    }

    public void onClick(View v){
        if(v.getId() == R.id.eraser){
            canvas.erase();
        }else if(v.getId() == R.id.save){
            canvas.save(getExternalPath());
        }else if(v.getId() == R.id.open){
            canvas.open(getExternalPath());
        }else if(v.getId() == R.id.rotate){
            chStamp.setChecked(true);
            canvas.rotate();
        }else if(v.getId() == R.id.move){
            chStamp.setChecked(true);
            canvas.move();
        }else if(v.getId() == R.id.scale){
            chStamp.setChecked(true);
            canvas.scale();
        }else if(v.getId() == R.id.skew){
            chStamp.setChecked(true);
            canvas.skew();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0,1,0,"Blurring").setCheckable(true);
        menu.add(0,2,0, "Coloring").setCheckable(true);
        menu.add(0,3,0, "Pen Width Big").setCheckable(true);
        menu.add(0,4,0, "Pen Color RED");
        menu.add(0,5,0, "Pen Color BLUE");

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == 1){
            if (item.isChecked()){
                item.setChecked(false);
            }else{
                item.setChecked(true);
            }
            canvas.blur(item.isChecked());

        }else if(item.getItemId() == 2){
            if(item.isChecked()){
                item.setChecked(false);
            }else{
                item.setChecked(true);
            }
            canvas.color(item.isChecked());
        }else if(item.getItemId() == 3){
            if(item.isChecked()){
                item.setChecked(false);
            }else{
                item.setChecked(true);
            }
            canvas.bigPen(item.isChecked());
        }else if(item.getItemId() == 4){
            canvas.setRed();
        }else{
            canvas.setBlue();
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkPermission(){
        int permissioninfo = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(permissioninfo == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(getApplicationContext(),
                    "쓰기권한 있음", Toast.LENGTH_SHORT).show();
        }else{
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                Toast.makeText(this, "권한 설명", Toast.LENGTH_SHORT).show();
            }else{
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},100);
            }
        }
    }

    public void onRequestPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "SD Card 쓰기권한 승인", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "SD Card 쓰기권한 거부", Toast.LENGTH_SHORT).show();
            }

        }
    }

    public String getExternalPath(){
        String sdPath = "";
        String ext = Environment.getExternalStorageState();
        if(ext.equals(Environment.MEDIA_MOUNTED)) {
            sdPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
        }else
            sdPath = getFilesDir()+"";
        Toast.makeText(getApplicationContext(), sdPath, Toast.LENGTH_SHORT).show();
        return sdPath;
    }


}
