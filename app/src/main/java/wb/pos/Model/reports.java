package wb.pos.Model;

public class reports {
    public String getItems_name() {
        return items_name;
    }

    public void setItems_name(String items_name) {
        this.items_name = items_name;
    }

    public String getRetailer_email() {
        return Retailer_email;
    }

    public void setRetailer_email(String retailer_email) {
        Retailer_email = retailer_email;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public float getTotal_amount() {
        return Total_amount;
    }

    public void setTotal_amount(int total_amount) {
        Total_amount = total_amount;
    }

    String items_name,Retailer_email,Date;
         int Id;
         float Total_amount;

}
