package androidhive.info.materialdesign.model.dataobjects;

import java.util.ArrayList;
import java.util.Random;

public class SampleData {

    public static final int SAMPLE_DATA_ITEM_COUNT = 11;

    public static ArrayList<Project> generateSampleData() {
        String repeat = " repeat";
        final ArrayList<Project> datas = new ArrayList<Project>();
        for (int i = 0; i < SAMPLE_DATA_ITEM_COUNT; i++) {

            Project data = new Project();

            if(i == 1){

                data.setAvatarUrls48x48("https://jiresal-test.s3.amazonaws.com/deal3.png");

            }else if(i == 2){

                data.setAvatarUrls48x48("https://codeascraft.com/wp-content/uploads/2014/01/grumpy_cat_search_screenshot_framed.png");

            }else if(i == 3){

                data.setAvatarUrls48x48("https://gitlab.com/assets/logo-ca207549507c811d027110077cf86e90.svg");

            }else{

                data.setAvatarUrls48x48("https://scontent-sin1-1.xx.fbcdn.net/hphotos-xpf1/v/t1.0-9/11012764_1623508491245447_6436273402752478003_n.jpg?oh=8a705e40923994b3aa1e105f69100abe&oe=5613E815");

            }

            data.setName( "Pinterest Card");
            data.setKey("Super awesome description");
            Random ran = new Random();
            int x = ran.nextInt(i + SAMPLE_DATA_ITEM_COUNT);
            for (int j = 0; j < x; j++)
                data.key += repeat;
            datas.add(data);
        }
        return datas;
    }

}
