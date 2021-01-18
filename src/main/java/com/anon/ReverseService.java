package com.anon;



public class ReverseService implements ExecutableService {

    public ReverseService() {}

    public String executeService(String input) {
        String output = null;
        if (input == null) {
            return output;
        }
        output = "";
        for (int i = input.length() - 1; i >= 0; i--) {
            output = output + input.charAt(i);
        }

        return output;
    }

    public void test() {}

}
