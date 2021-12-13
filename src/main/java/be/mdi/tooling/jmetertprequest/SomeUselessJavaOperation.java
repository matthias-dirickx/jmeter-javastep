package be.mdi.tooling.jmetertprequest;

public class SomeUselessJavaOperation {

    private String parameter_1;
    private String parameter_2;
    private int parameter_3;
    private String result;

    public SomeUselessJavaOperation(String parameter_1, String parameter_2, int parameter_3) {
        this.parameter_1 = parameter_1;
        this.parameter_2 = parameter_2;
        this.parameter_3 = parameter_3;
    }

    public SomeUselessJavaOperation doAction() {
        StringBuilder sb = new StringBuilder();
        sb.append(parameter_1)
                .append("_")
                .append(parameter_2)
                .append("_")
                .append(parameter_3);
        result = sb.toString();
        return this;
    }

    public boolean isSuccess() {
        double rand = Math.random();
        boolean fakeResult = true;
        if( rand < 0.1 ) {
            fakeResult = false;
        }
        return fakeResult;
    }

    public String getError() {
        return "The random success function returned 'false'.";
    }

    public String getResult() {
        return result;
    }

}
