package com.api.test;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.core.AnyOf;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Map;

import static io.restassured.parsing.Parser.JSON;
import static io.restassured.path.json.JsonPath.from;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.matchesRegex;


public class NavPriceInfoTest extends TestBase {


    @BeforeClass
    public void init() {
        RestAssured.defaultParser = JSON;
    }

    @Test(priority = 1, description = "Test to verify all the NAV values")
    public void verifyNavPriceArrayValues() {

        RestAssured.baseURI = props.getProperty("api.url");;
        Response response = null;
        String jsonAsString;

        String portidregex = "\\d{4}";
        String amountchangeregex ="(-?)[01].[0-9]+";
        String percentchangestringregex  = "(-?)[01].[0-9]+";
        String asofdateregex ="\\d{4}-\\d{2}-\\d{2}(T)\\d{2}:\\d{2}:\\d{2}-\\d{2}:\\d{2}";
        String priceregex  = "[0-9]+.[0-9]+";

        response = RestAssured.given()
                .when()
                .get(props.getProperty("api.path"));
        System.out.println("Response is" + response.getBody().prettyPrint());
        jsonAsString = response.asString();
        ArrayList<Map<String,?>> jsonAsArrayList = from(jsonAsString).get("navPriceArray");

        int size= jsonAsArrayList.size();
        for (int i=0;i<size;i++)

        {
            response.then().body("portId["+i+"].toString()", matchesRegex(portidregex));
            response.then().body("navPriceArray["+i+"].amountChange.get(0).toString()", matchesRegex(amountchangeregex));
            response.then().body("navPriceArray["+i+"].percentChange.get(0).toString()", matchesRegex(percentchangestringregex));
            response.then().body("navPriceArray["+i+"].asOfDate.get(0).toString()", matchesRegex(asofdateregex));
            response.then().body("navPriceArray["+i+"].currency.currencyCode.get(0).toString()", AnyOf.anyOf(equalTo("AUD"),equalTo("USD")));
            response.then().body("navPriceArray["+i+"].currency.currencyLocation.get(0).toString()", equalTo("L"));
            response.then().body("navPriceArray["+i+"].currency.currencySymbol.get(0).toString()", equalTo("$"));
            response.then().body("navPriceArray["+i+"].isFinal.get(0).toString()", equalTo("true"));
            response.then().body("navPriceArray["+i+"].measureType.measureCode.get(0).toString()", equalTo("NAV"));
            response.then().body("navPriceArray["+i+"].measureType.measureDesc.get(0).toString()", equalTo("Net Asset Value"));
            response.then().body("navPriceArray["+i+"].price.get(0).toString()", matchesRegex(priceregex));
            response.then().body("navPriceArray["+i+"].pricePeriodType.pricePeriodCode.get(0).toString()", equalTo("DAILY"));
            response.then().body("navPriceArray["+i+"].pricePeriodType.pricePeriodDesc.get(0).toString()", equalTo("Daily Price"));
            response.then().body("navPriceArray["+i+"].priceStatusType.priceStatusCode.get(0).toString()", equalTo("FINAL"));
            response.then().body("navPriceArray["+i+"].priceStatusType.priceStatusDesc.get(0).toString()", equalTo("Final Price"));
            response.then().body("navPriceArray["+i+"].final.get(0).toString()", equalTo("true"));
        }

    }

 @AfterClass(alwaysRun = true)
    public void cleanup() {
        RestAssured.reset();
    }
}
