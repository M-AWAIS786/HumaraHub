package com.example.humarahub;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SignUpFragment extends Fragment {



    public SignUpFragment() {
        // Required empty public constructor
    }

    private TextInputEditText email,fullname,password,confirmpassword;
    private Button register;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";
    private FrameLayout parentFrameLayout;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
       View view= inflater.inflate(R.layout.fragment_sign_up, container, false);
            email=view.findViewById(R.id.email);
            fullname=view.findViewById(R.id.username);
            password=view.findViewById(R.id.Password);
            confirmpassword=view.findViewById(R.id.confirmPassword);
            register=view.findViewById(R.id.signupbtn);
            firebaseAuth=FirebaseAuth.getInstance();
            firebaseFirestore=FirebaseFirestore.getInstance();
        MaterialCardView cardView = view.findViewById(R.id.cardView);
        float cornerRadius = 50f;
        ShapeAppearanceModel shapeAppearanceModel = new ShapeAppearanceModel()
                .toBuilder()
                .setTopLeftCorner(CornerFamily.ROUNDED, cornerRadius)
                .setTopRightCorner(CornerFamily.ROUNDED, cornerRadius)
                .build();
        cardView.setShapeAppearanceModel(shapeAppearanceModel);
            return view;
    }

    private void setFragment(Fragment fragment)
    {
        FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_left,R.anim.slideout_from_right);
        fragmentTransaction.replace(parentFrameLayout.getId(),fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }
    public void onViewCreated(View view,final Bundle savedInstanceState)
    {
        super.onViewCreated(view,savedInstanceState);
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        fullname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        confirmpassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // : send data to firebase
                checkEmailAndPassword();
            }
        });


    }
    private void checkInputs()
    {
        if(!TextUtils.isEmpty(email.getText()))
        {
            if(!TextUtils.isEmpty(fullname.getText()))
            {
                if(!TextUtils.isEmpty(password.getText()) && password.length() >=8)
                {
                    if(!TextUtils.isEmpty(confirmpassword.getText()))
                    {
                      register.setEnabled(true);
                      register.setTextColor(Color.rgb(255,255,255));
                    }else {
                        register.setEnabled(false);
                        register.setTextColor(Color.argb(50,255,255,255));
                    }
                }else {
                    register.setEnabled(false);
                    register.setTextColor(Color.argb(50,255,255,255));
                }
            }else{
                register.setEnabled(false);
                register.setTextColor(Color.argb(50,255,255,255));
            }
        }else{
                register.setEnabled(false);
                register.setTextColor(Color.argb(50,255,255,255));
        }

    }


    private void checkEmailAndPassword()
    {
        if(email.getText().toString().matches(emailPattern))
        {
            if(password.getText().toString().equals(confirmpassword.getText().toString()))
            {
                register.setEnabled(false);
                register.setTextColor(Color.argb(50,255,255,255));

                firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                               if(task.isSuccessful())
                               {
                                   Map<String,Object> userdata=new HashMap<>();
                                   userdata.put("Fullname",fullname.getText().toString());

                                   firebaseFirestore.collection("USERS").document(firebaseAuth.getUid())
                                           .set(userdata)
                                           .addOnCompleteListener(new OnCompleteListener<Void>() {
                                               @Override
                                               public void onComplete(@NonNull Task<Void> task) {
                                                   if(task.isSuccessful()) {

                                                       CollectionReference userDataReference=firebaseFirestore.collection("USERS")
                                                               .document(firebaseAuth.getUid()).collection("USER_DATA");


                                                       //MAPS
                                                       Map<String,Object> wishlistMap=new HashMap<>();
                                                       wishlistMap.put("list_size",(long)0);

                                                       Map<String,Object> cartMap=new HashMap<>();
                                                       cartMap.put("list_size",(long)0);

                                                       Map<String,Object> myAddressesMap=new HashMap<>();
                                                       myAddressesMap.put("list_size",(long)0);

                                                       //Maps
                                                       final List<String> documentNames=new ArrayList<>();
                                                       documentNames.add("MY_WISHLIST");
                                                       documentNames.add("MY_CART");
                                                       documentNames.add("MY_ADDRESSES");

                                                       List<Map<String,Object>> documentFields =new ArrayList<>();
                                                       documentFields.add(wishlistMap);
                                                       documentFields.add(cartMap);
                                                       documentFields.add(myAddressesMap);

                                                       for(int x=0; x<documentNames.size(); x++)
                                                       {
                                                         final  int finalX = x;
                                                           userDataReference.document(documentNames.get(x))
                                                                   .set(documentFields.get(x)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                       @Override
                                                                       public void onComplete(@NonNull Task<Void> task) {
                                                                           if(task.isSuccessful())
                                                                           {
                                                                               if(finalX ==documentNames.size()-1) {
                                                                                   mainIntent();
                                                                               }
                                                                           }else{
                                                                               register.setEnabled(true);
                                                                               register.setTextColor(Color.rgb(255,255,255));
                                                                               String error=task.getException().getMessage();
                                                                               Toast.makeText(getActivity(),error,Toast.LENGTH_SHORT).show();
                                                                           }
                                                                       }
                                                                   });
                                                       }
                                                   }else{
                                                       String error=task.getException().getMessage();
                                                       Toast.makeText(getActivity(),error,Toast.LENGTH_SHORT).show();
                                                   }
                                               }
                                           });


                               }else{
                                   register.setEnabled(true);
                                   register.setTextColor(Color.rgb(255,255,255));
                                   String error=task.getException().getMessage();
                                   Toast.makeText(getActivity(),error,Toast.LENGTH_SHORT).show();
                               }
                            }
                        });

            }else{
             confirmpassword.setError("Password doesn't matched");
            }
        }else{
            email.setError("Invalid Email");
        }
    }
private void mainIntent()
{
    Intent mainintent=new Intent(getActivity(),MainActivity.class);
    startActivity(mainintent);
    getActivity().finish();
}

}