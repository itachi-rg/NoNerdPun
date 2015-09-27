package com.eventer.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.eventer.AddContacts;
import com.eventer.CreateEvent;
import com.eventer.MainActivity;
import com.eventer.Objects.Person;
import com.eventer.R;

import java.util.List;

/**
 * Created by rg on 27-Aug-15.
 */
public class ContactListAdapter extends ArrayAdapter<String> {
    private AddContacts activity;

    public ContactListAdapter(Context context, List<String> persons) {
        super(context, 0, persons);
        activity = (AddContacts) context;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        String person = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_contact_list, parent, false);
        }

//      ImageView imageView = (ImageView) convertView.findViewById(R.id.removePerson);
        TextView textView = (TextView) convertView.findViewById(R.id.each_person);

/*        Bitmap coverImage = song.getCoverImage();
        if (coverImage != null) {
            imageView.setImageBitmap(coverImage);
        }*/

        textView.setText(person);

/*        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.removeMember(position);
            }
        });*/

        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.abc_fade_in);
        animation.setDuration(2000);
        convertView.startAnimation(animation);
        return convertView;

    }
}
