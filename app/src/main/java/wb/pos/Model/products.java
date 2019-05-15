package wb.pos.Model;

public class products {
    public products(long product_ID, String product_name, int product_quantity, int product_price, int id) {
        this.product_ID = product_ID;
        this.product_name = product_name;
        this.product_quantity = product_quantity;
        this.product_price = product_price;
        this.id = id;
    }

    public products() {

    }

    long product_ID;
    String product_name;
    int product_quantity;
    int product_price;
    int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getProduct_ID() {
        return product_ID;
    }

    public void setProduct_ID(long product_ID) {
        this.product_ID = product_ID;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public int getProduct_quantity() {
        return product_quantity;
    }

    public void setProduct_quantity(int product_quantity) {
        this.product_quantity = product_quantity;
    }

    public int getProduct_price() {
        return product_price;
    }

    public void setProduct_price(int product_price) {
        this.product_price = product_price;
    }
}
