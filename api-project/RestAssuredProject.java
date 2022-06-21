import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class RestAssuredProject {
    RequestSpecification requestSpecification;
    ResponseSpecification responseSpecification;
    String SSHKey;
    int id;

    @BeforeClass
    public void setup() {
        // Create request specification
        requestSpecification = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .addHeader("Authorization", "token ghp_XXXXXXXXXXXXXXXXXXXXXXXXX")
                .setBaseUri("https://api.github.com")
                .build();
        responseSpecification = new ResponseSpecBuilder()
                .expectStatusCode(201)
                .expectContentType("application/json")
                .build();
        SSHKey="ssh-ed25519 AAAAC3NzaC1lZDI1NTE5AAAAIG+6EBV18OgQOuLp---------";
    }

    @Test(priority = 1)
    public void postKey(){

        String reqBody = "{\"title\": \"RestAssuredAPIKey\",  \"key\": \""+SSHKey+"\" }";

        Response response=given().spec(requestSpecification).body(reqBody).when().post("/user/keys");
        String resBody= response.getBody().asPrettyString();
        System.out.println(resBody);
        System.out.println("POST Response Status: "+response.getStatusCode());
        id = response.then().extract().path("id");
        System.out.println("Token ID: " + id);
        Assert.assertEquals(response.getStatusCode(), 201, "Passed: Correct POST status code");

    }

    @Test(priority = 2)
    public void getKey(){
        Response response =
                given().spec(requestSpecification).when()
                        .get("/user/keys");
        System.out.println(response.asPrettyString());
        Assert.assertEquals(response.getStatusCode(), 200, "Correct status code is returned");

    }


    @Test(priority = 3)
    public void deleteKey(){
        //send DELETE request
        Response response = given()
                .spec(requestSpecification).
                when()
                .pathParam("keyId", id)
                .delete("/user/keys/{keyId}");

        //log the response to console and assert the status code
        response.then()
                .log().all()
                .statusCode(equalTo(204));
    }
}
