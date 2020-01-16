package com.example.barberbooking.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barberbooking.Common.Common;
import com.example.barberbooking.Interface.IRecyclerItemSelectedListener;
import com.example.barberbooking.Model.TimeSlot;
import com.example.barberbooking.R;

import java.util.ArrayList;
import java.util.List;

public class MyTimeSlotAdapter extends RecyclerView.Adapter<MyTimeSlotAdapter.MyViewHolder> {

    Context context;
    List<TimeSlot> timeSlotList;
    List<CardView> cardViewList;
    LocalBroadcastManager localBroadcastManager;

    public MyTimeSlotAdapter(Context context) {
        this.context = context;
        this.timeSlotList = new ArrayList<>();
        this.localBroadcastManager = LocalBroadcastManager.getInstance(context);
        cardViewList = new ArrayList<>();
    }

    public MyTimeSlotAdapter(Context context, List<TimeSlot> timeSlotList) {
        this.context = context;
        this.timeSlotList = timeSlotList;
        this.localBroadcastManager = LocalBroadcastManager.getInstance(context);
        cardViewList = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.layout_time_slot,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txt_time_slot.setText(new StringBuilder(Common.convertTimeSlotToString(position)).toString());
        if (timeSlotList.size() == 0) //Daca tot time-table-ul este liber, afiseaza lista
        {
            holder.card_time_slot.setCardBackgroundColor(context.getResources().getColor(android.R.color.white));

            holder.txt_time_slot_description.setText("Disponibil");
            holder.txt_time_slot_description.setTextColor(context.getResources()
            .getColor(android.R.color.black));
            holder.txt_time_slot.setTextColor(context.getResources().getColor(android.R.color.black));

        }
        else {
            for (TimeSlot slotValue:timeSlotList)
            {
                int slot = Integer.parseInt(slotValue.getSlot().toString());
                if (slot == position)
                {
                    holder.card_time_slot.setTag(Common.DISABLE_TAG);

                    holder.card_time_slot.setCardBackgroundColor(context.getResources().getColor(android.R.color.darker_gray));
                    holder.txt_time_slot_description.setText("Ocupat");
                    holder.txt_time_slot_description.setTextColor(context.getResources()
                            .getColor(android.R.color.white));
                    holder.txt_time_slot.setTextColor(context.getResources().getColor(android.R.color.white));
                }
            }
        }
        //Adaugare time-table-uri(20)
        if (!cardViewList.contains(holder.card_time_slot))
            cardViewList.add(holder.card_time_slot);
        //Verificare daca time-table disponibil

            holder.setiRecyclerItemSelectedListener(new IRecyclerItemSelectedListener(){
                @Override
                public void onItemSelectedListener(View view, int pos) {
                    ////parcurgere CardView
                    for (CardView cardView:cardViewList)
                    {
                        if (cardView.getTag() == null) //numai cardVIew disponibile
                            cardView.setCardBackgroundColor(context.getResources()
                                    .getColor(android.R.color.white));
                    }
                    //cardview slectate isi schimba culoarea
                    holder.card_time_slot.setCardBackgroundColor(context.getResources().getColor(android.R.color.holo_orange_dark));
                    //Broadcast pentru butonul Next
                    Intent intent = new Intent(Common.KEY_ENABLE_BUTTON_NEXY);
                    intent.putExtra(Common.KEY_TIME_SLOT,position); //Marcare time-slot selectat
                    intent.putExtra(Common.KEY_STEP,3); //Skip catre pas 3
                    localBroadcastManager.sendBroadcast(intent);
                }
            });
        }


    @Override
    public int getItemCount() {
        return Common.TIME_SLOT_TOTAL;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_time_slot,txt_time_slot_description;
        CardView card_time_slot;

        IRecyclerItemSelectedListener iRecyclerItemSelectedListener;

        public void setiRecyclerItemSelectedListener(IRecyclerItemSelectedListener iRecyclerItemSelectedListener) {
            this.iRecyclerItemSelectedListener = iRecyclerItemSelectedListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            card_time_slot = (CardView)itemView.findViewById(R.id.card_time_slot);
            txt_time_slot = (TextView)itemView.findViewById(R.id.txt_time_slot);
            txt_time_slot_description = (TextView)itemView.findViewById(R.id.txt_time_slot_description);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            iRecyclerItemSelectedListener.onItemSelectedListener(v,getAdapterPosition());
        }
    }
}
