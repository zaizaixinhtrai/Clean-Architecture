package com.gcox.fansmeet.webservice;

import com.gcox.fansmeet.data.entity.*;
import com.gcox.fansmeet.webservice.request.*;
import com.gcox.fansmeet.webservice.response.*;
import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.*;

import java.util.List;

/**
 * Created by User on 9/8/2015.
 */
public interface AppsterWebserviceAPI {

    @POST("api/users/LoginWithGoogle")
    Observable<BaseResponse<LoginResponseModel>> loginWithGoogle(@Body GoogleLoginRequestModel request);

    @POST("api/users/LoginWithFacebook")
    Observable<BaseResponse<LoginResponseModel>> loginWithFacebook(@Body LoginFacebookRequestModel request);

    @POST("api/users/LoginWithInstagram")
    Observable<BaseResponse<LoginResponseModel>> loginWithInstagram(@Body InstagramLoginRequestModel request);

    @GET("api/settings/home/banners")
    Observable<BaseResponse<List<String>>> homeBanners(@Header("Authorization") String connection);

    @GET("api/users/celebrities")
    Observable<BaseResponse<UsersEntity>> homeCelebrities(@Header("Authorization") String connection, @Query("model.nextId") int nextId, @Query("model.limit") int limit);

    @GET("api/users/events")
    Observable<BaseResponse<UsersEntity>> homeEvents(@Header("Authorization") String connection, @Query("model.nextId") int nextId, @Query("model.limit") int limit);

    @GET("api/users/influencers")
    Observable<BaseResponse<UsersEntity>> homeInfluencers(@Header("Authorization") String connection, @Query("model.nextId") int nextId, @Query("model.limit") int limit);

    @GET("api/challenges/celebrity/{celebrityId}")
    Observable<BaseResponse<CelebrityEntity>> homeCelebrityList(@Header("Authorization") String connection, @Path("celebrityId") int celebrityId, @Query("model.nextId") int nextId, @Query("model.limit") int limit);

    @GET("api/users/celebrity/{celebrityId}")
    Observable<BaseResponse<CelebrityProfileEntity>> celebrityProfile(@Header("Authorization") String connection, @Path("celebrityId") int celebrityId);

    @GET("api/challenges/{challengeId}")
    Observable<BaseResponse<CelebrityListEntity>> getChallenge(@Header("Authorization") String connection, @Path("challengeId") int celebrityId);

    @GET("api/challenges/{challengeId}/entries")
    Observable<BaseResponse<ChallengeEntriesEntityResponse>> getChallengeEntries(@Header("Authorization") String connection, @Path("challengeId") int celebrityId, @Query("model.nextId") int nextId, @Query("model.limit") int limit);

    @GET("api/challenges/entry/{entryId}")
    Observable<BaseResponse<ContestantEntriesEntity>> viewContestantChallengesEntries(@Header("Authorization") String connection, @Path("entryId") int entryId);

    @GET("api/challenges")
    Observable<BaseResponse<ChallengeListEntriesEntityResponse>> getChallengeListEntries(@Header("Authorization") String connection, @Query("model.nextId") int nextId, @Query("model.limit") int limit);

    @POST("api/users/RegisterWithGoogle")
    Observable<BaseResponse<RegisterWithFacebookResponseModel>> registerWithGoogle(@Body MultipartBody request);

    @POST("api/users/RegisterWithFacebook")
    Observable<BaseResponse<RegisterWithFacebookResponseModel>> registerWithFacebook(@Body MultipartBody request);

    @POST("api/users/RegisterWithInstagram")
    Observable<BaseResponse<RegisterWithFacebookResponseModel>> registerWithInstagram(@Body MultipartBody request);

    @POST("api/users/LogOut")
    Observable<BaseResponse<Boolean>> logoutApp(@Header("Authorization") String authen, @Body LogoutRequestModel request);

    @GET("api/users/merchants")
    Observable<BaseResponse<UsersEntity>> getMerchants(@Header("Authorization") String connection, @Query("model.nextId") int nextId, @Query("model.limit") int limit);

