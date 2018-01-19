package com.example.android.tguide;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

/**
 * ADAPTER TO POPULATE LIST VIEW WITH REMINDER INFORMATION
 * Created by zacha_000 on 2018-01-11.
 */

public class ReminderAdapter extends CursorAdapter {

    // Define variables
    private TextView mTitleText, mDateAndTimeText, mRepeatInfoText, mDescription, mDescriptionText, mUniqueID;
    private ImageView mActiveImage, mThumbnailImage;
    private ColorGenerator mColorGenerator = ColorGenerator.DEFAULT;
    private TextDrawable mDrawableBuilder;

    public ReminderAdapter(Context context, Cursor cursor){
        super(context, cursor, 0);
    }

    // Inflate item layout to go in list
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent){
        return LayoutInflater.from(context).inflate(R.layout.alarm_items,parent,false);
    }

    // Bind data to populate view in list
    @Override
    public void bindView(View view, Context context, Cursor cursor){
        mTitleText = (TextView) view.findViewById(R.id.recycle_title);
        mDateAndTimeText = (TextView) view.findViewById(R.id.recycle_date_time);
        mRepeatInfoText = (TextView) view.findViewById(R.id.recycle_details);
        mDescription = (TextView) view.findViewById(R.id.recycle_details);
        mDescriptionText = (TextView) view.findViewById(R.id.recycle_description);
        mActiveImage = (ImageView) view.findViewById(R.id.reminder_sound_icon);
        mThumbnailImage = (ImageView) view.findViewById(R.id.reminder_thumbnail_image);

        // Extra properties from cursor
        String title = cursor.getString(cursor.getColumnIndexOrThrow(ReminderDBHelper.REMINDER_COLUMN_TITLE));
        String description = cursor.getString(cursor.getColumnIndexOrThrow(ReminderDBHelper.REMINDER_COLUMN_DESCRIPTION));
        String date = cursor.getString(cursor.getColumnIndexOrThrow(ReminderDBHelper.REMINDER_COLUMN_DATE));
        String time = cursor.getString(cursor.getColumnIndexOrThrow(ReminderDBHelper.REMINDER_COLUMN_TIME));
        String repeat = cursor.getString(cursor.getColumnIndexOrThrow(ReminderDBHelper.REMINDER_COLUMN_REPEAT));
        String repeatNu = cursor.getString(cursor.getColumnIndexOrThrow(ReminderDBHelper.REMINDER_COLUMN_REPEATNUM));
        String repeatTy = cursor.getString(cursor.getColumnIndexOrThrow(ReminderDBHelper.REMINDER_COLUMN_REPEATTYPE));
        String active = cursor.getString(cursor.getColumnIndexOrThrow(ReminderDBHelper.REMINDER_COLUMN_SOUND));

        String dateTime = date + " " + time;


        //Populate View Fields
        // See if description emtpy or not
        mDescriptionText.setText(description);
        setReminderTitle(title);
        setReminderDateTime(dateTime);
        setReminderRepeatInfo(repeat, repeatNu, repeatTy);
        setActiveImage(active);
    }

    // Set reminder title view
    public void setReminderTitle(String title) {
        mTitleText.setText(title);
        String letter = "A";

        if(title != null && !title.isEmpty()) {
            letter = title.substring(0, 1);
        }

        int color = mColorGenerator.getRandomColor();

        // Create a circular icon consisting of  a random background colour and first letter of title
        mDrawableBuilder = TextDrawable.builder()
                .buildRound(letter, color);
        mThumbnailImage.setImageDrawable(mDrawableBuilder);
    }

    // Set date and time views
    public void setReminderDateTime(String datetime) {
        mDateAndTimeText.setText(datetime);
    }

    // Set repeat views
    public void setReminderRepeatInfo(String repeat, String repeatNo, String repeatType) {
        if(repeat.equals("true")){
            mRepeatInfoText.setText("Every " + repeatNo + " " + repeatType + "(s)");
        }else if (repeat.equals("false")) {
            mRepeatInfoText.setText("Repeat Off");
        }
    }

    // Set active image as on or off
    public void setActiveImage(String active){
        if(active.equals("true")){
            mActiveImage.setImageResource(android.R.drawable.ic_lock_silent_mode_off);
        }else if (active.equals("false")) {
            mActiveImage.setImageResource(android.R.drawable.ic_lock_silent_mode);
        }
    }
}

