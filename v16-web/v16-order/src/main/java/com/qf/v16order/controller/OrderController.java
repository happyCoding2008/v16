package com.qf.v16order.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qf.v16order.pojo.AlipayBizContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author huangguizhao
 */
@Controller
@RequestMapping("order")
public class OrderController {


    @Autowired
    private AlipayClient alipayClient;

    /**
     * 当用户在“订单确认页”确认之后，进入支付页面
     */
    @RequestMapping("toPay")
    @ResponseBody
    public void toPay(HttpServletResponse httpResponse,String orderNo) throws IOException {
        //1.创建alipayClient，用于跟支付宝进行对接
        //2.构建请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();//创建API对应的request
        //3.设置同步回调路径
        alipayRequest.setReturnUrl("http://edztb4.natappfree.cc/order/returnURL");
        //4.设置异步回调路径
        alipayRequest.setNotifyUrl("http://edztb4.natappfree.cc/order/notifyUrl");//在公共参数中设置回跳和通知地址

        //5.设置业务参数
        AlipayBizContent bizContent = new AlipayBizContent(orderNo,"8888","iphone16X","128G");
        //
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(bizContent);
        alipayRequest.setBizContent(json);
        /*alipayRequest.setBizContent("{" +
                "    \"out_trade_no\":\""+orderNo+"\"," +
                "    \"product_code\":\"FAST_INSTANT_TRADE_PAY\"," +
                "    \"total_amount\":8888," +
                "    \"subject\":\"Iphone6 16G\"," +
                "    \"body\":\"Iphone6 16G\""+
                "  }");//填充业务参数*/
        String form="";
        try {
            form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        httpResponse.setContentType("text/html;charset=utf-8");
        httpResponse.getWriter().write(form);//直接将完整的表单html输出到页面
        httpResponse.getWriter().flush();
        httpResponse.getWriter().close();
    }

    @RequestMapping("returnURL")
    public String returnURL(){
        //只是跳转展示给用户看而已
        //不能代表支付成功
        return "return_url";
    }

    @RequestMapping("notifyUrl")
    public void notifyUrl(HttpServletResponse response, HttpServletRequest request) throws AlipayApiException, IOException {
        //异步回调，是否支付成功，以这个为准
        System.out.println("notifyUrl..............");

        //将异步通知中收到的所有参数都存放到map中
        Map<String, String[]> sourceMap = request.getParameterMap();
        Map<String, String> paramsMap = new HashMap<>();
        //sourceMap-->paramsMap
        Set<Map.Entry<String, String[]>> entries = sourceMap.entrySet();
        for (Map.Entry<String, String[]> entry : entries) {
            //entry.getValue() -> string
            String[] values = entry.getValue();
            //String[] -> a,b,c
            StringBuilder value = new StringBuilder();
            for(int i=0;i<values.length-1;i++){
                value.append(values[i]).append(",");
            }
            value.append(values[values.length-1]);
            //逐个保存
            paramsMap.put(entry.getKey(),value.toString());
        }

        boolean signVerified = AlipaySignature.rsaCheckV1(
                paramsMap,
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwkz5NNzXt9IcV8sheHaYskJ7G1kOEAyR5cn5CUw0iVqHURqokImbvNAILwMqRy4forXJNKM4CcjbuhigTmMxx6CYnn6n1yE8BHW1VGeFoJP9zFMtwTZboHHLteHCmWD0QDjj9yqhGxuM9Il6vPX/gtcpe5fLDY6yvFr9vSlD3q60GCkOvOScpL1YKmdFU28A7tz6O4V/IUhZdwM4LOdZCCNpTKou75lFT1hUuTarMbl9nD40ntGv6FeY1QeDNEXMyTJa8yfUwjUOs/ixkvyfcVAa5GNLygKjg7IoZ3CSA06GIuZUF16cdatOfFGWclr/6Fx/x8ubaZzg9t6zgOCU3wIDAQAB",
                "utf-8",
                "RSA2") ;//调用SDK验证签名
        if(signVerified){
            // TODO 验签成功后，按照支付结果异步通知中的描述，对支付结果中的业务内容进行二次校验，
            // 校验成功后在response中返回success并继续商户自身业务处理，校验失败返回failure
            System.out.println("第一步：验签成功！是支付宝发过来的！");
            System.out.println("第二步：检查关键的业务参数是否正确！订单编号，订单金额，商家账号，AppID");
            //1.获取到支付宝的流水号
            String trade_no = request.getParameter("trade_no");
            //2.获取到商家的订单号
            String out_trade_no = request.getParameter("out_trade_no");
            //3.获取到appid
            String appid = request.getParameter("app_id");
            //4.获取到订单金额
            String total_amount = request.getParameter("total_amount");
            //5.获取支付状态
            String trade_status = request.getParameter("trade_status");
            if("TRADE_SUCCESS".equals(trade_status)){
                //支付成功，进一步核对各项业务参数的正确性，如果正确，更新订单状态为已支付
                System.out.println("支付成功！");
                //核对，查询订单表进行信息匹配
                response.getWriter().write("success");//
                response.getWriter().flush();

                //如果金额等业务参数核对错误，则需要记录日志，同时要将支付宝的流水号记录下来
            }else if("TRADE_CLOSED".equals(trade_status)){
                //关闭支付（超时），将订单状态修改为已取消
                System.out.println("超时未支付，支付已关闭，订单已取消");
                response.getWriter().write("success");//
                response.getWriter().flush();
            }else{
                response.getWriter().write("failure");//
                response.getWriter().flush();
            }
        }else{
            // TODO 验签失败则记录异常日志，并在response中返回failure.
            response.getWriter().write("failure");
            response.getWriter().flush();
        }
    }


}
