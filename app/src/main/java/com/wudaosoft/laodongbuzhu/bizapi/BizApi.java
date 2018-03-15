package com.wudaosoft.laodongbuzhu.bizapi;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.wudaosoft.laodongbuzhu.model.LoginInfo;
import com.wudaosoft.laodongbuzhu.model.UserInfo;
import com.wudaosoft.laodongbuzhu.utils.BitmapCallback;
import com.wudaosoft.laodongbuzhu.utils.CookieUtil;
import com.wudaosoft.laodongbuzhu.utils.DomainConfig;
import com.wudaosoft.laodongbuzhu.utils.EncryptUtil;
import com.wudaosoft.laodongbuzhu.utils.HttpRequest;
import com.wudaosoft.laodongbuzhu.utils.JsonCallback;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;

/**
 * Created on 2018/3/15 20:33.
 *
 * @author Changsoul.Wu
 */

public class BizApi {

    private static final String TAG = "BizApi";

    public static final String SERVER_TIME_PATTEN = "E, dd MMM yyyy HH:mm:ss";
    public static final String CLIENT_TIME_PATTEN = "yyyy-MM-dd HH:mm:ss";

    public static final Pattern APPLY_RECORD_PATTERN = Pattern.compile(
            "\\$\\(\"#ldlzy_gryw_grbtsb_bt_q_l\"\\).fwdatagrid\\([\\s\\S]+\"data\":[\\s\\S]*(\\{\"headers\":[\\s\\S]+\\}\\]\\})\\,\"pageSize\"");

    public static final Pattern APPLY_ID_PATTERN = Pattern.compile("onclick=\"ldlzy_viewSh\\('(.+?)'\\)");

    private Context context;
    private HttpRequest http;

    public BizApi(Context context) {
        this.context = context;
        http = HttpRequest.getInstance(context);
    }

    public void loadValiCodeImage(final BizCallback callback) {

        http.async(http.get(DomainConfig.IMAGE_CHECK), new BitmapCallback() {
            @Override
            public void onFail(Call call, Exception e) {
                toast(e.getMessage());
            }

            @Override
            public void onSuccess(Call call, Bitmap response) throws IOException {
                callback.post(response);
            }
        });
    }

    /**
     * 预登录，获取登录页面表单相关数据
     *
     * @return
     * @throws Exception
     */
    public LoginInfo preLogin() throws Exception {

        String loginForm = http.string(http.get(DomainConfig.LOGIN_PAGE));

        LoginInfo info = new LoginInfo();
        info.set_1_(findHiddenInputValue(loginForm, "_1_"));
        info.setDesKey(findHiddenInputValue(loginForm, "des_key"));
        info.setTicket(findHiddenInputValue(loginForm, "ticket"));
        info.setSubsys(findHiddenInputValue(loginForm, "subsys"));

        return info;
    }

    /**
     * 登录
     *
     * @param userInfo
     * @param imagCheck
     * @param userInfo
     * @return
     * @throws Exception
     */
    public JSONObject login(final UserInfo userInfo, String imagCheck) throws Exception {

        LoginInfo info = preLogin();

        String pwd = EncryptUtil.encrypt(info.get_1_(), userInfo.getPassword(), context);
        Map<String, String> params = new HashMap<>();
        params.put("LOGINID", userInfo.getLoginId());
        params.put("PASSWORD", pwd);
        params.put("IMAGCHECK", imagCheck);
        params.put("ISCA", "false");
        params.put("CAMY", "");
        params.put("ticket", info.getTicket());
        params.put("des_key", info.getDesKey());
        params.put("subsys", info.getSubsys());
        params.put("SUBSYS", info.getSubsys().toUpperCase());

        return http.json(http.postAjax(DomainConfig.AJAX_LOGIN, params));
    }

    /**
     * 退出登录
     *
     * @param userInfo
     * @return
     * @throws Exception
     */
    public void logout(final UserInfo userInfo, final BizCallback callback) throws Exception {

        String sessionId = CookieUtil.getCookieValue("JSESSIONIDGDLDWQ", http.getOkHttpClient());

        Map<String, String> params = new HashMap<>();
        params.put("sessionid", sessionId);

        http.async(http.postAjax(DomainConfig.AJAX_LOGOUT, params), new JsonCallback() {
            @Override
            public void onFail(Call call, Exception e) {
                toast(e.getMessage());
            }

            @Override
            public void onSuccess(Call call, JSONObject response) throws IOException {
                Log.d(TAG, "UserId[" + userInfo.getLoginId() + "] logout data: " + response);

                callback.post(response);
            }
        });
    }

    public String findHiddenInputValue(String text, String keyName) {
        Pattern pattern = Pattern.compile("<input type=\"hidden\" name=\"" + keyName + "\" value=\"(.+?)\" />");
        return findValue(text, pattern);
    }

    public String findValue(String text, Pattern pattern) {
        Matcher matcher = pattern.matcher(text);
        matcher.reset();
        boolean result = matcher.find();
        if (result) {
            return matcher.group(1);
        } else {
            return "";
        }
    }

    public void toast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
