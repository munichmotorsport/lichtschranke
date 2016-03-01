package munichmotorsport.photocellapplication.utils;

public class Utils {

    public static boolean nameCheck(String string) {
        if ( string.equals("") ) return false;
        if ( string.equals(" ") ) return false;
        return true;
    }

}
