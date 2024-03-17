package com.nguyenuyennhi.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.nguyenuyennhi.helloworld.MyContactAdvancedActivity;
import com.nguyenuyennhi.helloworld.R;
import com.nguyenuyennhi.model.MyContact;

public class MyContactAdapter extends ArrayAdapter<MyContact> {
    Activity context;
    int resource;

    public MyContactAdapter(@NonNull Activity context, int resource) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View view = inflater.inflate(resource, null);

        TextView txtContactName = view.findViewById(R.id.txtContactName);
        TextView txtPhoneNumber = view.findViewById(R.id.txtPhoneNumber);
        ImageView imgCall = view.findViewById(R.id.imgCall);
        ImageView imgSms = view.findViewById(R.id.imgSms);

        MyContact contact = getItem(position);
        txtContactName.setText(contact.getContactName());
        txtPhoneNumber.setText(contact.getPhoneNumber());

        imgCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MyContactAdvancedActivity) context).makeDirectCall(contact);
            }
        });

        imgSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MyContactAdvancedActivity) context).makeSms(contact);
            }
        });

        return view;
    }
}
