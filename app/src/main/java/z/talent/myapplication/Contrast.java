package z.talent.myapplication;

public class Contrast {
    volatile static String username = "";

    public static void setUsername(String username) {
        Contrast.username = username;
    }
}
