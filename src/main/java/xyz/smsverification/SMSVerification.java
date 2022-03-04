package xyz.smsverification;

import lombok.Getter;
import lombok.val;
import org.json.JSONObject;
import xyz.smsverification.objects.AccountBalanceChange;
import xyz.smsverification.objects.OrderCreated;
import xyz.smsverification.objects.OrderComplete;
import xyz.smsverification.objects.OrderStatusChange;
import xyz.smsverification.utils.HttpUtil;

public class SMSVerification {
    @Getter
    private final HttpUtil httpUtil;

    public SMSVerification (String apiKey) {
        this.httpUtil = new HttpUtil(apiKey);
    }

    /**
     * <h3>Get the balance on users account</h3>
     *
     * @return Balance on users account as float
     * @throws Exception When something goes differently than expected
     * @see <a href="https://developer.smsverification.xyz/api-reference/balance">SMS Verification's docs / balance</a>
     */
    public float getBalance() throws Exception {
        val res = httpUtil.request("https://smsverification.xyz/api/v2/balance", null, "GET");

        return res.getFloat("balance");
    }

    /**
     * <h3>Get the price of a service</h3>
     *
     * @param country Country of the number
     * @param service Service name
     * @return Price of given service
     * @throws Exception When something goes differently than expected
     * @see <a href="https://developer.smsverification.xyz/api-reference/disposable/get-price">SMS Verification's docs / disposable/get-price</a>
     */
    public float getPrice(String country, String service) throws Exception {
        val request = new JSONObject();
        request.put("country", country);
        request.put("service", service);
        val body = request.toString();

        val res = httpUtil.request("https://smsverification.xyz/api/v2/disposable/price", body, "GET");

        val phoneObj = res.getJSONObject("phone");

        return phoneObj.getFloat("price");
    }

    /**
     * <h3>Order a number</h3>
     *
     * @param country Country of the number
     * @param service Service name
     * @return OrderCreated object with order ID, SMS number and balance change
     * @throws Exception When something goes differently than expected
     * @see <a href="https://developer.smsverification.xyz/api-reference/disposable/order">SMS Verification's docs / disposable/order</a>
     */
    public OrderCreated order(String country, String service) throws Exception {
        val request = new JSONObject();
        request.put("country", country);
        request.put("service", service);
        val body = request.toString();

        val res = httpUtil.request("https://smsverification.xyz/api/v2/disposable", body, "POST");

        val oldBal = res.getFloat("old_balance");
        val newBal = res.getFloat("new_balance");

        val phoneObj = res.getJSONObject("phone");

        val number = phoneObj.getString("number");
        val id = phoneObj.getString("id");

        return new OrderCreated(new AccountBalanceChange(oldBal, newBal), number, id);
    }

    /**
     * <h3>Cancel an order</h3>
     *
     * @param id ID of the order you wish to cancel
     * @return Balance change
     * @throws Exception When something goes differently than expected
     * @see <a href="https://developer.smsverification.xyz/api-reference/disposable/cancel">SMS Verification's docs / disposable/cancel</a>
     */
    public AccountBalanceChange cancelOrder(String id) throws Exception {
        val request = new JSONObject();
        request.put("id", id);
        val body = request.toString();

        val res = httpUtil.request("https://smsverification.xyz/api/v2/disposable/cancel", body, "DELETE");

        val oldBal = res.getFloat("old_balance");
        val newBal = res.getFloat("new_balance");

        return new AccountBalanceChange(oldBal, newBal);
    }

    /**
     * <h3>Mark as sent</h3>
     *
     * @param id ID of the order you wish to mark as sent
     * @return Order status change
     * @throws Exception When something goes differently than expected
     * @see <a href="https://developer.smsverification.xyz/api-reference/disposable/sent">SMS Verification's docs / disposable/sent</a>
     */
    public OrderStatusChange markSent(String id) throws Exception {
        val request = new JSONObject();
        request.put("id", id);
        val body = request.toString();

        val res = httpUtil.request("https://smsverification.xyz/api/v2/disposable/sent", body, "PUT");

        val oldStatus = res.getString("old_status");
        val newStatus = res.getString("new_status");

        return new OrderStatusChange(oldStatus, newStatus);
    }

    /**
     * <h3>Check if delivered</h3>
     *
     * @param id ID of the order you wish to check if it has been complete
     * @return The SMS code
     * @throws Exception When something goes differently than expected
     * @see <a href="https://developer.smsverification.xyz/api-reference/disposable/check">SMS Verification's docs / disposable/check</a>
     */
    public OrderComplete check(String id) throws Exception {
        val request = new JSONObject();
        request.put("id", id);
        val body = request.toString();

        val res = httpUtil.request("https://smsverification.xyz/api/v2/disposable/check", body, "GET");

        val sms = res.getString("sms");

        return new OrderComplete(sms);
    }

}
