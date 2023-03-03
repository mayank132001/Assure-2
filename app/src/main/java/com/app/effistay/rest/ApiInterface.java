package com.app.effistay.rest;

import com.app.effistay.response.AboutUsRP;
import com.app.effistay.response.AppRP;
import com.app.effistay.response.BookingRoomRP;
import com.app.effistay.response.Cities;
import com.app.effistay.response.ContactRP;
import com.app.effistay.response.DataRP;
import com.app.effistay.response.FacilitiesRP;
import com.app.effistay.response.FaqRP;
import com.app.effistay.response.GalleryCatRP;
import com.app.effistay.response.GalleryListRP;
import com.app.effistay.response.HomeRP;
import com.app.effistay.response.HotelDetail;
import com.app.effistay.response.Hotels;
import com.app.effistay.response.LocationRP;
import com.app.effistay.response.LoginRP;
import com.app.effistay.response.MyOrder;
import com.app.effistay.response.PrivacyPolicyRP;
import com.app.effistay.response.ProfileRP;
import com.app.effistay.response.RegisterRP;
import com.app.effistay.response.ReviewRP;
import com.app.effistay.response.RoomDetailRP;
import com.app.effistay.response.RoomRP;
import com.app.effistay.response.TermsConditionsRP;
import com.app.effistay.response.UserReviewRP;
import com.app.effistay.response.UserReviewSubmitRP;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


public interface ApiInterface {

    //get app data
    @POST("api.php")
    @FormUrlEncoded
    Call<AppRP> getAppData(@Field("data") String data);

    //get about us
    @POST("api.php")
    @FormUrlEncoded
    Call<AboutUsRP> getAboutUs(@Field("data") String data);

    //get privacy policy
    @POST("api.php")
    @FormUrlEncoded
    Call<PrivacyPolicyRP> getPrivacyPolicy(@Field("data") String data);

    //get terms condition
    @POST("api.php")
    @FormUrlEncoded
    Call<TermsConditionsRP> getTermsCondition(@Field("data") String data);

    //get faq
    @POST("api.php")
    @FormUrlEncoded
    Call<FaqRP> getFaq(@Field("data") String data);

    //login
//    @Headers("Content-Type: application/json")
    @POST("customer_login")
    Call<LoginRP> getLogin(@Body Map<String, String> body);

    @POST("forget_password")
    Call<DataRP> forgotPassword(@Body Map<String, String> body);

    @POST("customer_social_login")
    Call<LoginRP> getSocialLogin(@Body Map<String, String> body);

    //register
    @POST("customer_register")
    Call<RegisterRP> getRegister(@Body Map<String, String> body);

    @POST("customer_social_register")
    Call<RegisterRP> getSocialRegister(@Body Map<String, String> body);

    //forgot password
    @POST("api.php")
    @FormUrlEncoded
    Call<DataRP> getForgotPass(@Field("data") String data);

    //login check

    @POST("customer_profile")
    Call<LoginRP> getLoginDetail(@Body Map<String, String> body);

    //get profile detail
    @POST("customer_profile")
    Call<ProfileRP> getProfile(@Body Map<String, String> body);

    //edit profile
    @POST("update_profile_data")
    Call<DataRP> editProfile(@Body Map<String, String> body);

    //update password
    @POST("change_password")
    Call<DataRP> updatePassword(@Body Map<String, String> body);

    @POST("update_profile_image")
    @Multipart
    Call<DataRP> imageUpdate(@Part MultipartBody.Part image,
                             @Part("customer_id") RequestBody customer_id);

    //get contact subject
    @POST("api.php")
    @FormUrlEncoded
    Call<ContactRP> getContactSub(@Field("data") String data);

    //submit contact
    @POST("api.php")
    @FormUrlEncoded
    Call<DataRP> submitContact(@Field("data") String data);

    //home
    @POST("api.php")
    @FormUrlEncoded
    Call<HomeRP> getHome(@Field("data") String data);

    //room
    @POST("api.php")
    @FormUrlEncoded
    Call<RoomRP> getRoom(@Field("data") String data);

    //room detail
    @POST("api.php")
    @FormUrlEncoded
    Call<RoomDetailRP> getRoomDetail(@Field("data") String data);

    //review
    @POST("api.php")
    @FormUrlEncoded
    Call<ReviewRP> getReview(@Field("data") String data);

    //user review
    @POST("api.php")
    @FormUrlEncoded
    Call<UserReviewRP> getUserReview(@Field("data") String data);

    //submit review
    @POST("api.php")
    @FormUrlEncoded
    Call<UserReviewSubmitRP> submitUserReview(@Field("data") String data);

    //booking
    @POST("api.php")
    @FormUrlEncoded
    Call<BookingRoomRP> roomBooking(@Field("data") String data);

    //get gallery
    @POST("api.php")
    @FormUrlEncoded
    Call<GalleryCatRP> getGallery(@Field("data") String data);

    //get gallery detail
    @POST("api.php")
    @FormUrlEncoded
    Call<GalleryListRP> getGalleryList(@Field("data") String data);

    //facilities
    @POST("api.php")
    @FormUrlEncoded
    Call<FacilitiesRP> getFacilities(@Field("data") String data);

    //location
    @POST("api.php")
    @FormUrlEncoded
    Call<LocationRP> getLocation(@Field("data") String data);

    @POST("city_list")
    Call<Cities> getCities(@Body Map<String, String> body);

    @POST("hotel_list")
    Call<Hotels> getHotelList(@Body Map<String, String> body);

    @POST("hotel_details")
    Call<HotelDetail> getHotelDetail(@Body Map<String, String> body);

    @POST("make_order")
    Call<DataRP> bookHotel(@Body Map<String, String> body);

    @POST("my_orders")
    Call<MyOrder> getMyOrders(@Body Map<String, String> body);

    @POST("update_order")
    Call<DataRP> updateOrder(@Body Map<String, String> body);

    @Multipart
    @POST("make_order")
    Call<DataRP> bookHotel(@Part("customer_id") RequestBody customer_id, @Part("hotel_id") RequestBody hotel_id, @Part("booking_date") RequestBody booking_date, @Part("checkin_time") RequestBody checkin_time,@Part("checkout_time") RequestBody checkout_time,@Part("hour_price") RequestBody hour_price,@Part("no_of_person") RequestBody no_of_person,@Part("total_room") RequestBody total_room,@Part("final_amount") RequestBody final_amount,@Part("payment_type") RequestBody payment_type,@Part("payment_ref_no") RequestBody payment_ref_no,@Part("coupon_code") RequestBody coupon_code, @Part MultipartBody.Part image);

    @Multipart
    @POST("make_pay_later_order")
    Call<DataRP> bookHotelWithCash(@Part("customer_id") RequestBody customer_id, @Part("hotel_id") RequestBody hotel_id, @Part("booking_date") RequestBody booking_date, @Part("checkin_time") RequestBody checkin_time,@Part("checkout_time") RequestBody checkout_time,@Part("hour_price") RequestBody hour_price,@Part("no_of_person") RequestBody no_of_person,@Part("total_room") RequestBody total_room,@Part("final_amount") RequestBody final_amount,@Part("coupon_code") RequestBody coupon_code, @Part MultipartBody.Part image);

    @POST("apply_coupon")
    Call<DataRP> applyCouponAPI(@Body Map<String, String> body);
}
