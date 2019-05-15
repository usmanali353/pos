package wb.pos;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import wb.pos.Model.UsersDB;
import wb.pos.Model.products;
import wb.pos.Model.reports;

public interface pos_interface {

    @GET("product_catagory_myshop.php")
    Call<ArrayList<products>> get_all_products();
    @FormUrlEncoded
    @POST("myshop_products.php")
    Call<String> insert_products(@Field("id")long id,@Field("Name")String name,@Field("Price")int price,@Field("Quantity") int quantity);
    @FormUrlEncoded
    @POST("myshop_products.php")
    Call<String> insert_products_without_id(@Field("Name")String name,@Field("Price")int price,@Field("Quantity") int quantity);
    @FormUrlEncoded
    @POST("Order_History_myshop.php")
    Call<String> add_report(@Field("items_name")String items_name,@Field("Date") String Date,@Field("retailer_email") String retailer_email,@Field("total_amount") String total_amount);
    @FormUrlEncoded
    @POST("get_all_reports.php")
    Call<ArrayList<reports>> get_all_reports(@Field("email") String email);
    @FormUrlEncoded
    @POST("login_myshop.php")
    Call<ArrayList<UsersDB>> sign_in(@Field("email")String email, @Field("password")String password);
    @FormUrlEncoded
    @POST("edit_product_detail.php")
    Call<String> edit_product_detail(@Field("id")long id,@Field("Name")String name,@Field("Quantity")int quantity,@Field("Price") int price);
    @FormUrlEncoded
    @POST("filter_reports.php")
    Call<ArrayList<reports>> get_reports_by_date(@Field("start_date")String start_date,@Field("end_date")String end_date);
    @FormUrlEncoded
    @POST("Decrement_quantity.php")
    Call<String> decrement_quantity(@Field("id") String id,@Field("quantity")String quantity);
    @FormUrlEncoded
    @POST("increment_quantity.php")
    Call<String> icrement_quantity(@Field("id") String id,@Field("quantity")String quantity);
}
