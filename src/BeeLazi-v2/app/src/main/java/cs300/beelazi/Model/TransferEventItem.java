package cs300.beelazi.Model;

import java.io.Serializable;


public class TransferEventItem implements Serializable{
    int id, sHour, sMinute, sDay, sMonth, sYear, eHour, eMinute, eDay, eMonth, eYear;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TransferEventItem(int id, int sHour, int sMinute, int sDay, int sMonth, int sYear, int eHour, int eMinute, int eDay, int eMonth, int eYear) {

        this.id = id;
        this.sHour = sHour;
        this.sMinute = sMinute;
        this.sDay = sDay;
        this.sMonth = sMonth;
        this.sYear = sYear;
        this.eHour = eHour;
        this.eMinute = eMinute;
        this.eDay = eDay;
        this.eMonth = eMonth;
        this.eYear = eYear;
    }

    public TransferEventItem(int sHour, int sMinute, int sDay, int sMonth, int sYear, int eHour, int eMinute, int eDay, int eMonth, int eYear) {
        this.sHour = sHour;
        this.sMinute = sMinute;
        this.sDay = sDay;
        this.sMonth = sMonth;
        this.sYear = sYear;
        this.eHour = eHour;
        this.eMinute = eMinute;
        this.eDay = eDay;
        this.eMonth = eMonth;
        this.eYear = eYear;
    }

    public int getsHour() {
        return sHour;
    }

    public void setsHour(int sHour) {
        this.sHour = sHour;
    }

    public int getsMinute() {
        return sMinute;
    }

    public void setsMinute(int sMinute) {
        this.sMinute = sMinute;
    }

    public int getsDay() {
        return sDay;
    }

    public void setsDay(int sDay) {
        this.sDay = sDay;
    }

    public int getsMonth() {
        return sMonth;
    }

    public void setsMonth(int sMonth) {
        this.sMonth = sMonth;
    }

    public int getsYear() {
        return sYear;
    }

    public void setsYear(int sYear) {
        this.sYear = sYear;
    }

    public int geteHour() {
        return eHour;
    }

    public void seteHour(int eHour) {
        this.eHour = eHour;
    }

    public int geteMinute() {
        return eMinute;
    }

    public void seteMinute(int eMinute) {
        this.eMinute = eMinute;
    }

    public int geteDay() {
        return eDay;
    }

    public void seteDay(int eDay) {
        this.eDay = eDay;
    }

    public int geteMonth() {
        return eMonth;
    }

    public void seteMonth(int eMonth) {
        this.eMonth = eMonth;
    }

    public int geteYear() {
        return eYear;
    }

    public void seteYear(int eYear) {
        this.eYear = eYear;
    }
}
