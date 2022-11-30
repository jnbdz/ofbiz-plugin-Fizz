package com.sitenetsoft.opensource.fizz.services;

import java.util.Map;

import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.service.DispatchContext;
import org.apache.ofbiz.service.ServiceUtil;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class FizzServices {

    public static final String module = FizzServices.class.getName();

    static class ZoneSections {
        public static String overview = "https://zone.fizz.ca/dce/customer-ui-prod/account/overview";
        public static String usage = "https://zone.fizz.ca/dce/customer-ui-prod/account/plan/usage?productId=";
        public static String plan = "https://zone.fizz.ca/dce/customer-ui-prod/account/plan";
        public static String device = "https://zone.fizz.ca/dce/customer-ui-prod/account/device";
        public static String profile = "https://zone.fizz.ca/dce/customer-ui-prod/account/profile";
        public static String paymentHistory = "https://zone.fizz.ca/dce/customer-ui-prod/wallet/payment-history?prvState=profile";
    }

    static class ZoneAuth {
        public static String authorize = "https://auth.fizz.ca/oauth2/aus1gk401eKpy4ZWH697/v1/authorize?scope=openid profile email&state={state}&response_type=code&client_id={client_id}&redirect_uri=https://sso.fizz.ca/auth/realms/etiyademo/broker/okta/endpoint";
        public static String authorizeHttpMethod = "GET";

        public static String introspect = "https://auth.fizz.ca/idp/idx/introspect";
        public static String introspectHttpMethod = "POST";

        public static String identify = "https://auth.fizz.ca/idp/idx/identify";
        public static String identifyHttpMethod = "POST";

        public static String token = "https://auth.fizz.ca/login/token/redirect?stateToken=";
        public static String tokenHttpMethod = "GET";

        public static String oktaEnpoint = "https://sso.fizz.ca/auth/realms/etiyademo/broker/okta/endpoint?code={code}&state={state}";
        public static String oktaEnpointHttpMethod = "GET";

        public static String openIdConnectKeycloak = "https://fizz.ca/openid-connect/keycloak?state={state}&code={code}"
        public static String openIdConnectKeycloakHttpMethod = "GET";

        public static String enOpenIdConnectKeycloak = "https://fizz.ca/en/openid-connect/keycloak?state={state}&code={code}"
        public static String enOpenIdConnectKeycloakHttpMethod = "GET";

        public static String redirectWsc = "https://fizz.ca/en/user/redirect/wsc"
        public static String redirectWscHttpMethod = "GET";
    }

    static class LoginFormInputs {
        public static String tokenName = "token_name";
        public static String tokenNameValue = "btoken";

        public static String btoken = "btoken";
        public static String btokenValue = "mwurcAAwpuJZIvjdkNAbiA9hL8ogy6sqEvnr";

        public static String dispatch = "dispatch";
        public static String dispatchValue = "login";

        public static String appId = "appId";
        public static String appIdValue = "EC";

        public static String domain = "domain";
        public static String domainValue = "domainType.business";

        public static String cookieEnabled = "cookieEnabled";
        public static String cookieEnabledValue = "false";

        public static String type = "type";
        public static String typeValue = "SSO-AFF-V1";

        public static String username = "username";
        public static String password = "password";
    }

    public static Map<String, Object> pullDataFizz(DispatchContext dctx, Map<String, ? extends Object> context) {
        Map<String, Object> result = ServiceUtil.returnSuccess();
        Delegator delegator = dctx.getDelegator();

        CustomerCenterSections customerCenterSections = new CustomerCenterSections();
        LoginFormInputs loginFormInputs = new LoginFormInputs();

        try {

            try {
                // Get the Cookie that is initialized in the login form page
                Connection.Response loginForm = Jsoup.connect(customerCenterSections.dashboard)
                        .method(Connection.Method.GET)
                        .execute();

                Document document = Jsoup.connect(customerCenterSections.dashboard)
                        //.data("cookieexists", "false")
                        .data(LoginFormInputs.tokenName, LoginFormInputs.tokenNameValue)
                        .data(LoginFormInputs.btoken, LoginFormInputs.btokenValue)
                        .data(LoginFormInputs.dispatch, LoginFormInputs.dispatchValue)
                        .data(LoginFormInputs.appId, LoginFormInputs.appIdValue)
                        .data(LoginFormInputs.domain, LoginFormInputs.domainValue)
                        .data(LoginFormInputs.cookieEnabled, LoginFormInputs.cookieEnabledValue)
                        .data(LoginFormInputs.type, LoginFormInputs.typeValue)
                        .data(LoginFormInputs.username, username)
                        .data(LoginFormInputs.password, password)
                        .cookies(loginForm.cookies())
                        .post();
                System.out.println(document);
            } catch (Exception exception) {
                System.out.println(exception);
            }

            /*try {
                Document doc = Jsoup.connect("http://example.com").get();
                doc.select("p").forEach(System.out::println);
                System.out.println(doc.select("p").toString());
            } catch (Exception exception) {
                System.out.println(exception);
            }*/

            GenericValue videotron = delegator.makeValue("FizzInvoices");
            // Auto generating next sequence of ofbizDemoId primary key
            fizz.setNextSeqId();
            // Setting up all non primary key field values from context map
            fizz.setNonPKFields(context);
            // Creating record in database for OfbizDemo entity for prepared value
            fizz = delegator.create(fizz);
            result.put("fizzId", fizz.getString("fizzId"));
            Debug.log("==========This is my first Java Service implementation in Apache OFBiz. Fizz record created successfully with ofbizDemoId: " + fizz.getString("fizzId"));
        } catch (GenericEntityException e) {
            Debug.logError(e, module);
            return ServiceUtil.returnError("Error in creating record in Fizz Invoice entity ........" +module);
        }
        return result;

        /*Connection.Response loginForm = Jsoup.connect("https://www.desco.org.bd/ebill/login.php")
        .method(Connection.Method.GET)
        .execute();

        Document document = Jsoup.connect("https://www.desco.org.bd/ebill/authentication.php")
        .data("cookieexists", "false")
        .data("username", "32007702")
        .data("login", "Login")
        .cookies(loginForm.cookies())
        .post();
        System.out.println(document);*/
    }

}