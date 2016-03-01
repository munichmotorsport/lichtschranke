package munichmotorsport.photocellapplication.utils;

public class Utils {

    /**
     * Checks, if an entered name is valid. Used for inputs by user.
     *  eg TeamName, CarName
     *
     * @param string
     * @return  true = name valid;
     *          false = name invalid;
     */
    public static boolean nameCheck(String string) {
        if ( string.trim().equals("") ) return false;
        return true;
    }

}
