package pl.currency.currency.model;

/**
 * Created by Lukasz on 29.11.2017.
 */
public class Rate {

    private String date;
    private Double mid;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getMid() {
        return mid;
    }

    public void setMid(Double mid) {
        this.mid = mid;
    }

}
