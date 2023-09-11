package com.example.humarahub;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class PaymentActivity extends AppCompatActivity {


   private String postData = "";
    private WebView mwebView;

    private final String Jazz_MerchantID = "MC56684";
    private final String Jazz_Password = "v9yv00x8g0";
    private final String Jazz_IntegritySalt = "dx002fxyz1";

    private static final String paymentReturnUrl = "http://localhost/order.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        mwebView = findViewById(R.id.activity_payment_webview);
        // Enable Javascript
        WebSettings webSettings = mwebView.getSettings();
        webSettings.setJavaScriptEnabled(true);


        mwebView.setWebViewClient(new MyWebViewClient());
        webSettings.setDomStorageEnabled(true);
        mwebView.addJavascriptInterface(new FormDataInterface(), "FORMOUT");


        Intent intentData = getIntent();
        String price = intentData.getStringExtra("price");
        System.out.println("AwaisLogs: price_before" + price);

        String[] values = price.split("\\.");
        price = values[0];
        price = price + "00";
        System.out.println("AwaisLogs: price " + price);

        Date Date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddkkmmss");
        String DateString = dateFormat.format(Date);
        System.out.println("AwaisLogs: DateString " + DateString);


        //convert Date to Calender

        Calendar c = Calendar.getInstance();
        c.setTime(Date);
        c.add(Calendar.HOUR, 1);


        //convert  calender back to Date
        Date currentDateHourPlusOne = c.getTime();
        String expiryDateString = dateFormat.format(currentDateHourPlusOne);
        System.out.println("AwaisLogs: ExpirydateString : " + expiryDateString);

        String TransactionIdString = "T" + DateString;
        System.out.println("AwaisLogs: TransactionIdString" + TransactionIdString);


        String pp_MerchantID= Jazz_MerchantID;
        String pp_Password= Jazz_Password;
        String IntegritySalt=Jazz_IntegritySalt;
        String pp_ReturnURL= paymentReturnUrl;
        String pp_Amount = price;
        String pp_TxnDateTime = DateString;
        String pp_TxnExpiryDateTime = expiryDateString;
        String pp_TxnRefNo = TransactionIdString;
        String pp_Version = "1.1";
        String pp_TxnType = "";
        String pp_Language = "EN";
        String pp_SubMerchantID = "";
        String pp_BankID = "TBANK";
        String pp_ProductID = "RETL";
        String pp_TxnCurrency = "PKR";
        String pp_BillReference = "billRef";
        String pp_Description = "Description of transaction";
        String pp_SecureHash = "";
        String pp_mpf_1 = "1";
        String pp_mpf_2 = "2";
        String pp_mpf_3 = "3";
        String pp_mpf_4 = "4";
        String pp_mpf_5 = "5";


        String sortedString="";

        sortedString += IntegritySalt + "&";
        sortedString += pp_Amount + "&";
        sortedString += pp_BankID + "&";
        sortedString += pp_BillReference + "&";
        sortedString += pp_Description + "&";
        sortedString += pp_Language + "&";
        sortedString += pp_MerchantID + "&";
        sortedString += pp_Password + "&";
        sortedString += pp_ProductID + "&";
        sortedString += pp_ReturnURL + "&";
        //sortedString += pp_SubMerchantID + "&";
        sortedString += pp_TxnCurrency + "&";
        sortedString += pp_TxnDateTime + "&";
        sortedString += pp_TxnExpiryDateTime + "&";
        //sortedString += pp_TxnType + "&";
        sortedString += pp_TxnRefNo + "&";
        sortedString += pp_Version + "&";
        sortedString += pp_mpf_1 + "&";
        sortedString += pp_mpf_2 + "&";
        sortedString += pp_mpf_3 + "&";
        sortedString += pp_mpf_4 + "&";
        sortedString += pp_mpf_5;



        pp_SecureHash = php_hash_hmac(sortedString, IntegritySalt);
        System.out.println("AwaisLogs: sortedString :"+sortedString);
        System.out.println("AwaisLogs: pp_SecureHash :"+pp_SecureHash);

        try {

            postData += URLEncoder.encode("pp_Version", "UTF-8")
                    + "=" + URLEncoder.encode(pp_Version, "UTF-8") + "&";

            postData += URLEncoder.encode("pp_TxnType", "UTF-8")
                    + "=" + pp_TxnType + "&";

            postData += URLEncoder.encode("pp_Language", "UTF-8")
                    + "=" + URLEncoder.encode(pp_Language, "UTF-8") + "&";

            postData += URLEncoder.encode("pp_MerchantID", "UTF-8")
                    + "=" + URLEncoder.encode(pp_MerchantID, "UTF-8") + "&";

            postData += URLEncoder.encode("pp_SubMerchantID", "UTF-8")
                    + "=" + pp_SubMerchantID + "&";

            postData += URLEncoder.encode("pp_Password", "UTF-8")
                    + "=" + URLEncoder.encode(pp_Password, "UTF-8") + "&";

            postData += URLEncoder.encode("pp_BankID", "UTF-8")
                    + "=" + URLEncoder.encode(pp_BankID, "UTF-8") + "&";

            postData += URLEncoder.encode("pp_ProductID", "UTF-8")
                    + "=" + URLEncoder.encode(pp_ProductID, "UTF-8") + "&";

            postData += URLEncoder.encode("pp_TxnRefNo", "UTF-8")
                    + "=" + URLEncoder.encode(pp_TxnRefNo, "UTF-8") + "&";

            postData += URLEncoder.encode("pp_Amount", "UTF-8")
                    + "=" + URLEncoder.encode(pp_Amount, "UTF-8") + "&";

            postData += URLEncoder.encode("pp_TxnCurrency", "UTF-8")
                    + "=" + URLEncoder.encode(pp_TxnCurrency, "UTF-8") + "&";

            postData += URLEncoder.encode("pp_TxnDateTime", "UTF-8")
                    + "=" + URLEncoder.encode(pp_TxnDateTime, "UTF-8") + "&";

            postData += URLEncoder.encode("pp_BillReference", "UTF-8")
                    + "=" + URLEncoder.encode(pp_BillReference, "UTF-8") + "&";

            postData += URLEncoder.encode("pp_Description", "UTF-8")
                    + "=" + URLEncoder.encode(pp_Description, "UTF-8") + "&";

            postData += URLEncoder.encode("pp_TxnExpiryDateTime", "UTF-8")
                    + "=" + URLEncoder.encode(pp_TxnExpiryDateTime, "UTF-8") + "&";

            postData += URLEncoder.encode("pp_ReturnURL", "UTF-8")
                    + "=" + URLEncoder.encode(pp_ReturnURL, "UTF-8") + "&";

            postData += URLEncoder.encode("pp_SecureHash", "UTF-8")
                    + "=" + pp_SecureHash + "&";

            postData += URLEncoder.encode("ppmpf_1", "UTF-8")
                    + "=" + URLEncoder.encode(pp_mpf_1, "UTF-8") + "&";

            postData += URLEncoder.encode("ppmpf_2", "UTF-8")
                    + "=" + URLEncoder.encode(pp_mpf_2, "UTF-8") + "&";

            postData += URLEncoder.encode("ppmpf_3", "UTF-8")
                    + "=" + URLEncoder.encode(pp_mpf_3, "UTF-8") + "&";

            postData += URLEncoder.encode("ppmpf_4", "UTF-8")
                    + "=" + URLEncoder.encode(pp_mpf_4, "UTF-8") + "&";

            postData += URLEncoder.encode("ppmpf_5", "UTF-8")
                    + "=" + URLEncoder.encode(pp_mpf_5, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            System.out.println("AwaisLogsthere was an issue");

        }

        System.out.println("AwaisLogs: post data" + postData);  //payments
        mwebView.postUrl("https://sandbox.jazzcash.com.pk/CustomerPortal/transactionmanagement/merchantform/", postData.getBytes());

    }


    private class MyWebViewClient extends WebViewClient {
        private final String jsCode = "" + "function parseForm(form){" +
                "var values='';" +
                "for(var i=0 ; i< form.elements.length; i++){" +
                "   values+=form.elements[i].name+'='+form.elements[i].value+'&'" +
                "}" +
                "var url=form.action;" +
                "console.log('parse form fired');" +
                "window.FORMOUT.processFormData(url,values);" +
                "   }" +
                "for(var i=0 ; i< document.forms.length ; i++){" +
                "   parseForm(document.forms[i]);" +
                "};";

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            if (url.equals(paymentReturnUrl)) {
                System.out.println(" return url cancelling");
                view.stopLoading();
                return;
            }
            super.onPageStarted(view, url, favicon);
        }


        @Override
        public void onPageFinished(WebView view, String url) {
            if (url.equals(paymentReturnUrl)) {
                return;
            }
            view.loadUrl("javascript:(function(){ " + jsCode + "})()");
            super.onPageFinished(view, url);

        }

    }

    public class FormDataInterface {

        @JavascriptInterface
        public void processFormData(String url, String formData) {
            Intent i = new Intent(PaymentActivity.this, DeliveryActivity.class);

            System.out.println("AwaisLogs data Url:" + url + "form data" + formData);
            if (url.equals(paymentReturnUrl)) {
                String[] values = formData.split("&");
                for (String pair : values) {
                    String[] nameValue = pair.split("=");
                    if (nameValue.length == 2) {
                        System.out.println("AwaisLogs data Name:" + nameValue[0] + "values" + Arrays.toString(nameValue));
                        i.putExtra(nameValue[0], nameValue[1]);
                    }
                }

                setResult(RESULT_OK, i);
                finish();

                return;
            }
        }
    }

    // phphash se hum hash create kr rha or humra pas hash byte ki form me ay ga
    public static String php_hash_hmac(String data, String secret) {
        String returnString = "";
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            byte[] res = sha256_HMAC.doFinal(data.getBytes());
            returnString = bytesToHex(res);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return returnString;
    }
//jazz cash byte me accept nhi krta hash isi lisa bytes to hex convert function use kia hian
    public static String bytesToHex(byte[] bytes) {
        final char[] hexArray = "0123456789abcdef".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0, v; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

}











