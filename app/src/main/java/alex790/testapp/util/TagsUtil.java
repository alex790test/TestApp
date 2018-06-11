package alex790.testapp.util;



/**
 * Created by alex790 on 08.06.2018.
 */

public class TagsUtil {


    /**
     * @param stringOne строка имеющихся тегов
     * @param stringTwo строка искомых тегов
     * @return были ли совпадения искомых тегов в имеющейся строке
     */
    public static boolean hasTags (String stringOne, String stringTwo) {

        String[] massTags = stringTwo.split(",");
        String sOne = stringOne.toLowerCase();

        for (String tag : massTags) {
            if (sOne.contains(tag.trim().toLowerCase())) {
                return true;
            }
        }

        return false;
    }
}