    @GET("api/challenges/celebrity/{celebrityId}/gridview")
    Observable<BaseResponse<CelebrityGridEntity>> celebrityGrid(@Header("Authorization") String connection, @Path("celebrityId") int celebrityId, @Query("model.nextId") int nextId, @Query("model.limit") int limit);

    @POST("api/payments/gift")
    Observable<BaseResponse<SendGiftEntity>> sendGift(@Header("Authorization") String authen, @Body SendGiftRequest request);

    @GET("api/users/username/{username}/isexist")
    Observable<BaseResponse<Boolean>> verifyUsername(@Path("username") String username);

    @POST("api/users/follow")
    Observable<BaseResponse<FollowUserEntity>> followUser(@Header("Authorization") String connection, @Body FollowUserRequestModel request);

    @POST("api/challenges/{challengeId}/like")
    Observable<BaseResponse<Integer>> likePost(@Header("Authorization") String connection, @Path("challengeId") int challengeId);

    @POST("api/challenges/entry/{entryId}/like")
    Observable<BaseResponse<Integer>> likeEntries(@Header("Authorization") String connection, @Path("entryId") int entryId);

    @POST("api/challenges/{challengeId}/unlike")
    Observable<BaseResponse<Integer>> unlike(@Header("Authorization") String connection, @Path("challengeId") int challengeId);

    @POST("api/challenges/entry/{entryId}/unlike")
    Observable<BaseResponse<Integer>> unlikeEntries(@Header("Authorization") String connection, @Path("entryId") int entryId);

    @POST("api/users/unfollow")
    Observable<BaseResponse<FollowUserEntity>> unFollowUser(@Header("Authorization") String connection, @Body FollowUserRequestModel request);

    @GET("api/challenges/{challengeId}/CanSubmit")
    Observable<BaseResponse<Boolean>> canSubmitChallenge(@Header("Authorization") String connection, @Path("challengeId") int challengeId);

    @GET("api/users/referral/{referralCode}/isexist")
    Observable<BaseResponse<Boolean>> isReferralCodeExist(@Path("referralCode") String referralCode);

    @GET("api/users/email/{email}/isexist")
    Observable<BaseResponse<Boolean>> isEmailExist(@Path("email") String email);

    @POST("api/users/{userId}/block")
    Observable<BaseResponse<Boolean>> blockUser(@Header("Authorization") String connection, @Path("userId") int userId);

    @POST("api/challenges/{challengeId}/report")
    Observable<BaseResponse<Boolean>> reportPost(@Header("Authorization") String connection, @Path("challengeId") int challengeId, @Body ReportRequestModel reason);

    @POST("api/challenges/{challengeId}/unreport")
    Observable<BaseResponse<Boolean>> unReportPost(@Header("Authorization") String connection, @Path("challengeId") int challengeId);

    @POST("api/challenges/entry/{entryId}/report")
    Observable<BaseResponse<Boolean>> reportEntries(@Header("Authorization") String connection, @Path("entryId") int entryId, @Body ReportRequestModel reason);

    @POST("api/challenges/entry/{entryId}/unreport")
    Observable<BaseResponse<Boolean>> unReportEntries(@Header("Authorization") String connection, @Path("entryId") int entryId);

    @DELETE("api/challenges/entry/{entryId}")
    Observable<BaseResponse<Boolean>> deleteSubmission(@Header("Authorization") String authen, @Path("entryId") int challengeId);

    @POST("api/payments/InAppPurchase")
    Observable<BaseResponse<VerifyIAPResponeModel>> verifyIAPPurchased(@Header("Authorization") String authen, @Body VerifyIAPRequestModel request);

    @GET("api/payments/InAppPurchase/{transactionId}/isfinished")
    Observable<BaseResponse<Boolean>> iAPPurchasedIsfinished(@Header("Authorization") String authen, @Path("transactionId") String transactionId);

    @GET("api/settings/app/configs")
    Observable<BaseResponse<VersionResponseModel>> checkVersion(@Query("model.deviceType") int deviceType, @Query("model.version") String version);

    @GET("api/users/leftmenu")
    Observable<BaseResponse<CreditsModel>> getUserCredits(@Header("Authorization") String authen);
}
