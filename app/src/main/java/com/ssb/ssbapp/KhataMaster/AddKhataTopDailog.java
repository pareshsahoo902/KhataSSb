package com.ssb.ssbapp.KhataMaster;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ssb.ssbapp.Model.KhataModel;
import com.ssb.ssbapp.R;
import com.ssb.ssbapp.Sessions.LocalSession;
import com.ssb.ssbapp.SplashScreen.SplashActivity;
import com.ssb.ssbapp.Utils.UtilsMethod;
import com.ssb.ssbapp.ViewHolder.KhataListViewHolder;

import java.util.HashMap;

import static com.ssb.ssbapp.Utils.Constants.SSB_PREF_BRANCH;
import static com.ssb.ssbapp.Utils.Constants.SSB_PREF_KID;

public class AddKhataTopDailog extends BottomSheetDialogFragment {

    private Button createbtn;
    private RecyclerView khatareycler;

    private DatabaseReference khataRef;


    private FirebaseRecyclerOptions<KhataModel> khataOption;
    private FirebaseRecyclerAdapter<KhataModel, KhataListViewHolder> khataRecyclerAdapter;


    public static AddKhataTopDailog newInstace() {
        return new AddKhataTopDailog();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bottom_sheet_khata, container,
                false);
        createbtn = view.findViewById(R.id.createkhatabtn);
        khatareycler = view.findViewById(R.id.newkhatabottomrecycler);
        khatareycler.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        khatareycler.hasFixedSize();


        khataRef = FirebaseDatabase.getInstance().getReference().child("khata");
        khataRef.keepSynced(true);

        createbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), KhataManagment.class));
            }
        });

        loadKhata();

        return view;
    }

    private void loadKhata() {

        khataOption = new FirebaseRecyclerOptions.Builder<KhataModel>().setQuery(khataRef, KhataModel.class).build();
        khataRecyclerAdapter = new FirebaseRecyclerAdapter<KhataModel, KhataListViewHolder>(khataOption) {
            @Override
            protected void onBindViewHolder(@NonNull KhataListViewHolder trayListViewHolder, int i, @NonNull KhataModel trayMasterModel) {
                trayListViewHolder.khataName.setText(UtilsMethod.capitalize(trayMasterModel.getName()));
                if (trayMasterModel.getKid().equals(LocalSession.getString(SSB_PREF_KID))){
                    trayListViewHolder.isActiveBtn.setVisibility(View.VISIBLE);
                }
                else {
                    trayListViewHolder.isActiveBtn.setVisibility(View.INVISIBLE);

                }
                trayListViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LocalSession.putString(SSB_PREF_KID,trayMasterModel.getKid());
                        LocalSession.putString(SSB_PREF_BRANCH,trayMasterModel.getName());
                        startActivity(new Intent(getContext(), SplashActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

                        dismiss();
                    }
                });

            }

            @NonNull
            @Override
            public KhataListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new KhataListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.khata_item,parent,false));
            }
        };


        khatareycler.setAdapter(khataRecyclerAdapter);
        khataRecyclerAdapter.startListening();

    }
}

