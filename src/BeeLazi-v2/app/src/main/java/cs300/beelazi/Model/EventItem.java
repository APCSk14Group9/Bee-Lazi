package cs300.beelazi.Model;

import com.appeaser.sublimepickerlibrary.datepicker.SelectedDate;

import java.io.Serializable;


public class EventItem implements Serializable{

    int mHour, mMinute;
    SelectedDate selectedDate;

    public EventItem(){
        mHour = mMinute = -1;
        selectedDate = null;
    }

    public EventItem(int mHour, int mMinute, SelectedDate selectedDate) {
        this.mHour = mHour;
        this.mMinute = mMinute;
        this.selectedDate = selectedDate;
    }


    public int getmHour() {
        return mHour;
    }

    public void setmHour(int mHour) {
        this.mHour = mHour;
    }

    public int getmMinute() {
        return mMinute;
    }

    public void setmMinute(int mMinute) {
        this.mMinute = mMinute;
    }

    public SelectedDate getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(SelectedDate selectedDate) {
        this.selectedDate = selectedDate;
    }

}
