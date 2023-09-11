package com.example.humarahub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class AddAddressActivity extends AppCompatActivity {
Button saveBtn;
private EditText city;
private EditText streetAddress;
private EditText permanentAddress;
private EditText pincode;
private Spinner state;
private EditText name;
private EditText mobileNo;
private String [] stateList;
 private String selectedState;
 private Dialog loadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add a new Address");

        //        loading dialog

        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(this.getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);



//        loading dialog


        saveBtn=findViewById(R.id.save_btn);
        city=findViewById(R.id.city);
        streetAddress=findViewById(R.id.streetAddress);
        permanentAddress=findViewById(R.id.permanentAddress);
        pincode=findViewById(R.id.pincode_item);
        state=findViewById(R.id.state);
        name=findViewById(R.id.Name);
        mobileNo=findViewById(R.id.mobileNo);
        stateList=getResources().getStringArray(R.array.pakistan_states);


        ArrayAdapter spinnerAdapter=new ArrayAdapter(this, android.R.layout.simple_spinner_item,stateList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        state.setAdapter(spinnerAdapter);
        state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               selectedState=stateList[i];

           }

           @Override
           public void onNothingSelected(AdapterView<?> adapterView) {

           }
       });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!TextUtils.isEmpty(city.getText())){
                    if(!TextUtils.isEmpty(streetAddress.getText()))
                    {
                        if(!TextUtils.isEmpty(permanentAddress.getText())){
                            if (!TextUtils.isEmpty(pincode.getText()) && pincode.getText().length()==6)
                            {
                                if(!TextUtils.isEmpty(name.getText()))
                                {
                                    if(!TextUtils.isEmpty(mobileNo.getText()) && mobileNo.getText().length()==11)
                                    {
                                        loadingDialog.show();
                                        String fullAddress=city.getText().toString() +" "+streetAddress.getText().toString()+" "+permanentAddress.getText().toString()+" "+selectedState;

//database data store horha hain
                                        Map<String,Object> addAddress=new HashMap();
                                        addAddress.put("list_size",(long)DBqueries.addressesModelList.size()+1);

                                        addAddress.put("name_"+String.valueOf((long)DBqueries.addressesModelList.size()+1),name.getText().toString() + " - " + mobileNo.getText().toString());
                                        addAddress.put("address_"+String.valueOf((long)DBqueries.addressesModelList.size()+1),fullAddress);
                                        addAddress.put("pincode_"+String.valueOf((long)DBqueries.addressesModelList.size()+1),pincode.getText().toString());
                                        addAddress.put("selected_"+String.valueOf((long)DBqueries.addressesModelList.size()+1),true);

                                        if(DBqueries.addressesModelList.size()>0) {
                                            addAddress.put("selected_" + (DBqueries.selectedAddress + 1), false);
                                        }
                                        FirebaseFirestore.getInstance().collection("USERS")
                                                .document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA")
                                                .document("MY_ADDRESSES")
                                                .update(addAddress).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful())
                                                              {
                                                                  if(DBqueries.addressesModelList.size()>0) {
                                                                      DBqueries.addressesModelList.get(DBqueries.selectedAddress).setSelected(false);
                                                                  }

                                                                  DBqueries.addressesModelList.add(new AddressesModel(name.getText().toString() + "-"+ mobileNo.getText().toString(),fullAddress,pincode.getText().toString(),true));

                                                                  if(getIntent().getStringExtra("INTENT").equals("deliveryIntent")) {
                                                                      Intent deliveryIntent = new Intent(AddAddressActivity.this, DeliveryActivity.class);
                                                                      startActivity(deliveryIntent);
                                                                  }else{
                                                                      MyAddressesActivity.refreshItem(DBqueries.selectedAddress,DBqueries.addressesModelList.size() - 1);
                                                                  }
                                                                  DBqueries.selectedAddress = DBqueries.addressesModelList.size() - 1;
                                                                  finish();
                                                              }else{
                                                                  String error = task.getException().getMessage();
                                                                  Toast.makeText(AddAddressActivity.this, error, Toast.LENGTH_SHORT).show();
                                                              }
                                                              loadingDialog.dismiss();
                                                    }
                                                });


                                    }else {
                                        mobileNo.requestFocus();
                                        Toast.makeText(AddAddressActivity.this, "Please Provide Valid Mobile No!!!", Toast.LENGTH_SHORT).show();

                                    }
                                }else {
                                    name.requestFocus();
                                }
                            }else{
                                pincode.requestFocus();
                                Toast.makeText(AddAddressActivity.this, "Please Provide Valid Pincode!!!", Toast.LENGTH_SHORT).show();

                            }
                        }else {
                            permanentAddress.requestFocus();

                        }
                    }else{
                        streetAddress.requestFocus();
                    }
                }else {
                    city.requestFocus();
                }





            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

          if(id==android.R.id.home)
          {
              finish();
              return true;
          }
        return super.onOptionsItemSelected(item);
    }

}