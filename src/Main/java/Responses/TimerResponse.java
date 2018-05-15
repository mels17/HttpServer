package Responses;

import Entities.HeaderDetails;
import Entities.Request;
import Entities.Response;
import Entities.STATUS_CODES;

public class TimerResponse implements HttpResponseCommand {


    @Override
    public Response process(Request request) {
        String body = "<!DOCTYPE HTML>" +
                "<html>" +
                "<head>" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">" +
                "<style>" +
                "p {" +
                "  text-align: center;" +
                "  font-size: 60px;" +
                "  margin-top:0px;" +
                "}" +
                "</style>" +
                "</head>" +
                "<body>" +
                "" +
                "<p id=\"demo\"></p>" +
                "<script>" +


                "var countDownDate = new Date(\"Sep 5, 2018 15:37:25\").getTime();"
                + "var x = setInterval(function() {"
                + "var now = new Date().getTime();"
                + "var distance = countDownDate - now;"
                + "var days = Math.floor(distance/(1000*60*60*24));"
                + "var hours = Math.floor((distance/(1000*60*60*24)) / (1000*60*60));"
                + "var minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));"
                + "var seconds = Math.floor((distance % (1000 * 60)) / 1000);"
                + "document.getElementById(\"demo\").innerHTML = days + \"d \" + hours + \"h \""
                + "+ minutes + \"m \" + seconds + \"s \";"
                + "if (distance < 0) {"
                + "clearInterval(x);"
                + "document.getElementById(\"demo\").innerHTML = \"EXPIRED\";"
                + "}"
                + "}, 1000);" +
                "</script>" +
                "</body>" +
                "</html>" +
                "";
        return new Response(STATUS_CODES.OK, HeaderDetails.STANDARD_HEADER, body, HeaderDetails.HTML_CONTENT_TYPE);
    }
}