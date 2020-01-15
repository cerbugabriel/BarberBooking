package com.example.barberbooking.Fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Space;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barberbooking.Adapter.MySalonAdapter;
import com.example.barberbooking.Common.SpacesItemDecoration;
import com.example.barberbooking.Interface.IAllSalonLoadListener;
import com.example.barberbooking.Interface.IBranchLoadListener;
import com.example.barberbooking.Model.Salon;
import com.example.barberbooking.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dmax.dialog.SpotsDialog;

public class BookingStep1Fragment extends Fragment implements IAllSalonLoadListener, IBranchLoadListener {

    //Variable
    CollectionReference allSalonRef;
    CollectionReference branchRef;

    IAllSalonLoadListener iAllSalonLoadListener;
    IBranchLoadListener iBranchLoadListener;

    @BindView(R.id.spinner)
    MaterialSpinner spinner;
    @BindView(R.id.recycler_salon)
    RecyclerView recycler_salon;

    Unbinder unbinder;

    AlertDialog dialog;

    static BookingStep1Fragment instance;

    public static BookingStep1Fragment getInstance() {
        if (instance == null)
            instance = new BookingStep1Fragment();
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        allSalonRef = FirebaseFirestore.getInstance().collection("AllSalon");
        iAllSalonLoadListener =  this;
        iBranchLoadListener = this;

        dialog = new SpotsDialog.Builder().setContext(getActivity()).build();
    }

    @androidx.annotation.Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @androidx.annotation.Nullable ViewGroup container, @androidx.annotation.Nullable Bundle savedInstanceState) {

        View itemView = inflater.inflate(R.layout.fragment_booking_step_one,container,false);
        unbinder = ButterKnife.bind(this,itemView);

        initView();
        loadAllSalon();

        return itemView;
    }

    private void initView() {
        recycler_salon.setHasFixedSize(true);
        recycler_salon.setLayoutManager(new GridLayoutManager(getActivity(),2));
        recycler_salon.addItemDecoration(new SpacesItemDecoration(4));
    }

    private void loadAllSalon() {
        allSalonRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful())
                        {
                            List<String> list = new ArrayList<>();
                            list.add("Alege Orasul");
                            for (QueryDocumentSnapshot documentSnapshot:task.getResult())
                                list.add(documentSnapshot.getId());
                            iAllSalonLoadListener.onAllSalonLoadSucces(list);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iAllSalonLoadListener.onAllSalonLoadFailed(e.getMessage());
            }
        });
    }

    @Override
    public void onAllSalonLoadSucces(List<String> areaNameList) {
        spinner.setItems(areaNameList);
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if (position > 0)
                {
                    loadBranchOfCity(item.toString());
                }
                else recycler_salon.setVisibility(View.GONE);
            }
        });

    }

    private void loadBranchOfCity(String cityName) {
        dialog.show();

        branchRef = FirebaseFirestore.getInstance()
                .collection("AllSalon")
                .document(cityName)
                .collection("Branch");

        branchRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Salon> list = new ArrayList<>();
                if(task.isSuccessful())
                {
                    for (QueryDocumentSnapshot documentSnapshot:task.getResult())
                        list.add(documentSnapshot.toObject(Salon.class));
                    iBranchLoadListener.onBranchLoadSucces(list);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iBranchLoadListener.onBranchLoadFailed(e.getMessage());
            }
        });
    }

    @Override
    public void onAllSalonLoadFailed(String message) {
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onBranchLoadSucces(List<Salon> salonList) {
        MySalonAdapter adapter = new MySalonAdapter(getActivity(),salonList);
        recycler_salon.setAdapter(adapter);
        recycler_salon.setVisibility(View.VISIBLE);

        dialog.dismiss();
    }

    @Override
    public void onBranchLoadFailed(String message) {
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
        dialog.dismiss();
    }
}
