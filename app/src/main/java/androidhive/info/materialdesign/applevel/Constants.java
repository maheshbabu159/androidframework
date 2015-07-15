package androidhive.info.materialdesign.applevel;

/**
 * Created by maheshbabusomineni on 7/15/15.
 */

public class Constants {

    public static final String base_url = "http://api.androidhive.info/volley/string_response.html";
    public static final String x_parse_application_id_key = "X-Parse-Application-Id";
    public static final String x_parse_application_id_value = "ORY7BYT28z07dlH1rdlZoJyL2PUOiszHBItyMVSb";

    public static final String x_parse_rest_api_key = "X-Parse-REST-API-Key";
    public static final String x_parse_rest_api_value = "ZvJyc70WKYorqXsJ4DYE649JwPKJ5YEL6TilZfn5";

    public static final String request_content_type_key = "Content-Type";
    public static final String request_content_type_value = "application/json";

    public static final String request_content_length_key = "Content-Length";

    public static final String request_type_value = "POST";


    //Web api request methods
    public enum RequestMethods {
        LoginRequest("Login"),
        GetAllStudents("GetAllStudents"),
        ;

        private final String text;

        /**
         * @param text
         */
        private RequestMethods(final String text) {
            this.text = text;
        }

        /* (non-Javadoc)
         * @see java.lang.Enum#toString()
         */
        @Override
        public String toString() {
            return text;
        }
    }
}
