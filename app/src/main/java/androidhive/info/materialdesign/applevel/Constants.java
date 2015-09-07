package androidhive.info.materialdesign.applevel;

/**
 * Created by maheshbabusomineni on 7/15/15.
 */

public class Constants {

    public static final String root_url="http://voxapi.voxpopulii.com/";
    public static String serviceUrl = "http://voxapi.voxpopulii.com/Api/";

    public static String post_images_url = "http://voxapi.voxpopulii.com/uploads/uploadpostsfilepath/";
    public static String profile_images_url = "http://voxapi.voxpopulii.com/uploads/users/thumbnails/";
    //public static String profile_images_url = "http://voxapi.voxpopulii.com/uploads/users/";


    public static final String AppName = "Voxpopulii" ;

    public static final String x_parse_application_id_key = "X-Parse-Application-Id";
    public static final String x_parse_application_id_value = "ORY7BYT28z07dlH1rdlZoJyL2PUOiszHBItyMVSb";

    public static final String x_parse_rest_api_key = "X-Parse-REST-API-Key";
    public static final String x_parse_rest_api_value = "ZvJyc70WKYorqXsJ4DYE649JwPKJ5YEL6TilZfn5";

    public static final String request_content_type_key = "Content-Type";
    public static final String request_content_type_value = "application/json";

    public static final String request_content_length_key = "Content-Length";

    public static final String request_type_value = "POST";

    public static final String TAG = "StaggeredGridActivity";

    //Shared preference keys
    public static final String access_token_default_key = "access_token";
    public static final String user_name_default_key = "userName";
    public static final String token_type_default_key = "token_type";
    public static final String issued_date_default_key = ".issued";
    public static final String expiry_date_default_key = ".expires";
    public static final String expirs_in_default_key = "expires_in";
    public static final String user_id_default_key = "ID_USR";


    //Web api request methods
    public enum RequestMethods {
        LoginRequest("Login"),
        GetAllStudents("GetAllStudents"),
        AddPost("AddPost"),
        FileUpload("UUIDImageUpload/PostAsync"),
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
    //Web api request methods
    public enum PostType {
        Text("text"),
        Image("image"),
        Audio("audio"),
        Video("video"),
        ;

        private final String text;

        /**
         * @param text
         */
        private PostType(final String text) {
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
