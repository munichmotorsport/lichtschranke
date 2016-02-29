package munichmotorsport.photocellapplication.utils;

/**
 * Created by Nils Grünewald on 26.02.2016.
 */
public class Utils {

    public static boolean nameCheck(String string) {
        if ( string.equals("") ) return false;
        if ( string.equals(" ") ) return false;
        return true;
    }

}
