package com.hhxk.app.interfaces;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface Http {

    /**
     * 登录
     * @param userAccount
     * @param userPassword
     * @return
     */
    @POST("dologin")
    @FormUrlEncoded
    Call<ResponseBody> Login(@Field("userAccount") String userAccount, @Field("userPassword") String userPassword);

    /**
     * 首页
     * @param userId
     * @return
     */
    @POST("indexInfo")
    @FormUrlEncoded
    Call<ResponseBody> indexInfo(@Field("userId") String userId);

    /**
     * 操作管理-成员列表
     * @param start
     * @return
     */
    @POST("findUserList")
    @FormUrlEncoded
    Call<ResponseBody> findUserList(@Field("start") int start,@Field("departmentId") int departmentId);

    /**
     * 操作管理-部门列表
     * @return
     */
    @POST("findDept")
    Call<ResponseBody> findDept();

    /**
     * 操作管理-添加部门
     * @param departmentName
     * @return
     */
    @POST("addDept")
    @FormUrlEncoded
    Call<ResponseBody> addDept(@Field("departmentName") String departmentName);

    /**
     * 操作管理-删除成员
     * @param userId
     * @return
     */
    @POST("deleteUserById")
    @FormUrlEncoded
    Call<ResponseBody> deleteUserById(@Field("userId") int userId);

    /**
     * 操作管理-删除部门
     * @param departmentId
     * @return
     */
    @POST("deleteDept")
    @FormUrlEncoded
    Call<ResponseBody> deleteDept(@Field("departmentId") int departmentId);

    /**
     * 操作管理-修改部门
     * @param departmentId
     * @param departmentName
     * @return
     */
    @POST("changeDept")
    @FormUrlEncoded
    Call<ResponseBody> changeDept(@Field("departmentId") int departmentId,@Field("departmentName") String departmentName);

    /**
     * 操作管理-添加人员
     * @param userName
     * @param departmentId
     * @param positionId
     * @param userAccount
     * @return
     */
    @POST("register")
    @FormUrlEncoded
    Call<ResponseBody> register(@Field("userName") String userName,@Field("departmentId") String departmentId,@Field("positionId") String positionId,
                                @Field("userAccount") String userAccount);

    /**
     * 操作管理-修改人员
     * @param userId
     * @param departmentId
     * @param positionId
     * @return
     */
    @POST("changeInfo")
    @FormUrlEncoded
    Call<ResponseBody> changeInfo(@Field("userId") String userId,@Field("departmentId") String departmentId,@Field("positionId") String positionId);

    /**
     * 操作管理-根据部门查询职务
     * @param departmentId
     * @return
     */
    @POST("findPosition")
    @FormUrlEncoded
    Call<ResponseBody> findPosition(@Field("departmentId") String departmentId);

    /**
     * 操作管理-修改职务
     * @param positionId
     * @param positionName
     * @return
     */
    @POST("changePosition")
    @FormUrlEncoded
    Call<ResponseBody> changePosition(@Field("positionId") String positionId, @Field("positionName") String positionName);

    /**
     * 操作管理-添加职务
     * @param positionName
     * @return
     */
    @POST("addPosition")
    @FormUrlEncoded
    Call<ResponseBody> addPosition(@Field("positionName") String positionName,@Field("departmentId") String departmentId);

    /**
     * 操作管理-删除职务
     * @param positionId
     * @return
     */
    @POST("deletePosition")
    @FormUrlEncoded
    Call<ResponseBody> deletePosition(@Field("positionId") String positionId);

    /**
     * 重置密码
     * @param userAccount
     * @return
     */
    @POST("resetPass")
    @FormUrlEncoded
    Call<ResponseBody> resetPass(@Field("userAccount") String userAccount);

    /**
     * 修改密码
     * @param userPassword
     * @return
     */
    @POST("changePass")
    @FormUrlEncoded
    Call<ResponseBody> changePass(@Field("userPassword") String userPassword,@Field("userId") String userId);

    /**
     * 所有会议
     * @return
     */
    @POST("allMeeting")
    @FormUrlEncoded
    Call<ResponseBody> allMeeting(@Field("start") int start);

    /**
     * 未开始的会议
     * @return
     */
    @POST("notStartMeeting")
    Call<ResponseBody> notStartMeeting();

    /**
     * 已结束的会议
     * @return
     */
    @POST("isEedMeeting")
    Call<ResponseBody> isEedMeeting();

    /**
     * 进行中的会议
     * @return
     */
    @POST("ongoingMeeting")
    Call<ResponseBody> ongoingMeeting();

    /**
     * 我的全部会议
     * @param userId
     * @return
     */
    @POST("allMeetingForMe")
    @FormUrlEncoded
    Call<ResponseBody> allMeetingForMe(@Field("userId") String userId);

    /**
     * 我的发起会议
     * @param userId
     * @return
     */
    @POST("createMeetingForMe")
    @FormUrlEncoded
    Call<ResponseBody> createMeetingForMe(@Field("userId") String userId);

    /**
     * 我的主持会议
     */
    @POST("presideMeetingForMe")
    @FormUrlEncoded
    Call<ResponseBody> presideMeetingForMe(@Field("userId") String userId);

    /**
     * 未保存的会议
     * @param userId
     * @return
     */
    @POST("notCompleteMeeting")
    @FormUrlEncoded
    Call<ResponseBody> notCompleteMeeting(@Field("userId") String userId);

    /**
     * 我的参加会议
     * @param userId
     * @return
     */
    @POST("joinMeetingForMe")
    @FormUrlEncoded
    Call<ResponseBody> joinMeetingForMe(@Field("userId") String userId);

    /**
     * 保存会议基本信息
     * @param userId
     * @param meetingName
     * @param startDate
     * @param endDate
     * @param meetingInfo
     * @param meetingAddress
     * @return
     */
    @POST("preservationMeeting")
    @FormUrlEncoded
    Call<ResponseBody> preservationMeeting(@Field("userId") String userId,@Field("meetingName") String meetingName, @Field("startDate") String startDate,
                                    @Field("endDate") String endDate,@Field("meetingInfo") String meetingInfo,@Field("meetingAddress") String meetingAddress);

    /**
     * 下一步会议基本信息
     * @param userId
     * @param meetingName
     * @param startDate
     * @param endDate
     * @param meetingInfo
     * @param meetingAddress
     * @return
     */
    @POST("startMeeting")
    @FormUrlEncoded
    Call<ResponseBody> startMeeting(@Field("userId") String userId,@Field("meetingName") String meetingName, @Field("startDate") String startDate,
                                    @Field("endDate") String endDate,@Field("meetingInfo") String meetingInfo,@Field("meetingAddress") String meetingAddress);


    /**
     * 参会人员，添加主持人
     * @param parmStringm
     * @param meetingId
     * @return
     */
    @POST("addMeetingHost")
    @FormUrlEncoded
    Call<ResponseBody> addMeetingHost(@Field("parmString[]") List<String> parmStringm,@Field("meetingId") int meetingId);

    /**
     * 参会人员，添加参会人员
     * @param parmStringm
     * @param meetingId
     * @return
     */
    @POST("addMeetingOther")
    @FormUrlEncoded
    Call<ResponseBody> addMeetingOther(@Field("parmString[]") List<String> parmStringm,@Field("meetingId") int meetingId);

    /**
     * 删除参会人员
     * @param meetingId
     * @param userId
     * @return
     */
    @POST("deleteMeetingHost")
    @FormUrlEncoded
    Call<ResponseBody> deleteMeetingHost(@Field("meetingId") String meetingId,@Field("userId") String userId);

    /**
     * 添加会议议题
     * @param meetingId
     * @param lssueName
     * @param userName
     * @param position
     * @return
     */
    @POST("addLssue")
    @FormUrlEncoded
    Call<ResponseBody> addLssue(@Field("meetingId") int meetingId,@Field("lssueName") String lssueName,@Field("userName") String userName,@Field("position") String position);

    /**
     * 删除会议议题
     * @param lssueId
     * @return
     */
    @POST("deleteLssue")
    @FormUrlEncoded
    Call<ResponseBody> deleteLssue(@Field("lssueId") int lssueId);

    /**
     * 修改会议议题
     * @param lssueId
     * @param lssueName
     * @param userName
     * @param position
     * @return
     */
    @POST("changeLssue")
    @FormUrlEncoded
    Call<ResponseBody> changeLssue(@Field("lssueId") int lssueId,@Field("lssueName") String lssueName,@Field("userName") String userName,@Field("position") String position);

    /**
     * 查看会议详情
     * @param meetingId
     * @return
     */
    @POST("findMeetingDetails")
    @FormUrlEncoded
    Call<ResponseBody> findMeetingDetails(@Field("meetingId") int meetingId);

    /**
     * 根据议题ID查询对应的附件
     * @param userId
     * @param lssueId
     * @return
     */
    @POST("selectLssueFileById")
    @FormUrlEncoded
    Call<ResponseBody> selectLssueFileById(@Field("userId") int userId, @Field("lssueId") String lssueId);

    /**
     * 会议议题上移
     * @param meetingId
     * @param lssueId
     * @param lssueOrder
     * @return
     */
    @POST("moveUpward")
    @FormUrlEncoded
    Call<ResponseBody> moveUpward(@Field("meetingId") String meetingId,@Field("lssueId") int lssueId,@Field("lssueOrder") int lssueOrder);

    /**
     * 会议议题下移
     * @param meetingId
     * @param lssueId
     * @param lssueOrder
     * @return
     */
    @POST("moveDown")
    @FormUrlEncoded
    Call<ResponseBody> moveDown(@Field("meetingId") String meetingId,@Field("lssueId") int lssueId,@Field("lssueOrder") int lssueOrder);

    /**
     * 会议议题上传材料
     * @param fileupload
     * @param lssueId
     * @param lssusefileName
     * @return
     */
    @POST("upload")
    @Multipart
    Call<ResponseBody> upload(@Part MultipartBody.Part fileupload,@Query("lssueId") String lssueId,@Query("lssusefileName") String lssusefileName);

    /**
     * 删除会议材料
     * @param lssusefileId
     * @return
     */
    @POST("deleteLssueFile")
    @FormUrlEncoded
    Call<ResponseBody> deleteLssueFile(@Field("lssusefileId") int lssusefileId);

    /**
     * 完成发起会议
     * @param meetingId
     * @return
     */
    @POST("generateMeeting")
    @FormUrlEncoded
    Call<ResponseBody> generateMeeting(@Field("meetingId") int meetingId);

    /**
     * 签到
     * @param userId
     * @param meetingId
     * @return
     */
    @POST("signIn")
    @FormUrlEncoded
    Call<ResponseBody> signIn(@Field("userId") int userId,@Field("meetingId") int meetingId);

    /**
     * 修改基本信息
     * @param meetingId
     * @param meetingName
     * @param startDate
     * @param endDate
     * @param meetingInfo
     * @param meetingAddress
     * @return
     */
    @POST("changeMeeting")
    @FormUrlEncoded
    Call<ResponseBody> changeMeeting(@Field("meetingId") int meetingId,@Field("meetingName") String meetingName, @Field("startDate") String startDate,
                                    @Field("endDate") String endDate,@Field("meetingInfo") String meetingInfo,@Field("meetingAddress") String meetingAddress);

    /**
     * 上传会议纪要
     * @param meetingId
     * @param summary
     * @return
     */
    @POST("addSummary")
    @FormUrlEncoded
    Call<ResponseBody> addSummary(@Field("meetingId") String meetingId,@Field("summary") String summary);

    /**
     * 查询会议纪要
     * @param meetingId
     * @return
     */
    @POST("findSummary")
    @FormUrlEncoded
    Call<ResponseBody> findSummary(@Field("meetingId") String meetingId);

    /**
     * 删除会议
     * @param meetingId
     * @return
     */
    @POST("deleteMeetingById")
    @FormUrlEncoded
    Call<ResponseBody> deleteMeetingById(@Field("meetingId") int meetingId);

    /**
     * 取消会议
     * @param meetingId
     * @return
     */
    @POST("cancelMeeting")
    @FormUrlEncoded
    Call<ResponseBody> cancelMeeting(@Field("meetingId") int meetingId);

    /**
     * 根据条件查询会议
     * @param type
     * @param start
     * @param startDate
     * @param endDate
     * @param meetingName
     * @return
     */
    @POST("searchInfoByType")
    @FormUrlEncoded
    Call<ResponseBody> searchInfoByType(@Field("type") int type,@Field("start") int start,@Field("startDate") String startDate,@Field("endDate") String endDate,
                                        @Field("meetingName") String meetingName);

    /**
     * 文件已查看过标识
     * @param lssusefileId
     * @param lssueId
     * @param userId
     * @return
     */
    @POST("findFileRecord")
    @FormUrlEncoded
    Call<ResponseBody> findFileRecord(@Field("lssusefileId") String lssusefileId, @Field("lssueId") String lssueId, @Field("userId") int userId);
}

