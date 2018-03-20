package com.wudaosoft.laodongbuzhu.bizapi;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.wudaosoft.laodongbuzhu.exception.ServiceException;
import com.wudaosoft.laodongbuzhu.model.LoginInfo;
import com.wudaosoft.laodongbuzhu.model.UserInfo;
import com.wudaosoft.laodongbuzhu.utils.BitmapCallback;
import com.wudaosoft.laodongbuzhu.utils.CookieUtil;
import com.wudaosoft.laodongbuzhu.utils.DateUtil;
import com.wudaosoft.laodongbuzhu.utils.DomainConfig;
import com.wudaosoft.laodongbuzhu.utils.EncryptUtil;
import com.wudaosoft.laodongbuzhu.utils.HttpRequest;
import com.wudaosoft.laodongbuzhu.utils.JsonCallback;
import com.wudaosoft.laodongbuzhu.utils.StringUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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

    /**
     * 获取图片验证码
     *
     * @param callback
     */
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
    private LoginInfo preLogin() throws Exception {

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
     * @param account
     * @param password
     * @param imagCheck
     * @return JSONObject
     * @throws Exception
     */
    public JSONObject login(String account, String password, String imagCheck) throws Exception {

        LoginInfo info = preLogin();

        String pwd = EncryptUtil.encrypt(info.get_1_(), password, context);
        Map<String, String> params = new HashMap<>();
        params.put("LOGINID", account);
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
     * @param account
     * @throws Exception
     */
    public void logout(final String account, final BizCallback callback) {

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
                Log.d(TAG, "UserId[" + account + "] logout data: " + response);

                callback.post(response);
            }
        });
    }

    /**
     * 获取结果页面源码
     *
     * @return String
     * @throws Exception
     */
    private String getResultPageSourceCode() throws Exception {

        return http.string(http.get(DomainConfig.QUERY_RESULT));
    }


    private JSONObject getApplyRecord() throws Exception {

        String source = getResultPageSourceCode();
        String dataStr = findValue(source, APPLY_RECORD_PATTERN);
        JSONObject data = null;
        try {
            data = JSON.parseObject(dataStr);
        } catch (JSONException e) {
        }

        Log.d(TAG, "UserId apply record data: " + data);

        return data;
    }


	/*public JSONObject getApplyRecord(UserInfo userInfo) throws Exception {
		Map<String, String> params = new HashMap<>();
		params.put("confid", "ldlzy_gryw_grbtsb_bt_q_l");
		params.put("dynDictWhereCls", "null");
		params.put("dsId", "");
		params.put("rowstart", "1");
		params.put("pageSize", "20");
		params.put("whereCls", "  a.BCC859 = c.BCC859(+)  and a.BCC857 = '2553150' and a.BOE545='01'");

		JSONObject data = request.postAjax(DomainConfig.DOMAIN, DomainConfig.GLT_PAGE, params, userInfo.getContext());

		Log.d(TAG, "UserId[" + userInfo.getLoginId() + "] apply record data: " + data);

		return data;
	}*/

    /**
     * 获取在官网上的申请ID
     *
     * @return
     * @throws Exception
     */
    public String getApplyId() throws Exception {

        JSONObject data = getApplyRecord();

        if (data == null)
            return null;

        int rowCount = data.getIntValue("total");
        String id = null;

//		String year = DateUtil.date2String(serverTime.getTime(), "yyyy");

        if (rowCount > 0) {

            for (int i = rowCount - 1; i >= 0; i--) {

                JSONObject row = data.getJSONArray("rows").getJSONObject(i);

                String aab080 = row.getString("AAB080");
//				if (aab080 == null || !aab080.startsWith(year))
//					continue;
                if (aab080 == null)
                    continue;

                String acc78b = row.getString("ACC78B");

                if (acc78b == null)
                    continue;

                id = findValue(acc78b, APPLY_ID_PATTERN);
                id = "".equals(id) ? null : id;

                Log.i(TAG, "applyId: " + id);

                if (acc78b.contains("未提交")) {//审核中

//                    userInfo.setApplyStatus(ApplyStatus.unsubmit);

                } else if (acc78b.contains("审核中")) {

//                    userInfo.setApplyStatus(ApplyStatus.appling);
                } else if (acc78b.contains("办理成功")) {

//                    userInfo.setApplyStatus(ApplyStatus.passed);
                }

                break;
            }
        }

        return id;
    }

    public Map<String, Object> getApplyPageData() throws Exception {

        String html = http.string(http.get(DomainConfig.APPLY_DADA_PAGE));

        //Log.d(TAG, "UserId[" + userInfo.getLoginId() + "] getApplyPageData source code: " + html);

        Document doc = Jsoup.parse(html);
        Elements elements = doc.select("form[name='gtForm'] input, form[name='gtForm'] select");
        Map<String, Object> data = new HashMap<>();

        for (Element e : elements) {

            boolean isSelect = "select".equals(e.tagName());

            String name = e.attr("name");
            String value = isSelect ? e.getElementsByAttribute("selected").val() : e.val();

            if (StringUtils.isNotBlank(name)) {

                if (data.containsKey(name) && StringUtils.isBlank(data.get(name)) && StringUtils.isNotBlank(value)) {
                    data.put(name, value);
                } else {
                    data.put(name, value);
                }

                if (isSelect) {
                    String text = e.text();
                    String[] texts = text.split("-");
                    if ("CCE029".equals(name) && texts.length == 2) {
                        data.put("_DIC_" + name, texts[1]);
                    } else
                        data.put("_DIC_" + name, text);
                }
            }
            //System.out.println(e.attr("vldStr") + "[=]" +  e.attr("name") + "=" + e.val());
        }

        //data.put("readOnly", "false");
        data.put("edit", "true");
        data.put("AAB080", DateUtil.today());
        data.put("_multiple", Arrays.asList("", ""));

        Log.d(TAG, "getApplyPageData: " + JSON.toJSONString(data));

        return data;
    }

    /**
     * 检查是否已提交成功
     *
     * @param id
     * @return
     * @throws Exception
     */
    public boolean checkIfHasSubmitted(String id) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("BCC859", id);

        String rs = doService("btxxService.checkIfHasSubmitted", params);

        Log.d(TAG, "checkIfHasSubmitted: " + rs);

        return "[true]".equals(rs);
    }

    public boolean submitBtxx(final Map<String, Object> params) throws Exception {

        String rs = doService("btxxService.submitBtxx", params);

        Log.i(TAG, "SY2:" + params.get("SYZBS") + " submitBtxx: " + rs);

        JSONObject obj = JSON.parseArray(rs).getJSONObject(0);

        if ("1".equals(obj.getString("flag"))) {
            Log.i(TAG, "操作成功! SY2:" + params.get("SYZBS"));
            return true;
        }

        if ("true".equals(obj.getString("SFSBKJYYC"))) {
            //不是成功提交

            params.put("BCC859", obj.getString("YWLSH"));

            Log.i(TAG, obj.getString("SBKERRMSG"));

            return false;
        } else {

//			document.gtForm.BCC859.value = obj.getString("YWLSH");
//			document.gtForm.readOnly.value = "true" ;
//			document.gtForm.action = "/gdweb/ggfw/web/wsyw/app/ldlzy/gryw/grbtsb/btxx!toTjS.do";
//			document.gtForm.submit();
            params.put("BCC859", obj.getString("YWLSH"));
            params.put("readOnly", "true");

            Map<String, String> pars = new HashMap<>();

            for (Map.Entry<String, Object> e : params.entrySet()) {

                pars.put(e.getKey(), e.getValue().toString());
            }

            String subRs = http.string(http.post(DomainConfig.APPLY_DADA_SUBMIT, pars));

            Log.d(TAG, "submitApplyData: " + subRs);

            Log.d(TAG, "申请信息已经提交到" + params.get("BCC864NAME") + "人力资源和社会保障局，请于5个工作日内携带规定的资料去受理部门书面申请培训补贴！");
        }

        return true;
    }

    public Map<String, Object> getZsxx() throws Exception {

        Map<String, Object> data = getApplyPageData();
        Map<String, Object> params = new HashMap<>();

        params.put("BHE034", data.get("BHE034"));// 证书类别
        params.put("CCE029", data.get("CCE029"));// 证书级别
        params.put("BCC867", data.get("BCC867"));// 工种流水号
        params.put("AAB080", data.get("AAB080"));// 申报日期
        params.put("AAC058", data.get("AAC058"));// 身份证件类型
        params.put("AAC147", data.get("AAC147"));// 身份证件号码
        params.put("AAC003", data.get("AAC003"));// 姓名
        params.put("BOE531", data.get("BOE531"));// 户口类别
        params.put("BCC870", data.get("BCC870"));// 是否贫困家庭学员
        params.put("BOD215", data.get("BOD215"));// 现居住地（行政区划）
        params.put("BCC864", data.get("BCC864"));// 申请鉴定地
        params.put("BCC859", data.get("BCC859"));// 个人补贴申请流水号
        params.put("BHB321", data.get("BHB321"));// 工种别名信息维护表流水号
        params.put("edit", data.get("edit"));// 编辑标志


        Log.d(TAG, "getZsxx parametes: " + JSON.toJSONString(params));

        if (StringUtils.isBlank(data.get("BCC864")) || StringUtils.isBlank(data.get("BHE034"))
                || StringUtils.isBlank(data.get("CCE029")) || StringUtils.isBlank(data.get("BCC867"))
                || StringUtils.isBlank(data.get("AAB080")) || StringUtils.isBlank(data.get("AAC058"))
                || StringUtils.isBlank(data.get("AAC147")) || StringUtils.isBlank(data.get("AAC003"))) {

            throw new ServiceException("getZsxx error. Illegal parameters: " + JSON.toJSONString(params));
        }

        String rs = doService("btxxService.getZsxx", params);

        Log.d(TAG, "getZsxx: " + rs);

        JSONObject obj = JSON.parseArray(rs).getJSONObject(0);

        int bcc229 = obj.getIntValue("BCC229");
        int bca060 = obj.getIntValue("BCC869");
        int syzbs = obj.getIntValue("SY2");
        boolean isLackOfIndicators = obj.getBooleanValue("isLackOfIndicators");

        data.put("BCC290", obj.get("BCC290"));
        data.put("BCC868", obj.get("BCC868"));
        data.put("BCA060", bca060 + "");
        data.put("BCC869", bca060 + "");
        data.put("ZSBAE001", obj.get("ZSBAE001"));
        data.put("ACC560", obj.get("ZSBAE001"));
        data.put("BCC229", bcc229 + "");
        data.put("BCC871", bcc229 + "");
        data.put("BCC856", obj.get("BCC856"));
        data.put("SYZBS", syzbs);
        data.put("BCEA6Z", obj.get("BCEA6Z"));
        data.put("isLackOfIndicators", isLackOfIndicators);
        data.put("ZJ", (bcc229 + bca060) + "");

        Log.d(TAG, "getZsxx data: " + JSON.toJSONString(data));

        return data;
    }

    public String doService(String serviceName, Map<String, Object> params) throws Exception {

        String[] acts = serviceName.split("\\.");

        List<Map<String, Object>> list = new ArrayList<>();

        Map<String, Object> map = new HashMap<>();

        map.put("serviceId", acts[0]);
        map.put("method", acts[1]);
        map.put("parameters", params);

        list.add(map);

        Map<String, String> tmpParams = new HashMap<>();
        tmpParams.put("parameters", JSON.toJSONString(list));
        tmpParams.put("method", "{}");
        tmpParams.put("shareArguments", "{}");

        Log.d(TAG, "doService: " + serviceName + ", parameter: " + JSON.toJSONString(tmpParams));

        JSONObject result = http.json(http.postAjax(DomainConfig.AJAX_ADAPTER, tmpParams));

        Log.d(TAG, "doService: " + serviceName + ", result: " + result);

        // String token = result.getString("Token");
        String fhz = result.getString("FHZ");
        // String msg = result.getString("MSG");

        if (!"1".equals(fhz)) {
            // if ("loginTimeout".equals(fhz)) {
            //
            // JOptionPane.showMessageDialog(mainForm, "登录超时，请重新登录！" + msg,
            // "提示", JOptionPane.WARNING_MESSAGE);
            // } else if ("CSRF".equals(fhz)) {
            //
            // JOptionPane.showMessageDialog(mainForm, msg, "提示",
            // JOptionPane.WARNING_MESSAGE);
            // } else {
            //
            // JOptionPane.showMessageDialog(mainForm, msg, "错误",
            // JOptionPane.WARNING_MESSAGE);
            // }
            // return "";

            throw new ServiceException("Do Service[" + serviceName + "] error. msg: " + result);
        } else {
            return result.getString("RTN");
        }
    }

    private String findHiddenInputValue(String text, String keyName) {
        Pattern pattern = Pattern.compile("<input type=\"hidden\" name=\"" + keyName + "\" value=\"(.+?)\" />");
        return findValue(text, pattern);
    }

    private String findValue(String text, Pattern pattern) {
        Matcher matcher = pattern.matcher(text);
        matcher.reset();
        boolean result = matcher.find();
        if (result) {
            return matcher.group(1);
        } else {
            return "";
        }
    }

    private void toast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
