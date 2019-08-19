package com.qf.v16order.pojo;

/**
 * @author huangguizhao
 */
public class AlipayBizContent {

    private String out_trade_no;
    private String product_code = "FAST_INSTANT_TRADE_PAY";
    private String total_amount;
    private String subject;
    private String body;

    public AlipayBizContent(String out_trade_no, String total_amount, String subject, String body) {
        this.out_trade_no = out_trade_no;
        this.total_amount = total_amount;
        this.subject = subject;
        this.body = body;
    }

    public AlipayBizContent() {
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
