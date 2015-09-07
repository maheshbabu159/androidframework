package androidhive.info.materialdesign.model.operations;

import org.json.JSONObject;

import java.util.ArrayList;

import androidhive.info.materialdesign.model.dataobjects.PostsView;

/**
 * Created by apple on 7/29/15.
 */
public class PostsViewModel extends PostsView{

    public static PostsView initWithDictionary(JSONObject jsonObject){

        PostsView object = new PostsView();

        //int i = jsonObject.getInt(object.abu_cou_pos.toUpperCase().toString());

        //  `object.abu_cou_pos = String.valueOf(jsonObject.getInt(object.abu_cou_pos.toUpperCase()));

        return object;
    }



}
