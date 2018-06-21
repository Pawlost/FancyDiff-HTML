package redhat.work.pdf.Core;

public class Convert {

    public static void main(String[] args) {
        boolean test = false;
        for (String arg : args) {
            switch (arg) {
                case "--test_mode":
                    new UserTestConvert(args);
                    test = true;
                    break;
            }
        }

        if(!test) {
            new NormalConvert(args);
        }
    }
}
